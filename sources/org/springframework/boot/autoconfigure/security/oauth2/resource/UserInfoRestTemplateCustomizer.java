package org.springframework.boot.autoconfigure.security.oauth2.resource;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/UserInfoRestTemplateCustomizer.class */
public interface UserInfoRestTemplateCustomizer {
    void customize(OAuth2RestTemplate oAuth2RestTemplate);
}
