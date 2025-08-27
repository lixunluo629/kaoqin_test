package org.springframework.expression;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/BeanResolver.class */
public interface BeanResolver {
    Object resolve(EvaluationContext evaluationContext, String str) throws AccessException;
}
