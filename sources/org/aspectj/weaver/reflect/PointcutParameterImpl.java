package org.aspectj.weaver.reflect;

import org.aspectj.weaver.tools.PointcutParameter;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/reflect/PointcutParameterImpl.class */
public class PointcutParameterImpl implements PointcutParameter {
    String name;
    Class type;
    Object binding;

    public PointcutParameterImpl(String name, Class type) {
        this.name = name;
        this.type = type;
    }

    @Override // org.aspectj.weaver.tools.PointcutParameter
    public String getName() {
        return this.name;
    }

    @Override // org.aspectj.weaver.tools.PointcutParameter
    public Class getType() {
        return this.type;
    }

    @Override // org.aspectj.weaver.tools.PointcutParameter
    public Object getBinding() {
        return this.binding;
    }

    void setBinding(Object boundValue) {
        this.binding = boundValue;
    }
}
