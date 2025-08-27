package org.springframework.hateoas.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/Relation.class */
public @interface Relation {
    public static final String NO_RELATION = "";

    String value() default "";

    String collectionRelation() default "";
}
