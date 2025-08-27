package org.springframework.boot.autoconfigure.security.oauth2.resource;

import java.util.Map;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/FixedPrincipalExtractor.class */
public class FixedPrincipalExtractor implements PrincipalExtractor {
    private static final String[] PRINCIPAL_KEYS = {"user", "username", "userid", "user_id", "login", "id", "name"};

    @Override // org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor
    public Object extractPrincipal(Map<String, Object> map) {
        for (String key : PRINCIPAL_KEYS) {
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        return null;
    }
}
