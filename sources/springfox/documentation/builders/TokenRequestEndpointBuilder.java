package springfox.documentation.builders;

import springfox.documentation.service.TokenRequestEndpoint;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/TokenRequestEndpointBuilder.class */
public class TokenRequestEndpointBuilder {
    private String url;
    private String clientIdName;
    private String clientSecretName;

    public TokenRequestEndpointBuilder url(String url) {
        this.url = (String) BuilderDefaults.defaultIfAbsent(url, this.url);
        return this;
    }

    public TokenRequestEndpointBuilder clientIdName(String clientIdName) {
        this.clientIdName = (String) BuilderDefaults.defaultIfAbsent(clientIdName, this.clientIdName);
        return this;
    }

    public TokenRequestEndpointBuilder clientSecretName(String clientSecretName) {
        this.clientSecretName = (String) BuilderDefaults.defaultIfAbsent(clientSecretName, this.clientSecretName);
        return this;
    }

    public TokenRequestEndpoint build() {
        return new TokenRequestEndpoint(this.url, this.clientIdName, this.clientSecretName);
    }
}
