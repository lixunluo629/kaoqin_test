package org.springframework.expression.spel;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/CompiledExpression.class */
public abstract class CompiledExpression {
    public abstract Object getValue(Object obj, EvaluationContext evaluationContext) throws EvaluationException;
}
