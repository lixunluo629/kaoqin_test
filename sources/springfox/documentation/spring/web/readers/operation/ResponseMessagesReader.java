package springfox.documentation.spring.web.readers.operation;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.Collections;
import springfox.documentation.schema.Maps;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.schema.Types;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.HandlerMethodReturnTypes;

@Component
@Order(Integer.MIN_VALUE)
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/operation/ResponseMessagesReader.class */
public class ResponseMessagesReader implements OperationBuilderPlugin {
    private final TypeResolver typeResolver;
    private final TypeNameExtractor typeNameExtractor;

    @Autowired
    public ResponseMessagesReader(TypeResolver typeResolver, TypeNameExtractor typeNameExtractor) {
        this.typeResolver = typeResolver;
        this.typeNameExtractor = typeNameExtractor;
    }

    @Override // springfox.documentation.spi.service.OperationBuilderPlugin
    public void apply(OperationContext context) {
        List<ResponseMessage> responseMessages = context.getGlobalResponseMessages(context.httpMethod().toString());
        context.operationBuilder().responseMessages(Sets.newHashSet(responseMessages));
        applyReturnTypeOverride(context);
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private void applyReturnTypeOverride(OperationContext context) {
        ResolvedType returnType = context.alternateFor(HandlerMethodReturnTypes.handlerReturnType(this.typeResolver, context.getHandlerMethod()));
        int httpStatusCode = httpStatusCode(context.getHandlerMethod());
        String message = message(context.getHandlerMethod());
        ModelRef modelRef = null;
        if (!Types.isVoid(returnType)) {
            ModelContext modelContext = ModelContext.returnValue(returnType, context.getDocumentationType(), context.getAlternateTypeProvider(), context.getDocumentationContext().getGenericsNamingStrategy());
            modelRef = modelRef(returnType, modelContext);
        }
        ResponseMessage built = new ResponseMessageBuilder().code(httpStatusCode).message(message).responseModel(modelRef).build();
        context.operationBuilder().responseMessages(Sets.newHashSet(built));
    }

    private ModelRef modelRef(ResolvedType type, ModelContext modelContext) {
        if (Collections.isContainerType(type)) {
            ResolvedType collectionElementType = Collections.collectionElementType(type);
            String elementTypeName = this.typeNameExtractor.typeName(ModelContext.fromParent(modelContext, collectionElementType));
            return new ModelRef(Collections.containerType(type), elementTypeName);
        }
        if (Maps.isMapType(type)) {
            String elementTypeName2 = this.typeNameExtractor.typeName(ModelContext.fromParent(modelContext, Maps.mapValueType(type)));
            return new ModelRef("Map", elementTypeName2, true);
        }
        String typeName = this.typeNameExtractor.typeName(ModelContext.fromParent(modelContext, type));
        return new ModelRef(typeName);
    }

    public static int httpStatusCode(HandlerMethod handlerMethod) {
        Optional<ResponseStatus> responseStatus = Optional.fromNullable(AnnotationUtils.getAnnotation(handlerMethod.getMethod(), ResponseStatus.class));
        int httpStatusCode = HttpStatus.OK.value();
        if (responseStatus.isPresent()) {
            httpStatusCode = responseStatus.get().value().value();
        }
        return httpStatusCode;
    }

    public static String message(HandlerMethod handlerMethod) {
        Optional<ResponseStatus> responseStatus = Optional.fromNullable(AnnotationUtils.getAnnotation(handlerMethod.getMethod(), ResponseStatus.class));
        String reasonPhrase = HttpStatus.OK.getReasonPhrase();
        if (responseStatus.isPresent()) {
            reasonPhrase = responseStatus.get().reason();
        }
        return reasonPhrase;
    }
}
