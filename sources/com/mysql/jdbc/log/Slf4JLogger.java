package com.mysql.jdbc.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/log/Slf4JLogger.class */
public class Slf4JLogger implements Log {
    private Logger log;

    public Slf4JLogger(String name) {
        this.log = LoggerFactory.getLogger(name);
    }

    @Override // com.mysql.jdbc.log.Log
    public boolean isDebugEnabled() {
        return this.log.isDebugEnabled();
    }

    @Override // com.mysql.jdbc.log.Log
    public boolean isErrorEnabled() {
        return this.log.isErrorEnabled();
    }

    @Override // com.mysql.jdbc.log.Log
    public boolean isFatalEnabled() {
        return this.log.isErrorEnabled();
    }

    @Override // com.mysql.jdbc.log.Log
    public boolean isInfoEnabled() {
        return this.log.isInfoEnabled();
    }

    @Override // com.mysql.jdbc.log.Log
    public boolean isTraceEnabled() {
        return this.log.isTraceEnabled();
    }

    @Override // com.mysql.jdbc.log.Log
    public boolean isWarnEnabled() {
        return this.log.isWarnEnabled();
    }

    @Override // com.mysql.jdbc.log.Log
    public void logDebug(Object msg) {
        this.log.debug(msg.toString());
    }

    @Override // com.mysql.jdbc.log.Log
    public void logDebug(Object msg, Throwable thrown) {
        this.log.debug(msg.toString(), thrown);
    }

    @Override // com.mysql.jdbc.log.Log
    public void logError(Object msg) {
        this.log.error(msg.toString());
    }

    @Override // com.mysql.jdbc.log.Log
    public void logError(Object msg, Throwable thrown) {
        this.log.error(msg.toString(), thrown);
    }

    @Override // com.mysql.jdbc.log.Log
    public void logFatal(Object msg) {
        this.log.error(msg.toString());
    }

    @Override // com.mysql.jdbc.log.Log
    public void logFatal(Object msg, Throwable thrown) {
        this.log.error(msg.toString(), thrown);
    }

    @Override // com.mysql.jdbc.log.Log
    public void logInfo(Object msg) {
        this.log.info(msg.toString());
    }

    @Override // com.mysql.jdbc.log.Log
    public void logInfo(Object msg, Throwable thrown) {
        this.log.info(msg.toString(), thrown);
    }

    @Override // com.mysql.jdbc.log.Log
    public void logTrace(Object msg) {
        this.log.trace(msg.toString());
    }

    @Override // com.mysql.jdbc.log.Log
    public void logTrace(Object msg, Throwable thrown) {
        this.log.trace(msg.toString(), thrown);
    }

    @Override // com.mysql.jdbc.log.Log
    public void logWarn(Object msg) {
        this.log.warn(msg.toString());
    }

    @Override // com.mysql.jdbc.log.Log
    public void logWarn(Object msg, Throwable thrown) {
        this.log.warn(msg.toString(), thrown);
    }
}
