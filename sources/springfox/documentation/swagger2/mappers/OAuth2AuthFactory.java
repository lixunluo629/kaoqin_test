package springfox.documentation.swagger2.mappers;

import com.mysql.jdbc.NonRegisteringDriver;
import io.swagger.models.auth.OAuth2Definition;
import io.swagger.models.auth.SecuritySchemeDefinition;
import springfox.documentation.service.AuthorizationCodeGrant;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.ImplicitGrant;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.SecurityScheme;

/* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/OAuth2AuthFactory.class */
class OAuth2AuthFactory implements SecuritySchemeFactory {
    OAuth2AuthFactory() {
    }

    @Override // springfox.documentation.swagger2.mappers.SecuritySchemeFactory
    public SecuritySchemeDefinition create(SecurityScheme input) {
        OAuth oAuth = (OAuth) input;
        OAuth2Definition definition = new OAuth2Definition();
        for (GrantType each : oAuth.getGrantTypes()) {
            if ("authorization_code".equals(each.getType())) {
                definition.accessCode(((AuthorizationCodeGrant) each).getTokenRequestEndpoint().getUrl(), ((AuthorizationCodeGrant) each).getTokenEndpoint().getUrl());
            } else if ("implicit".equals(each.getType())) {
                definition.implicit(((ImplicitGrant) each).getLoginEndpoint().getUrl());
            } else if ("application".equals(each.getType())) {
                definition.application(each.getType());
            } else if (NonRegisteringDriver.PASSWORD_PROPERTY_KEY.equals(each.getType())) {
                definition.password(each.getType());
            } else {
                throw new IllegalArgumentException(String.format("Security scheme of type %s not supported", input.getClass().getSimpleName()));
            }
        }
        for (AuthorizationScope each2 : oAuth.getScopes()) {
            definition.addScope(each2.getScope(), each2.getDescription());
        }
        return definition;
    }
}
