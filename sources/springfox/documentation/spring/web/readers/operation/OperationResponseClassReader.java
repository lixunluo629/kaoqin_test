package springfox.documentation.spring.web.readers.operation;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
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

@Component
@Order(Integer.MIN_VALUE)
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/operation/OperationResponseClassReader.class */
public class OperationResponseClassReader implements OperationBuilderPlugin {
    private static Logger log = LoggerFactory.getLogger((Class<?>) OperationResponseClassReader.class);
    private final TypeResolver typeResolver;
    private final TypeNameExtractor nameExtractor;

    @Autowired
    public OperationResponseClassReader(TypeResolver typeResolver, TypeNameExtractor nameExtractor) {
        this.typeResolver = typeResolver;
        this.nameExtractor = nameExtractor;
    }

    @Override // springfox.documentation.spi.service.OperationBuilderPlugin
    public void apply(OperationContext context) {
        HandlerMethod handlerMethod = context.getHandlerMethod();
        ResolvedType returnType = context.alternateFor(HandlerMethodReturnTypes.handlerReturnType(this.typeResolver, handlerMethod));
        ModelContext modelContext = ModelContext.returnValue(returnType, context.getDocumentationType(), context.getAlternateTypeProvider(), context.getDocumentationContext().getGenericsNamingStrategy());
        String responseTypeName = this.nameExtractor.typeName(modelContext);
        log.debug("Setting spring response class to:" + responseTypeName);
        context.operationBuilder().responseModel(ModelRefs.modelRef(returnType, modelContext, this.nameExtractor));
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
