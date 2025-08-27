package org.springframework.web.bind;

import org.springframework.core.MethodParameter;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/bind/MissingPathVariableException.class */
public class MissingPathVariableException extends ServletRequestBindingException {
    private final String variableName;
    private final MethodParameter parameter;

    public MissingPathVariableException(String variableName, MethodParameter parameter) {
        super("");
        this.variableName = variableName;
        this.parameter = parameter;
    }

    @Override // org.springframework.web.util.NestedServletException, java.lang.Throwable
    public String getMessage() {
        return "Missing URI template variable '" + this.variableName + "' for method parameter of type " + this.parameter.getNestedParameterType().getSimpleName();
    }

    public final String getVariableName() {
        return this.variableName;
    }

    public final MethodParameter getParameter() {
        return this.parameter;
    }
}
