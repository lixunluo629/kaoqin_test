package springfox.documentation.service;

import com.fasterxml.classmate.ResolvedType;
import org.springframework.core.MethodParameter;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/ResolvedMethodParameter.class */
public class ResolvedMethodParameter {
    private final MethodParameter methodParameter;
    private final ResolvedType resolvedParameterType;

    public ResolvedMethodParameter(MethodParameter methodParameter, ResolvedType resolvedParameterType) {
        this.methodParameter = methodParameter;
        this.resolvedParameterType = resolvedParameterType;
    }

    public MethodParameter getMethodParameter() {
        return this.methodParameter;
    }

    public ResolvedType getResolvedParameterType() {
        return this.resolvedParameterType;
    }
}
