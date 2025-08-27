package springfox.documentation.spring.web.readers.operation;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ResourceGroup;
import springfox.documentation.spi.service.ResourceGroupingStrategy;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;

@Component
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/operation/DefaultTagsProvider.class */
public class DefaultTagsProvider {
    private final DocumentationPluginsManager pluginsManager;

    @Autowired
    public DefaultTagsProvider(DocumentationPluginsManager pluginsManager) {
        this.pluginsManager = pluginsManager;
    }

    public ImmutableSet<String> tags(OperationContext context) {
        ResourceGroupingStrategy groupingStrategy = this.pluginsManager.resourceGroupingStrategy(context.getDocumentationType());
        Set<ResourceGroup> resourceGroups = groupingStrategy.getResourceGroups(context.getRequestMappingInfo(), context.getHandlerMethod());
        FluentIterable<String> tags = FluentIterable.from(resourceGroups).transform(toTags());
        return tags.toSet();
    }

    private Function<ResourceGroup, String> toTags() {
        return new Function<ResourceGroup, String>() { // from class: springfox.documentation.spring.web.readers.operation.DefaultTagsProvider.1
            @Override // com.google.common.base.Function
            public String apply(ResourceGroup input) {
                return input.getGroupName();
            }
        };
    }
}
