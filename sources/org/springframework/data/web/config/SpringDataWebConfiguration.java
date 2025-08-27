package org.springframework.data.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.geo.format.DistanceFormatter;
import org.springframework.data.geo.format.PointFormatter;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.ProjectingJackson2HttpMessageConverter;
import org.springframework.data.web.ProxyingHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.data.web.XmlBeamHttpMessageConverter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/config/SpringDataWebConfiguration.class */
public class SpringDataWebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private ApplicationContext context;

    @Autowired
    @Qualifier("mvcConversionService")
    private ObjectFactory<ConversionService> conversionService;

    @Autowired(required = false)
    private XmlBeamHttpMessageConverter xmlBeamHttpMessageConverter;

    @Bean
    public PageableHandlerMethodArgumentResolver pageableResolver() {
        return new PageableHandlerMethodArgumentResolver(sortResolver());
    }

    @Bean
    public SortHandlerMethodArgumentResolver sortResolver() {
        return new SortHandlerMethodArgumentResolver();
    }

    @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter, org.springframework.web.servlet.config.annotation.WebMvcConfigurer
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(DistanceFormatter.INSTANCE);
        registry.addFormatter(PointFormatter.INSTANCE);
        if (!(registry instanceof FormattingConversionService)) {
            return;
        }
        FormattingConversionService conversionService = (FormattingConversionService) registry;
        DomainClassConverter<FormattingConversionService> converter = new DomainClassConverter<>(conversionService);
        converter.setApplicationContext(this.context);
    }

    @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter, org.springframework.web.servlet.config.annotation.WebMvcConfigurer
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) throws BeansException {
        argumentResolvers.add(sortResolver());
        argumentResolvers.add(pageableResolver());
        ProxyingHandlerMethodArgumentResolver resolver = new ProxyingHandlerMethodArgumentResolver(this.conversionService, true);
        resolver.setBeanFactory(this.context);
        resolver.setBeanClassLoader(this.context.getClassLoader());
        argumentResolvers.add(resolver);
    }

    @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter, org.springframework.web.servlet.config.annotation.WebMvcConfigurer
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) throws BeansException {
        if (ClassUtils.isPresent("com.jayway.jsonpath.DocumentContext", this.context.getClassLoader()) && ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", this.context.getClassLoader())) {
            ObjectMapper mapper = (ObjectMapper) getUniqueBean(ObjectMapper.class, this.context);
            ProjectingJackson2HttpMessageConverter converter = new ProjectingJackson2HttpMessageConverter(mapper == null ? new ObjectMapper() : mapper);
            converter.setBeanClassLoader(this.context.getClassLoader());
            converter.setBeanFactory(this.context);
            converters.add(0, converter);
        }
        if (ClassUtils.isPresent("org.xmlbeam.XBProjector", this.context.getClassLoader())) {
            converters.add(0, this.xmlBeamHttpMessageConverter == null ? new XmlBeamHttpMessageConverter() : this.xmlBeamHttpMessageConverter);
        }
    }

    private static <T> T getUniqueBean(Class<T> cls, ApplicationContext applicationContext) {
        try {
            return (T) applicationContext.getBean(cls);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }
}
