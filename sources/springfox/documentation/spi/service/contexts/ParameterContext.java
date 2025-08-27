package springfox.documentation.spi.service.contexts;

import com.fasterxml.classmate.ResolvedType;
import org.springframework.core.MethodParameter;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.AlternateTypeProvider;
import springfox.documentation.spi.schema.GenericTypeNamingStrategy;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/contexts/ParameterContext.class */
public class ParameterContext {
    private final ParameterBuilder parameterBuilder;
    private final ResolvedMethodParameter resolvedMethodParameter;
    private final DocumentationContext documentationContext;
    private final GenericTypeNamingStrategy genericNamingStrategy;
    private final OperationContext operationContext;

    public ParameterContext(ResolvedMethodParameter resolvedMethodParameter, ParameterBuilder parameterBuilder, DocumentationContext documentationContext, GenericTypeNamingStrategy genericNamingStrategy, OperationContext operationContext) {
        this.parameterBuilder = parameterBuilder;
        this.resolvedMethodParameter = resolvedMethodParameter;
        this.documentationContext = documentationContext;
        this.genericNamingStrategy = genericNamingStrategy;
        this.operationContext = operationContext;
    }

    public ResolvedMethodParameter resolvedMethodParameter() {
        return this.resolvedMethodParameter;
    }

    public MethodParameter methodParameter() {
        return this.resolvedMethodParameter.getMethodParameter();
    }

    public ParameterBuilder parameterBuilder() {
        return this.parameterBuilder;
    }

    public DocumentationContext getDocumentationContext() {
        return this.documentationContext;
    }

    public DocumentationType getDocumentationType() {
        return this.documentationContext.getDocumentationType();
    }

    public ResolvedType alternateFor(ResolvedType parameterType) {
        return getAlternateTypeProvider().alternateFor(parameterType);
    }

    public AlternateTypeProvider getAlternateTypeProvider() {
        return this.documentationContext.getAlternateTypeProvider();
    }

    public GenericTypeNamingStrategy getGenericNamingStrategy() {
        return this.genericNamingStrategy;
    }

    public OperationContext getOperationContext() {
        return this.operationContext;
    }
}
