package com.mysql.jdbc.log;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/log/NullLogger.class */
public class NullLogger implements Log {
    public NullLogger(String instanceName) {
    }

    @Override // com.mysql.jdbc.log.Log
    public boolean isDebugEnabled() {
        return false;
    }

    @Override // com.mysql.jdbc.log.Log
    public boolean isErrorEnabled() {
        return false;
    }

    @Override // com.mysql.jdbc.log.Log
    public boolean isFatalEnabled() {
        return false;
    }

    @Override // com.mysql.jdbc.log.Log
    public boolean isInfoEnabled() {
        return false;
    }

    @Override // com.mysql.jdbc.log.Log
    public boolean isTraceEnabled() {
        return false;
    }

    @Override // com.mysql.jdbc.log.Log
    public boolean isWarnEnabled() {
        return false;
    }

    @Override // com.mysql.jdbc.log.Log
    public void logDebug(Object msg) {
    }

    @Override // com.mysql.jdbc.log.Log
    public void logDebug(Object msg, Throwable thrown) {
    }

    @Override // com.mysql.jdbc.log.Log
    public void logError(Object msg) {
    }

    @Override // com.mysql.jdbc.log.Log
    public void logError(Object msg, Throwable thrown) {
    }

    @Override // com.mysql.jdbc.log.Log
    public void logFatal(Object msg) {
    }

    @Override // com.mysql.jdbc.log.Log
    public void logFatal(Object msg, Throwable thrown) {
    }

    @Override // com.mysql.jdbc.log.Log
    public void logInfo(Object msg) {
    }

    @Override // com.mysql.jdbc.log.Log
    public void logInfo(Object msg, Throwable thrown) {
    }

    @Override // com.mysql.jdbc.log.Log
    public void logTrace(Object msg) {
    }

    @Override // com.mysql.jdbc.log.Log
    public void logTrace(Object msg, Throwable thrown) {
    }

    @Override // com.mysql.jdbc.log.Log
    public void logWarn(Object msg) {
    }

    @Override // com.mysql.jdbc.log.Log
    public void logWarn(Object msg, Throwable thrown) {
    }
}
