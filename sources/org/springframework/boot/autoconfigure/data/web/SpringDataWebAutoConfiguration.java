package org.springframework.boot.autoconfigure.data.web;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ConditionalOnClass({PageableHandlerMethodArgumentResolver.class, WebMvcConfigurerAdapter.class})
@AutoConfigureAfter({RepositoryRestMvcAutoConfiguration.class})
@ConditionalOnMissingBean({PageableHandlerMethodArgumentResolver.class})
@EnableSpringDataWebSupport
@ConditionalOnWebApplication
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/web/SpringDataWebAutoConfiguration.class */
public class SpringDataWebAutoConfiguration {
}
