package org.springframework.data.keyvalue.core;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.util.Assert;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/SpelCriteria.class */
public class SpelCriteria {
    private final SpelExpression expression;
    private final EvaluationContext context;

    public SpelCriteria(SpelExpression expression) {
        this(expression, SimpleEvaluationContext.forReadOnlyDataBinding().withInstanceMethods().build());
    }

    public SpelCriteria(SpelExpression expression, EvaluationContext context) {
        Assert.notNull(expression, "SpEL expression must not be null!");
        Assert.notNull(context, "EvaluationContext must not be null!");
        this.expression = expression;
        this.context = context;
    }

    public EvaluationContext getContext() {
        return this.context;
    }

    public SpelExpression getExpression() {
        return this.expression;
    }
}
