package springfox.documentation.swagger.readers.operation;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.HandlerMethodReturnTypes;
import springfox.documentation.spring.web.readers.operation.ModelRefs;
import springfox.documentation.spring.web.readers.operation.ResponseMessagesReader;
import springfox.documentation.swagger.annotations.Annotations;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/readers/operation/SwaggerResponseMessageReader.class */
public class SwaggerResponseMessageReader implements OperationBuilderPlugin {
    private final TypeNameExtractor typeNameExtractor;
    private final TypeResolver typeResolver;

    @Autowired
    public SwaggerResponseMessageReader(TypeNameExtractor typeNameExtractor, TypeResolver typeResolver) {
        this.typeNameExtractor = typeNameExtractor;
        this.typeResolver = typeResolver;
    }

    @Override // springfox.documentation.spi.service.OperationBuilderPlugin
    public void apply(OperationContext context) {
        HandlerMethod handlerMethod = context.getHandlerMethod();
        context.operationBuilder().responseMessages(read(handlerMethod, context));
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    protected Set<ResponseMessage> read(HandlerMethod handlerMethod, OperationContext context) {
        ResolvedType defaultResponse = HandlerMethodReturnTypes.handlerReturnType(this.typeResolver, handlerMethod);
        Optional<? extends ResolvedType> optionalTransform = Annotations.findApiOperationAnnotation(handlerMethod.getMethod()).transform(Annotations.resolvedTypeFromOperation(this.typeResolver, defaultResponse));
        Optional<ApiResponses> apiResponses = Annotations.findApiResponsesAnnotations(handlerMethod.getMethod());
        Set<ResponseMessage> responseMessages = Sets.newHashSet();
        if (apiResponses.isPresent()) {
            ApiResponse[] apiResponseAnnotations = apiResponses.get().value();
            for (ApiResponse apiResponse : apiResponseAnnotations) {
                ModelContext modelContext = ModelContext.returnValue(apiResponse.response(), context.getDocumentationType(), context.getAlternateTypeProvider(), context.getDocumentationContext().getGenericsNamingStrategy());
                Optional<ModelRef> responseModel = Optional.absent();
                Optional<ResolvedType> type = resolvedType(null, apiResponse);
                if (isSuccessful(apiResponse.code())) {
                    type = type.or(optionalTransform);
                }
                if (type.isPresent()) {
                    responseModel = Optional.of(ModelRefs.modelRef(context.alternateFor(type.get()), modelContext, this.typeNameExtractor));
                }
                responseMessages.add(new ResponseMessageBuilder().code(apiResponse.code()).message(apiResponse.message()).responseModel(responseModel.orNull()).build());
            }
        }
        if (optionalTransform.isPresent()) {
            ModelContext modelContext2 = ModelContext.returnValue(optionalTransform.get(), context.getDocumentationType(), context.getAlternateTypeProvider(), context.getDocumentationContext().getGenericsNamingStrategy());
            ResolvedType resolvedType = context.alternateFor(optionalTransform.get());
            ModelRef responseModel2 = ModelRefs.modelRef(resolvedType, modelContext2, this.typeNameExtractor);
            context.operationBuilder().responseModel(responseModel2);
            ResponseMessage defaultMessage = new ResponseMessageBuilder().code(ResponseMessagesReader.httpStatusCode(handlerMethod)).message(ResponseMessagesReader.message(handlerMethod)).responseModel(responseModel2).build();
            if (!responseMessages.contains(defaultMessage) && !"void".equals(responseModel2.getType())) {
                responseMessages.add(defaultMessage);
            }
        }
        return responseMessages;
    }

    static boolean isSuccessful(int code) {
        return HttpStatus.Series.SUCCESSFUL.equals(HttpStatus.Series.valueOf(code));
    }

    private Optional<ResolvedType> resolvedType(ResolvedType resolvedType, ApiResponse apiResponse) {
        return Optional.fromNullable(Annotations.resolvedTypeFromResponse(this.typeResolver, resolvedType).apply(apiResponse));
    }
}
