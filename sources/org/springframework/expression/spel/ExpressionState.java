package org.springframework.expression.spel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Operation;
import org.springframework.expression.OperatorOverloader;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypeComparator;
import org.springframework.expression.TypeConverter;
import org.springframework.expression.TypedValue;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/ExpressionState.class */
public class ExpressionState {
    private final EvaluationContext relatedContext;
    private final TypedValue rootObject;
    private final SpelParserConfiguration configuration;
    private Stack<TypedValue> contextObjects;
    private Stack<VariableScope> variableScopes;
    private Stack<TypedValue> scopeRootObjects;

    public ExpressionState(EvaluationContext context) {
        this(context, context.getRootObject(), new SpelParserConfiguration(false, false));
    }

    public ExpressionState(EvaluationContext context, SpelParserConfiguration configuration) {
        this(context, context.getRootObject(), configuration);
    }

    public ExpressionState(EvaluationContext context, TypedValue rootObject) {
        this(context, rootObject, new SpelParserConfiguration(false, false));
    }

    public ExpressionState(EvaluationContext context, TypedValue rootObject, SpelParserConfiguration configuration) {
        Assert.notNull(context, "EvaluationContext must not be null");
        Assert.notNull(configuration, "SpelParserConfiguration must not be null");
        this.relatedContext = context;
        this.rootObject = rootObject;
        this.configuration = configuration;
    }

    private void ensureVariableScopesInitialized() {
        if (this.variableScopes == null) {
            this.variableScopes = new Stack<>();
            this.variableScopes.add(new VariableScope());
        }
        if (this.scopeRootObjects == null) {
            this.scopeRootObjects = new Stack<>();
        }
    }

    public TypedValue getActiveContextObject() {
        if (CollectionUtils.isEmpty(this.contextObjects)) {
            return this.rootObject;
        }
        return this.contextObjects.peek();
    }

    public void pushActiveContextObject(TypedValue obj) {
        if (this.contextObjects == null) {
            this.contextObjects = new Stack<>();
        }
        this.contextObjects.push(obj);
    }

    public void popActiveContextObject() {
        if (this.contextObjects == null) {
            this.contextObjects = new Stack<>();
        }
        this.contextObjects.pop();
    }

    public TypedValue getRootContextObject() {
        return this.rootObject;
    }

    public TypedValue getScopeRootContextObject() {
        if (CollectionUtils.isEmpty(this.scopeRootObjects)) {
            return this.rootObject;
        }
        return this.scopeRootObjects.peek();
    }

    public void setVariable(String name, Object value) {
        this.relatedContext.setVariable(name, value);
    }

    public TypedValue lookupVariable(String name) {
        Object value = this.relatedContext.lookupVariable(name);
        return value != null ? new TypedValue(value) : TypedValue.NULL;
    }

    public TypeComparator getTypeComparator() {
        return this.relatedContext.getTypeComparator();
    }

    public Class<?> findType(String type) throws EvaluationException {
        return this.relatedContext.getTypeLocator().findType(type);
    }

    public Object convertValue(Object value, TypeDescriptor targetTypeDescriptor) throws EvaluationException {
        return this.relatedContext.getTypeConverter().convertValue(value, TypeDescriptor.forObject(value), targetTypeDescriptor);
    }

    public TypeConverter getTypeConverter() {
        return this.relatedContext.getTypeConverter();
    }

    public Object convertValue(TypedValue value, TypeDescriptor targetTypeDescriptor) throws EvaluationException {
        Object val = value.getValue();
        return this.relatedContext.getTypeConverter().convertValue(val, TypeDescriptor.forObject(val), targetTypeDescriptor);
    }

    public void enterScope(Map<String, Object> argMap) {
        ensureVariableScopesInitialized();
        this.variableScopes.push(new VariableScope(argMap));
        this.scopeRootObjects.push(getActiveContextObject());
    }

    public void enterScope() {
        ensureVariableScopesInitialized();
        this.variableScopes.push(new VariableScope(Collections.emptyMap()));
        this.scopeRootObjects.push(getActiveContextObject());
    }

    public void enterScope(String name, Object value) {
        ensureVariableScopesInitialized();
        this.variableScopes.push(new VariableScope(name, value));
        this.scopeRootObjects.push(getActiveContextObject());
    }

    public void exitScope() {
        ensureVariableScopesInitialized();
        this.variableScopes.pop();
        this.scopeRootObjects.pop();
    }

    public void setLocalVariable(String name, Object value) {
        ensureVariableScopesInitialized();
        this.variableScopes.peek().setVariable(name, value);
    }

    public Object lookupLocalVariable(String name) {
        ensureVariableScopesInitialized();
        int scopeNumber = this.variableScopes.size() - 1;
        for (int i = scopeNumber; i >= 0; i--) {
            if (this.variableScopes.get(i).definesVariable(name)) {
                return this.variableScopes.get(i).lookupVariable(name);
            }
        }
        return null;
    }

    public TypedValue operate(Operation op, Object left, Object right) throws EvaluationException {
        OperatorOverloader overloader = this.relatedContext.getOperatorOverloader();
        if (overloader.overridesOperation(op, left, right)) {
            Object returnValue = overloader.operate(op, left, right);
            return new TypedValue(returnValue);
        }
        String leftType = left == null ? "null" : left.getClass().getName();
        String rightType = right == null ? "null" : right.getClass().getName();
        throw new SpelEvaluationException(SpelMessage.OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES, op, leftType, rightType);
    }

    public List<PropertyAccessor> getPropertyAccessors() {
        return this.relatedContext.getPropertyAccessors();
    }

    public EvaluationContext getEvaluationContext() {
        return this.relatedContext;
    }

    public SpelParserConfiguration getConfiguration() {
        return this.configuration;
    }

    /* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/ExpressionState$VariableScope.class */
    private static class VariableScope {
        private final Map<String, Object> vars = new HashMap();

        public VariableScope() {
        }

        public VariableScope(Map<String, Object> arguments) {
            if (arguments != null) {
                this.vars.putAll(arguments);
            }
        }

        public VariableScope(String name, Object value) {
            this.vars.put(name, value);
        }

        public Object lookupVariable(String name) {
            return this.vars.get(name);
        }

        public void setVariable(String name, Object value) {
            this.vars.put(name, value);
        }

        public boolean definesVariable(String name) {
            return this.vars.containsKey(name);
        }
    }
}
