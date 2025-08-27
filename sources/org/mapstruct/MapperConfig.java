package org.mapstruct;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
/* loaded from: mapstruct-1.0.0.CR1.jar:org/mapstruct/MapperConfig.class */
public @interface MapperConfig {
    Class<?>[] uses() default {};

    ReportingPolicy unmappedTargetPolicy() default ReportingPolicy.WARN;

    String componentModel() default "default";

    CollectionMappingStrategy collectionMappingStrategy() default CollectionMappingStrategy.ACCESSOR_ONLY;

    NullValueMappingStrategy nullValueMappingStrategy() default NullValueMappingStrategy.RETURN_NULL;

    MappingInheritanceStrategy mappingInheritanceStrategy() default MappingInheritanceStrategy.EXPLICIT;
}
