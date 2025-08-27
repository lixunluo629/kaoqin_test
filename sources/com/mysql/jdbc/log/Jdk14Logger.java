package com.mysql.jdbc.log;

import com.mysql.jdbc.profiler.ProfilerEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/log/Jdk14Logger.class */
public class Jdk14Logger implements Log {
    private static final Level DEBUG = Level.FINE;
    private static final Level ERROR = Level.SEVERE;
    private static final Level FATAL = Level.SEVERE;
    private static final Level INFO = Level.INFO;
    private static final Level TRACE = Level.FINEST;
    private static final Level WARN = Level.WARNING;
    protected Logger jdkLogger;

    public Jdk14Logger(String name) {
        this.jdkLogger = null;
        this.jdkLogger = Logger.getLogger(name);
    }

    @Override // com.mysql.jdbc.log.Log
    public boolean isDebugEnabled() {
        return this.jdkLogger.isLoggable(Level.FINE);
    }

    @Override // com.mysql.jdbc.log.Log
    public boolean isErrorEnabled() {
        return this.jdkLogger.isLoggable(Level.SEVERE);
    }

    @Override // com.mysql.jdbc.log.Log
    public boolean isFatalEnabled() {
        return this.jdkLogger.isLoggable(Level.SEVERE);
    }

    @Override // com.mysql.jdbc.log.Log
    public boolean isInfoEnabled() {
        return this.jdkLogger.isLoggable(Level.INFO);
    }

    @Override // com.mysql.jdbc.log.Log
    public boolean isTraceEnabled() {
        return this.jdkLogger.isLoggable(Level.FINEST);
    }

    @Override // com.mysql.jdbc.log.Log
    public boolean isWarnEnabled() {
        return this.jdkLogger.isLoggable(Level.WARNING);
    }

    @Override // com.mysql.jdbc.log.Log
    public void logDebug(Object message) {
        logInternal(DEBUG, message, null);
    }

    @Override // com.mysql.jdbc.log.Log
    public void logDebug(Object message, Throwable exception) {
        logInternal(DEBUG, message, exception);
    }

    @Override // com.mysql.jdbc.log.Log
    public void logError(Object message) {
        logInternal(ERROR, message, null);
    }

    @Override // com.mysql.jdbc.log.Log
    public void logError(Object message, Throwable exception) {
        logInternal(ERROR, message, exception);
    }

    @Override // com.mysql.jdbc.log.Log
    public void logFatal(Object message) {
        logInternal(FATAL, message, null);
    }

    @Override // com.mysql.jdbc.log.Log
    public void logFatal(Object message, Throwable exception) {
        logInternal(FATAL, message, exception);
    }

    @Override // com.mysql.jdbc.log.Log
    public void logInfo(Object message) {
        logInternal(INFO, message, null);
    }

    @Override // com.mysql.jdbc.log.Log
    public void logInfo(Object message, Throwable exception) {
        logInternal(INFO, message, exception);
    }

    @Override // com.mysql.jdbc.log.Log
    public void logTrace(Object message) {
        logInternal(TRACE, message, null);
    }

    @Override // com.mysql.jdbc.log.Log
    public void logTrace(Object message, Throwable exception) {
        logInternal(TRACE, message, exception);
    }

    @Override // com.mysql.jdbc.log.Log
    public void logWarn(Object message) {
        logInternal(WARN, message, null);
    }

    @Override // com.mysql.jdbc.log.Log
    public void logWarn(Object message, Throwable exception) {
        logInternal(WARN, message, exception);
    }

    private static final int findCallerStackDepth(StackTraceElement[] stackTrace) {
        int numFrames = stackTrace.length;
        for (int i = 0; i < numFrames; i++) {
            String callerClassName = stackTrace[i].getClassName();
            if (!callerClassName.startsWith("com.mysql.jdbc") || callerClassName.startsWith("com.mysql.jdbc.compliance")) {
                return i;
            }
        }
        return 0;
    }

    private void logInternal(Level level, Object msg, Throwable exception) {
        String messageAsString;
        if (this.jdkLogger.isLoggable(level)) {
            String callerMethodName = "N/A";
            String callerClassName = "N/A";
            if (msg instanceof ProfilerEvent) {
                messageAsString = msg.toString();
            } else {
                Throwable locationException = new Throwable();
                StackTraceElement[] locations = locationException.getStackTrace();
                int frameIdx = findCallerStackDepth(locations);
                if (frameIdx != 0) {
                    callerClassName = locations[frameIdx].getClassName();
                    callerMethodName = locations[frameIdx].getMethodName();
                }
                messageAsString = String.valueOf(msg);
            }
            if (exception == null) {
                this.jdkLogger.logp(level, callerClassName, callerMethodName, messageAsString);
            } else {
                this.jdkLogger.logp(level, callerClassName, callerMethodName, messageAsString, exception);
            }
        }
    }
}
