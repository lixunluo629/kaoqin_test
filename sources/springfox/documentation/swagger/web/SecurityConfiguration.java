package springfox.documentation.swagger.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/web/SecurityConfiguration.class */
public class SecurityConfiguration {
    public static final SecurityConfiguration DEFAULT = new SecurityConfiguration();
    private String clientId;
    private String realm;
    private String appName;
    private String apiKey;

    private SecurityConfiguration() {
        this(null, null, null, null);
    }

    public SecurityConfiguration(String clientId, String realm, String appName, String apiKey) {
        this.clientId = clientId;
        this.realm = realm;
        this.appName = appName;
        this.apiKey = apiKey;
    }

    @JsonProperty("clientId")
    public String getClientId() {
        return this.clientId;
    }

    @JsonProperty("realm")
    public String getRealm() {
        return this.realm;
    }

    @JsonProperty("appName")
    public String getAppName() {
        return this.appName;
    }

    @JsonProperty("apiKey")
    public String getApiKey() {
        return this.apiKey;
    }
}
