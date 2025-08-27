package org.springframework.aop;

import org.aopalliance.aop.Advice;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/DynamicIntroductionAdvice.class */
public interface DynamicIntroductionAdvice extends Advice {
    boolean implementsInterface(Class<?> cls);
}
