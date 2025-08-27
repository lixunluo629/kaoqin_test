package org.springframework.data.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/annotation/AccessType.class */
public @interface AccessType {

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/annotation/AccessType$Type.class */
    public enum Type {
        FIELD,
        PROPERTY
    }

    Type value();
}
