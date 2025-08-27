package org.springframework.boot.autoconfigure.security.oauth2.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.oauth2.sso")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/OAuth2SsoProperties.class */
public class OAuth2SsoProperties {
    public static final String DEFAULT_LOGIN_PATH = "/login";
    private String loginPath = DEFAULT_LOGIN_PATH;
    private Integer filterOrder;

    public String getLoginPath() {
        return this.loginPath;
    }

    public void setLoginPath(String loginPath) {
        this.loginPath = loginPath;
    }

    public Integer getFilterOrder() {
        return this.filterOrder;
    }

    public void setFilterOrder(Integer filterOrder) {
        this.filterOrder = filterOrder;
    }
}
