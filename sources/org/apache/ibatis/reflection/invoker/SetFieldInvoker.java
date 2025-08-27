package org.apache.ibatis.reflection.invoker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/invoker/SetFieldInvoker.class */
public class SetFieldInvoker implements Invoker {
    private final Field field;

    public SetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override // org.apache.ibatis.reflection.invoker.Invoker
    public Object invoke(Object target, Object[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        this.field.set(target, args[0]);
        return null;
    }

    @Override // org.apache.ibatis.reflection.invoker.Invoker
    public Class<?> getType() {
        return this.field.getType();
    }
}
