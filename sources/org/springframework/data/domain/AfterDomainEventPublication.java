package org.springframework.data.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/AfterDomainEventPublication.class */
public @interface AfterDomainEventPublication {
}
