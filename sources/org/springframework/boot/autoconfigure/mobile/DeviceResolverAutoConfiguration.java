package org.springframework.boot.autoconfigure.mobile;

import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ConditionalOnClass({DeviceResolverHandlerInterceptor.class, DeviceHandlerMethodArgumentResolver.class})
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
@ConditionalOnWebApplication
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mobile/DeviceResolverAutoConfiguration.class */
public class DeviceResolverAutoConfiguration {
    @ConditionalOnMissingBean({DeviceResolverHandlerInterceptor.class})
    @Bean
    public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
        return new DeviceResolverHandlerInterceptor();
    }

    @Bean
    public DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver() {
        return new DeviceHandlerMethodArgumentResolver();
    }

    @Configuration
    @Order(0)
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mobile/DeviceResolverAutoConfiguration$DeviceResolverMvcConfiguration.class */
    protected static class DeviceResolverMvcConfiguration extends WebMvcConfigurerAdapter {
        private DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor;
        private DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver;

        protected DeviceResolverMvcConfiguration(DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor, DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver) {
            this.deviceResolverHandlerInterceptor = deviceResolverHandlerInterceptor;
            this.deviceHandlerMethodArgumentResolver = deviceHandlerMethodArgumentResolver;
        }

        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter, org.springframework.web.servlet.config.annotation.WebMvcConfigurer
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(this.deviceResolverHandlerInterceptor);
        }

        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter, org.springframework.web.servlet.config.annotation.WebMvcConfigurer
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
            argumentResolvers.add(this.deviceHandlerMethodArgumentResolver);
        }
    }
}
