package org.springframework.aop.aspectj;

import org.springframework.core.Ordered;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/aspectj/AspectInstanceFactory.class */
public interface AspectInstanceFactory extends Ordered {
    Object getAspectInstance();

    ClassLoader getAspectClassLoader();
}
