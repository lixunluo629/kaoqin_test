package org.apache.ibatis.logging.log4j2;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/logging/log4j2/Log4j2LoggerImpl.class */
public class Log4j2LoggerImpl implements Log {
    private static final Marker MARKER = MarkerManager.getMarker(LogFactory.MARKER);
    private final Logger log;

    public Log4j2LoggerImpl(Logger logger) {
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
        this.log.error(MARKER, s, e);
    }

    @Override // org.apache.ibatis.logging.Log
    public void error(String s) {
        this.log.error(MARKER, s);
    }

    @Override // org.apache.ibatis.logging.Log
    public void debug(String s) {
        this.log.debug(MARKER, s);
    }

    @Override // org.apache.ibatis.logging.Log
    public void trace(String s) {
        this.log.trace(MARKER, s);
    }

    @Override // org.apache.ibatis.logging.Log
    public void warn(String s) {
        this.log.warn(MARKER, s);
    }
}
