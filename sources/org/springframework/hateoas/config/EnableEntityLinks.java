package org.springframework.hateoas.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Target({ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({LinkBuilderBeanDefinitionRegistrar.class})
/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/config/EnableEntityLinks.class */
public @interface EnableEntityLinks {
}
