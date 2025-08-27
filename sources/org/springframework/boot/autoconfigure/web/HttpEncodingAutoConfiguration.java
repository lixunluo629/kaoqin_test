package org.springframework.boot.autoconfigure.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.HttpEncodingProperties;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.filter.OrderedCharacterEncodingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.CharacterEncodingFilter;

@EnableConfigurationProperties({HttpEncodingProperties.class})
@Configuration
@ConditionalOnClass({CharacterEncodingFilter.class})
@ConditionalOnProperty(prefix = "spring.http.encoding", value = {"enabled"}, matchIfMissing = true)
@ConditionalOnWebApplication
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/HttpEncodingAutoConfiguration.class */
public class HttpEncodingAutoConfiguration {
    private final HttpEncodingProperties properties;

    public HttpEncodingAutoConfiguration(HttpEncodingProperties properties) {
        this.properties = properties;
    }

    @ConditionalOnMissingBean({CharacterEncodingFilter.class})
    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
        filter.setEncoding(this.properties.getCharset().name());
        filter.setForceRequestEncoding(this.properties.shouldForce(HttpEncodingProperties.Type.REQUEST));
        filter.setForceResponseEncoding(this.properties.shouldForce(HttpEncodingProperties.Type.RESPONSE));
        return filter;
    }

    @Bean
    public LocaleCharsetMappingsCustomizer localeCharsetMappingsCustomizer() {
        return new LocaleCharsetMappingsCustomizer(this.properties);
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/HttpEncodingAutoConfiguration$LocaleCharsetMappingsCustomizer.class */
    private static class LocaleCharsetMappingsCustomizer implements EmbeddedServletContainerCustomizer, Ordered {
        private final HttpEncodingProperties properties;

        LocaleCharsetMappingsCustomizer(HttpEncodingProperties properties) {
            this.properties = properties;
        }

        @Override // org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer
        public void customize(ConfigurableEmbeddedServletContainer container) {
            if (this.properties.getMapping() != null) {
                container.setLocaleCharsetMappings(this.properties.getMapping());
            }
        }

        @Override // org.springframework.core.Ordered
        public int getOrder() {
            return 0;
        }
    }
}
