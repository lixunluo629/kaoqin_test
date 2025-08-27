package org.springframework.aop;

import org.aopalliance.aop.Advice;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/Advisor.class */
public interface Advisor {
    Advice getAdvice();

    boolean isPerInstance();
}
