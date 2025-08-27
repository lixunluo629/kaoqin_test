package com.moredian.onpremise.core.aop.limit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/aop/limit/annotation/AccessLimitAnnotation.class */
public @interface AccessLimitAnnotation {
    int limit() default 100;

    int sec() default 1;
}
