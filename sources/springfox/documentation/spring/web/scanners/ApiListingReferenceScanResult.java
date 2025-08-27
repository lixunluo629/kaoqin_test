package springfox.documentation.spring.web.scanners;

import java.util.List;
import java.util.Map;
import springfox.documentation.service.ApiListingReference;
import springfox.documentation.service.ResourceGroup;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/scanners/ApiListingReferenceScanResult.class */
public class ApiListingReferenceScanResult {
    private final List<ApiListingReference> apiListingReferences;
    private final Map<ResourceGroup, List<RequestMappingContext>> resourceGroupRequestMappings;

    public ApiListingReferenceScanResult(List<ApiListingReference> apiListingReferences, Map<ResourceGroup, List<RequestMappingContext>> resourceGroupRequestMappings) {
        this.apiListingReferences = apiListingReferences;
        this.resourceGroupRequestMappings = resourceGroupRequestMappings;
    }

    public List<ApiListingReference> getApiListingReferences() {
        return this.apiListingReferences;
    }

    public Map<ResourceGroup, List<RequestMappingContext>> getResourceGroupRequestMappings() {
        return this.resourceGroupRequestMappings;
    }
}
