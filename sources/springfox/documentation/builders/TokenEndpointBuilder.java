package springfox.documentation.builders;

import springfox.documentation.service.TokenEndpoint;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/TokenEndpointBuilder.class */
public class TokenEndpointBuilder {
    private String url;
    private String tokenName;

    public TokenEndpointBuilder url(String url) {
        this.url = (String) BuilderDefaults.defaultIfAbsent(url, this.url);
        return this;
    }

    public TokenEndpointBuilder tokenName(String tokenName) {
        this.tokenName = (String) BuilderDefaults.defaultIfAbsent(tokenName, this.tokenName);
        return this;
    }

    public TokenEndpoint build() {
        return new TokenEndpoint(this.url, this.tokenName);
    }
}
