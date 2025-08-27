package org.springframework.boot.context.embedded;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.annotation.Value;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Value("${local.server.port}")
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/LocalServerPort.class */
public @interface LocalServerPort {
}
