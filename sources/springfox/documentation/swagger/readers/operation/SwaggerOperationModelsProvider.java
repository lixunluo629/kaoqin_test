package springfox.documentation.swagger.readers.operation;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.schema.ResolvedTypes;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.spring.web.HandlerMethodReturnTypes;
import springfox.documentation.swagger.annotations.Annotations;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/readers/operation/SwaggerOperationModelsProvider.class */
public class SwaggerOperationModelsProvider implements OperationModelsProviderPlugin {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) SwaggerOperationModelsProvider.class);
    private final TypeResolver typeResolver;

    @Autowired
    public SwaggerOperationModelsProvider(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    @Override // springfox.documentation.spi.service.OperationModelsProviderPlugin
    public void apply(RequestMappingContext context) {
        collectFromApiOperation(context);
        collectApiResponses(context);
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    private void collectFromApiOperation(RequestMappingContext context) {
        HandlerMethod handlerMethod = context.getHandlerMethod();
        ResolvedType returnType = context.alternateFor(HandlerMethodReturnTypes.handlerReturnType(this.typeResolver, handlerMethod));
        Optional<V> optionalTransform = Annotations.findApiOperationAnnotation(handlerMethod.getMethod()).transform(Annotations.resolvedTypeFromOperation(this.typeResolver, returnType));
        if (optionalTransform.isPresent() && optionalTransform.get() != returnType) {
            LOG.debug("Adding return parameter of type {}", ResolvedTypes.resolvedTypeSignature((ResolvedType) optionalTransform.get()).or((Optional<String>) "<null>"));
            context.operationModelsBuilder().addReturn((Type) optionalTransform.get());
        }
    }

    private void collectApiResponses(RequestMappingContext context) {
        HandlerMethod handlerMethod = context.getHandlerMethod();
        Optional<ApiResponses> apiResponses = Annotations.findApiResponsesAnnotations(handlerMethod.getMethod());
        LOG.debug("Reading parameters models for handlerMethod |{}|", handlerMethod.getMethod().getName());
        List<ResolvedType> modelTypes = (List) apiResponses.transform(toResolvedTypes(context)).or((Optional<V>) new ArrayList());
        for (ResolvedType modelType : modelTypes) {
            context.operationModelsBuilder().addReturn(modelType);
        }
    }

    private Function<ApiResponses, List<ResolvedType>> toResolvedTypes(final RequestMappingContext context) {
        return new Function<ApiResponses, List<ResolvedType>>() { // from class: springfox.documentation.swagger.readers.operation.SwaggerOperationModelsProvider.1
            @Override // com.google.common.base.Function
            public List<ResolvedType> apply(ApiResponses input) {
                List<ResolvedType> resolvedTypes = Lists.newArrayList();
                for (ApiResponse response : input.value()) {
                    ResolvedType modelType = context.alternateFor(SwaggerOperationModelsProvider.this.typeResolver.resolve(response.response(), new Type[0]));
                    SwaggerOperationModelsProvider.LOG.debug("Adding input parameter of type {}", ResolvedTypes.resolvedTypeSignature(modelType).or((Optional<String>) "<null>"));
                    resolvedTypes.add(modelType);
                }
                return resolvedTypes;
            }
        };
    }
}
