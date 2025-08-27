package org.springframework.boot.autoconfigure.security.oauth2.resource;

import org.springframework.web.client.RestTemplate;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/JwtAccessTokenConverterRestTemplateCustomizer.class */
public interface JwtAccessTokenConverterRestTemplateCustomizer {
    void customize(RestTemplate restTemplate);
}
