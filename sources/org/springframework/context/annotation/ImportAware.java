package org.springframework.context.annotation;

import org.springframework.beans.factory.Aware;
import org.springframework.core.type.AnnotationMetadata;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ImportAware.class */
public interface ImportAware extends Aware {
    void setImportMetadata(AnnotationMetadata annotationMetadata);
}
