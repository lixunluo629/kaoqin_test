package org.springframework.boot.autoconfigure.security.oauth2.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

@Configuration
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/OAuth2ProtectedResourceDetailsConfiguration.class */
class OAuth2ProtectedResourceDetailsConfiguration {
    OAuth2ProtectedResourceDetailsConfiguration() {
    }

    @ConfigurationProperties(prefix = "security.oauth2.client")
    @Bean
    @Primary
    public AuthorizationCodeResourceDetails oauth2RemoteResource() {
        return new AuthorizationCodeResourceDetails();
    }
}
