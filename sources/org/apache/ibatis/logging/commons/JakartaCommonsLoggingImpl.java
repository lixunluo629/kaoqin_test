package org.apache.ibatis.logging.commons;

import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.logging.Log;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/logging/commons/JakartaCommonsLoggingImpl.class */
public class JakartaCommonsLoggingImpl implements Log {
    private final org.apache.commons.logging.Log log;

    public JakartaCommonsLoggingImpl(String clazz) {
        this.log = LogFactory.getLog(clazz);
    }

    @Override // org.apache.ibatis.logging.Log
    public boolean isDebugEnabled() {
        return this.log.isDebugEnabled();
    }

    @Override // org.apache.ibatis.logging.Log
    public boolean isTraceEnabled() {
        return this.log.isTraceEnabled();
    }

    @Override // org.apache.ibatis.logging.Log
    public void error(String s, Throwable e) {
        this.log.error(s, e);
    }

    @Override // org.apache.ibatis.logging.Log
    public void error(String s) {
        this.log.error(s);
    }

    @Override // org.apache.ibatis.logging.Log
    public void debug(String s) {
        this.log.debug(s);
    }

    @Override // org.apache.ibatis.logging.Log
    public void trace(String s) {
        this.log.trace(s);
    }

    @Override // org.apache.ibatis.logging.Log
    public void warn(String s) {
        this.log.warn(s);
    }
}
