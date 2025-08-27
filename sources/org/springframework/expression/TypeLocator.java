package org.springframework.expression;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/TypeLocator.class */
public interface TypeLocator {
    Class<?> findType(String str) throws EvaluationException;
}
