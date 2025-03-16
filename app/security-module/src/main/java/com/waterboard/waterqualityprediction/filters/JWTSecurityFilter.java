package com.waterboard.waterqualityprediction.filters;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.waterboard.waterqualityprediction.*;
import com.waterboard.waterqualityprediction.commonExceptions.UnauthorizeException;
import com.waterboard.waterqualityprediction.models.user.User;
import com.waterboard.waterqualityprediction.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class JWTSecurityFilter extends BasicAuthenticationFilter {
    private UserService userService;
    private GlobalAppConfig globalAppConfig;
    private RequestContextManager requestContextManager;

    public JWTSecurityFilter(AuthenticationManager authenticationManager,
                             UserService userService,
                             GlobalAppConfig globalAppConfig,
                             RequestContextManager requestContextManager) {
        super(authenticationManager);
        this.userService = userService;
        this.globalAppConfig = globalAppConfig;
        this.requestContextManager = requestContextManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        MDC.put("request_unique_id", "[" + requestContextManager.getRequestHash() + "]");

        String token = request.getHeader("Authorization");
        if (token == null) {
            token = request.getParameter("_authorization");
        }
        if (token == null) {
            logger.debug("token not found in the request");
            chain.doFilter(request, response);
            return;
        }
        try {
            SessionData sessionData = new SessionData();
            var jwtContent = JwtUtil.decode(token, globalAppConfig.getSecretKey());
            log.debug("jwt token decoded, user = {}", jwtContent.getMainSubject());
            Optional<User> user = this.userService.getUserById(jwtContent.getMainSubject());
            if (user.isEmpty()) {
                log.error("user not found for id = {}", jwtContent.getMainSubject());
                throw new UnauthorizeException("user not found");
            }

            // update request hash
            requestContextManager.setRequestHash(requestContextManager.getRequestHash() + "-" +  user.get().getEmail());
            MDC.put("request_unique_id", "[" + requestContextManager.getRequestHash() + "]");
            sessionData.setUser(user.get());
            Session.setSessionData(sessionData);
        } catch (TokenExpiredException exp) {
            log.info("token expired = {}", exp.getMessage());
            request.setAttribute("tok-expired", true);
        } catch (Exception exp) {
            log.error("token decode failed token = {}, error = {}", token, exp.getMessage());
        }
        chain.doFilter(request, response);
    }

}
