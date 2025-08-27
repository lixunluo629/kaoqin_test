package org.apache.ibatis.reflection.invoker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/invoker/GetFieldInvoker.class */
public class GetFieldInvoker implements Invoker {
    private final Field field;

    public GetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override // org.apache.ibatis.reflection.invoker.Invoker
    public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
        return this.field.get(target);
    }

    @Override // org.apache.ibatis.reflection.invoker.Invoker
    public Class<?> getType() {
        return this.field.getType();
    }
}
