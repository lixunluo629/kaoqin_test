package org.springframework.data.web;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.data.domain.Sort;

@Target({ElementType.PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/PageableDefault.class */
public @interface PageableDefault {
    int value() default 10;

    int size() default 10;

    int page() default 0;

    String[] sort() default {};

    Sort.Direction direction() default Sort.Direction.ASC;
}
