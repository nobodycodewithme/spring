package com.nobodycodewithme.springtransactional.aspect;

import com.nobodycodewithme.springtransactional.annotation.CustomTransactional;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class TransactionalAspect {

    @Pointcut("@annotation(com.nobodycodewithme.springtransactional.annotation.CustomTransactional)")
    public void methodHasAnnotationCustomTransactional() {

    }

    @Around("methodHasAnnotationCustomTransactional()")
    public Object aroundMethodWithTransaction(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Object[] args = proceedingJoinPoint.getArgs();
        Method method = signature.getMethod();

        CustomTransactional customTransactional = AnnotationUtils.findAnnotation(method, CustomTransactional.class);
        if (customTransactional != null) {
            // get meta data from annotation
        }

        log.info("custom transactional using spring AOP");

        log.info("opening transaction to db");
        try {
            log.info("invoking method {}", signature);
            Object returnValue = proceedingJoinPoint.proceed(args);
            log.info("committing transaction");
            return returnValue;
        } catch (Throwable e) {
            log.error("has exception in method: {}, error message: {}, rollback transaction", method.getName(), e.getMessage());
            throw e;
        }
    }
}
