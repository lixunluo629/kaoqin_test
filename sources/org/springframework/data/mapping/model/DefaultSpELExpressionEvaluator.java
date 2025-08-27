package org.springframework.data.mapping.model;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/DefaultSpELExpressionEvaluator.class */
public class DefaultSpELExpressionEvaluator implements SpELExpressionEvaluator {
    private final Object source;
    private final SpELContext factory;

    public DefaultSpELExpressionEvaluator(Object source, SpELContext factory) {
        this.source = source;
        this.factory = factory;
    }

    @Override // org.springframework.data.mapping.model.SpELExpressionEvaluator
    public <T> T evaluate(String str) {
        return (T) this.factory.getParser().parseExpression(str).getValue(this.factory.getEvaluationContext(this.source));
    }
}
