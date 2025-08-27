package org.springframework.boot.autoconfigure.security;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;

@Configuration
@ConditionalOnClass({GlobalAuthenticationConfigurerAdapter.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/BootGlobalAuthenticationConfiguration.class */
public class BootGlobalAuthenticationConfiguration {
    @Bean
    public static BootGlobalAuthenticationConfigurationAdapter bootGlobalAuthenticationConfigurationAdapter(ApplicationContext context) {
        return new BootGlobalAuthenticationConfigurationAdapter(context);
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/BootGlobalAuthenticationConfiguration$BootGlobalAuthenticationConfigurationAdapter.class */
    private static class BootGlobalAuthenticationConfigurationAdapter extends GlobalAuthenticationConfigurerAdapter {
        private static final Log logger = LogFactory.getLog(BootGlobalAuthenticationConfiguration.class);
        private final ApplicationContext context;

        BootGlobalAuthenticationConfigurationAdapter(ApplicationContext context) {
            this.context = context;
        }

        public void init(AuthenticationManagerBuilder auth) {
            Map<String, Object> beansWithAnnotation = this.context.getBeansWithAnnotation(EnableAutoConfiguration.class);
            if (logger.isDebugEnabled()) {
                logger.debug("Eagerly initializing " + beansWithAnnotation);
            }
        }
    }
}
