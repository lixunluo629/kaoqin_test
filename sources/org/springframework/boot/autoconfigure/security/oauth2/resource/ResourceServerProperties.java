package org.springframework.boot.autoconfigure.security.oauth2.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.tomcat.util.buf.AbstractChunk;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@ConfigurationProperties(prefix = "security.oauth2.resource")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/ResourceServerProperties.class */
public class ResourceServerProperties implements Validator, BeanFactoryAware {

    @JsonIgnore
    private final String clientId;

    @JsonIgnore
    private final String clientSecret;

    @JsonIgnore
    private ListableBeanFactory beanFactory;
    private String serviceId;
    private String id;
    private String userInfoUri;
    private String tokenInfoUri;
    private boolean preferTokenInfo;
    private String tokenType;
    private Jwt jwt;
    private Jwk jwk;
    private int filterOrder;

    public ResourceServerProperties() {
        this(null, null);
    }

    public ResourceServerProperties(String clientId, String clientSecret) {
        this.serviceId = "resource";
        this.preferTokenInfo = true;
        this.tokenType = "Bearer";
        this.jwt = new Jwt();
        this.jwk = new Jwk();
        this.filterOrder = AbstractChunk.ARRAY_MAX_SIZE;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }

    public String getResourceId() {
        return this.id;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserInfoUri() {
        return this.userInfoUri;
    }

    public void setUserInfoUri(String userInfoUri) {
        this.userInfoUri = userInfoUri;
    }

    public String getTokenInfoUri() {
        return this.tokenInfoUri;
    }

    public void setTokenInfoUri(String tokenInfoUri) {
        this.tokenInfoUri = tokenInfoUri;
    }

    public boolean isPreferTokenInfo() {
        return this.preferTokenInfo;
    }

    public void setPreferTokenInfo(boolean preferTokenInfo) {
        this.preferTokenInfo = preferTokenInfo;
    }

    public String getTokenType() {
        return this.tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Jwt getJwt() {
        return this.jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public Jwk getJwk() {
        return this.jwk;
    }

    public void setJwk(Jwk jwk) {
        this.jwk = jwk;
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getClientSecret() {
        return this.clientSecret;
    }

    public int getFilterOrder() {
        return this.filterOrder;
    }

    public void setFilterOrder(int filterOrder) {
        this.filterOrder = filterOrder;
    }

    @Override // org.springframework.validation.Validator
    public boolean supports(Class<?> clazz) {
        return ResourceServerProperties.class.isAssignableFrom(clazz);
    }

    @Override // org.springframework.validation.Validator
    public void validate(Object target, Errors errors) {
        if (countBeans(AuthorizationServerEndpointsConfiguration.class) > 0 || countBeans(ResourceServerTokenServicesConfiguration.class) == 0) {
            return;
        }
        ResourceServerProperties resource = (ResourceServerProperties) target;
        validate(resource, errors);
    }

    private void validate(ResourceServerProperties target, Errors errors) {
        if (!StringUtils.hasText(this.clientId)) {
            return;
        }
        boolean jwtConfigPresent = StringUtils.hasText(this.jwt.getKeyUri()) || StringUtils.hasText(this.jwt.getKeyValue());
        boolean jwkConfigPresent = StringUtils.hasText(this.jwk.getKeySetUri());
        if (jwtConfigPresent && jwkConfigPresent) {
            errors.reject("ambiguous.keyUri", "Only one of jwt.keyUri (or jwt.keyValue) and jwk.keySetUri should be configured.");
            return;
        }
        if (jwtConfigPresent || jwkConfigPresent) {
            return;
        }
        if (!StringUtils.hasText(target.getUserInfoUri()) && !StringUtils.hasText(target.getTokenInfoUri())) {
            errors.rejectValue("tokenInfoUri", "missing.tokenInfoUri", "Missing tokenInfoUri and userInfoUri and there is no JWT verifier key");
        }
        if (StringUtils.hasText(target.getTokenInfoUri()) && isPreferTokenInfo() && !StringUtils.hasText(this.clientSecret)) {
            errors.rejectValue("clientSecret", "missing.clientSecret", "Missing client secret");
        }
    }

    private int countBeans(Class<?> type) {
        return BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.beanFactory, type, true, false).length;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/ResourceServerProperties$Jwt.class */
    public class Jwt {
        private String keyValue;
        private String keyUri;

        public Jwt() {
        }

        public String getKeyValue() {
            return this.keyValue;
        }

        public void setKeyValue(String keyValue) {
            this.keyValue = keyValue;
        }

        public void setKeyUri(String keyUri) {
            this.keyUri = keyUri;
        }

        public String getKeyUri() {
            return this.keyUri;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/ResourceServerProperties$Jwk.class */
    public class Jwk {
        private String keySetUri;

        public Jwk() {
        }

        public String getKeySetUri() {
            return this.keySetUri;
        }

        public void setKeySetUri(String keySetUri) {
            this.keySetUri = keySetUri;
        }
    }
}
