package org.springframework.boot.autoconfigure.dao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;

@Configuration
@ConditionalOnClass({PersistenceExceptionTranslationPostProcessor.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/dao/PersistenceExceptionTranslationAutoConfiguration.class */
public class PersistenceExceptionTranslationAutoConfiguration {
    @ConditionalOnMissingBean({PersistenceExceptionTranslationPostProcessor.class})
    @ConditionalOnProperty(prefix = "spring.dao.exceptiontranslation", name = {"enabled"}, matchIfMissing = true)
    @Bean
    public static PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor(Environment environment) {
        PersistenceExceptionTranslationPostProcessor postProcessor = new PersistenceExceptionTranslationPostProcessor();
        postProcessor.setProxyTargetClass(determineProxyTargetClass(environment));
        return postProcessor;
    }

    private static boolean determineProxyTargetClass(Environment environment) {
        RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment, "spring.aop.");
        Boolean value = (Boolean) resolver.getProperty("proxyTargetClass", Boolean.class);
        if (value != null) {
            return value.booleanValue();
        }
        return true;
    }
}
