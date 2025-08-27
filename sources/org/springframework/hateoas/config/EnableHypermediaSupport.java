package org.springframework.hateoas.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({HypermediaSupportBeanDefinitionRegistrar.class, HateoasConfiguration.class})
/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/config/EnableHypermediaSupport.class */
public @interface EnableHypermediaSupport {

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/config/EnableHypermediaSupport$HypermediaType.class */
    public enum HypermediaType {
        HAL
    }

    HypermediaType[] type();
}
