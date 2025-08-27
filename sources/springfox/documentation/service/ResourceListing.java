package springfox.documentation.service;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/ResourceListing.class */
public class ResourceListing {
    private final String apiVersion;
    private final List<ApiListingReference> apis;
    private final LinkedHashMap<String, SecurityScheme> securitySchemes;
    private final ApiInfo info;

    public ResourceListing(String apiVersion, List<ApiListingReference> apis, List<SecurityScheme> securitySchemes, ApiInfo info) {
        this.apiVersion = apiVersion;
        this.apis = apis;
        this.securitySchemes = initializeSecuritySchemes(securitySchemes);
        this.info = info;
    }

    private LinkedHashMap<String, SecurityScheme> initializeSecuritySchemes(List<SecurityScheme> securitySchemes) {
        LinkedHashMap<String, SecurityScheme> mapped = new LinkedHashMap<>();
        for (SecurityScheme securityScheme : (List) Optional.fromNullable(securitySchemes).or((Optional) Lists.newArrayList())) {
            mapped.put(securityScheme.getType(), securityScheme);
        }
        return mapped;
    }

    public String getApiVersion() {
        return this.apiVersion;
    }

    public List<ApiListingReference> getApis() {
        return this.apis;
    }

    public List<SecurityScheme> getSecuritySchemes() {
        return new ArrayList(this.securitySchemes.values());
    }

    public ApiInfo getInfo() {
        return this.info;
    }
}
