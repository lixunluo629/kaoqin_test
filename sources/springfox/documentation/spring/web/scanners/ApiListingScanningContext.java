package springfox.documentation.spring.web.scanners;

import com.google.common.collect.Ordering;
import java.util.List;
import java.util.Map;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ResourceGroup;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/scanners/ApiListingScanningContext.class */
public class ApiListingScanningContext {
    private final DocumentationContext documentationContext;
    private final Map<ResourceGroup, List<RequestMappingContext>> requestMappingsByResourceGroup;

    public ApiListingScanningContext(DocumentationContext documentationContext, Map<ResourceGroup, List<RequestMappingContext>> requestMappingsByResourceGroup) {
        this.documentationContext = documentationContext;
        this.requestMappingsByResourceGroup = requestMappingsByResourceGroup;
    }

    public Map<ResourceGroup, List<RequestMappingContext>> getRequestMappingsByResourceGroup() {
        return this.requestMappingsByResourceGroup;
    }

    public DocumentationContext getDocumentationContext() {
        return this.documentationContext;
    }

    public Ordering<ApiDescription> apiDescriptionOrdering() {
        return this.documentationContext.getApiDescriptionOrdering();
    }

    public DocumentationType getDocumentationType() {
        return this.documentationContext.getDocumentationType();
    }
}
