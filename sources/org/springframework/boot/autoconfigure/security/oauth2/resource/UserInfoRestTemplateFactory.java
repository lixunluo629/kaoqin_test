package org.springframework.boot.autoconfigure.security.oauth2.resource;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/UserInfoRestTemplateFactory.class */
public interface UserInfoRestTemplateFactory {
    OAuth2RestTemplate getUserInfoRestTemplate();
}
