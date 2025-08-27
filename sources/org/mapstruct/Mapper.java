package org.mapstruct;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
/* loaded from: mapstruct-1.0.0.CR1.jar:org/mapstruct/Mapper.class */
public @interface Mapper {
    Class<?>[] uses() default {};

    Class<?>[] imports() default {};

    ReportingPolicy unmappedTargetPolicy() default ReportingPolicy.DEFAULT;

    String componentModel() default "default";

    Class<?> config() default void.class;

    CollectionMappingStrategy collectionMappingStrategy() default CollectionMappingStrategy.DEFAULT;

    NullValueMappingStrategy nullValueMappingStrategy() default NullValueMappingStrategy.DEFAULT;

    MappingInheritanceStrategy mappingInheritanceStrategy() default MappingInheritanceStrategy.DEFAULT;
}
