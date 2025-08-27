package springfox.documentation.builders;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import springfox.documentation.schema.Model;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.SecurityReference;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/ApiListingBuilder.class */
public class ApiListingBuilder {
    private final Ordering<ApiDescription> descriptionOrdering;
    private String apiVersion;
    private String basePath;
    private String resourcePath;
    private String description;
    private int position;
    private Set<String> produces = Sets.newHashSet();
    private Set<String> consumes = Sets.newHashSet();
    private Set<String> protocol = Sets.newHashSet();
    private Set<String> tags = Sets.newTreeSet();
    private List<SecurityReference> securityReferences = Lists.newArrayList();
    private List<ApiDescription> apis = Lists.newArrayList();
    private Map<String, Model> models = Maps.newHashMap();

    public ApiListingBuilder(Ordering<ApiDescription> descriptionOrdering) {
        this.descriptionOrdering = descriptionOrdering;
    }

    public ApiListingBuilder apiVersion(String apiVersion) {
        this.apiVersion = (String) BuilderDefaults.defaultIfAbsent(apiVersion, this.apiVersion);
        return this;
    }

    public ApiListingBuilder basePath(String basePath) {
        this.basePath = (String) BuilderDefaults.defaultIfAbsent(basePath, this.basePath);
        return this;
    }

    public ApiListingBuilder resourcePath(String resourcePath) {
        this.resourcePath = (String) BuilderDefaults.defaultIfAbsent(resourcePath, this.resourcePath);
        return this;
    }

    public ApiListingBuilder produces(Set<String> mediaTypes) {
        if (mediaTypes != null) {
            this.produces = Sets.newHashSet(mediaTypes);
        }
        return this;
    }

    public ApiListingBuilder consumes(Set<String> mediaTypes) {
        if (mediaTypes != null) {
            this.consumes = Sets.newHashSet(mediaTypes);
        }
        return this;
    }

    public ApiListingBuilder appendProduces(List<String> produces) {
        this.produces.addAll(BuilderDefaults.nullToEmptyList(produces));
        return this;
    }

    public ApiListingBuilder appendConsumes(List<String> consumes) {
        this.consumes.addAll(BuilderDefaults.nullToEmptyList(consumes));
        return this;
    }

    public ApiListingBuilder protocols(Set<String> protocols) {
        this.protocol.addAll(BuilderDefaults.nullToEmptySet(protocols));
        return this;
    }

    public ApiListingBuilder securityReferences(List<SecurityReference> securityReferences) {
        if (securityReferences != null) {
            this.securityReferences = Lists.newArrayList(securityReferences);
        }
        return this;
    }

    public ApiListingBuilder apis(List<ApiDescription> apis) {
        if (apis != null) {
            this.apis = this.descriptionOrdering.sortedCopy(apis);
        }
        return this;
    }

    public ApiListingBuilder models(Map<String, Model> models) {
        this.models.putAll(BuilderDefaults.nullToEmptyMap(models));
        return this;
    }

    public ApiListingBuilder description(String description) {
        this.description = (String) BuilderDefaults.defaultIfAbsent(description, this.description);
        return this;
    }

    public ApiListingBuilder position(int position) {
        this.position = position;
        return this;
    }

    public ApiListingBuilder tags(Set<String> tags) {
        this.tags = (Set) BuilderDefaults.defaultIfAbsent(tags, this.tags);
        return this;
    }

    public ApiListing build() {
        return new ApiListing(this.apiVersion, this.basePath, this.resourcePath, this.produces, this.consumes, this.protocol, this.securityReferences, this.apis, this.models, this.description, this.position, this.tags);
    }
}
