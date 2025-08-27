package springfox.documentation.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.util.List;
import java.util.Set;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/Documentation.class */
public class Documentation {
    private final String groupName;
    private final String basePath;
    private final Multimap<String, ApiListing> apiListings;
    private final Set<Tag> tags;
    private final ResourceListing resourceListing;
    private final Set<String> produces;
    private final Set<String> consumes;
    private final Set<String> schemes;

    public Documentation(String groupName, String basePath, Set<Tag> tags, Multimap<String, ApiListing> apiListings, ResourceListing resourceListing, Set<String> produces, Set<String> consumes, Set<String> schemes) {
        this.groupName = groupName;
        this.basePath = basePath;
        this.tags = tags;
        this.apiListings = apiListings;
        this.resourceListing = resourceListing;
        this.produces = produces;
        this.consumes = consumes;
        this.schemes = schemes;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public Multimap<String, ApiListing> getApiListings() {
        return this.apiListings;
    }

    public ResourceListing getResourceListing() {
        return this.resourceListing;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public String getBasePath() {
        return this.basePath;
    }

    public List<String> getProduces() {
        return Lists.newArrayList(this.produces);
    }

    public List<String> getSchemes() {
        return Lists.newArrayList(this.schemes);
    }

    public List<String> getConsumes() {
        return Lists.newArrayList(this.consumes);
    }
}
