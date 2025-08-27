package springfox.documentation.service;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/ImplicitGrant.class */
public class ImplicitGrant extends GrantType {
    private final LoginEndpoint loginEndpoint;
    private final String tokenName;

    public ImplicitGrant(LoginEndpoint loginEndpoint, String tokenName) {
        super("implicit");
        this.loginEndpoint = loginEndpoint;
        this.tokenName = tokenName;
    }

    public LoginEndpoint getLoginEndpoint() {
        return this.loginEndpoint;
    }

    public String getTokenName() {
        return this.tokenName;
    }
}
