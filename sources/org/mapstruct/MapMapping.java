package org.mapstruct;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
/* loaded from: mapstruct-1.0.0.CR1.jar:org/mapstruct/MapMapping.class */
public @interface MapMapping {
    String keyDateFormat() default "";

    String valueDateFormat() default "";

    Class<? extends Annotation>[] keyQualifiedBy() default {};

    Class<? extends Annotation>[] valueQualifiedBy() default {};

    Class<?> keyTargetType() default void.class;

    Class<?> valueTargetType() default void.class;

    NullValueMappingStrategy nullValueMappingStrategy() default NullValueMappingStrategy.DEFAULT;
}
