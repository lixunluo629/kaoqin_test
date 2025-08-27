package springfox.documentation.spring.web.scanners;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.PathProvider;
import springfox.documentation.builders.ApiListingBuilder;
import springfox.documentation.schema.Model;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.PathAdjuster;
import springfox.documentation.service.ResourceGroup;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.ResourceGroupingStrategy;
import springfox.documentation.spi.service.contexts.ApiListingContext;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.Orderings;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.spring.web.paths.PathMappingAdjuster;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;

@Component
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/scanners/ApiListingScanner.class */
public class ApiListingScanner {
    private final ApiDescriptionReader apiDescriptionReader;
    private final ApiModelReader apiModelReader;
    private final DocumentationPluginsManager pluginsManager;

    @Autowired
    public ApiListingScanner(ApiDescriptionReader apiDescriptionReader, ApiModelReader apiModelReader, DocumentationPluginsManager pluginsManager) {
        this.apiDescriptionReader = apiDescriptionReader;
        this.apiModelReader = apiModelReader;
        this.pluginsManager = pluginsManager;
    }

    public Multimap<String, ApiListing> scan(ApiListingScanningContext context) {
        Multimap<String, ApiListing> apiListingMap = LinkedListMultimap.create();
        int position = 0;
        Map<ResourceGroup, List<RequestMappingContext>> requestMappingsByResourceGroup = context.getRequestMappingsByResourceGroup();
        List<SecurityReference> securityReferences = Lists.newArrayList();
        for (ResourceGroup resourceGroup : sortedByName(requestMappingsByResourceGroup.keySet())) {
            DocumentationContext documentationContext = context.getDocumentationContext();
            Set<String> produces = new LinkedHashSet<>(documentationContext.getProduces());
            Set<String> consumes = new LinkedHashSet<>(documentationContext.getConsumes());
            Set<String> protocols = new LinkedHashSet<>(documentationContext.getProtocols());
            Set<ApiDescription> apiDescriptions = Sets.newHashSet();
            ResourceGroupingStrategy resourceGroupingStrategy = documentationContext.getResourceGroupingStrategy();
            String listingDescription = null;
            Map<String, Model> models = new LinkedHashMap<>();
            for (RequestMappingContext each : sortedByMethods(requestMappingsByResourceGroup.get(resourceGroup))) {
                models.putAll(this.apiModelReader.read(each.withKnownModels(models)));
                apiDescriptions.addAll(this.apiDescriptionReader.read(each));
                listingDescription = resourceGroupingStrategy.getResourceDescription(each.getRequestMappingInfo(), each.getHandlerMethod());
            }
            List<ApiDescription> sortedApis = Lists.newArrayList(apiDescriptions);
            Collections.sort(sortedApis, documentationContext.getApiDescriptionOrdering());
            String resourcePath = longestCommonPath(sortedApis);
            PathProvider pathProvider = documentationContext.getPathProvider();
            String basePath = pathProvider.getApplicationBasePath();
            PathAdjuster adjuster = new PathMappingAdjuster(documentationContext);
            int i = position;
            position++;
            ApiListingBuilder apiListingBuilder = new ApiListingBuilder(context.apiDescriptionOrdering()).apiVersion(documentationContext.getApiInfo().getVersion()).basePath(adjuster.adjustedPath(basePath)).resourcePath(resourcePath).produces(produces).consumes(consumes).protocols(protocols).securityReferences(securityReferences).apis(sortedApis).models(models).description(listingDescription).position(i);
            ApiListingContext apiListingContext = new ApiListingContext(context.getDocumentationType(), resourceGroup, apiListingBuilder, resourceGroup);
            apiListingMap.put(resourceGroup.getGroupName(), this.pluginsManager.apiListing(apiListingContext));
        }
        return apiListingMap;
    }

    private Iterable<ResourceGroup> sortedByName(Set<ResourceGroup> resourceGroups) {
        return FluentIterable.from(resourceGroups).toSortedList(Orderings.resourceGroupComparator());
    }

    private Iterable<RequestMappingContext> sortedByMethods(List<RequestMappingContext> contexts) {
        return FluentIterable.from(contexts).toSortedList(Orderings.methodComparator());
    }

    static String longestCommonPath(List<ApiDescription> apiDescriptions) {
        List<String> commons = Lists.newArrayList();
        if (null == apiDescriptions || apiDescriptions.isEmpty()) {
            return null;
        }
        List<String> firstWords = urlParts(apiDescriptions.get(0));
        for (int position = 0; position < firstWords.size(); position++) {
            String word = firstWords.get(position);
            boolean allContain = true;
            for (int i = 1; i < apiDescriptions.size(); i++) {
                List<String> words = urlParts(apiDescriptions.get(i));
                if (words.size() < position + 1 || !words.get(position).equals(word)) {
                    allContain = false;
                    break;
                }
            }
            if (allContain) {
                commons.add(word);
            }
        }
        Joiner joiner = Joiner.on("/").skipNulls();
        return "/" + joiner.join(commons);
    }

    static List<String> urlParts(ApiDescription apiDescription) {
        return Splitter.on('/').omitEmptyStrings().trimResults().splitToList(apiDescription.getPath());
    }
}
