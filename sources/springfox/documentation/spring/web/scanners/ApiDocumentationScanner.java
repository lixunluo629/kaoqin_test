package springfox.documentation.spring.web.scanners;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.DocumentationBuilder;
import springfox.documentation.builders.ResourceListingBuilder;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.ApiListingReference;
import springfox.documentation.service.Documentation;
import springfox.documentation.service.ResourceListing;
import springfox.documentation.service.Tags;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.Orderings;

@Component
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/scanners/ApiDocumentationScanner.class */
public class ApiDocumentationScanner {
    private ApiListingReferenceScanner apiListingReferenceScanner;
    private ApiListingScanner apiListingScanner;

    @Autowired
    public ApiDocumentationScanner(ApiListingReferenceScanner apiListingReferenceScanner, ApiListingScanner apiListingScanner) {
        this.apiListingReferenceScanner = apiListingReferenceScanner;
        this.apiListingScanner = apiListingScanner;
    }

    public Documentation scan(DocumentationContext context) {
        ApiListingReferenceScanResult result = this.apiListingReferenceScanner.scan(context);
        Collection<? extends ApiListingReference> apiListingReferences = result.getApiListingReferences();
        ApiListingScanningContext listingContext = new ApiListingScanningContext(context, result.getResourceGroupRequestMappings());
        Multimap<String, ApiListing> apiListings = this.apiListingScanner.scan(listingContext);
        DocumentationBuilder group = new DocumentationBuilder().name(context.getGroupName()).apiListingsByResourceGroupName(apiListings).produces(context.getProduces()).consumes(context.getConsumes()).schemes(context.getProtocols()).basePath(context.getPathProvider().getApplicationBasePath()).tags(Tags.toTags(apiListings));
        Set<ApiListingReference> apiReferenceSet = Sets.newTreeSet(Orderings.listingReferencePathComparator());
        apiReferenceSet.addAll(apiListingReferences);
        ResourceListing resourceListing = new ResourceListingBuilder().apiVersion(context.getApiInfo().getVersion()).apis(FluentIterable.from(apiReferenceSet).toSortedList(context.getListingReferenceOrdering())).securitySchemes(context.getSecuritySchemes()).info(context.getApiInfo()).build();
        group.resourceListing(resourceListing);
        return group.build();
    }
}
