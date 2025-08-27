package org.springframework.data.projection;

import org.aopalliance.intercept.MethodInterceptor;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/MethodInterceptorFactory.class */
public interface MethodInterceptorFactory {
    MethodInterceptor createMethodInterceptor(Object obj, Class<?> cls);

    boolean supports(Object obj, Class<?> cls);
}
