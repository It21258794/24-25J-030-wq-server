package com.waterboard.waterqualityprediction.annotations;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.waterboard.waterqualityprediction.Session;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnlySuperAdmin {

}

@Aspect
@Component
@Slf4j
class OnlySuperAdminAccessCheck {

    @Around("@annotation(com.waterboard.waterqualityprediction.annotations.OnlySuperAdmin)")
    public Object methodTimeLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Session.throwIfNotSuperAdmin();
        Object result = proceedingJoinPoint.proceed();
        return result;
    }
}
