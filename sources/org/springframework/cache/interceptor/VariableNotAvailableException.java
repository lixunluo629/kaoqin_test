package org.springframework.cache.interceptor;

import org.springframework.expression.EvaluationException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/VariableNotAvailableException.class */
class VariableNotAvailableException extends EvaluationException {
    private final String name;

    public VariableNotAvailableException(String name) {
        super("Variable not available");
        this.name = name;
    }

    public final String getName() {
        return this.name;
    }
}
