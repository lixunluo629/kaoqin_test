package org.springframework.boot.autoconfigure.mobile;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration;
import org.springframework.boot.autoconfigure.mustache.web.MustacheViewResolver;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.view.LiteDeviceDelegatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import org.springframework.web.servlet.view.groovy.GroovyMarkupViewResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

@EnableConfigurationProperties({DeviceDelegatingViewResolverProperties.class})
@Configuration
@ConditionalOnClass({LiteDeviceDelegatingViewResolver.class})
@AutoConfigureAfter({WebMvcAutoConfiguration.class, FreeMarkerAutoConfiguration.class, GroovyTemplateAutoConfiguration.class, MustacheAutoConfiguration.class, ThymeleafAutoConfiguration.class})
@ConditionalOnProperty(prefix = "spring.mobile.devicedelegatingviewresolver", name = {"enabled"}, havingValue = "true")
@ConditionalOnWebApplication
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mobile/DeviceDelegatingViewResolverAutoConfiguration.class */
public class DeviceDelegatingViewResolverAutoConfiguration {

    @Configuration
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mobile/DeviceDelegatingViewResolverAutoConfiguration$LiteDeviceDelegatingViewResolverFactoryConfiguration.class */
    protected static class LiteDeviceDelegatingViewResolverFactoryConfiguration {
        protected LiteDeviceDelegatingViewResolverFactoryConfiguration() {
        }

        @Bean
        public DeviceDelegatingViewResolverFactory deviceDelegatingViewResolverFactory(DeviceDelegatingViewResolverProperties properties) {
            return new DeviceDelegatingViewResolverFactory(properties);
        }
    }

    @Configuration
    @ConditionalOnClass({FreeMarkerViewResolver.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mobile/DeviceDelegatingViewResolverAutoConfiguration$DeviceDelegatingFreeMarkerViewResolverConfiguration.class */
    protected static class DeviceDelegatingFreeMarkerViewResolverConfiguration {
        protected DeviceDelegatingFreeMarkerViewResolverConfiguration() {
        }

        @ConditionalOnBean({FreeMarkerViewResolver.class})
        @Bean
        public LiteDeviceDelegatingViewResolver deviceDelegatingFreeMarkerViewResolver(DeviceDelegatingViewResolverFactory factory, FreeMarkerViewResolver viewResolver) {
            return factory.createViewResolver(viewResolver);
        }
    }

    @Configuration
    @ConditionalOnClass({GroovyMarkupViewResolver.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mobile/DeviceDelegatingViewResolverAutoConfiguration$DeviceDelegatingGroovyMarkupViewResolverConfiguration.class */
    protected static class DeviceDelegatingGroovyMarkupViewResolverConfiguration {
        protected DeviceDelegatingGroovyMarkupViewResolverConfiguration() {
        }

        @ConditionalOnBean({GroovyMarkupViewResolver.class})
        @Bean
        public LiteDeviceDelegatingViewResolver deviceDelegatingGroovyMarkupViewResolver(DeviceDelegatingViewResolverFactory factory, GroovyMarkupViewResolver viewResolver) {
            return factory.createViewResolver(viewResolver);
        }
    }

    @Configuration
    @ConditionalOnClass({InternalResourceViewResolver.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mobile/DeviceDelegatingViewResolverAutoConfiguration$DeviceDelegatingJspViewResolverConfiguration.class */
    protected static class DeviceDelegatingJspViewResolverConfiguration {
        protected DeviceDelegatingJspViewResolverConfiguration() {
        }

        @ConditionalOnBean({InternalResourceViewResolver.class})
        @Bean
        public LiteDeviceDelegatingViewResolver deviceDelegatingJspViewResolver(DeviceDelegatingViewResolverFactory factory, InternalResourceViewResolver viewResolver) {
            return factory.createViewResolver(viewResolver);
        }
    }

    @Configuration
    @ConditionalOnClass({MustacheViewResolver.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mobile/DeviceDelegatingViewResolverAutoConfiguration$DeviceDelegatingMustacheViewResolverConfiguration.class */
    protected static class DeviceDelegatingMustacheViewResolverConfiguration {
        protected DeviceDelegatingMustacheViewResolverConfiguration() {
        }

        @ConditionalOnBean({MustacheViewResolver.class})
        @Bean
        public LiteDeviceDelegatingViewResolver deviceDelegatingMustacheViewResolver(DeviceDelegatingViewResolverFactory factory, MustacheViewResolver viewResolver) {
            return factory.createViewResolver(viewResolver);
        }
    }

    @Configuration
    @ConditionalOnClass({ThymeleafViewResolver.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mobile/DeviceDelegatingViewResolverAutoConfiguration$DeviceDelegatingThymeleafViewResolverConfiguration.class */
    protected static class DeviceDelegatingThymeleafViewResolverConfiguration {
        protected DeviceDelegatingThymeleafViewResolverConfiguration() {
        }

        @ConditionalOnBean({ThymeleafViewResolver.class})
        @Bean
        public LiteDeviceDelegatingViewResolver deviceDelegatingThymeleafViewResolver(DeviceDelegatingViewResolverFactory factory, ThymeleafViewResolver viewResolver) {
            return factory.createViewResolver(viewResolver);
        }
    }
}
