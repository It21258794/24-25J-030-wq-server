package com.waterboard.waterqualityprediction.filters;

import com.waterboard.waterqualityprediction.coreExceptions.ExType;
import com.waterboard.waterqualityprediction.coreExceptions.UnauthorizeException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JWTAuthEntryHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, AuthenticationException authException) throws IOException, jakarta.servlet.ServletException {
        final boolean isExpired = request.getAttribute("tok-expired") != null;
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        if (isExpired) {
            response.getOutputStream().println(new UnauthorizeException(ExType.TOKEN_EXPIRED, "token expired").getJsonAsString(null));
        } else {
            response.getOutputStream().println(new UnauthorizeException("authorization token invalid", ExType.INVALID_TOKEN).getJsonAsString(null));
        }
    }
}

