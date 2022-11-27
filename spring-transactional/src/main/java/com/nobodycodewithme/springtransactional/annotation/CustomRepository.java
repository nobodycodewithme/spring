package com.nobodycodewithme.springtransactional.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomRepository {
    @AliasFor("beanName")
    String value() default "";

    @AliasFor("value")
    String beanName() default "";

    boolean singleton() default true;
}
