package org.mapstruct;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
/* loaded from: mapstruct-1.0.0.CR1.jar:org/mapstruct/Mapping.class */
public @interface Mapping {
    String target();

    String source() default "";

    String dateFormat() default "";

    String constant() default "";

    String expression() default "";

    boolean ignore() default false;

    Class<? extends Annotation>[] qualifiedBy() default {};

    Class<?> resultType() default void.class;

    String[] dependsOn() default {};
}
