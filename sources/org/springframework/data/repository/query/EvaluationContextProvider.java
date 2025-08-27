package org.springframework.data.repository.query;

import org.springframework.expression.EvaluationContext;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/EvaluationContextProvider.class */
public interface EvaluationContextProvider {
    <T extends Parameters<?, ?>> EvaluationContext getEvaluationContext(T t, Object[] objArr);
}
