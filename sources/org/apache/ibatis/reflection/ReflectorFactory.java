package org.apache.ibatis.reflection;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/ReflectorFactory.class */
public interface ReflectorFactory {
    boolean isClassCacheEnabled();

    void setClassCacheEnabled(boolean z);

    Reflector findForClass(Class<?> cls);
}
