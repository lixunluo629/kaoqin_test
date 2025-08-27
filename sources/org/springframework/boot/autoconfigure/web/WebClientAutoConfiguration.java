package org.springframework.boot.autoconfigure.web;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

@Configuration
@AutoConfigureAfter({HttpMessageConvertersAutoConfiguration.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/WebClientAutoConfiguration.class */
public class WebClientAutoConfiguration {

    @Configuration
    @ConditionalOnClass({RestTemplate.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/WebClientAutoConfiguration$RestTemplateConfiguration.class */
    public static class RestTemplateConfiguration {
        private final ObjectProvider<HttpMessageConverters> messageConverters;
        private final ObjectProvider<List<RestTemplateCustomizer>> restTemplateCustomizers;

        public RestTemplateConfiguration(ObjectProvider<HttpMessageConverters> messageConverters, ObjectProvider<List<RestTemplateCustomizer>> restTemplateCustomizers) {
            this.messageConverters = messageConverters;
            this.restTemplateCustomizers = restTemplateCustomizers;
        }

        @ConditionalOnMissingBean
        @Bean
        public RestTemplateBuilder restTemplateBuilder() throws BeansException {
            RestTemplateBuilder builder = new RestTemplateBuilder(new RestTemplateCustomizer[0]);
            HttpMessageConverters converters = this.messageConverters.getIfUnique();
            if (converters != null) {
                builder = builder.messageConverters(converters.getConverters());
            }
            List<RestTemplateCustomizer> customizers = this.restTemplateCustomizers.getIfAvailable();
            if (!CollectionUtils.isEmpty(customizers)) {
                List<RestTemplateCustomizer> customizers2 = new ArrayList<>(customizers);
                AnnotationAwareOrderComparator.sort(customizers2);
                builder = builder.customizers(customizers2);
            }
            return builder;
        }
    }
}
