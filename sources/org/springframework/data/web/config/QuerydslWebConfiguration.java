package org.springframework.data.web.config;

import java.util.List;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/config/QuerydslWebConfiguration.class */
public class QuerydslWebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    @Qualifier("mvcConversionService")
    ObjectFactory<ConversionService> conversionService;

    @Lazy
    @Bean
    public QuerydslPredicateArgumentResolver querydslPredicateArgumentResolver() {
        return new QuerydslPredicateArgumentResolver(querydslBindingsFactory(), this.conversionService.getObject());
    }

    @Lazy
    @Bean
    public QuerydslBindingsFactory querydslBindingsFactory() {
        return new QuerydslBindingsFactory(SimpleEntityPathResolver.INSTANCE);
    }

    @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter, org.springframework.web.servlet.config.annotation.WebMvcConfigurer
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(0, querydslPredicateArgumentResolver());
    }
}
