package org.springframework.data.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Persistent
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/annotation/TypeAlias.class */
public @interface TypeAlias {
    String value();
}
