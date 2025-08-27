package springfox.documentation.builders;

import springfox.documentation.service.ApiInfo;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/ApiInfoBuilder.class */
public class ApiInfoBuilder {
    private String title;
    private String description;
    private String termsOfServiceUrl;
    private String contact;
    private String license;
    private String licenseUrl;
    private String version;

    public ApiInfoBuilder title(String title) {
        this.title = title;
        return this;
    }

    public ApiInfoBuilder description(String description) {
        this.description = description;
        return this;
    }

    public ApiInfoBuilder termsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
        return this;
    }

    public ApiInfoBuilder version(String version) {
        this.version = version;
        return this;
    }

    public ApiInfoBuilder contact(String contact) {
        this.contact = contact;
        return this;
    }

    public ApiInfoBuilder license(String license) {
        this.license = license;
        return this;
    }

    public ApiInfoBuilder licenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
        return this;
    }

    public ApiInfo build() {
        return new ApiInfo(this.title, this.description, this.version, this.termsOfServiceUrl, this.contact, this.license, this.licenseUrl);
    }
}
