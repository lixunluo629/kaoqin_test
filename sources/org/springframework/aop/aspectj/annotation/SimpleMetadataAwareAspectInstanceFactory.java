package org.springframework.aop.aspectj.annotation;

import org.springframework.aop.aspectj.SimpleAspectInstanceFactory;
import org.springframework.core.annotation.OrderUtils;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/aspectj/annotation/SimpleMetadataAwareAspectInstanceFactory.class */
public class SimpleMetadataAwareAspectInstanceFactory extends SimpleAspectInstanceFactory implements MetadataAwareAspectInstanceFactory {
    private final AspectMetadata metadata;

    public SimpleMetadataAwareAspectInstanceFactory(Class<?> aspectClass, String aspectName) {
        super(aspectClass);
        this.metadata = new AspectMetadata(aspectClass, aspectName);
    }

    @Override // org.springframework.aop.aspectj.annotation.MetadataAwareAspectInstanceFactory
    public final AspectMetadata getAspectMetadata() {
        return this.metadata;
    }

    @Override // org.springframework.aop.aspectj.annotation.MetadataAwareAspectInstanceFactory
    public Object getAspectCreationMutex() {
        return this;
    }

    @Override // org.springframework.aop.aspectj.SimpleAspectInstanceFactory
    protected int getOrderForAspectClass(Class<?> aspectClass) {
        return OrderUtils.getOrder(aspectClass, Integer.MAX_VALUE).intValue();
    }
}
