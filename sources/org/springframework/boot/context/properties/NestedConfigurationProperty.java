package org.springframework.boot.context.properties;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/properties/NestedConfigurationProperty.class */
public @interface NestedConfigurationProperty {
}
