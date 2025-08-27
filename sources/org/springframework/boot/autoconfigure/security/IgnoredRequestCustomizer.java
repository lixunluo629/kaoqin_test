package org.springframework.boot.autoconfigure.security;

import org.springframework.security.config.annotation.web.builders.WebSecurity;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/IgnoredRequestCustomizer.class */
public interface IgnoredRequestCustomizer {
    void customize(WebSecurity.IgnoredRequestConfigurer ignoredRequestConfigurer);
}
