package org.springframework.boot.autoconfigure.security.oauth2;

import java.util.UUID;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.oauth2.client")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/OAuth2ClientProperties.class */
public class OAuth2ClientProperties {
    private String clientId;
    private String clientSecret = UUID.randomUUID().toString();
    private boolean defaultSecret = true;

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return this.clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        this.defaultSecret = false;
    }

    public boolean isDefaultSecret() {
        return this.defaultSecret;
    }
}
