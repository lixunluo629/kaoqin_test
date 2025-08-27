package org.springframework.boot.autoconfigure.security.oauth2.authserver;

import com.mysql.jdbc.NonRegisteringDriver;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;

@EnableConfigurationProperties({AuthorizationServerProperties.class})
@Configuration
@ConditionalOnClass({EnableAuthorizationServer.class})
@ConditionalOnMissingBean({AuthorizationServerConfigurer.class})
@ConditionalOnBean({AuthorizationServerEndpointsConfiguration.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/authserver/OAuth2AuthorizationServerConfiguration.class */
public class OAuth2AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    private static final Log logger = LogFactory.getLog(OAuth2AuthorizationServerConfiguration.class);
    private final BaseClientDetails details;
    private final AuthenticationManager authenticationManager;
    private final TokenStore tokenStore;
    private final AccessTokenConverter tokenConverter;
    private final AuthorizationServerProperties properties;

    public OAuth2AuthorizationServerConfiguration(BaseClientDetails details, AuthenticationManager authenticationManager, ObjectProvider<TokenStore> tokenStore, ObjectProvider<AccessTokenConverter> tokenConverter, AuthorizationServerProperties properties) {
        this.details = details;
        this.authenticationManager = authenticationManager;
        this.tokenStore = tokenStore.getIfAvailable();
        this.tokenConverter = tokenConverter.getIfAvailable();
        this.properties = properties;
    }

    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        ClientDetailsServiceBuilder<InMemoryClientDetailsServiceBuilder>.ClientBuilder builder = clients.inMemory().withClient(this.details.getClientId());
        builder.secret(this.details.getClientSecret()).resourceIds((String[]) this.details.getResourceIds().toArray(new String[0])).authorizedGrantTypes((String[]) this.details.getAuthorizedGrantTypes().toArray(new String[0])).authorities((String[]) AuthorityUtils.authorityListToSet(this.details.getAuthorities()).toArray(new String[0])).scopes((String[]) this.details.getScope().toArray(new String[0]));
        if (this.details.getAutoApproveScopes() != null) {
            builder.autoApprove((String[]) this.details.getAutoApproveScopes().toArray(new String[0]));
        }
        if (this.details.getAccessTokenValiditySeconds() != null) {
            builder.accessTokenValiditySeconds(this.details.getAccessTokenValiditySeconds().intValue());
        }
        if (this.details.getRefreshTokenValiditySeconds() != null) {
            builder.refreshTokenValiditySeconds(this.details.getRefreshTokenValiditySeconds().intValue());
        }
        if (this.details.getRegisteredRedirectUri() != null) {
            builder.redirectUris((String[]) this.details.getRegisteredRedirectUri().toArray(new String[0]));
        }
    }

    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        if (this.tokenConverter != null) {
            endpoints.accessTokenConverter(this.tokenConverter);
        }
        if (this.tokenStore != null) {
            endpoints.tokenStore(this.tokenStore);
        }
        if (this.details.getAuthorizedGrantTypes().contains(NonRegisteringDriver.PASSWORD_PROPERTY_KEY)) {
            endpoints.authenticationManager(this.authenticationManager);
        }
    }

    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        if (this.properties.getCheckTokenAccess() != null) {
            security.checkTokenAccess(this.properties.getCheckTokenAccess());
        }
        if (this.properties.getTokenKeyAccess() != null) {
            security.tokenKeyAccess(this.properties.getTokenKeyAccess());
        }
        if (this.properties.getRealm() != null) {
            security.realm(this.properties.getRealm());
        }
    }

    @Configuration
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/authserver/OAuth2AuthorizationServerConfiguration$ClientDetailsLogger.class */
    protected static class ClientDetailsLogger {
        private final OAuth2ClientProperties credentials;

        protected ClientDetailsLogger(OAuth2ClientProperties credentials) {
            this.credentials = credentials;
        }

        @PostConstruct
        public void init() {
            boolean defaultSecret = this.credentials.isDefaultSecret();
            Log log = OAuth2AuthorizationServerConfiguration.logger;
            Object[] objArr = new Object[4];
            objArr[0] = "security.oauth2.client";
            objArr[1] = this.credentials.getClientId();
            objArr[2] = "security.oauth2.client";
            objArr[3] = defaultSecret ? this.credentials.getClientSecret() : "****";
            log.info(String.format("Initialized OAuth2 Client%n%n%s.client-id = %s%n%s.client-secret = %s%n%n", objArr));
        }
    }

    @ConditionalOnMissingBean({BaseClientDetails.class})
    @Configuration
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/authserver/OAuth2AuthorizationServerConfiguration$BaseClientDetailsConfiguration.class */
    protected static class BaseClientDetailsConfiguration {
        private final OAuth2ClientProperties client;

        protected BaseClientDetailsConfiguration(OAuth2ClientProperties client) {
            this.client = client;
        }

        @ConfigurationProperties(prefix = "security.oauth2.client")
        @Bean
        public BaseClientDetails oauth2ClientDetails() {
            BaseClientDetails details = new BaseClientDetails();
            if (this.client.getClientId() == null) {
                this.client.setClientId(UUID.randomUUID().toString());
            }
            details.setClientId(this.client.getClientId());
            details.setClientSecret(this.client.getClientSecret());
            details.setAuthorizedGrantTypes(Arrays.asList("authorization_code", NonRegisteringDriver.PASSWORD_PROPERTY_KEY, "client_credentials", "implicit", "refresh_token"));
            details.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
            details.setRegisteredRedirectUri(Collections.emptySet());
            return details;
        }
    }
}
