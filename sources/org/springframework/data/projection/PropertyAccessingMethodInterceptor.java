package org.springframework.data.projection;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/PropertyAccessingMethodInterceptor.class */
class PropertyAccessingMethodInterceptor implements MethodInterceptor {
    private final BeanWrapper target;

    public PropertyAccessingMethodInterceptor(Object target) {
        Assert.notNull(target, "Proxy target must not be null!");
        this.target = new DirectFieldAccessFallbackBeanWrapper(target);
    }

    @Override // org.aopalliance.intercept.MethodInterceptor
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        if (ReflectionUtils.isObjectMethod(method)) {
            return invocation.proceed();
        }
        PropertyDescriptor descriptor = BeanUtils.findPropertyForMethod(method);
        if (descriptor == null) {
            throw new IllegalStateException("Invoked method is not a property accessor!");
        }
        if (!isSetterMethod(method, descriptor)) {
            return this.target.getPropertyValue(descriptor.getName());
        }
        if (invocation.getArguments().length != 1) {
            throw new IllegalStateException("Invoked setter method requires exactly one argument!");
        }
        this.target.setPropertyValue(descriptor.getName(), invocation.getArguments()[0]);
        return null;
    }

    private static boolean isSetterMethod(Method method, PropertyDescriptor descriptor) {
        return method.equals(descriptor.getWriteMethod());
    }
}
