package springfox.documentation.builders;

import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeMultimap;
import java.util.Comparator;
import java.util.Set;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Documentation;
import springfox.documentation.service.ResourceListing;
import springfox.documentation.service.Tag;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/DocumentationBuilder.class */
public class DocumentationBuilder {
    private String groupName;
    private ResourceListing resourceListing;
    private String basePath;
    private Multimap<String, ApiListing> apiListings = TreeMultimap.create(Ordering.natural(), byListingPosition());
    private Set<Tag> tags = Sets.newHashSet();
    private Set<String> produces = Sets.newHashSet();
    private Set<String> consumes = Sets.newHashSet();
    private Set<String> schemes = Sets.newHashSet();

    public DocumentationBuilder name(String groupName) {
        this.groupName = (String) BuilderDefaults.defaultIfAbsent(groupName, this.groupName);
        return this;
    }

    public DocumentationBuilder apiListingsByResourceGroupName(Multimap<String, ApiListing> apiListings) {
        this.apiListings.putAll(BuilderDefaults.nullToEmptyMultimap(apiListings));
        return this;
    }

    public DocumentationBuilder resourceListing(ResourceListing resourceListing) {
        this.resourceListing = (ResourceListing) BuilderDefaults.defaultIfAbsent(resourceListing, this.resourceListing);
        return this;
    }

    public DocumentationBuilder tags(Set<Tag> tags) {
        this.tags.addAll(BuilderDefaults.nullToEmptySet(tags));
        return this;
    }

    public DocumentationBuilder produces(Set<String> mediaTypes) {
        this.produces.addAll(BuilderDefaults.nullToEmptySet(mediaTypes));
        return this;
    }

    public DocumentationBuilder consumes(Set<String> mediaTypes) {
        this.consumes.addAll(BuilderDefaults.nullToEmptySet(mediaTypes));
        return this;
    }

    public DocumentationBuilder schemes(Set<String> schemes) {
        this.schemes.addAll(BuilderDefaults.nullToEmptySet(schemes));
        return this;
    }

    public DocumentationBuilder basePath(String basePath) {
        this.basePath = (String) BuilderDefaults.defaultIfAbsent(basePath, this.basePath);
        return this;
    }

    public static Comparator<ApiListing> byListingPosition() {
        return new Comparator<ApiListing>() { // from class: springfox.documentation.builders.DocumentationBuilder.1
            @Override // java.util.Comparator
            public int compare(ApiListing first, ApiListing second) {
                return first.getPosition() - second.getPosition();
            }
        };
    }

    public Documentation build() {
        return new Documentation(this.groupName, this.basePath, this.tags, this.apiListings, this.resourceListing, this.produces, this.consumes, this.schemes);
    }
}
