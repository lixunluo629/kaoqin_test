package springfox.documentation.spring.web.scanners;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import springfox.documentation.PathProvider;
import springfox.documentation.RequestHandler;
import springfox.documentation.service.ApiListingReference;
import springfox.documentation.service.PathAdjuster;
import springfox.documentation.service.ResourceGroup;
import springfox.documentation.spi.service.ResourceGroupingStrategy;
import springfox.documentation.spi.service.contexts.ApiSelector;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.spring.web.paths.PathMappingAdjuster;

@Component
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/scanners/ApiListingReferenceScanner.class */
public class ApiListingReferenceScanner {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) ApiListingReferenceScanner.class);

    public ApiListingReferenceScanResult scan(DocumentationContext context) {
        LOG.info("Scanning for api listing references");
        List<ApiListingReference> apiListingReferences = Lists.newArrayList();
        ArrayListMultimap<ResourceGroup, RequestMappingContext> resourceGroupRequestMappings = ArrayListMultimap.create();
        ApiSelector selector = context.getApiSelector();
        Iterable<RequestHandler> matchingHandlers = FluentIterable.from(context.getRequestHandlers()).filter(selector.getRequestHandlerSelector());
        for (RequestHandler handler : matchingHandlers) {
            RequestMappingInfo requestMappingInfo = handler.getRequestMapping();
            HandlerMethod handlerMethod = handler.getHandlerMethod();
            ResourceGroupingStrategy resourceGroupingStrategy = context.getResourceGroupingStrategy();
            Set<ResourceGroup> resourceGroups = resourceGroupingStrategy.getResourceGroups(requestMappingInfo, handlerMethod);
            String handlerMethodName = handlerMethod.getMethod().getName();
            RequestMappingContext requestMappingContext = new RequestMappingContext(context, requestMappingInfo, handlerMethod);
            LOG.info("Request mapping: {} belongs to groups: [{}] ", handlerMethodName, resourceGroups);
            for (ResourceGroup group : resourceGroups) {
                resourceGroupRequestMappings.put(group, requestMappingContext);
            }
        }
        for (ResourceGroup resourceGroup : resourceGroupRequestMappings.keySet()) {
            String resourceGroupName = resourceGroup.getGroupName();
            String listingDescription = getResourceDescription(resourceGroupRequestMappings.get((Object) resourceGroup), context);
            Integer position = resourceGroup.getPosition();
            PathProvider pathProvider = context.getPathProvider();
            String path = pathProvider.getResourceListingPath(context.getGroupName(), resourceGroupName);
            LOG.info("Created resource listing Path: {} Description: {} Position: {}", path, resourceGroupName, position);
            PathAdjuster adjuster = new PathMappingAdjuster(context);
            apiListingReferences.add(new ApiListingReference(adjuster.adjustedPath(path), listingDescription, position.intValue()));
        }
        return new ApiListingReferenceScanResult(context.getListingReferenceOrdering().sortedCopy(apiListingReferences), Multimaps.asMap((ListMultimap) resourceGroupRequestMappings));
    }

    private String getResourceDescription(List<RequestMappingContext> requestMappings, DocumentationContext context) {
        Iterator<RequestMappingContext> iterator = requestMappings.iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        RequestMappingContext requestMapping = iterator.next();
        ResourceGroupingStrategy resourceGroupingStrategy = context.getResourceGroupingStrategy();
        return resourceGroupingStrategy.getResourceDescription(requestMapping.getRequestMappingInfo(), requestMapping.getHandlerMethod());
    }
}
