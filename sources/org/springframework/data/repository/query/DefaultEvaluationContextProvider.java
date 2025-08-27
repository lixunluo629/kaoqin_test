package org.springframework.data.repository.query;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/DefaultEvaluationContextProvider.class */
public enum DefaultEvaluationContextProvider implements EvaluationContextProvider {
    INSTANCE;

    @Override // org.springframework.data.repository.query.EvaluationContextProvider
    public <T extends Parameters<?, ?>> EvaluationContext getEvaluationContext(T parameters, Object[] parameterValues) {
        return ObjectUtils.isEmpty(parameterValues) ? new StandardEvaluationContext() : new StandardEvaluationContext(parameterValues);
    }
}
