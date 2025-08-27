package org.apache.ibatis.logging.nologging;

import org.apache.ibatis.logging.Log;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/logging/nologging/NoLoggingImpl.class */
public class NoLoggingImpl implements Log {
    public NoLoggingImpl(String clazz) {
    }

    @Override // org.apache.ibatis.logging.Log
    public boolean isDebugEnabled() {
        return false;
    }

    @Override // org.apache.ibatis.logging.Log
    public boolean isTraceEnabled() {
        return false;
    }

    @Override // org.apache.ibatis.logging.Log
    public void error(String s, Throwable e) {
    }

    @Override // org.apache.ibatis.logging.Log
    public void error(String s) {
    }

    @Override // org.apache.ibatis.logging.Log
    public void debug(String s) {
    }

    @Override // org.apache.ibatis.logging.Log
    public void trace(String s) {
    }

    @Override // org.apache.ibatis.logging.Log
    public void warn(String s) {
    }
}
