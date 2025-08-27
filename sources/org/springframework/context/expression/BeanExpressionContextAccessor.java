package org.springframework.context.expression;

import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/expression/BeanExpressionContextAccessor.class */
public class BeanExpressionContextAccessor implements PropertyAccessor {
    @Override // org.springframework.expression.PropertyAccessor
    public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
        return ((BeanExpressionContext) target).containsObject(name);
    }

    @Override // org.springframework.expression.PropertyAccessor
    public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
        return new TypedValue(((BeanExpressionContext) target).getObject(name));
    }

    @Override // org.springframework.expression.PropertyAccessor
    public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
        return false;
    }

    @Override // org.springframework.expression.PropertyAccessor
    public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
        throw new AccessException("Beans in a BeanFactory are read-only");
    }

    @Override // org.springframework.expression.PropertyAccessor
    public Class<?>[] getSpecificTargetClasses() {
        return new Class[]{BeanExpressionContext.class};
    }
}
