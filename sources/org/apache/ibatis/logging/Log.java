package org.apache.ibatis.logging;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/logging/Log.class */
public interface Log {
    boolean isDebugEnabled();

    boolean isTraceEnabled();

    void error(String str, Throwable th);

    void error(String str);

    void debug(String str);

    void trace(String str);

    void warn(String str);
}
