package com.waterboard.waterqualityprediction.annotations;

import com.waterboard.waterqualityprediction.AnalyticsModule;
import com.waterboard.waterqualityprediction.AnalyticsModuleConfigs;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LimitPerIP {
    int limit();
    int time() default 1;
    String event();
}

@Aspect
@Component
@Slf4j
class LimitPerIPAspect {

    @Autowired
    AnalyticsModule analyticsModule;

    @Autowired
    AnalyticsModuleConfigs analyticsModuleConfigs;

    @Around("@annotation(com.waterboard.waterqualityprediction.annotations.LimitPerIP)")
    public Object methodTimeLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        LimitPerIP annotation = method.getAnnotation(LimitPerIP.class);
        String event = annotation.event();
        int limit = annotation.limit();
        int time = annotation.time();
        if(analyticsModuleConfigs.isEnableRateLimits()) {
            this.analyticsModule.throwIfLimitExceedForIP(event, limit, time);
        }
        Object result = proceedingJoinPoint.proceed();
        return result;
    }
}