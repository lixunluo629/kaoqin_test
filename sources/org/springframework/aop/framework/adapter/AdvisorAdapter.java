package org.springframework.aop.framework.adapter;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/adapter/AdvisorAdapter.class */
public interface AdvisorAdapter {
    boolean supportsAdvice(Advice advice);

    MethodInterceptor getInterceptor(Advisor advisor);
}
