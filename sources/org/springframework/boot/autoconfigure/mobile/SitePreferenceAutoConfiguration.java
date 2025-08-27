package org.springframework.boot.autoconfigure.mobile;

import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.site.SitePreferenceHandlerInterceptor;
import org.springframework.mobile.device.site.SitePreferenceHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ConditionalOnClass({SitePreferenceHandlerInterceptor.class, SitePreferenceHandlerMethodArgumentResolver.class})
@AutoConfigureAfter({DeviceResolverAutoConfiguration.class})
@ConditionalOnProperty(prefix = "spring.mobile.sitepreference", name = {"enabled"}, havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mobile/SitePreferenceAutoConfiguration.class */
public class SitePreferenceAutoConfiguration {
    @ConditionalOnMissingBean({SitePreferenceHandlerInterceptor.class})
    @Bean
    public SitePreferenceHandlerInterceptor sitePreferenceHandlerInterceptor() {
        return new SitePreferenceHandlerInterceptor();
    }

    @Bean
    public SitePreferenceHandlerMethodArgumentResolver sitePreferenceHandlerMethodArgumentResolver() {
        return new SitePreferenceHandlerMethodArgumentResolver();
    }

    @Configuration
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mobile/SitePreferenceAutoConfiguration$SitePreferenceMvcConfiguration.class */
    protected static class SitePreferenceMvcConfiguration extends WebMvcConfigurerAdapter {
        private final SitePreferenceHandlerInterceptor sitePreferenceHandlerInterceptor;
        private final SitePreferenceHandlerMethodArgumentResolver sitePreferenceHandlerMethodArgumentResolver;

        protected SitePreferenceMvcConfiguration(SitePreferenceHandlerInterceptor sitePreferenceHandlerInterceptor, SitePreferenceHandlerMethodArgumentResolver sitePreferenceHandlerMethodArgumentResolver) {
            this.sitePreferenceHandlerInterceptor = sitePreferenceHandlerInterceptor;
            this.sitePreferenceHandlerMethodArgumentResolver = sitePreferenceHandlerMethodArgumentResolver;
        }

        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter, org.springframework.web.servlet.config.annotation.WebMvcConfigurer
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(this.sitePreferenceHandlerInterceptor);
        }

        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter, org.springframework.web.servlet.config.annotation.WebMvcConfigurer
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
            argumentResolvers.add(this.sitePreferenceHandlerMethodArgumentResolver);
        }
    }
}
