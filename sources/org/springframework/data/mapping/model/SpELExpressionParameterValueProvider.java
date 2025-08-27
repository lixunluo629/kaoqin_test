package org.springframework.data.mapping.model;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PreferredConstructor;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/SpELExpressionParameterValueProvider.class */
public class SpELExpressionParameterValueProvider<P extends PersistentProperty<P>> implements ParameterValueProvider<P> {
    private final SpELExpressionEvaluator evaluator;
    private final ParameterValueProvider<P> delegate;
    private final ConversionService conversionService;

    public SpELExpressionParameterValueProvider(SpELExpressionEvaluator evaluator, ConversionService conversionService, ParameterValueProvider<P> delegate) {
        Assert.notNull(evaluator, "SpELExpressionEvaluator must not be null!");
        Assert.notNull(conversionService, "ConversionService must not be null!");
        Assert.notNull(delegate, "ParameterValueProvider delegate must not be null!");
        this.evaluator = evaluator;
        this.conversionService = conversionService;
        this.delegate = delegate;
    }

    @Override // org.springframework.data.mapping.model.ParameterValueProvider
    public <T> T getParameterValue(PreferredConstructor.Parameter<T, P> parameter) {
        if (!parameter.hasSpelExpression()) {
            if (this.delegate == null) {
                return null;
            }
            return (T) this.delegate.getParameterValue(parameter);
        }
        Object objEvaluate = this.evaluator.evaluate(parameter.getSpelExpression());
        if (objEvaluate == null) {
            return null;
        }
        return (T) potentiallyConvertSpelValue(objEvaluate, parameter);
    }

    protected <T> T potentiallyConvertSpelValue(Object obj, PreferredConstructor.Parameter<T, P> parameter) {
        return (T) this.conversionService.convert(obj, parameter.getRawType());
    }
}
