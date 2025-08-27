package org.apache.ibatis.logging.log4j2;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.spi.ExtendedLoggerWrapper;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/logging/log4j2/Log4j2AbstractLoggerImpl.class */
public class Log4j2AbstractLoggerImpl implements Log {
    private static final Marker MARKER = MarkerManager.getMarker(LogFactory.MARKER);
    private static final String FQCN = Log4j2Impl.class.getName();
    private final ExtendedLoggerWrapper log;

    public Log4j2AbstractLoggerImpl(AbstractLogger abstractLogger) {
        this.log = new ExtendedLoggerWrapper(abstractLogger, abstractLogger.getName(), abstractLogger.getMessageFactory());
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
        this.log.logIfEnabled(FQCN, Level.ERROR, MARKER, new SimpleMessage(s), e);
    }

    @Override // org.apache.ibatis.logging.Log
    public void error(String s) {
        this.log.logIfEnabled(FQCN, Level.ERROR, MARKER, new SimpleMessage(s), (Throwable) null);
    }

    @Override // org.apache.ibatis.logging.Log
    public void debug(String s) {
        this.log.logIfEnabled(FQCN, Level.DEBUG, MARKER, new SimpleMessage(s), (Throwable) null);
    }

    @Override // org.apache.ibatis.logging.Log
    public void trace(String s) {
        this.log.logIfEnabled(FQCN, Level.TRACE, MARKER, new SimpleMessage(s), (Throwable) null);
    }

    @Override // org.apache.ibatis.logging.Log
    public void warn(String s) {
        this.log.logIfEnabled(FQCN, Level.WARN, MARKER, new SimpleMessage(s), (Throwable) null);
    }
}
