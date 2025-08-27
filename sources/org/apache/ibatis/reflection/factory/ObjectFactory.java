package org.apache.ibatis.reflection.factory;

import java.util.List;
import java.util.Properties;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/factory/ObjectFactory.class */
public interface ObjectFactory {
    void setProperties(Properties properties);

    <T> T create(Class<T> cls);

    <T> T create(Class<T> cls, List<Class<?>> list, List<Object> list2);

    <T> boolean isCollection(Class<T> cls);
}
