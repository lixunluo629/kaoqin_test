package org.springframework.data.mapping.model;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/SpELContext.class */
public class SpELContext {
    private final SpelExpressionParser parser;
    private final PropertyAccessor accessor;
    private final BeanFactory factory;

    public SpELContext(PropertyAccessor accessor) {
        this(accessor, null, null);
    }

    public SpELContext(SpelExpressionParser parser, PropertyAccessor accessor) {
        this(accessor, parser, null);
    }

    public SpELContext(SpELContext source, BeanFactory factory) {
        this(source.accessor, source.parser, factory);
    }

    private SpELContext(PropertyAccessor accessor, SpelExpressionParser parser, BeanFactory factory) {
        this.parser = parser == null ? new SpelExpressionParser() : parser;
        this.accessor = accessor;
        this.factory = factory;
    }

    public ExpressionParser getParser() {
        return this.parser;
    }

    public EvaluationContext getEvaluationContext(Object source) {
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext(source);
        if (this.accessor != null) {
            evaluationContext.addPropertyAccessor(this.accessor);
        }
        if (this.factory != null) {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(this.factory));
        }
        return evaluationContext;
    }
}
