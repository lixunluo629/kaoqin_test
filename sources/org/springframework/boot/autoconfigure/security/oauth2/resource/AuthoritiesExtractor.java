package org.springframework.boot.autoconfigure.security.oauth2.resource;

import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/AuthoritiesExtractor.class */
public interface AuthoritiesExtractor {
    List<GrantedAuthority> extractAuthorities(Map<String, Object> map);
}
