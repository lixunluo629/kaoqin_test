package org.apache.ibatis.logging.log4j2;

import org.apache.ibatis.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.spi.AbstractLogger;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/logging/log4j2/Log4j2Impl.class */
public class Log4j2Impl implements Log {
    private final Log log;

    public Log4j2Impl(String clazz) {
        AbstractLogger logger = LogManager.getLogger(clazz);
        if (logger instanceof AbstractLogger) {
            this.log = new Log4j2AbstractLoggerImpl(logger);
        } else {
            this.log = new Log4j2LoggerImpl(logger);
        }
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
