package org.mapstruct;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
/* loaded from: mapstruct-1.0.0.CR1.jar:org/mapstruct/IterableMapping.class */
public @interface IterableMapping {
    String dateFormat() default "";

    Class<? extends Annotation>[] qualifiedBy() default {};

    Class<?> elementTargetType() default void.class;

    NullValueMappingStrategy nullValueMappingStrategy() default NullValueMappingStrategy.DEFAULT;
}
