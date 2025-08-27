package org.springframework.boot.autoconfigure.web;

import com.google.gson.Gson;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
@ConditionalOnClass({Gson.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/GsonHttpMessageConvertersConfiguration.class */
class GsonHttpMessageConvertersConfiguration {
    GsonHttpMessageConvertersConfiguration() {
    }

    @Configuration
    @ConditionalOnBean({Gson.class})
    @Conditional({PreferGsonOrMissingJacksonCondition.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/GsonHttpMessageConvertersConfiguration$GsonHttpMessageConverterConfiguration.class */
    protected static class GsonHttpMessageConverterConfiguration {
        protected GsonHttpMessageConverterConfiguration() {
        }

        @ConditionalOnMissingBean
        @Bean
        public GsonHttpMessageConverter gsonHttpMessageConverter(Gson gson) {
            GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
            converter.setGson(gson);
            return converter;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/GsonHttpMessageConvertersConfiguration$PreferGsonOrMissingJacksonCondition.class */
    private static class PreferGsonOrMissingJacksonCondition extends AnyNestedCondition {
        PreferGsonOrMissingJacksonCondition() {
            super(ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
        }

        @ConditionalOnProperty(name = {"spring.http.converters.preferred-json-mapper"}, havingValue = "gson", matchIfMissing = false)
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/GsonHttpMessageConvertersConfiguration$PreferGsonOrMissingJacksonCondition$GsonPreferred.class */
        static class GsonPreferred {
            GsonPreferred() {
            }
        }

        @ConditionalOnMissingBean({MappingJackson2HttpMessageConverter.class})
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/GsonHttpMessageConvertersConfiguration$PreferGsonOrMissingJacksonCondition$JacksonMissing.class */
        static class JacksonMissing {
            JacksonMissing() {
            }
        }
    }
}
