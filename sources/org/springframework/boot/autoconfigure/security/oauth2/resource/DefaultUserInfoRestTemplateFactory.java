package org.springframework.boot.autoconfigure.security.oauth2.resource;

import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/DefaultUserInfoRestTemplateFactory.class */
public class DefaultUserInfoRestTemplateFactory implements UserInfoRestTemplateFactory {
    private static final AuthorizationCodeResourceDetails DEFAULT_RESOURCE_DETAILS;
    private final List<UserInfoRestTemplateCustomizer> customizers;
    private final OAuth2ProtectedResourceDetails details;
    private final OAuth2ClientContext oauth2ClientContext;
    private OAuth2RestTemplate oauth2RestTemplate;

    static {
        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        details.setClientId("<N/A>");
        details.setUserAuthorizationUri("Not a URI because there is no client");
        details.setAccessTokenUri("Not a URI because there is no client");
        DEFAULT_RESOURCE_DETAILS = details;
    }

    public DefaultUserInfoRestTemplateFactory(ObjectProvider<List<UserInfoRestTemplateCustomizer>> customizers, ObjectProvider<OAuth2ProtectedResourceDetails> details, ObjectProvider<OAuth2ClientContext> oauth2ClientContext) {
        this.customizers = customizers.getIfAvailable();
        this.details = details.getIfAvailable();
        this.oauth2ClientContext = oauth2ClientContext.getIfAvailable();
    }

    @Override // org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory
    public OAuth2RestTemplate getUserInfoRestTemplate() {
        if (this.oauth2RestTemplate == null) {
            this.oauth2RestTemplate = createOAuth2RestTemplate(this.details != null ? this.details : DEFAULT_RESOURCE_DETAILS);
            this.oauth2RestTemplate.getInterceptors().add(new ResourceServerTokenServicesConfiguration.AcceptJsonRequestInterceptor());
            AuthorizationCodeAccessTokenProvider accessTokenProvider = new AuthorizationCodeAccessTokenProvider();
            accessTokenProvider.setTokenRequestEnhancer(new ResourceServerTokenServicesConfiguration.AcceptJsonRequestEnhancer());
            this.oauth2RestTemplate.setAccessTokenProvider(accessTokenProvider);
            if (!CollectionUtils.isEmpty(this.customizers)) {
                AnnotationAwareOrderComparator.sort(this.customizers);
                for (UserInfoRestTemplateCustomizer customizer : this.customizers) {
                    customizer.customize(this.oauth2RestTemplate);
                }
            }
        }
        return this.oauth2RestTemplate;
    }

    private OAuth2RestTemplate createOAuth2RestTemplate(OAuth2ProtectedResourceDetails details) {
        if (this.oauth2ClientContext == null) {
            return new OAuth2RestTemplate(details);
        }
        return new OAuth2RestTemplate(details, this.oauth2ClientContext);
    }
}
