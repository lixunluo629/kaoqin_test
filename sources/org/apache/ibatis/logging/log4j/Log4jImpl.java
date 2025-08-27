package org.apache.ibatis.logging.log4j;

import org.apache.ibatis.logging.Log;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/logging/log4j/Log4jImpl.class */
public class Log4jImpl implements Log {
    private static final String FQCN = Log4jImpl.class.getName();
    private final Logger log;

    public Log4jImpl(String clazz) {
        this.log = Logger.getLogger(clazz);
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
        this.log.log(FQCN, Level.ERROR, s, e);
    }

    @Override // org.apache.ibatis.logging.Log
    public void error(String s) {
        this.log.log(FQCN, Level.ERROR, s, null);
    }

    @Override // org.apache.ibatis.logging.Log
    public void debug(String s) {
        this.log.log(FQCN, Level.DEBUG, s, null);
    }

    @Override // org.apache.ibatis.logging.Log
    public void trace(String s) {
        this.log.log(FQCN, Level.TRACE, s, null);
    }

    @Override // org.apache.ibatis.logging.Log
    public void warn(String s) {
        this.log.log(FQCN, Level.WARN, s, null);
    }
}
