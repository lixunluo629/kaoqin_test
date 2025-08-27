package org.springframework.data.web.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.web.ProxyingHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Configuration
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/config/ProjectingArgumentResolverRegistrar.class */
public class ProjectingArgumentResolverRegistrar {
    @Bean
    public static ProjectingArgumentResolverBeanPostProcessor projectingArgumentResolverBeanPostProcessor(@Qualifier("mvcConversionService") ObjectFactory<ConversionService> conversionService) {
        return new ProjectingArgumentResolverBeanPostProcessor(conversionService);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/config/ProjectingArgumentResolverRegistrar$ProjectingArgumentResolverBeanPostProcessor.class */
    private static class ProjectingArgumentResolverBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware, BeanClassLoaderAware {
        private final ProxyingHandlerMethodArgumentResolver resolver;

        public ProjectingArgumentResolverBeanPostProcessor(@Qualifier("mvcConversionService") ObjectFactory<ConversionService> conversionService) {
            this.resolver = new ProxyingHandlerMethodArgumentResolver(conversionService, false);
        }

        @Override // org.springframework.beans.factory.BeanFactoryAware
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.resolver.setBeanFactory(beanFactory);
        }

        @Override // org.springframework.beans.factory.BeanClassLoaderAware
        public void setBeanClassLoader(ClassLoader classLoader) {
            this.resolver.setBeanClassLoader(classLoader);
        }

        @Override // org.springframework.beans.factory.config.BeanPostProcessor
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        @Override // org.springframework.beans.factory.config.BeanPostProcessor
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (!RequestMappingHandlerAdapter.class.isInstance(bean)) {
                return bean;
            }
            RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) bean;
            List<HandlerMethodArgumentResolver> currentResolvers = adapter.getArgumentResolvers();
            List<HandlerMethodArgumentResolver> newResolvers = new ArrayList<>(currentResolvers.size() + 1);
            newResolvers.add(this.resolver);
            newResolvers.addAll(currentResolvers);
            adapter.setArgumentResolvers(newResolvers);
            return adapter;
        }
    }
}
