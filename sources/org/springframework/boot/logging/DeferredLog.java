package org.springframework.boot.logging;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/logging/DeferredLog.class */
public class DeferredLog implements Log {
    private List<Line> lines = new ArrayList();

    @Override // org.apache.commons.logging.Log
    public boolean isTraceEnabled() {
        return true;
    }

    @Override // org.apache.commons.logging.Log
    public boolean isDebugEnabled() {
        return true;
    }

    @Override // org.apache.commons.logging.Log
    public boolean isInfoEnabled() {
        return true;
    }

    @Override // org.apache.commons.logging.Log
    public boolean isWarnEnabled() {
        return true;
    }

    @Override // org.apache.commons.logging.Log
    public boolean isErrorEnabled() {
        return true;
    }

    @Override // org.apache.commons.logging.Log
    public boolean isFatalEnabled() {
        return true;
    }

    @Override // org.apache.commons.logging.Log
    public void trace(Object message) {
        log(LogLevel.TRACE, message, null);
    }

    @Override // org.apache.commons.logging.Log
    public void trace(Object message, Throwable t) {
        log(LogLevel.TRACE, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void debug(Object message) {
        log(LogLevel.DEBUG, message, null);
    }

    @Override // org.apache.commons.logging.Log
    public void debug(Object message, Throwable t) {
        log(LogLevel.DEBUG, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void info(Object message) {
        log(LogLevel.INFO, message, null);
    }

    @Override // org.apache.commons.logging.Log
    public void info(Object message, Throwable t) {
        log(LogLevel.INFO, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void warn(Object message) {
        log(LogLevel.WARN, message, null);
    }

    @Override // org.apache.commons.logging.Log
    public void warn(Object message, Throwable t) {
        log(LogLevel.WARN, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void error(Object message) {
        log(LogLevel.ERROR, message, null);
    }

    @Override // org.apache.commons.logging.Log
    public void error(Object message, Throwable t) {
        log(LogLevel.ERROR, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void fatal(Object message) {
        log(LogLevel.FATAL, message, null);
    }

    @Override // org.apache.commons.logging.Log
    public void fatal(Object message, Throwable t) {
        log(LogLevel.FATAL, message, t);
    }

    private void log(LogLevel level, Object message, Throwable t) {
        this.lines.add(new Line(level, message, t));
    }

    public void replayTo(Class<?> destination) {
        replayTo(LogFactory.getLog(destination));
    }

    public void replayTo(Log destination) {
        for (Line line : this.lines) {
            line.replayTo(destination);
        }
        this.lines.clear();
    }

    public static Log replay(Log source, Class<?> destination) {
        return replay(source, LogFactory.getLog(destination));
    }

    public static Log replay(Log source, Log destination) {
        if (source instanceof DeferredLog) {
            ((DeferredLog) source).replayTo(destination);
        }
        return destination;
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/logging/DeferredLog$Line.class */
    private static class Line {
        private final LogLevel level;
        private final Object message;
        private final Throwable throwable;

        Line(LogLevel level, Object message, Throwable throwable) {
            this.level = level;
            this.message = message;
            this.throwable = throwable;
        }

        public void replayTo(Log log) {
            switch (this.level) {
                case TRACE:
                    log.trace(this.message, this.throwable);
                    break;
                case DEBUG:
                    log.debug(this.message, this.throwable);
                    break;
                case INFO:
                    log.info(this.message, this.throwable);
                    break;
                case WARN:
                    log.warn(this.message, this.throwable);
                    break;
                case ERROR:
                    log.error(this.message, this.throwable);
                    break;
                case FATAL:
                    log.fatal(this.message, this.throwable);
                    break;
            }
        }
    }
}
