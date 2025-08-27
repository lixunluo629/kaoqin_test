package org.springframework.boot.autoconfigure.web;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@EnableConfigurationProperties
@Configuration
@ConditionalOnWebApplication
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerPropertiesAutoConfiguration.class */
public class ServerPropertiesAutoConfiguration {
    @ConditionalOnMissingBean(search = SearchStrategy.CURRENT)
    @Bean
    public ServerProperties serverProperties() {
        return new ServerProperties();
    }

    @Bean
    public DuplicateServerPropertiesDetector duplicateServerPropertiesDetector() {
        return new DuplicateServerPropertiesDetector();
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ServerPropertiesAutoConfiguration$DuplicateServerPropertiesDetector.class */
    private static class DuplicateServerPropertiesDetector implements EmbeddedServletContainerCustomizer, Ordered, ApplicationContextAware {
        private ApplicationContext applicationContext;

        private DuplicateServerPropertiesDetector() {
        }

        @Override // org.springframework.core.Ordered
        public int getOrder() {
            return 0;
        }

        @Override // org.springframework.context.ApplicationContextAware
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }

        @Override // org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer
        public void customize(ConfigurableEmbeddedServletContainer container) {
            String[] serverPropertiesBeans = this.applicationContext.getBeanNamesForType(ServerProperties.class);
            Assert.state(serverPropertiesBeans.length != 0, "No ServerProperties registered");
            Assert.state(serverPropertiesBeans.length == 1, "Multiple ServerProperties registered " + StringUtils.arrayToCommaDelimitedString(serverPropertiesBeans));
        }
    }
}
