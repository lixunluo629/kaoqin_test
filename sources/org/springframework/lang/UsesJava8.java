package org.springframework.lang;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
@Documented
/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/lang/UsesJava8.class */
public @interface UsesJava8 {
}
