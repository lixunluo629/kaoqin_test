package springfox.documentation.spi.service.contexts;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import java.util.Map;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import springfox.documentation.builders.ApiDescriptionBuilder;
import springfox.documentation.schema.Model;
import springfox.documentation.service.Operation;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/contexts/RequestMappingContext.class */
public class RequestMappingContext {
    private final RequestMappingInfo requestMappingInfo;
    private final HandlerMethod handlerMethod;
    private final OperationModelContextsBuilder operationModelContextsBuilder;
    private final DocumentationContext documentationContext;
    private final String requestMappingPattern;
    private final ApiDescriptionBuilder apiDescriptionBuilder;
    private final Map<String, Model> modelMap;

    public RequestMappingContext(DocumentationContext context, RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
        this.modelMap = Maps.newHashMap();
        this.documentationContext = context;
        this.requestMappingInfo = requestMappingInfo;
        this.handlerMethod = handlerMethod;
        this.requestMappingPattern = "";
        this.operationModelContextsBuilder = new OperationModelContextsBuilder(context.getDocumentationType(), context.getAlternateTypeProvider(), context.getGenericsNamingStrategy());
        this.apiDescriptionBuilder = new ApiDescriptionBuilder(this.documentationContext.operationOrdering());
    }

    private RequestMappingContext(DocumentationContext context, RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod, OperationModelContextsBuilder operationModelContextsBuilder, String requestMappingPattern) {
        this.modelMap = Maps.newHashMap();
        this.documentationContext = context;
        this.requestMappingInfo = requestMappingInfo;
        this.handlerMethod = handlerMethod;
        this.operationModelContextsBuilder = operationModelContextsBuilder;
        this.requestMappingPattern = requestMappingPattern;
        this.apiDescriptionBuilder = new ApiDescriptionBuilder(this.documentationContext.operationOrdering());
    }

    private RequestMappingContext(DocumentationContext context, RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod, OperationModelContextsBuilder operationModelContextsBuilder, String requestMappingPattern, Map<String, Model> knownModels) {
        this.modelMap = Maps.newHashMap();
        this.documentationContext = context;
        this.requestMappingInfo = requestMappingInfo;
        this.handlerMethod = handlerMethod;
        this.operationModelContextsBuilder = operationModelContextsBuilder;
        this.requestMappingPattern = requestMappingPattern;
        this.apiDescriptionBuilder = new ApiDescriptionBuilder(this.documentationContext.operationOrdering());
        this.modelMap.putAll(knownModels);
    }

    public RequestMappingInfo getRequestMappingInfo() {
        return this.requestMappingInfo;
    }

    public HandlerMethod getHandlerMethod() {
        return this.handlerMethod;
    }

    public DocumentationContext getDocumentationContext() {
        return this.documentationContext;
    }

    public String getRequestMappingPattern() {
        return this.requestMappingPattern;
    }

    public ImmutableMap<String, Model> getModelMap() {
        return ImmutableMap.copyOf((Map) this.modelMap);
    }

    public OperationModelContextsBuilder operationModelsBuilder() {
        return this.operationModelContextsBuilder;
    }

    public ApiDescriptionBuilder apiDescriptionBuilder() {
        return this.apiDescriptionBuilder;
    }

    public ResolvedType alternateFor(ResolvedType resolvedType) {
        return this.documentationContext.getAlternateTypeProvider().alternateFor(resolvedType);
    }

    public Ordering<Operation> operationOrdering() {
        return this.documentationContext.operationOrdering();
    }

    public RequestMappingContext copyPatternUsing(String requestMappingPattern) {
        return new RequestMappingContext(this.documentationContext, this.requestMappingInfo, this.handlerMethod, this.operationModelContextsBuilder, requestMappingPattern);
    }

    public RequestMappingContext withKnownModels(Map<String, Model> knownModels) {
        return new RequestMappingContext(this.documentationContext, this.requestMappingInfo, this.handlerMethod, this.operationModelContextsBuilder, this.requestMappingPattern, knownModels);
    }
}
