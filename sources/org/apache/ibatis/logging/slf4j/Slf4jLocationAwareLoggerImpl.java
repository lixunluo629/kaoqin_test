package org.apache.ibatis.logging.slf4j;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.spi.LocationAwareLogger;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/logging/slf4j/Slf4jLocationAwareLoggerImpl.class */
class Slf4jLocationAwareLoggerImpl implements Log {
    private static final Marker MARKER = MarkerFactory.getMarker(LogFactory.MARKER);
    private static final String FQCN = Slf4jImpl.class.getName();
    private final LocationAwareLogger logger;

    Slf4jLocationAwareLoggerImpl(LocationAwareLogger logger) {
        this.logger = logger;
    }

    @Override // org.apache.ibatis.logging.Log
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    @Override // org.apache.ibatis.logging.Log
    public boolean isTraceEnabled() {
        return this.logger.isTraceEnabled();
    }

    @Override // org.apache.ibatis.logging.Log
    public void error(String s, Throwable e) {
        this.logger.log(MARKER, FQCN, 40, s, null, e);
    }

    @Override // org.apache.ibatis.logging.Log
    public void error(String s) {
        this.logger.log(MARKER, FQCN, 40, s, null, null);
    }

    @Override // org.apache.ibatis.logging.Log
    public void debug(String s) {
        this.logger.log(MARKER, FQCN, 10, s, null, null);
    }

    @Override // org.apache.ibatis.logging.Log
    public void trace(String s) {
        this.logger.log(MARKER, FQCN, 0, s, null, null);
    }

    @Override // org.apache.ibatis.logging.Log
    public void warn(String s) {
        this.logger.log(MARKER, FQCN, 30, s, null, null);
    }
}
