package org.apache.ibatis.plugin;

import java.util.Properties;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/plugin/Interceptor.class */
public interface Interceptor {
    Object intercept(Invocation invocation) throws Throwable;

    Object plugin(Object obj);

    void setProperties(Properties properties);
}
