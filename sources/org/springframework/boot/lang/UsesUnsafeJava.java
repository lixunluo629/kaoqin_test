package org.springframework.boot.lang;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
@Documented
/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/lang/UsesUnsafeJava.class */
public @interface UsesUnsafeJava {
}
