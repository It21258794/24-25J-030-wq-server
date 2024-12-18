package com.waterboard.waterqualityprediction;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RequestContextManager {

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void postConstruct() { }

    public String getClientIP() {
        if(org.springframework.web.context.request.RequestContextHolder.getRequestAttributes() == null) {
            return "n/a";
        }
        RequestContextHolder requestContextHolder = applicationContext.getBean(RequestContextHolder.class);
        if(requestContextHolder != null) {
            return requestContextHolder.getClientIP();
        }
        return "n/a";
    }

    public String getAppVersion() {
        if(org.springframework.web.context.request.RequestContextHolder.getRequestAttributes() == null) {
            return "n/a";
        }
        RequestContextHolder requestContextHolder = applicationContext.getBean(RequestContextHolder.class);

        if(requestContextHolder != null) {
            return requestContextHolder.getAppVersion();
        }
        return "n/a";
    }

    public String getAppPlatform() {
        if(org.springframework.web.context.request.RequestContextHolder.getRequestAttributes() == null) {
            return "n/a";
        }
        RequestContextHolder requestContextHolder = applicationContext.getBean(RequestContextHolder.class);

        if(requestContextHolder != null) {
            return requestContextHolder.getAppPlatform();
        }
        return "n/a";
    }

    public String getDevice() {
        if(org.springframework.web.context.request.RequestContextHolder.getRequestAttributes() == null) {
            return "n/a";
        }
        RequestContextHolder requestContextHolder = applicationContext.getBean(RequestContextHolder.class);

        if(requestContextHolder != null) {
            return requestContextHolder.getDevice();
        }
        return "n/a";
    }

    public String getDeviceType() {
        if(org.springframework.web.context.request.RequestContextHolder.getRequestAttributes() == null) {
            return null;
        }
        RequestContextHolder requestContextHolder = applicationContext.getBean(RequestContextHolder.class);

        if(requestContextHolder != null) {
            return requestContextHolder.getDeviceType();
        }
        return null;
    }

    public String getRequestHash() {
        if(org.springframework.web.context.request.RequestContextHolder.getRequestAttributes() == null) {
            return "n/a";
        }
        RequestContextHolder requestContextHolder = applicationContext.getBean(RequestContextHolder.class);

        if(requestContextHolder != null) {
            return requestContextHolder.getRequestHash();
        }
        return "n/a";
    }

    public void setRequestHash(String requestHash) {
        if(org.springframework.web.context.request.RequestContextHolder.getRequestAttributes() == null) {
            return;
        }
        RequestContextHolder requestContextHolder = applicationContext.getBean(RequestContextHolder.class);

        if(requestContextHolder != null) {
            requestContextHolder.setRequestHash(requestHash);
        }
    }

}