package org.springframework.expression;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/MethodExecutor.class */
public interface MethodExecutor {
    TypedValue execute(EvaluationContext evaluationContext, Object obj, Object... objArr) throws AccessException;
}
