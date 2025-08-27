package org.apache.ibatis.logging.slf4j;

import org.apache.ibatis.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.spi.LocationAwareLogger;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/logging/slf4j/Slf4jImpl.class */
public class Slf4jImpl implements Log {
    private Log log;

    public Slf4jImpl(String clazz) throws NoSuchMethodException, SecurityException {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (logger instanceof LocationAwareLogger) {
            try {
                logger.getClass().getMethod("log", Marker.class, String.class, Integer.TYPE, String.class, Object[].class, Throwable.class);
                this.log = new Slf4jLocationAwareLoggerImpl((LocationAwareLogger) logger);
                return;
            } catch (NoSuchMethodException e) {
            } catch (SecurityException e2) {
            }
        }
        this.log = new Slf4jLoggerImpl(logger);
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
