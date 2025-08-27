package org.springframework.context.annotation;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/AnnotationConfigRegistry.class */
public interface AnnotationConfigRegistry {
    void register(Class<?>... clsArr);

    void scan(String... strArr);
}
