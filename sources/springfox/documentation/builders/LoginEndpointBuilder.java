package springfox.documentation.builders;

import springfox.documentation.service.LoginEndpoint;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/LoginEndpointBuilder.class */
public class LoginEndpointBuilder {
    private String url;

    public LoginEndpointBuilder url(String url) {
        this.url = (String) BuilderDefaults.defaultIfAbsent(url, this.url);
        return this;
    }

    public LoginEndpoint build() {
        return new LoginEndpoint(this.url);
    }
}
