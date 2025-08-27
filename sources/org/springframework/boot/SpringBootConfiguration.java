package org.springframework.boot;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Configuration;

@Target({ElementType.TYPE})
@Configuration
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/SpringBootConfiguration.class */
public @interface SpringBootConfiguration {
}
