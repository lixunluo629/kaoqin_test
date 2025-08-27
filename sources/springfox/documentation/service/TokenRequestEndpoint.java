package springfox.documentation.service;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/TokenRequestEndpoint.class */
public class TokenRequestEndpoint {
    private final String url;
    private final String clientIdName;
    private final String clientSecretName;

    public TokenRequestEndpoint(String url, String clientIdName, String clientSecretName) {
        this.url = url;
        this.clientIdName = clientIdName;
        this.clientSecretName = clientSecretName;
    }

    public String getUrl() {
        return this.url;
    }

    public String getClientIdName() {
        return this.clientIdName;
    }

    public String getClientSecretName() {
        return this.clientSecretName;
    }
}
