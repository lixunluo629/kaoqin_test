package org.springframework.beans;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/GenericTypeAwarePropertyDescriptor.class */
final class GenericTypeAwarePropertyDescriptor extends PropertyDescriptor {
    private final Class<?> beanClass;
    private final Method readMethod;
    private final Method writeMethod;
    private volatile Set<Method> ambiguousWriteMethods;
    private MethodParameter writeMethodParameter;
    private Class<?> propertyType;
    private final Class<?> propertyEditorClass;

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.beans.IntrospectionException */
    public GenericTypeAwarePropertyDescriptor(Class<?> beanClass, String propertyName, Method readMethod, Method writeMethod, Class<?> propertyEditorClass) throws SecurityException, IllegalArgumentException, IntrospectionException {
        Method candidate;
        super(propertyName, (Method) null, (Method) null);
        if (beanClass == null) {
            throw new IntrospectionException("Bean class must not be null");
        }
        this.beanClass = beanClass;
        Method readMethodToUse = BridgeMethodResolver.findBridgedMethod(readMethod);
        Method writeMethodToUse = BridgeMethodResolver.findBridgedMethod(writeMethod);
        if (writeMethodToUse == null && readMethodToUse != null && (candidate = ClassUtils.getMethodIfAvailable(this.beanClass, "set" + StringUtils.capitalize(getName()), (Class[]) null)) != null && candidate.getParameterTypes().length == 1) {
            writeMethodToUse = candidate;
        }
        this.readMethod = readMethodToUse;
        this.writeMethod = writeMethodToUse;
        if (this.writeMethod != null) {
            if (this.readMethod == null) {
                Set<Method> ambiguousCandidates = new HashSet<>();
                for (Method method : beanClass.getMethods()) {
                    if (method.getName().equals(writeMethodToUse.getName()) && !method.equals(writeMethodToUse) && !method.isBridge() && method.getParameterTypes().length == writeMethodToUse.getParameterTypes().length) {
                        ambiguousCandidates.add(method);
                    }
                }
                if (!ambiguousCandidates.isEmpty()) {
                    this.ambiguousWriteMethods = ambiguousCandidates;
                }
            }
            this.writeMethodParameter = new MethodParameter(this.writeMethod, 0);
            GenericTypeResolver.resolveParameterType(this.writeMethodParameter, this.beanClass);
        }
        if (this.readMethod != null) {
            this.propertyType = GenericTypeResolver.resolveReturnType(this.readMethod, this.beanClass);
        } else if (this.writeMethodParameter != null) {
            this.propertyType = this.writeMethodParameter.getParameterType();
        }
        this.propertyEditorClass = propertyEditorClass;
    }

    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    public Method getReadMethod() {
        return this.readMethod;
    }

    public Method getWriteMethod() {
        return this.writeMethod;
    }

    public Method getWriteMethodForActualAccess() {
        Set<Method> ambiguousCandidates = this.ambiguousWriteMethods;
        if (ambiguousCandidates != null) {
            this.ambiguousWriteMethods = null;
            LogFactory.getLog(GenericTypeAwarePropertyDescriptor.class).warn("Invalid JavaBean property '" + getName() + "' being accessed! Ambiguous write methods found next to actually used [" + this.writeMethod + "]: " + ambiguousCandidates);
        }
        return this.writeMethod;
    }

    public MethodParameter getWriteMethodParameter() {
        return this.writeMethodParameter;
    }

    public Class<?> getPropertyType() {
        return this.propertyType;
    }

    public Class<?> getPropertyEditorClass() {
        return this.propertyEditorClass;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof GenericTypeAwarePropertyDescriptor)) {
            return false;
        }
        GenericTypeAwarePropertyDescriptor otherPd = (GenericTypeAwarePropertyDescriptor) other;
        return getBeanClass().equals(otherPd.getBeanClass()) && PropertyDescriptorUtils.equals(this, otherPd);
    }

    public int hashCode() {
        int hashCode = getBeanClass().hashCode();
        return (29 * ((29 * hashCode) + ObjectUtils.nullSafeHashCode(getReadMethod()))) + ObjectUtils.nullSafeHashCode(getWriteMethod());
    }
}
