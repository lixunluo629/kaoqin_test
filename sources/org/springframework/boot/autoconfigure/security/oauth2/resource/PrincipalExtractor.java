package org.springframework.boot.autoconfigure.security.oauth2.resource;

import java.util.Map;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/PrincipalExtractor.class */
public interface PrincipalExtractor {
    Object extractPrincipal(Map<String, Object> map);
}
