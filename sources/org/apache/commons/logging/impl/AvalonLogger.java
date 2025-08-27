package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.avalon.framework.logger.Logger;
import org.apache.commons.logging.Log;

/* loaded from: commons-logging-1.0.4.jar:org/apache/commons/logging/impl/AvalonLogger.class */
public class AvalonLogger implements Log, Serializable {
    private static Logger defaultLogger = null;
    private transient Logger logger;
    private String name;

    public AvalonLogger(Logger logger) {
        this.logger = null;
        this.name = null;
        this.name = this.name;
        this.logger = logger;
    }

    public AvalonLogger(String name) {
        this.logger = null;
        this.name = null;
        if (defaultLogger == null) {
            throw new NullPointerException("default logger has to be specified if this constructor is used!");
        }
        this.logger = getLogger();
    }

    public Logger getLogger() {
        if (this.logger == null) {
            this.logger = defaultLogger.getChildLogger(this.name);
        }
        return this.logger;
    }

    public static void setDefaultLogger(Logger logger) {
        defaultLogger = logger;
    }

    @Override // org.apache.commons.logging.Log
    public void debug(Object o, Throwable t) {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(String.valueOf(o), t);
        }
    }

    @Override // org.apache.commons.logging.Log
    public void debug(Object o) {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(String.valueOf(o));
        }
    }

    @Override // org.apache.commons.logging.Log
    public void error(Object o, Throwable t) {
        if (getLogger().isErrorEnabled()) {
            getLogger().error(String.valueOf(o), t);
        }
    }

    @Override // org.apache.commons.logging.Log
    public void error(Object o) {
        if (getLogger().isErrorEnabled()) {
            getLogger().error(String.valueOf(o));
        }
    }

    @Override // org.apache.commons.logging.Log
    public void fatal(Object o, Throwable t) {
        if (getLogger().isFatalErrorEnabled()) {
            getLogger().fatalError(String.valueOf(o), t);
        }
    }

    @Override // org.apache.commons.logging.Log
    public void fatal(Object o) {
        if (getLogger().isFatalErrorEnabled()) {
            getLogger().fatalError(String.valueOf(o));
        }
    }

    @Override // org.apache.commons.logging.Log
    public void info(Object o, Throwable t) {
        if (getLogger().isInfoEnabled()) {
            getLogger().info(String.valueOf(o), t);
        }
    }

    @Override // org.apache.commons.logging.Log
    public void info(Object o) {
        if (getLogger().isInfoEnabled()) {
            getLogger().info(String.valueOf(o));
        }
    }

    @Override // org.apache.commons.logging.Log
    public boolean isDebugEnabled() {
        return getLogger().isDebugEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isErrorEnabled() {
        return getLogger().isErrorEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isFatalEnabled() {
        return getLogger().isFatalErrorEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isInfoEnabled() {
        return getLogger().isInfoEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isTraceEnabled() {
        return getLogger().isDebugEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isWarnEnabled() {
        return getLogger().isWarnEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public void trace(Object o, Throwable t) {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(String.valueOf(o), t);
        }
    }

    @Override // org.apache.commons.logging.Log
    public void trace(Object o) {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(String.valueOf(o));
        }
    }

    @Override // org.apache.commons.logging.Log
    public void warn(Object o, Throwable t) {
        if (getLogger().isWarnEnabled()) {
            getLogger().warn(String.valueOf(o), t);
        }
    }

    @Override // org.apache.commons.logging.Log
    public void warn(Object o) {
        if (getLogger().isWarnEnabled()) {
            getLogger().warn(String.valueOf(o));
        }
    }
}
