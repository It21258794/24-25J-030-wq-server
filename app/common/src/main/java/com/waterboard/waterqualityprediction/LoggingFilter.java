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

    private final RequestDataProvider requestDataProvider;

    public  LoggingFilter(RequestDataProvider requestDataProvider) {
        this.requestDataProvider = requestDataProvider;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long requestTime = System.currentTimeMillis();
        MDC.put("request_unique_id", "[" + requestDataProvider.getRequestHash() + "]");
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
                        "requestHash", requestDataProvider.getRequestHash(),
                        "clientIP", requestDataProvider.getClientIP(),
                        "clientAppPlatform", requestDataProvider.getAppPlatform(),
                        "clientAppVersion", requestDataProvider.getAppVersion(),
                        "clientDevice", requestDataProvider.getDevice()
                )
        );
        String token = httpServletRequest.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            token = httpServletRequest.getParameter("_authorization");
        }
        if (StringUtils.isNotBlank(token) && !((HttpServletRequest) servletRequest).getRequestURI().startsWith("/api/external/")) {
            String subject = JWT.decodeWithoutSecret(token).getSubject();
            if (StringUtils.isNotBlank(subject)) {
                data.put("authorizedSubject", subject);
            }
        }
        log.info("http request trace {}", JSON.objectToString(
                data
        ));
    }
}
