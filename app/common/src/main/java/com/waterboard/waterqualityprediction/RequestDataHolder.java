package com.waterboard.waterqualityprediction;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestDataHolder {

    @Autowired
    private HttpServletRequest httpServletRequest;
    private String requestHash;

    @PostConstruct
    public void postConstruct() { }

    public String getClientIP() {

        String ip = httpServletRequest.getHeader("X-Forwarded-For");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            String[] splitedIps = ip.split(",");
            return (splitedIps.length > 1) ? splitedIps[0] : ip;
        }
        return httpServletRequest.getRemoteAddr();
    }

    public String getRequestHash() {
        if(StringUtils.isBlank(requestHash)) {
            requestHash = RandomStringUtils.randomAlphanumeric(10);
        }
        return requestHash;
    }
}
