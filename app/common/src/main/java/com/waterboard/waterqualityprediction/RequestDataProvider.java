package com.waterboard.waterqualityprediction;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Slf4j
@Component
public class RequestDataProvider {

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void postConstruct() { }

    public String getClientIP() {
        if(RequestContextHolder.getRequestAttributes() == null) {
            return "n/a";
        }
        RequestDataHolder requestDataHolder = applicationContext.getBean(RequestDataHolder.class);
        if(requestDataHolder != null) {
            return requestDataHolder.getClientIP();
        }
        return "n/a";
    }

    public String getAppVersion() {
        if(RequestContextHolder.getRequestAttributes() == null) {
            return "n/a";
        }
        RequestDataHolder requestDataHolder = applicationContext.getBean(RequestDataHolder.class);

        if(requestDataHolder != null) {
            return requestDataHolder.getAppVersion();
        }
        return "n/a";
    }

    public String getAppPlatform() {
        if(RequestContextHolder.getRequestAttributes() == null) {
            return "n/a";
        }
        RequestDataHolder requestDataHolder = applicationContext.getBean(RequestDataHolder.class);

        if(requestDataHolder != null) {
            return requestDataHolder.getAppPlatform();
        }
        return "n/a";
    }

    public String getDevice() {
        if(RequestContextHolder.getRequestAttributes() == null) {
            return "n/a";
        }
        RequestDataHolder requestDataHolder = applicationContext.getBean(RequestDataHolder.class);

        if(requestDataHolder != null) {
            return requestDataHolder.getDevice();
        }
        return "n/a";
    }

    public String getDeviceType() {
        if(RequestContextHolder.getRequestAttributes() == null) {
            return null;
        }
        RequestDataHolder requestDataHolder = applicationContext.getBean(RequestDataHolder.class);

        if(requestDataHolder != null) {
            return requestDataHolder.getDeviceType();
        }
        return null;
    }

    public String getRequestHash() {
        if(RequestContextHolder.getRequestAttributes() == null) {
            return "n/a";
        }
        RequestDataHolder requestDataHolder = applicationContext.getBean(RequestDataHolder.class);

        if(requestDataHolder != null) {
            return requestDataHolder.getRequestHash();
        }
        return "n/a";
    }

    public void setRequestHash(String requestHash) {
        if(RequestContextHolder.getRequestAttributes() == null) {
            return;
        }
        RequestDataHolder requestDataHolder = applicationContext.getBean(RequestDataHolder.class);

        if(requestDataHolder != null) {
            requestDataHolder.setRequestHash(requestHash);
        }
    }

}