package org.springframework.boot.context.annotation;

import java.util.Set;
import org.springframework.core.type.AnnotationMetadata;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/annotation/DeterminableImports.class */
public interface DeterminableImports {
    Set<Object> determineImports(AnnotationMetadata annotationMetadata);
}
