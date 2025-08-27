package org.springframework.aop.aspectj;

import org.springframework.aop.PointcutAdvisor;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/aspectj/InstantiationModelAwarePointcutAdvisor.class */
public interface InstantiationModelAwarePointcutAdvisor extends PointcutAdvisor {
    boolean isLazy();

    boolean isAdviceInstantiated();
}
