package org.springframework.boot.autoconfigure.security.oauth2.authserver;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.oauth2.authorization")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/authserver/AuthorizationServerProperties.class */
public class AuthorizationServerProperties {
    private String checkTokenAccess;
    private String tokenKeyAccess;
    private String realm;

    public String getCheckTokenAccess() {
        return this.checkTokenAccess;
    }

    public void setCheckTokenAccess(String checkTokenAccess) {
        this.checkTokenAccess = checkTokenAccess;
    }

    public String getTokenKeyAccess() {
        return this.tokenKeyAccess;
    }

    public void setTokenKeyAccess(String tokenKeyAccess) {
        this.tokenKeyAccess = tokenKeyAccess;
    }

    public String getRealm() {
        return this.realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }
}
