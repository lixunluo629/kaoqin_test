package org.springframework.context.annotation;

import org.springframework.core.type.AnnotationMetadata;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ImportSelector.class */
public interface ImportSelector {
    String[] selectImports(AnnotationMetadata annotationMetadata);
}
