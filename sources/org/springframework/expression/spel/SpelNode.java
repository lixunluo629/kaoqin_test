package org.springframework.expression.spel;

import org.springframework.expression.EvaluationException;
import org.springframework.expression.TypedValue;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/SpelNode.class */
public interface SpelNode {
    Object getValue(ExpressionState expressionState) throws EvaluationException;

    TypedValue getTypedValue(ExpressionState expressionState) throws EvaluationException;

    boolean isWritable(ExpressionState expressionState) throws EvaluationException;

    void setValue(ExpressionState expressionState, Object obj) throws EvaluationException;

    String toStringAST();

    int getChildCount();

    SpelNode getChild(int i);

    Class<?> getObjectClass(Object obj);

    int getStartPosition();

    int getEndPosition();
}
