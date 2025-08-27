package org.apache.ibatis.reflection.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/invoker/MethodInvoker.class */
public class MethodInvoker implements Invoker {
    private final Class<?> type;
    private final Method method;

    public MethodInvoker(Method method) {
        this.method = method;
        if (method.getParameterTypes().length == 1) {
            this.type = method.getParameterTypes()[0];
        } else {
            this.type = method.getReturnType();
        }
    }

    @Override // org.apache.ibatis.reflection.invoker.Invoker
    public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
        return this.method.invoke(target, args);
    }

    @Override // org.apache.ibatis.reflection.invoker.Invoker
    public Class<?> getType() {
        return this.type;
    }
}
