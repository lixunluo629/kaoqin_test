package org.springframework.boot.autoconfigure.security.oauth2.resource;

import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/JwtAccessTokenConverterConfigurer.class */
public interface JwtAccessTokenConverterConfigurer {
    void configure(JwtAccessTokenConverter jwtAccessTokenConverter);
}
