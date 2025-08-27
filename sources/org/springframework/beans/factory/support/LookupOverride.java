package org.springframework.beans.factory.support;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/LookupOverride.class */
public class LookupOverride extends MethodOverride {
    private final String beanName;
    private Method method;

    public LookupOverride(String methodName, String beanName) {
        super(methodName);
        this.beanName = beanName;
    }

    public LookupOverride(Method method, String beanName) {
        super(method.getName());
        this.method = method;
        this.beanName = beanName;
    }

    public String getBeanName() {
        return this.beanName;
    }

    @Override // org.springframework.beans.factory.support.MethodOverride
    public boolean matches(Method method) {
        if (this.method != null) {
            return method.equals(this.method);
        }
        return method.getName().equals(getMethodName()) && (!isOverloaded() || Modifier.isAbstract(method.getModifiers()) || method.getParameterTypes().length == 0);
    }

    @Override // org.springframework.beans.factory.support.MethodOverride
    public boolean equals(Object other) {
        if (!(other instanceof LookupOverride) || !super.equals(other)) {
            return false;
        }
        LookupOverride that = (LookupOverride) other;
        return ObjectUtils.nullSafeEquals(this.method, that.method) && ObjectUtils.nullSafeEquals(this.beanName, that.beanName);
    }

    @Override // org.springframework.beans.factory.support.MethodOverride
    public int hashCode() {
        return (29 * super.hashCode()) + ObjectUtils.nullSafeHashCode(this.beanName);
    }

    public String toString() {
        return "LookupOverride for method '" + getMethodName() + "'";
    }
}
