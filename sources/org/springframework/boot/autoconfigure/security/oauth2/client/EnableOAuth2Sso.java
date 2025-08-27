package org.springframework.boot.autoconfigure.security.oauth2.client;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Target({ElementType.TYPE})
@EnableConfigurationProperties({OAuth2SsoProperties.class})
@EnableOAuth2Client
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({OAuth2SsoDefaultConfiguration.class, OAuth2SsoCustomConfiguration.class, ResourceServerTokenServicesConfiguration.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/EnableOAuth2Sso.class */
public @interface EnableOAuth2Sso {
}
