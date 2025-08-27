package org.mapstruct;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
/* loaded from: mapstruct-1.0.0.CR1.jar:org/mapstruct/BeanMapping.class */
public @interface BeanMapping {
    Class<?> resultType() default void.class;

    Class<? extends Annotation>[] qualifiedBy() default {};

    NullValueMappingStrategy nullValueMappingStrategy() default NullValueMappingStrategy.DEFAULT;
}
