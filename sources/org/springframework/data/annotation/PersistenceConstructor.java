package org.springframework.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/annotation/PersistenceConstructor.class */
public @interface PersistenceConstructor {
}
