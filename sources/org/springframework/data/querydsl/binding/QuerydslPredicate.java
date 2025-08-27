package org.springframework.data.querydsl.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/binding/QuerydslPredicate.class */
public @interface QuerydslPredicate {
    Class<?> root() default Object.class;

    Class<? extends QuerydslBinderCustomizer> bindings() default QuerydslBinderCustomizer.class;
}
