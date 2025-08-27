package springfox.documentation.spring.web.readers.operation;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander;

@Component
@Order(Integer.MIN_VALUE)
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/operation/OperationParameterReader.class */
public class OperationParameterReader implements OperationBuilderPlugin {
    private final TypeResolver typeResolver;
    private final ModelAttributeParameterExpander expander;

    @Autowired
    private DocumentationPluginsManager pluginsManager;

    @Autowired
    public OperationParameterReader(TypeResolver typeResolver, ModelAttributeParameterExpander expander) {
        this.typeResolver = typeResolver;
        this.expander = expander;
    }

    @Override // springfox.documentation.spi.service.OperationBuilderPlugin
    public void apply(OperationContext context) {
        context.operationBuilder().parameters(context.getGlobalOperationParameters());
        context.operationBuilder().parameters(readParameters(context));
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    protected List<Parameter> readParameters(OperationContext context) throws NegativeArraySizeException {
        HandlerMethod handlerMethod = context.getHandlerMethod();
        HandlerMethodResolver handlerMethodResolver = new HandlerMethodResolver(this.typeResolver);
        List<ResolvedMethodParameter> methodParameters = handlerMethodResolver.methodParameters(handlerMethod);
        List<Parameter> parameters = Lists.newArrayList();
        for (ResolvedMethodParameter methodParameter : methodParameters) {
            if (!shouldIgnore(methodParameter, context.getDocumentationContext().getIgnorableParameterTypes())) {
                ParameterContext parameterContext = new ParameterContext(methodParameter, new ParameterBuilder(), context.getDocumentationContext(), context.getDocumentationContext().getGenericsNamingStrategy(), context);
                if (shouldExpand(methodParameter)) {
                    this.expander.expand("", methodParameter.getResolvedParameterType().getErasedType(), parameters, context.getDocumentationContext());
                } else {
                    parameters.add(this.pluginsManager.parameter(parameterContext));
                }
            }
        }
        return parameters;
    }

    private boolean shouldIgnore(ResolvedMethodParameter parameter, Set<Class> ignorableParamTypes) {
        if (ignorableParamTypes.contains(parameter.getMethodParameter().getParameterType())) {
            return true;
        }
        for (Annotation annotation : parameter.getMethodParameter().getParameterAnnotations()) {
            if (ignorableParamTypes.contains(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldExpand(ResolvedMethodParameter parameter) {
        return !FluentIterable.from(Lists.newArrayList(parameter.getMethodParameter().getParameterAnnotations())).filter(ModelAttribute.class).isEmpty();
    }
}
