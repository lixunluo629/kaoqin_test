package springfox.documentation.builders;

import com.google.common.collect.Lists;
import java.util.List;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiListingReference;
import springfox.documentation.service.ResourceListing;
import springfox.documentation.service.SecurityScheme;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/ResourceListingBuilder.class */
public class ResourceListingBuilder {
    private String apiVersion;
    private List<ApiListingReference> apis = Lists.newArrayList();
    private List<SecurityScheme> securitySchemes = Lists.newArrayList();
    private ApiInfo info;

    public ResourceListingBuilder apiVersion(String apiVersion) {
        this.apiVersion = (String) BuilderDefaults.defaultIfAbsent(apiVersion, this.apiVersion);
        return this;
    }

    public ResourceListingBuilder apis(List<ApiListingReference> apis) {
        this.apis.addAll(BuilderDefaults.nullToEmptyList(apis));
        return this;
    }

    public ResourceListingBuilder securitySchemes(List<? extends SecurityScheme> authorizations) {
        this.securitySchemes.addAll(BuilderDefaults.nullToEmptyList(authorizations));
        return this;
    }

    public ResourceListingBuilder info(ApiInfo info) {
        this.info = (ApiInfo) BuilderDefaults.defaultIfAbsent(info, this.info);
        return this;
    }

    public ResourceListing build() {
        return new ResourceListing(this.apiVersion, this.apis, this.securitySchemes, this.info);
    }
}
