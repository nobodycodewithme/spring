package com.nobodycodewithme.springtransactional.annotation;

import com.nobodycodewithme.springtransactional.RepositoryImportRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RepositoryImportRegistrar.class)
public @interface EnableCustomRepository {
    String[] packages() default {};
}
