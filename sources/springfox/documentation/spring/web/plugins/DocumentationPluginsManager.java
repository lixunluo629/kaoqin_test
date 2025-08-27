package springfox.documentation.spring.web.plugins;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Operation;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.PathDecorator;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.ApiListingBuilderPlugin;
import springfox.documentation.spi.service.DefaultsProviderPlugin;
import springfox.documentation.spi.service.DocumentationPlugin;
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.ResourceGroupingStrategy;
import springfox.documentation.spi.service.contexts.ApiListingContext;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.DocumentationContextBuilder;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;
import springfox.documentation.spi.service.contexts.PathContext;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.spring.web.SpringGroupingStrategy;

@Component
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/plugins/DocumentationPluginsManager.class */
public class DocumentationPluginsManager {

    @Autowired
    @Qualifier("documentationPluginRegistry")
    private PluginRegistry<DocumentationPlugin, DocumentationType> documentationPlugins;

    @Autowired
    @Qualifier("apiListingBuilderPluginRegistry")
    private PluginRegistry<ApiListingBuilderPlugin, DocumentationType> apiListingPlugins;

    @Autowired
    @Qualifier("parameterBuilderPluginRegistry")
    private PluginRegistry<ParameterBuilderPlugin, DocumentationType> parameterPlugins;

    @Autowired
    @Qualifier("expandedParameterBuilderPluginRegistry")
    private PluginRegistry<ExpandedParameterBuilderPlugin, DocumentationType> parameterExpanderPlugins;

    @Autowired
    @Qualifier("operationBuilderPluginRegistry")
    private PluginRegistry<OperationBuilderPlugin, DocumentationType> operationBuilderPlugins;

    @Autowired
    @Qualifier("resourceGroupingStrategyRegistry")
    private PluginRegistry<ResourceGroupingStrategy, DocumentationType> resourceGroupingStrategies;

    @Autowired
    @Qualifier("operationModelsProviderPluginRegistry")
    private PluginRegistry<OperationModelsProviderPlugin, DocumentationType> operationModelsProviders;

    @Autowired
    @Qualifier("defaultsProviderPluginRegistry")
    private PluginRegistry<DefaultsProviderPlugin, DocumentationType> defaultsProviders;

    @Autowired
    @Qualifier("pathDecoratorRegistry")
    private PluginRegistry<PathDecorator, DocumentationContext> pathDecorators;

    public Iterable<DocumentationPlugin> documentationPlugins() throws IllegalStateException {
        List<T> plugins = this.documentationPlugins.getPlugins();
        DuplicateGroupsDetector.ensureNoDuplicateGroups(plugins);
        return plugins.isEmpty() ? Lists.newArrayList(defaultDocumentationPlugin()) : plugins;
    }

    public Parameter parameter(ParameterContext parameterContext) {
        for (T each : this.parameterPlugins.getPluginsFor(parameterContext.getDocumentationType())) {
            each.apply(parameterContext);
        }
        return parameterContext.parameterBuilder().build();
    }

    public Parameter expandParameter(ParameterExpansionContext context) {
        for (T each : this.parameterExpanderPlugins.getPluginsFor(context.getDocumentationType())) {
            each.apply(context);
        }
        return context.getParameterBuilder().build();
    }

    public Operation operation(OperationContext operationContext) {
        for (T each : this.operationBuilderPlugins.getPluginsFor(operationContext.getDocumentationType())) {
            each.apply(operationContext);
        }
        return operationContext.operationBuilder().build();
    }

    public ApiListing apiListing(ApiListingContext context) {
        for (T each : this.apiListingPlugins.getPluginsFor(context.getDocumentationType())) {
            each.apply(context);
        }
        return context.apiListingBuilder().build();
    }

    public Set<ModelContext> modelContexts(RequestMappingContext context) {
        DocumentationType documentationType = context.getDocumentationContext().getDocumentationType();
        for (T each : this.operationModelsProviders.getPluginsFor(documentationType)) {
            each.apply(context);
        }
        return context.operationModelsBuilder().build();
    }

    public ResourceGroupingStrategy resourceGroupingStrategy(DocumentationType documentationType) {
        return (ResourceGroupingStrategy) this.resourceGroupingStrategies.getPluginFor((PluginRegistry<ResourceGroupingStrategy, DocumentationType>) documentationType, (DocumentationType) new SpringGroupingStrategy());
    }

    private DocumentationPlugin defaultDocumentationPlugin() {
        return new Docket(DocumentationType.SWAGGER_12);
    }

    public DocumentationContextBuilder createContextBuilder(DocumentationType documentationType, DefaultConfiguration defaultConfiguration) {
        return ((DefaultsProviderPlugin) this.defaultsProviders.getPluginFor((PluginRegistry<DefaultsProviderPlugin, DocumentationType>) documentationType, (DocumentationType) defaultConfiguration)).create(documentationType).withResourceGroupingStrategy(resourceGroupingStrategy(documentationType));
    }

    public Function<String, String> decorator(final PathContext context) {
        return new Function<String, String>() { // from class: springfox.documentation.spring.web.plugins.DocumentationPluginsManager.1
            @Override // com.google.common.base.Function
            public String apply(String input) {
                Iterable<Function<String, String>> decorators = FluentIterable.from(DocumentationPluginsManager.this.pathDecorators.getPluginsFor(context.documentationContext())).transform(DocumentationPluginsManager.this.toDecorator(context));
                for (Function<String, String> decorator : decorators) {
                    input = decorator.apply(input);
                }
                return input;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Function<? super PathDecorator, Function<String, String>> toDecorator(final PathContext context) {
        return new Function<PathDecorator, Function<String, String>>() { // from class: springfox.documentation.spring.web.plugins.DocumentationPluginsManager.2
            @Override // com.google.common.base.Function
            public Function<String, String> apply(PathDecorator input) {
                return input.decorator(context);
            }
        };
    }
}
