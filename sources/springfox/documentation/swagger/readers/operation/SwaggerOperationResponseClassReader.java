package springfox.documentation.swagger.readers.operation;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.HandlerMethodReturnTypes;
import springfox.documentation.spring.web.readers.operation.ModelRefs;
import springfox.documentation.swagger.annotations.Annotations;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/readers/operation/SwaggerOperationResponseClassReader.class */
public class SwaggerOperationResponseClassReader implements OperationBuilderPlugin {
    private static Logger log = LoggerFactory.getLogger((Class<?>) SwaggerOperationResponseClassReader.class);
    private final TypeResolver typeResolver;
    private final TypeNameExtractor nameExtractor;

    @Autowired
    public SwaggerOperationResponseClassReader(TypeResolver typeResolver, TypeNameExtractor nameExtractor) {
        this.typeResolver = typeResolver;
        this.nameExtractor = nameExtractor;
    }

    @Override // springfox.documentation.spi.service.OperationBuilderPlugin
    public void apply(OperationContext context) {
        HandlerMethod handlerMethod = context.getHandlerMethod();
        ResolvedType returnType = context.alternateFor(HandlerMethodReturnTypes.handlerReturnType(this.typeResolver, handlerMethod));
        ResolvedType returnType2 = (ResolvedType) Annotations.findApiOperationAnnotation(handlerMethod.getMethod()).transform(Annotations.resolvedTypeFromOperation(this.typeResolver, returnType)).or((Optional<V>) returnType);
        if (canSkip(context, returnType2)) {
            return;
        }
        ModelContext modelContext = ModelContext.returnValue(returnType2, context.getDocumentationType(), context.getAlternateTypeProvider(), context.getDocumentationContext().getGenericsNamingStrategy());
        String responseTypeName = this.nameExtractor.typeName(modelContext);
        log.debug("Setting response class to:" + responseTypeName);
        context.operationBuilder().responseModel(ModelRefs.modelRef((Optional<ResolvedType>) Optional.of(returnType2), modelContext, this.nameExtractor).orNull());
    }

    private boolean canSkip(OperationContext context, ResolvedType returnType) {
        return context.getDocumentationContext().getIgnorableParameterTypes().contains(returnType);
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
