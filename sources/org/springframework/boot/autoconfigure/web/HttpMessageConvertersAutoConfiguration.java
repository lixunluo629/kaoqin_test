package org.springframework.boot.autoconfigure.web;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

@Configuration
@ConditionalOnClass({HttpMessageConverter.class})
@AutoConfigureAfter({GsonAutoConfiguration.class, JacksonAutoConfiguration.class})
@Import({JacksonHttpMessageConvertersConfiguration.class, GsonHttpMessageConvertersConfiguration.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/HttpMessageConvertersAutoConfiguration.class */
public class HttpMessageConvertersAutoConfiguration {
    static final String PREFERRED_MAPPER_PROPERTY = "spring.http.converters.preferred-json-mapper";
    private final List<HttpMessageConverter<?>> converters;

    public HttpMessageConvertersAutoConfiguration(ObjectProvider<List<HttpMessageConverter<?>>> convertersProvider) {
        this.converters = convertersProvider.getIfAvailable();
    }

    @ConditionalOnMissingBean
    @Bean
    public HttpMessageConverters messageConverters() {
        return new HttpMessageConverters(this.converters != null ? this.converters : Collections.emptyList());
    }

    @EnableConfigurationProperties({HttpEncodingProperties.class})
    @Configuration
    @ConditionalOnClass({StringHttpMessageConverter.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/HttpMessageConvertersAutoConfiguration$StringHttpMessageConverterConfiguration.class */
    protected static class StringHttpMessageConverterConfiguration {
        private final HttpEncodingProperties encodingProperties;

        protected StringHttpMessageConverterConfiguration(HttpEncodingProperties encodingProperties) {
            this.encodingProperties = encodingProperties;
        }

        @ConditionalOnMissingBean
        @Bean
        public StringHttpMessageConverter stringHttpMessageConverter() {
            StringHttpMessageConverter converter = new StringHttpMessageConverter(this.encodingProperties.getCharset());
            converter.setWriteAcceptCharset(false);
            return converter;
        }
    }
}
