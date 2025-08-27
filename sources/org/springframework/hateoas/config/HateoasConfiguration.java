package org.springframework.hateoas.config;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/config/HateoasConfiguration.class */
class HateoasConfiguration {
    HateoasConfiguration() {
    }

    @Bean
    public MessageSourceAccessor linkRelationMessageSource() {
        try {
            ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
            messageSource.setBasename("classpath:rest-messages");
            return new MessageSourceAccessor(messageSource);
        } catch (Exception o_O) {
            throw new BeanCreationException("resourceDescriptionMessageSourceAccessor", "", o_O);
        }
    }
}
