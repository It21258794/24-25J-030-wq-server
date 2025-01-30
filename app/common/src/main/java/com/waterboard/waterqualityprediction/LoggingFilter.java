package com.waterboard.waterqualityprediction;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LoggingFilter implements Filter {

    private final RequestContextManager requestContextManager;

    public  LoggingFilter(RequestContextManager requestContextManager) {
        this.requestContextManager = requestContextManager;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long requestTime = System.currentTimeMillis();
        MDC.put("request_unique_id", "[" + requestContextManager.getRequestHash() + "]");
        filterChain.doFilter(servletRequest, servletResponse);
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
//        if (httpServletRequest.getRequestURI().equalsIgnoreCase("/api/health/status")) {
//            return;
//        }

        Map<String, Object> data = new HashMap<>(
                Map.of(
                        "url", httpServletRequest.getRequestURI(),
                        "method", httpServletRequest.getMethod(),
                        "status", httpServletResponse.getStatus(),
                        "responseTime", System.currentTimeMillis() - requestTime,
                        "requestHash", requestContextManager.getRequestHash(),
                        "clientIP", requestContextManager.getClientIP(),
                        "clientAppPlatform", requestContextManager.getAppPlatform(),
                        "clientAppVersion", requestContextManager.getAppVersion(),
                        "clientDevice", requestContextManager.getDevice()
                )
        );
        String token = httpServletRequest.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            token = httpServletRequest.getParameter("_authorization");
        }
        if (StringUtils.isNotBlank(token) && !((HttpServletRequest) servletRequest).getRequestURI().startsWith("/api/external/")) {
            String subject = JwtUtil.decodeWithoutSecret(token).getMainSubject();
            if (StringUtils.isNotBlank(subject)) {
                data.put("authorizedSubject", subject);
            }
        }
        log.info("http request trace {}", JsonUtils.objectToString(
                data
        ));
    }
}
