package org.springframework.data.repository;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/NoRepositoryBean.class */
public @interface NoRepositoryBean {
}
