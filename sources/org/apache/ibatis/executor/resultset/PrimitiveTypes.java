package org.apache.ibatis.executor.resultset;

import java.util.HashMap;
import java.util.Map;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/resultset/PrimitiveTypes.class */
public class PrimitiveTypes {
    private final Map<Class<?>, Class<?>> primitiveToWrappers = new HashMap();
    private final Map<Class<?>, Class<?>> wrappersToPrimitives = new HashMap();

    public PrimitiveTypes() {
        add(Boolean.TYPE, Boolean.class);
        add(Byte.TYPE, Byte.class);
        add(Character.TYPE, Character.class);
        add(Double.TYPE, Double.class);
        add(Float.TYPE, Float.class);
        add(Integer.TYPE, Integer.class);
        add(Long.TYPE, Long.class);
        add(Short.TYPE, Short.class);
        add(Void.TYPE, Void.class);
    }

    private void add(Class<?> primitiveType, Class<?> wrapperType) {
        this.primitiveToWrappers.put(primitiveType, wrapperType);
        this.wrappersToPrimitives.put(wrapperType, primitiveType);
    }

    public Class<?> getWrapper(Class<?> primitiveType) {
        return this.primitiveToWrappers.get(primitiveType);
    }

    public Class<?> getPrimitive(Class<?> wrapperType) {
        return this.wrappersToPrimitives.get(wrapperType);
    }
}
