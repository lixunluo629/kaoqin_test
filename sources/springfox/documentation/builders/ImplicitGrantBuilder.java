package springfox.documentation.builders;

import springfox.documentation.service.ImplicitGrant;
import springfox.documentation.service.LoginEndpoint;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/ImplicitGrantBuilder.class */
public class ImplicitGrantBuilder {
    private LoginEndpoint loginEndpoint;
    private String tokenName;

    public ImplicitGrantBuilder loginEndpoint(LoginEndpoint loginEndpoint) {
        this.loginEndpoint = (LoginEndpoint) BuilderDefaults.defaultIfAbsent(loginEndpoint, this.loginEndpoint);
        return this;
    }

    public ImplicitGrantBuilder tokenName(String tokenName) {
        this.tokenName = (String) BuilderDefaults.defaultIfAbsent(tokenName, this.tokenName);
        return this;
    }

    public ImplicitGrant build() {
        return new ImplicitGrant(this.loginEndpoint, this.tokenName);
    }
}
