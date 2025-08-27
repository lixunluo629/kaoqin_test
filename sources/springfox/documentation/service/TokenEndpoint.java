package springfox.documentation.service;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/TokenEndpoint.class */
public class TokenEndpoint {
    private final String url;
    private final String tokenName;

    public TokenEndpoint(String url, String tokenName) {
        this.url = url;
        this.tokenName = tokenName;
    }

    public String getUrl() {
        return this.url;
    }

    public String getTokenName() {
        return this.tokenName;
    }
}
