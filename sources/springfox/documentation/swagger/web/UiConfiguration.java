package springfox.documentation.swagger.web;

import com.fasterxml.jackson.annotation.JsonProperty;

/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/web/UiConfiguration.class */
public class UiConfiguration {
    public static final UiConfiguration DEFAULT = new UiConfiguration(null);
    private String validatorUrl;

    public UiConfiguration(String validatorUrl) {
        this.validatorUrl = validatorUrl;
    }

    @JsonProperty("validatorUrl")
    public String getValidatorUrl() {
        return this.validatorUrl;
    }
}
