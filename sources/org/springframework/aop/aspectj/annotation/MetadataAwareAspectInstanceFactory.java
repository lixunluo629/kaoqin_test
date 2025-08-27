package org.springframework.aop.aspectj.annotation;

import org.springframework.aop.aspectj.AspectInstanceFactory;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/aspectj/annotation/MetadataAwareAspectInstanceFactory.class */
public interface MetadataAwareAspectInstanceFactory extends AspectInstanceFactory {
    AspectMetadata getAspectMetadata();

    Object getAspectCreationMutex();
}
