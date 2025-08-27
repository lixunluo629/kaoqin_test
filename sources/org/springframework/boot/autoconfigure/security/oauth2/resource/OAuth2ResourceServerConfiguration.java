package org.springframework.boot.autoconfigure.security.oauth2.resource;

import io.jsonwebtoken.JwsHeader;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnClass({EnableResourceServer.class, SecurityProperties.class})
@Conditional({ResourceServerCondition.class})
@ConditionalOnBean({ResourceServerConfiguration.class})
@ConditionalOnWebApplication
@Import({ResourceServerTokenServicesConfiguration.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/OAuth2ResourceServerConfiguration.class */
public class OAuth2ResourceServerConfiguration {
    private final ResourceServerProperties resource;

    public OAuth2ResourceServerConfiguration(ResourceServerProperties resource) {
        this.resource = resource;
    }

    @ConditionalOnMissingBean({ResourceServerConfigurer.class})
    @Bean
    public ResourceServerConfigurer resourceServer() {
        return new ResourceSecurityConfigurer(this.resource);
    }

    @Bean
    public static ResourceServerFilterChainOrderProcessor resourceServerFilterChainOrderProcessor(ResourceServerProperties properties) {
        return new ResourceServerFilterChainOrderProcessor(properties);
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/OAuth2ResourceServerConfiguration$ResourceSecurityConfigurer.class */
    protected static class ResourceSecurityConfigurer extends ResourceServerConfigurerAdapter {
        private ResourceServerProperties resource;

        public ResourceSecurityConfigurer(ResourceServerProperties resource) {
            this.resource = resource;
        }

        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId(this.resource.getResourceId());
        }

        public void configure(HttpSecurity http) throws Exception {
            ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) http.authorizeRequests().anyRequest()).authenticated();
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/OAuth2ResourceServerConfiguration$ResourceServerFilterChainOrderProcessor.class */
    private static final class ResourceServerFilterChainOrderProcessor implements BeanPostProcessor, ApplicationContextAware {
        private final ResourceServerProperties properties;
        private ApplicationContext context;

        private ResourceServerFilterChainOrderProcessor(ResourceServerProperties properties) {
            this.properties = properties;
        }

        @Override // org.springframework.context.ApplicationContextAware
        public void setApplicationContext(ApplicationContext context) throws BeansException {
            this.context = context;
        }

        @Override // org.springframework.beans.factory.config.BeanPostProcessor
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        @Override // org.springframework.beans.factory.config.BeanPostProcessor
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if ((bean instanceof ResourceServerConfiguration) && this.context.getBeanNamesForType(ResourceServerConfiguration.class, false, false).length == 1) {
                ResourceServerConfiguration config = (ResourceServerConfiguration) bean;
                config.setOrder(this.properties.getFilterOrder());
            }
            return bean;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/OAuth2ResourceServerConfiguration$ResourceServerCondition.class */
    protected static class ResourceServerCondition extends SpringBootCondition implements ConfigurationCondition {
        private static final String AUTHORIZATION_ANNOTATION = "org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration";

        protected ResourceServerCondition() {
        }

        @Override // org.springframework.context.annotation.ConfigurationCondition
        public ConfigurationCondition.ConfigurationPhase getConfigurationPhase() {
            return ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN;
        }

        @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            ConditionMessage.Builder message = ConditionMessage.forCondition("OAuth ResourceServer Condition", new Object[0]);
            Environment environment = context.getEnvironment();
            RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment, "security.oauth2.resource.");
            if (hasOAuthClientId(environment)) {
                return ConditionOutcome.match(message.foundExactly("client-id property"));
            }
            if (!resolver.getSubProperties("jwt").isEmpty()) {
                return ConditionOutcome.match(message.foundExactly("JWT resource configuration"));
            }
            if (!resolver.getSubProperties(JwsHeader.JSON_WEB_KEY).isEmpty()) {
                return ConditionOutcome.match(message.foundExactly("JWK resource configuration"));
            }
            if (StringUtils.hasText(resolver.getProperty("user-info-uri"))) {
                return ConditionOutcome.match(message.foundExactly("user-info-uri property"));
            }
            if (StringUtils.hasText(resolver.getProperty("token-info-uri"))) {
                return ConditionOutcome.match(message.foundExactly("token-info-uri property"));
            }
            return (ClassUtils.isPresent(AUTHORIZATION_ANNOTATION, null) && AuthorizationServerEndpointsConfigurationBeanCondition.matches(context)) ? ConditionOutcome.match(message.found("class").items(AUTHORIZATION_ANNOTATION)) : ConditionOutcome.noMatch(message.didNotFind("client id, JWT resource or authorization server").atAll());
        }

        private boolean hasOAuthClientId(Environment environment) {
            RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment, "security.oauth2.client.");
            return StringUtils.hasLength(resolver.getProperty("client-id", ""));
        }
    }

    @ConditionalOnBean({AuthorizationServerEndpointsConfiguration.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/OAuth2ResourceServerConfiguration$AuthorizationServerEndpointsConfigurationBeanCondition.class */
    private static class AuthorizationServerEndpointsConfigurationBeanCondition {
        private AuthorizationServerEndpointsConfigurationBeanCondition() {
        }

        public static boolean matches(ConditionContext context) {
            Conditional conditional = (Conditional) AnnotationUtils.findAnnotation((Class<?>) AuthorizationServerEndpointsConfigurationBeanCondition.class, Conditional.class);
            StandardAnnotationMetadata metadata = new StandardAnnotationMetadata(AuthorizationServerEndpointsConfigurationBeanCondition.class);
            for (Class<? extends Condition> conditionType : conditional.value()) {
                Condition condition = (Condition) BeanUtils.instantiateClass(conditionType);
                if (condition.matches(context, metadata)) {
                    return true;
                }
            }
            return false;
        }
    }
}
