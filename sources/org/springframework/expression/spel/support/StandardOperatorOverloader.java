package org.springframework.expression.spel.support;

import org.springframework.expression.EvaluationException;
import org.springframework.expression.Operation;
import org.springframework.expression.OperatorOverloader;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/support/StandardOperatorOverloader.class */
public class StandardOperatorOverloader implements OperatorOverloader {
    @Override // org.springframework.expression.OperatorOverloader
    public boolean overridesOperation(Operation operation, Object leftOperand, Object rightOperand) throws EvaluationException {
        return false;
    }

    @Override // org.springframework.expression.OperatorOverloader
    public Object operate(Operation operation, Object leftOperand, Object rightOperand) throws EvaluationException {
        throw new EvaluationException("No operation overloaded by default");
    }
}
