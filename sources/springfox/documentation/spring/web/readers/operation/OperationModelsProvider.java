package springfox.documentation.spring.web.readers.operation;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.schema.ResolvedTypes;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.spring.web.HandlerMethodReturnTypes;

@Component
@Order(Integer.MIN_VALUE)
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/operation/OperationModelsProvider.class */
public class OperationModelsProvider implements OperationModelsProviderPlugin {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) OperationModelsProvider.class);
    private final TypeResolver typeResolver;

    @Autowired
    public OperationModelsProvider(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    @Override // springfox.documentation.spi.service.OperationModelsProviderPlugin
    public void apply(RequestMappingContext context) throws NegativeArraySizeException {
        collectFromReturnType(context);
        collectParameters(context);
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private void collectFromReturnType(RequestMappingContext context) {
        ResolvedType modelType = context.alternateFor(HandlerMethodReturnTypes.handlerReturnType(this.typeResolver, context.getHandlerMethod()));
        LOG.debug("Adding return parameter of type {}", ResolvedTypes.resolvedTypeSignature(modelType).or((Optional<String>) "<null>"));
        context.operationModelsBuilder().addReturn(modelType);
    }

    private void collectParameters(RequestMappingContext context) throws NegativeArraySizeException {
        HandlerMethod handlerMethod = context.getHandlerMethod();
        Method method = handlerMethod.getMethod();
        LOG.debug("Reading parameters models for handlerMethod |{}|", handlerMethod.getMethod().getName());
        HandlerMethodResolver handlerMethodResolver = new HandlerMethodResolver(this.typeResolver);
        List<ResolvedMethodParameter> parameterTypes = handlerMethodResolver.methodParameters(handlerMethod);
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            Annotation[] pAnnotations = annotations[i];
            for (Annotation annotation : pAnnotations) {
                if ((annotation instanceof RequestBody) || (annotation instanceof RequestPart)) {
                    ResolvedMethodParameter pType = parameterTypes.get(i);
                    ResolvedType modelType = context.alternateFor(pType.getResolvedParameterType());
                    LOG.debug("Adding input parameter of type {}", ResolvedTypes.resolvedTypeSignature(modelType).or((Optional<String>) "<null>"));
                    context.operationModelsBuilder().addInputParam(modelType);
                }
            }
        }
        LOG.debug("Finished reading parameters models for handlerMethod |{}|", handlerMethod.getMethod().getName());
    }
}
