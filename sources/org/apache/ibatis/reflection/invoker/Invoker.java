package org.apache.ibatis.reflection.invoker;

import java.lang.reflect.InvocationTargetException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/invoker/Invoker.class */
public interface Invoker {
    Object invoke(Object obj, Object[] objArr) throws IllegalAccessException, InvocationTargetException;

    Class<?> getType();
}
