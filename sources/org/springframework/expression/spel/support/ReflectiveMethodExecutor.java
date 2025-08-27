package org.springframework.expression.spel.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.MethodExecutor;
import org.springframework.expression.TypedValue;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/support/ReflectiveMethodExecutor.class */
public class ReflectiveMethodExecutor implements MethodExecutor {
    private final Method method;
    private final Integer varargsPosition;
    private Class<?> publicDeclaringClass;
    private boolean computedPublicDeclaringClass = false;
    private boolean argumentConversionOccurred = false;

    public ReflectiveMethodExecutor(Method method) {
        this.method = method;
        if (method.isVarArgs()) {
            Class<?>[] paramTypes = method.getParameterTypes();
            this.varargsPosition = Integer.valueOf(paramTypes.length - 1);
        } else {
            this.varargsPosition = null;
        }
    }

    public Method getMethod() {
        return this.method;
    }

    public Class<?> getPublicDeclaringClass() {
        if (!this.computedPublicDeclaringClass) {
            this.publicDeclaringClass = discoverPublicClass(this.method, this.method.getDeclaringClass());
            this.computedPublicDeclaringClass = true;
        }
        return this.publicDeclaringClass;
    }

    private Class<?> discoverPublicClass(Method method, Class<?> clazz) throws NoSuchMethodException, SecurityException {
        if (Modifier.isPublic(clazz.getModifiers())) {
            try {
                clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
                return clazz;
            } catch (NoSuchMethodException e) {
            }
        }
        Class<?>[] ifcs = clazz.getInterfaces();
        for (Class<?> ifc : ifcs) {
            discoverPublicClass(method, ifc);
        }
        if (clazz.getSuperclass() != null) {
            return discoverPublicClass(method, clazz.getSuperclass());
        }
        return null;
    }

    public boolean didArgumentConversionOccur() {
        return this.argumentConversionOccurred;
    }

    @Override // org.springframework.expression.MethodExecutor
    public TypedValue execute(EvaluationContext context, Object target, Object... arguments) throws IllegalAccessException, IllegalArgumentException, AccessException, InvocationTargetException {
        if (arguments != null) {
            try {
                this.argumentConversionOccurred = ReflectionHelper.convertArguments(context.getTypeConverter(), arguments, this.method, this.varargsPosition);
                if (this.method.isVarArgs()) {
                    arguments = ReflectionHelper.setupArgumentsForVarargsInvocation(this.method.getParameterTypes(), arguments);
                }
            } catch (Exception ex) {
                throw new AccessException("Problem invoking method: " + this.method, ex);
            }
        }
        ReflectionUtils.makeAccessible(this.method);
        Object value = this.method.invoke(target, arguments);
        return new TypedValue(value, new TypeDescriptor(new MethodParameter(this.method, -1)).narrow(value));
    }
}
