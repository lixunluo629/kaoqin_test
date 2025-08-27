package org.springframework.aop.aspectj.annotation;

import java.io.Serializable;
import org.springframework.util.Assert;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/aspectj/annotation/LazySingletonAspectInstanceFactoryDecorator.class */
public class LazySingletonAspectInstanceFactoryDecorator implements MetadataAwareAspectInstanceFactory, Serializable {
    private final MetadataAwareAspectInstanceFactory maaif;
    private volatile Object materialized;

    public LazySingletonAspectInstanceFactoryDecorator(MetadataAwareAspectInstanceFactory maaif) {
        Assert.notNull(maaif, "AspectInstanceFactory must not be null");
        this.maaif = maaif;
    }

    @Override // org.springframework.aop.aspectj.AspectInstanceFactory
    public Object getAspectInstance() {
        if (this.materialized == null) {
            Object mutex = this.maaif.getAspectCreationMutex();
            if (mutex == null) {
                this.materialized = this.maaif.getAspectInstance();
            } else {
                synchronized (mutex) {
                    if (this.materialized == null) {
                        this.materialized = this.maaif.getAspectInstance();
                    }
                }
            }
        }
        return this.materialized;
    }

    public boolean isMaterialized() {
        return this.materialized != null;
    }

    @Override // org.springframework.aop.aspectj.AspectInstanceFactory
    public ClassLoader getAspectClassLoader() {
        return this.maaif.getAspectClassLoader();
    }

    @Override // org.springframework.aop.aspectj.annotation.MetadataAwareAspectInstanceFactory
    public AspectMetadata getAspectMetadata() {
        return this.maaif.getAspectMetadata();
    }

    @Override // org.springframework.aop.aspectj.annotation.MetadataAwareAspectInstanceFactory
    public Object getAspectCreationMutex() {
        return this.maaif.getAspectCreationMutex();
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.maaif.getOrder();
    }

    public String toString() {
        return "LazySingletonAspectInstanceFactoryDecorator: decorating " + this.maaif;
    }
}
