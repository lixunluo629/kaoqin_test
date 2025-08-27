package org.springframework.boot.autoconfigure.security.oauth2.client;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.NoneNestedConditions;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.OAuth2ClientConfiguration;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnClass({EnableOAuth2Client.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/OAuth2RestOperationsConfiguration.class */
public class OAuth2RestOperationsConfiguration {

    @Configuration
    @Conditional({ClientCredentialsCondition.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/OAuth2RestOperationsConfiguration$SingletonScopedConfiguration.class */
    protected static class SingletonScopedConfiguration {
        protected SingletonScopedConfiguration() {
        }

        @ConfigurationProperties(prefix = "security.oauth2.client")
        @Bean
        @Primary
        public ClientCredentialsResourceDetails oauth2RemoteResource() {
            ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
            return details;
        }

        @Bean
        public DefaultOAuth2ClientContext oauth2ClientContext() {
            return new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest());
        }
    }

    @Configuration
    @ConditionalOnBean({OAuth2ClientConfiguration.class})
    @Conditional({OAuth2ClientIdCondition.class, NoClientCredentialsCondition.class})
    @Import({OAuth2ProtectedResourceDetailsConfiguration.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/OAuth2RestOperationsConfiguration$SessionScopedConfiguration.class */
    protected static class SessionScopedConfiguration {
        protected SessionScopedConfiguration() {
        }

        @Bean
        public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter, SecurityProperties security) {
            FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(filter);
            registration.setOrder(security.getFilterOrder() - 10);
            return registration;
        }

        @Configuration
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/OAuth2RestOperationsConfiguration$SessionScopedConfiguration$ClientContextConfiguration.class */
        protected static class ClientContextConfiguration {
            private final AccessTokenRequest accessTokenRequest;

            public ClientContextConfiguration(@Qualifier("accessTokenRequest") ObjectProvider<AccessTokenRequest> accessTokenRequest) {
                this.accessTokenRequest = accessTokenRequest.getIfAvailable();
            }

            @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
            @Bean
            public DefaultOAuth2ClientContext oauth2ClientContext() {
                return new DefaultOAuth2ClientContext(this.accessTokenRequest);
            }
        }
    }

    @Configuration
    @ConditionalOnMissingBean({OAuth2ClientConfiguration.class})
    @Conditional({OAuth2ClientIdCondition.class, NoClientCredentialsCondition.class})
    @Import({OAuth2ProtectedResourceDetailsConfiguration.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/OAuth2RestOperationsConfiguration$RequestScopedConfiguration.class */
    protected static class RequestScopedConfiguration {
        protected RequestScopedConfiguration() {
        }

        @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
        @Bean
        public DefaultOAuth2ClientContext oauth2ClientContext() {
            DefaultOAuth2ClientContext context = new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest());
            OAuth2Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof OAuth2Authentication) {
                OAuth2Authentication authentication2 = authentication;
                Object details = authentication2.getDetails();
                if (details instanceof OAuth2AuthenticationDetails) {
                    OAuth2AuthenticationDetails oauthsDetails = (OAuth2AuthenticationDetails) details;
                    String token = oauthsDetails.getTokenValue();
                    context.setAccessToken(new DefaultOAuth2AccessToken(token));
                }
            }
            return context;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/OAuth2RestOperationsConfiguration$OAuth2ClientIdCondition.class */
    static class OAuth2ClientIdCondition extends SpringBootCondition {
        OAuth2ClientIdCondition() {
        }

        @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            PropertyResolver resolver = new RelaxedPropertyResolver(context.getEnvironment(), "security.oauth2.client.");
            String clientId = resolver.getProperty("client-id");
            ConditionMessage.Builder message = ConditionMessage.forCondition("OAuth Client ID", new Object[0]);
            if (StringUtils.hasLength(clientId)) {
                return ConditionOutcome.match(message.foundExactly("security.oauth2.client.client-id property"));
            }
            return ConditionOutcome.noMatch(message.didNotFind("security.oauth2.client.client-id property").atAll());
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/OAuth2RestOperationsConfiguration$NoClientCredentialsCondition.class */
    static class NoClientCredentialsCondition extends NoneNestedConditions {
        NoClientCredentialsCondition() {
            super(ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION);
        }

        @Conditional({ClientCredentialsCondition.class})
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/OAuth2RestOperationsConfiguration$NoClientCredentialsCondition$ClientCredentialsActivated.class */
        static class ClientCredentialsActivated {
            ClientCredentialsActivated() {
            }
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/OAuth2RestOperationsConfiguration$ClientCredentialsCondition.class */
    static class ClientCredentialsCondition extends AnyNestedCondition {
        ClientCredentialsCondition() {
            super(ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION);
        }

        @ConditionalOnProperty(prefix = "security.oauth2.client", name = {"grant-type"}, havingValue = "client_credentials", matchIfMissing = false)
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/OAuth2RestOperationsConfiguration$ClientCredentialsCondition$ClientCredentialsConfigured.class */
        static class ClientCredentialsConfigured {
            ClientCredentialsConfigured() {
            }
        }

        @ConditionalOnNotWebApplication
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/OAuth2RestOperationsConfiguration$ClientCredentialsCondition$NoWebApplication.class */
        static class NoWebApplication {
            NoWebApplication() {
            }
        }
    }
}
