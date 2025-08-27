package org.apache.ibatis.logging.stdout;

import org.apache.ibatis.logging.Log;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/logging/stdout/StdOutImpl.class */
public class StdOutImpl implements Log {
    public StdOutImpl(String clazz) {
    }

    @Override // org.apache.ibatis.logging.Log
    public boolean isDebugEnabled() {
        return true;
    }

    @Override // org.apache.ibatis.logging.Log
    public boolean isTraceEnabled() {
        return true;
    }

    @Override // org.apache.ibatis.logging.Log
    public void error(String s, Throwable e) {
        System.err.println(s);
        e.printStackTrace(System.err);
    }

    @Override // org.apache.ibatis.logging.Log
    public void error(String s) {
        System.err.println(s);
    }

    @Override // org.apache.ibatis.logging.Log
    public void debug(String s) {
        System.out.println(s);
    }

    @Override // org.apache.ibatis.logging.Log
    public void trace(String s) {
        System.out.println(s);
    }

    @Override // org.apache.ibatis.logging.Log
    public void warn(String s) {
        System.out.println(s);
    }
}
