package springfox.documentation.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import springfox.documentation.schema.Model;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/ApiListing.class */
public class ApiListing {
    private final String apiVersion;
    private final String basePath;
    private final String resourcePath;
    private final Set<String> produces;
    private final Set<String> consumes;
    private final Set<String> protocols;
    private final List<SecurityReference> securityReferences;
    private final List<ApiDescription> apis;
    private final Map<String, Model> models;
    private final String description;
    private final int position;
    private final Set<String> tags;

    public ApiListing(String apiVersion, String basePath, String resourcePath, Set<String> produces, Set<String> consumes, Set<String> protocols, List<SecurityReference> securityReferences, List<ApiDescription> apis, Map<String, Model> models, String description, int position, Set<String> tags) {
        this.apiVersion = apiVersion;
        this.basePath = basePath;
        this.resourcePath = resourcePath;
        this.produces = produces;
        this.consumes = consumes;
        this.protocols = protocols;
        this.securityReferences = securityReferences;
        this.apis = apis;
        this.models = models;
        this.description = description;
        this.position = position;
        this.tags = tags;
    }

    public String getApiVersion() {
        return this.apiVersion;
    }

    public String getBasePath() {
        return this.basePath;
    }

    public String getResourcePath() {
        return this.resourcePath;
    }

    public Set<String> getProduces() {
        return this.produces;
    }

    public Set<String> getConsumes() {
        return this.consumes;
    }

    public Set<String> getProtocols() {
        return this.protocols;
    }

    public List<SecurityReference> getSecurityReferences() {
        return this.securityReferences;
    }

    public List<ApiDescription> getApis() {
        return this.apis;
    }

    public Map<String, Model> getModels() {
        return this.models;
    }

    public String getDescription() {
        return this.description;
    }

    public int getPosition() {
        return this.position;
    }

    public Set<String> getTags() {
        return this.tags;
    }
}
