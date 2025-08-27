package org.springframework.boot.autoconfigure.security.oauth2.resource;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/SpringSocialTokenServices.class */
public class SpringSocialTokenServices implements ResourceServerTokenServices {
    private final OAuth2ConnectionFactory<?> connectionFactory;
    private final String clientId;

    public SpringSocialTokenServices(OAuth2ConnectionFactory<?> connectionFactory, String clientId) {
        this.connectionFactory = connectionFactory;
        this.clientId = clientId;
    }

    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        AccessGrant accessGrant = new AccessGrant(accessToken);
        Connection<?> connection = this.connectionFactory.createConnection(accessGrant);
        UserProfile user = connection.fetchUserProfile();
        return extractAuthentication(user);
    }

    private OAuth2Authentication extractAuthentication(UserProfile user) {
        String principal = user.getUsername();
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
        OAuth2Request request = new OAuth2Request((Map) null, this.clientId, (Collection) null, true, (Set) null, (Set) null, (String) null, (Set) null, (Map) null);
        return new OAuth2Authentication(request, new UsernamePasswordAuthenticationToken(principal, "N/A", authorities));
    }

    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }
}
