package org.springframework.data.web.config;

import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.HateoasSortHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.PagedResourcesAssemblerArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

@Configuration
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/config/HateoasAwareSpringDataWebConfiguration.class */
public class HateoasAwareSpringDataWebConfiguration extends SpringDataWebConfiguration {
    @Override // org.springframework.data.web.config.SpringDataWebConfiguration
    @Bean
    public HateoasPageableHandlerMethodArgumentResolver pageableResolver() {
        return new HateoasPageableHandlerMethodArgumentResolver(sortResolver());
    }

    @Override // org.springframework.data.web.config.SpringDataWebConfiguration
    @Bean
    public HateoasSortHandlerMethodArgumentResolver sortResolver() {
        return new HateoasSortHandlerMethodArgumentResolver();
    }

    @Bean
    public PagedResourcesAssembler<?> pagedResourcesAssembler() {
        return new PagedResourcesAssembler<>(pageableResolver(), null);
    }

    @Bean
    public PagedResourcesAssemblerArgumentResolver pagedResourcesAssemblerArgumentResolver() {
        return new PagedResourcesAssemblerArgumentResolver(pageableResolver(), null);
    }

    @Override // org.springframework.data.web.config.SpringDataWebConfiguration, org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter, org.springframework.web.servlet.config.annotation.WebMvcConfigurer
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) throws BeansException {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(pagedResourcesAssemblerArgumentResolver());
    }
}
