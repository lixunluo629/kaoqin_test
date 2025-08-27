package org.apache.ibatis.executor.loader;

import java.util.List;
import java.util.Properties;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.Configuration;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/loader/ProxyFactory.class */
public interface ProxyFactory {
    void setProperties(Properties properties);

    Object createProxy(Object obj, ResultLoaderMap resultLoaderMap, Configuration configuration, ObjectFactory objectFactory, List<Class<?>> list, List<Object> list2);
}
