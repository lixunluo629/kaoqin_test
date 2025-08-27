package org.springframework.boot.autoconfigure.security;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.util.ReflectionUtils;

@Configuration
@ConditionalOnMissingBean({AuthenticationManager.class})
@ConditionalOnBean({ObjectPostProcessor.class})
@Order(0)
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/AuthenticationManagerConfiguration.class */
public class AuthenticationManagerConfiguration {
    private static final Log logger = LogFactory.getLog(AuthenticationManagerConfiguration.class);

    @Bean
    @Primary
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public static SpringBootAuthenticationConfigurerAdapter springBootAuthenticationConfigurerAdapter(SecurityProperties securityProperties, List<SecurityPrerequisite> dependencies) {
        return new SpringBootAuthenticationConfigurerAdapter(securityProperties);
    }

    @Bean
    public AuthenticationManagerConfigurationListener authenticationManagerConfigurationListener() {
        return new AuthenticationManagerConfigurationListener();
    }

    @Order(2147483547)
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/AuthenticationManagerConfiguration$SpringBootAuthenticationConfigurerAdapter.class */
    private static class SpringBootAuthenticationConfigurerAdapter extends GlobalAuthenticationConfigurerAdapter {
        private final SecurityProperties securityProperties;

        SpringBootAuthenticationConfigurerAdapter(SecurityProperties securityProperties) {
            this.securityProperties = securityProperties;
        }

        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.apply(new DefaultInMemoryUserDetailsManagerConfigurer(this.securityProperties));
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/AuthenticationManagerConfiguration$DefaultInMemoryUserDetailsManagerConfigurer.class */
    private static class DefaultInMemoryUserDetailsManagerConfigurer extends InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> {
        private final SecurityProperties securityProperties;

        DefaultInMemoryUserDetailsManagerConfigurer(SecurityProperties securityProperties) {
            this.securityProperties = securityProperties;
        }

        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            if (auth.isConfigured()) {
                return;
            }
            SecurityProperties.User user = this.securityProperties.getUser();
            if (user.isDefaultPassword()) {
                AuthenticationManagerConfiguration.logger.info(String.format("%n%nUsing default security password: %s%n", user.getPassword()));
            }
            Set<String> roles = new LinkedHashSet<>(user.getRole());
            withUser(user.getName()).password(user.getPassword()).roles((String[]) roles.toArray(new String[roles.size()]));
            setField(auth, "defaultUserDetailsService", getUserDetailsService());
            super.configure(auth);
        }

        private void setField(Object target, String name, Object value) {
            try {
                Field field = ReflectionUtils.findField(target.getClass(), name);
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, target, value);
            } catch (Exception e) {
                AuthenticationManagerConfiguration.logger.info("Could not set " + name);
            }
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/AuthenticationManagerConfiguration$AuthenticationManagerConfigurationListener.class */
    protected static class AuthenticationManagerConfigurationListener implements SmartInitializingSingleton {

        @Autowired
        private AuthenticationEventPublisher eventPublisher;

        @Autowired
        private ApplicationContext context;

        protected AuthenticationManagerConfigurationListener() {
        }

        @Override // org.springframework.beans.factory.SmartInitializingSingleton
        public void afterSingletonsInstantiated() {
            try {
                configureAuthenticationManager((AuthenticationManager) this.context.getBean(AuthenticationManager.class));
            } catch (NoSuchBeanDefinitionException e) {
            }
        }

        private void configureAuthenticationManager(AuthenticationManager manager) {
            if (manager instanceof ProviderManager) {
                ((ProviderManager) manager).setAuthenticationEventPublisher(this.eventPublisher);
            }
        }
    }
}
