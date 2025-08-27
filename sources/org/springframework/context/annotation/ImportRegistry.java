package org.springframework.context.annotation;

import org.springframework.core.type.AnnotationMetadata;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ImportRegistry.class */
interface ImportRegistry {
    AnnotationMetadata getImportingClassFor(String str);

    void removeImportingClass(String str);
}
