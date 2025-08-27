package org.springframework.data.repository.query;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/Param.class */
public @interface Param {
    String value();
}
