package org.springframework.data.repository.query.spi;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.Assert;
import org.springframework.util.TypeUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/spi/Function.class */
public class Function {
    private final Method method;
    private final Object target;

    public Function(Method method) {
        this(method, null);
        Assert.isTrue(Modifier.isStatic(method.getModifiers()), "Method must be static!");
    }

    public Function(Method method, Object target) {
        Assert.notNull(method, "Method must not be null!");
        Assert.isTrue(target != null || Modifier.isStatic(method.getModifiers()), "Method must either be static or a non-static one with a target object!");
        this.method = method;
        this.target = target;
    }

    public Object invoke(Object[] arguments) throws Exception {
        return this.method.invoke(this.target, arguments);
    }

    public String getName() {
        return this.method.getName();
    }

    public Class<?> getDeclaringClass() {
        return this.method.getDeclaringClass();
    }

    public boolean supports(List<TypeDescriptor> argumentTypes) {
        Class<?>[] parameterTypes = this.method.getParameterTypes();
        if (parameterTypes.length != argumentTypes.size()) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            if (!TypeUtils.isAssignable(parameterTypes[i], argumentTypes.get(i).getType())) {
                return false;
            }
        }
        return true;
    }
}
