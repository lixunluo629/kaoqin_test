package org.apache.ibatis.builder.annotation;

import java.lang.reflect.Method;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/builder/annotation/ProviderContext.class */
public final class ProviderContext {
    private final Class<?> mapperType;
    private final Method mapperMethod;

    ProviderContext(Class<?> mapperType, Method mapperMethod) {
        this.mapperType = mapperType;
        this.mapperMethod = mapperMethod;
    }

    public Class<?> getMapperType() {
        return this.mapperType;
    }

    public Method getMapperMethod() {
        return this.mapperMethod;
    }
}
