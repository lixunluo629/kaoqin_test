package org.springframework.expression;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/OperatorOverloader.class */
public interface OperatorOverloader {
    boolean overridesOperation(Operation operation, Object obj, Object obj2) throws EvaluationException;

    Object operate(Operation operation, Object obj, Object obj2) throws EvaluationException;
}
