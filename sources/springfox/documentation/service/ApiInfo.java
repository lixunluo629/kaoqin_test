package springfox.documentation.service;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/ApiInfo.class */
public class ApiInfo {
    public static final ApiInfo DEFAULT = new ApiInfo("Api Documentation", "Api Documentation", "1.0", "urn:tos", "Contact Email", "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0");
    private final String version;
    private final String title;
    private final String description;
    private final String termsOfServiceUrl;
    private final String contact;
    private final String license;
    private final String licenseUrl;

    public ApiInfo(String title, String description, String version, String termsOfServiceUrl, String contact, String license, String licenseUrl) {
        this.title = title;
        this.description = description;
        this.version = version;
        this.termsOfServiceUrl = termsOfServiceUrl;
        this.contact = contact;
        this.license = license;
        this.licenseUrl = licenseUrl;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTermsOfServiceUrl() {
        return this.termsOfServiceUrl;
    }

    public String getContact() {
        return this.contact;
    }

    public String getLicense() {
        return this.license;
    }

    public String getLicenseUrl() {
        return this.licenseUrl;
    }

    public String getVersion() {
        return this.version;
    }
}
