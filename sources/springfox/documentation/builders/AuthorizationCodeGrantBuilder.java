package springfox.documentation.builders;

import springfox.documentation.service.AuthorizationCodeGrant;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/AuthorizationCodeGrantBuilder.class */
public class AuthorizationCodeGrantBuilder {
    private TokenRequestEndpoint tokenRequestEndpoint;
    private TokenEndpoint tokenEndpoint;

    public AuthorizationCodeGrantBuilder tokenRequestEndpoint(TokenRequestEndpoint tokenRequestEndpoint) {
        this.tokenRequestEndpoint = (TokenRequestEndpoint) BuilderDefaults.defaultIfAbsent(tokenRequestEndpoint, this.tokenRequestEndpoint);
        return this;
    }

    public AuthorizationCodeGrantBuilder tokenEndpoint(TokenEndpoint tokenEndpoint) {
        this.tokenEndpoint = (TokenEndpoint) BuilderDefaults.defaultIfAbsent(tokenEndpoint, this.tokenEndpoint);
        return this;
    }

    public AuthorizationCodeGrant build() {
        return new AuthorizationCodeGrant(this.tokenRequestEndpoint, this.tokenEndpoint);
    }
}
