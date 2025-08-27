package springfox.documentation.spi.service.contexts;

import springfox.documentation.builders.ApiListingBuilder;
import springfox.documentation.service.ResourceGroup;
import springfox.documentation.spi.DocumentationType;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/contexts/ApiListingContext.class */
public class ApiListingContext {
    private final DocumentationType documentationType;
    private final ResourceGroup resourceGroup;
    private final ApiListingBuilder apiListingBuilder;
    private final ResourceGroup group;

    public ApiListingContext(DocumentationType documentationType, ResourceGroup resourceGroup, ApiListingBuilder apiListingBuilder, ResourceGroup group) {
        this.documentationType = documentationType;
        this.resourceGroup = resourceGroup;
        this.apiListingBuilder = apiListingBuilder;
        this.group = group;
    }

    public DocumentationType getDocumentationType() {
        return this.documentationType;
    }

    public ResourceGroup getResourceGroup() {
        return this.resourceGroup;
    }

    public ApiListingBuilder apiListingBuilder() {
        return this.apiListingBuilder;
    }

    public ResourceGroup getGroup() {
        return this.group;
    }
}
