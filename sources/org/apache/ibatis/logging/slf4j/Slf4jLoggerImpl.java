package org.apache.ibatis.logging.slf4j;

import org.apache.ibatis.logging.Log;
import org.slf4j.Logger;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/logging/slf4j/Slf4jLoggerImpl.class */
class Slf4jLoggerImpl implements Log {
    private final Logger log;

    public Slf4jLoggerImpl(Logger logger) {
        this.log = logger;
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
