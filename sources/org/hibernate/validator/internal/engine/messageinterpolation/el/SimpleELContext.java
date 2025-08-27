package org.hibernate.validator.internal.engine.messageinterpolation.el;

import java.lang.reflect.Method;
import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ResourceBundleELResolver;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/messageinterpolation/el/SimpleELContext.class */
public class SimpleELContext extends ELContext {
    private static final ELResolver DEFAULT_RESOLVER = new CompositeELResolver() { // from class: org.hibernate.validator.internal.engine.messageinterpolation.el.SimpleELContext.1
        {
            add(new RootResolver());
            add(new ArrayELResolver(false));
            add(new ListELResolver(false));
            add(new MapELResolver(false));
            add(new ResourceBundleELResolver());
            add(new BeanELResolver(false));
        }
    };
    private final MapBasedFunctionMapper functions;
    private final VariableMapper variableMapper;
    private final ELResolver resolver;

    public SimpleELContext(ExpressionFactory expressionFactory) {
        putContext(ExpressionFactory.class, expressionFactory);
        this.functions = new MapBasedFunctionMapper();
        this.variableMapper = new MapBasedVariableMapper();
        this.resolver = DEFAULT_RESOLVER;
    }

    @Override // javax.el.ELContext
    public ELResolver getELResolver() {
        return this.resolver;
    }

    @Override // javax.el.ELContext
    public MapBasedFunctionMapper getFunctionMapper() {
        return this.functions;
    }

    @Override // javax.el.ELContext
    public VariableMapper getVariableMapper() {
        return this.variableMapper;
    }

    public ValueExpression setVariable(String name, ValueExpression expression) {
        return this.variableMapper.setVariable(name, expression);
    }

    public void setFunction(String prefix, String localName, Method method) {
        this.functions.setFunction(prefix, localName, method);
    }
}
