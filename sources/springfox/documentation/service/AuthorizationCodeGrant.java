package springfox.documentation.service;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/AuthorizationCodeGrant.class */
public class AuthorizationCodeGrant extends GrantType {
    private final TokenRequestEndpoint tokenRequestEndpoint;
    private final TokenEndpoint tokenEndpoint;

    public AuthorizationCodeGrant(TokenRequestEndpoint tokenRequestEndpoint, TokenEndpoint tokenEndpoint) {
        super("authorization_code");
        this.tokenRequestEndpoint = tokenRequestEndpoint;
        this.tokenEndpoint = tokenEndpoint;
    }

    public TokenRequestEndpoint getTokenRequestEndpoint() {
        return this.tokenRequestEndpoint;
    }

    public TokenEndpoint getTokenEndpoint() {
        return this.tokenEndpoint;
    }
}
