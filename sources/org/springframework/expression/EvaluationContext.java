package org.springframework.expression;

import java.util.List;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/EvaluationContext.class */
public interface EvaluationContext {
    TypedValue getRootObject();

    List<PropertyAccessor> getPropertyAccessors();

    List<ConstructorResolver> getConstructorResolvers();

    List<MethodResolver> getMethodResolvers();

    BeanResolver getBeanResolver();

    TypeLocator getTypeLocator();

    TypeConverter getTypeConverter();

    TypeComparator getTypeComparator();

    OperatorOverloader getOperatorOverloader();

    void setVariable(String str, Object obj);

    Object lookupVariable(String str);
}
