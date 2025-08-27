package org.springframework.context.annotation;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.Assert;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ScannedGenericBeanDefinition.class */
public class ScannedGenericBeanDefinition extends GenericBeanDefinition implements AnnotatedBeanDefinition {
    private final AnnotationMetadata metadata;

    public ScannedGenericBeanDefinition(MetadataReader metadataReader) {
        Assert.notNull(metadataReader, "MetadataReader must not be null");
        this.metadata = metadataReader.getAnnotationMetadata();
        setBeanClassName(this.metadata.getClassName());
    }

    @Override // org.springframework.beans.factory.annotation.AnnotatedBeanDefinition
    public final AnnotationMetadata getMetadata() {
        return this.metadata;
    }

    @Override // org.springframework.beans.factory.annotation.AnnotatedBeanDefinition
    public MethodMetadata getFactoryMethodMetadata() {
        return null;
    }
}
