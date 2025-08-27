package springfox.documentation.spi.service.contexts;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.PathProvider;
import springfox.documentation.RequestHandler;
import springfox.documentation.annotations.Incubating;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiListingReference;
import springfox.documentation.service.Operation;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.AlternateTypeProvider;
import springfox.documentation.spi.schema.GenericTypeNamingStrategy;
import springfox.documentation.spi.service.ResourceGroupingStrategy;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/contexts/DocumentationContext.class */
public class DocumentationContext {
    private final DocumentationType documentationType;
    private final List<RequestHandler> handlerMappings;
    private final ApiInfo apiInfo;
    private final String groupName;
    private final ApiSelector apiSelector;
    private final AlternateTypeProvider alternateTypeProvider;
    private final Set<Class> ignorableParameterTypes;
    private final Map<RequestMethod, List<ResponseMessage>> globalResponseMessages;
    private final List<Parameter> globalOperationParameters;
    private final ResourceGroupingStrategy resourceGroupingStrategy;
    private final PathProvider pathProvider;
    private final List<SecurityContext> securityContexts;
    private final List<? extends SecurityScheme> securitySchemes;
    private final Ordering<ApiListingReference> listingReferenceOrdering;
    private final Ordering<ApiDescription> apiDescriptionOrdering;
    private final Ordering<Operation> operationOrdering;
    private Set<String> produces;
    private Set<String> consumes;
    private Set<String> protocols;
    private final GenericTypeNamingStrategy genericsNamingStrategy;
    private final Optional<String> pathMapping;
    private boolean isUrlTemplatesEnabled;

    public DocumentationContext(DocumentationType documentationType, List<RequestHandler> handlerMappings, ApiInfo apiInfo, String groupName, ApiSelector apiSelector, Set<Class> ignorableParameterTypes, Map<RequestMethod, List<ResponseMessage>> globalResponseMessages, List<Parameter> globalOperationParameter, ResourceGroupingStrategy resourceGroupingStrategy, PathProvider pathProvider, List<SecurityContext> securityContexts, List<? extends SecurityScheme> securitySchemes, List<AlternateTypeRule> alternateTypeRules, Ordering<ApiListingReference> listingReferenceOrdering, Ordering<ApiDescription> apiDescriptionOrdering, Ordering<Operation> operationOrdering, Set<String> produces, Set<String> consumes, Set<String> protocols, GenericTypeNamingStrategy genericsNamingStrategy, Optional<String> pathMapping, boolean isUrlTemplatesEnabled) {
        this.documentationType = documentationType;
        this.handlerMappings = handlerMappings;
        this.apiInfo = apiInfo;
        this.groupName = groupName;
        this.apiSelector = apiSelector;
        this.ignorableParameterTypes = ignorableParameterTypes;
        this.globalResponseMessages = globalResponseMessages;
        this.globalOperationParameters = globalOperationParameter;
        this.resourceGroupingStrategy = resourceGroupingStrategy;
        this.pathProvider = pathProvider;
        this.securityContexts = securityContexts;
        this.securitySchemes = securitySchemes;
        this.listingReferenceOrdering = listingReferenceOrdering;
        this.apiDescriptionOrdering = apiDescriptionOrdering;
        this.operationOrdering = operationOrdering;
        this.produces = produces;
        this.consumes = consumes;
        this.protocols = protocols;
        this.genericsNamingStrategy = genericsNamingStrategy;
        this.pathMapping = pathMapping;
        this.isUrlTemplatesEnabled = isUrlTemplatesEnabled;
        this.alternateTypeProvider = new AlternateTypeProvider(alternateTypeRules);
    }

    public DocumentationType getDocumentationType() {
        return this.documentationType;
    }

    public List<RequestHandler> getRequestHandlers() {
        return this.handlerMappings;
    }

    public ApiInfo getApiInfo() {
        return this.apiInfo;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public ApiSelector getApiSelector() {
        return this.apiSelector;
    }

    public ImmutableSet<Class> getIgnorableParameterTypes() {
        return ImmutableSet.copyOf((Collection) this.ignorableParameterTypes);
    }

    public Map<RequestMethod, List<ResponseMessage>> getGlobalResponseMessages() {
        return this.globalResponseMessages;
    }

    public List<Parameter> getGlobalRequestParameters() {
        return this.globalOperationParameters;
    }

    public ResourceGroupingStrategy getResourceGroupingStrategy() {
        return this.resourceGroupingStrategy;
    }

    public PathProvider getPathProvider() {
        return this.pathProvider;
    }

    public List<SecurityContext> getSecurityContexts() {
        return this.securityContexts;
    }

    public List<? extends SecurityScheme> getSecuritySchemes() {
        return this.securitySchemes;
    }

    public Ordering<ApiListingReference> getListingReferenceOrdering() {
        return this.listingReferenceOrdering;
    }

    public Ordering<ApiDescription> getApiDescriptionOrdering() {
        return this.apiDescriptionOrdering;
    }

    public AlternateTypeProvider getAlternateTypeProvider() {
        return this.alternateTypeProvider;
    }

    public Ordering<Operation> operationOrdering() {
        return this.operationOrdering;
    }

    public Set<String> getProduces() {
        return this.produces;
    }

    public Set<String> getConsumes() {
        return this.consumes;
    }

    public Set<String> getProtocols() {
        return this.protocols;
    }

    public GenericTypeNamingStrategy getGenericsNamingStrategy() {
        return this.genericsNamingStrategy;
    }

    public Optional<String> getPathMapping() {
        return this.pathMapping;
    }

    @Incubating("2.1.0")
    public boolean isUriTemplatesEnabled() {
        return this.isUrlTemplatesEnabled;
    }
}
