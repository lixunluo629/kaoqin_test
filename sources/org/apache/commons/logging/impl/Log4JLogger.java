package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/* loaded from: commons-logging-1.0.4.jar:org/apache/commons/logging/impl/Log4JLogger.class */
public class Log4JLogger implements Log, Serializable {
    private static final String FQCN;
    private static final boolean is12;
    private transient Logger logger;
    private String name;
    static Class class$org$apache$commons$logging$impl$Log4JLogger;
    static Class class$org$apache$log4j$Level;
    static Class class$org$apache$log4j$Priority;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        Class clsClass$2;
        Class<?> clsClass$3;
        if (class$org$apache$commons$logging$impl$Log4JLogger == null) {
            clsClass$ = class$("org.apache.commons.logging.impl.Log4JLogger");
            class$org$apache$commons$logging$impl$Log4JLogger = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$logging$impl$Log4JLogger;
        }
        FQCN = clsClass$.getName();
        if (class$org$apache$log4j$Priority == null) {
            clsClass$2 = class$("org.apache.log4j.Priority");
            class$org$apache$log4j$Priority = clsClass$2;
        } else {
            clsClass$2 = class$org$apache$log4j$Priority;
        }
        if (class$org$apache$log4j$Level == null) {
            clsClass$3 = class$("org.apache.log4j.Level");
            class$org$apache$log4j$Level = clsClass$3;
        } else {
            clsClass$3 = class$org$apache$log4j$Level;
        }
        is12 = clsClass$2.isAssignableFrom(clsClass$3);
    }

    public Log4JLogger() {
        this.logger = null;
        this.name = null;
    }

    public Log4JLogger(String name) {
        this.logger = null;
        this.name = null;
        this.name = name;
        this.logger = getLogger();
    }

    public Log4JLogger(Logger logger) {
        this.logger = null;
        this.name = null;
        this.name = logger.getName();
        this.logger = logger;
    }

    @Override // org.apache.commons.logging.Log
    public void trace(Object message) {
        if (is12) {
            getLogger().log(FQCN, Level.DEBUG, message, null);
        } else {
            getLogger().log(FQCN, Level.DEBUG, message, (Throwable) null);
        }
    }

    @Override // org.apache.commons.logging.Log
    public void trace(Object message, Throwable t) {
        if (is12) {
            getLogger().log(FQCN, Level.DEBUG, message, t);
        } else {
            getLogger().log(FQCN, Level.DEBUG, message, t);
        }
    }

    @Override // org.apache.commons.logging.Log
    public void debug(Object message) {
        if (is12) {
            getLogger().log(FQCN, Level.DEBUG, message, null);
        } else {
            getLogger().log(FQCN, Level.DEBUG, message, (Throwable) null);
        }
    }

    @Override // org.apache.commons.logging.Log
    public void debug(Object message, Throwable t) {
        if (is12) {
            getLogger().log(FQCN, Level.DEBUG, message, t);
        } else {
            getLogger().log(FQCN, Level.DEBUG, message, t);
        }
    }

    @Override // org.apache.commons.logging.Log
    public void info(Object message) {
        if (is12) {
            getLogger().log(FQCN, Level.INFO, message, null);
        } else {
            getLogger().log(FQCN, Level.INFO, message, (Throwable) null);
        }
    }

    @Override // org.apache.commons.logging.Log
    public void info(Object message, Throwable t) {
        if (is12) {
            getLogger().log(FQCN, Level.INFO, message, t);
        } else {
            getLogger().log(FQCN, Level.INFO, message, t);
        }
    }

    @Override // org.apache.commons.logging.Log
    public void warn(Object message) {
        if (is12) {
            getLogger().log(FQCN, Level.WARN, message, null);
        } else {
            getLogger().log(FQCN, Level.WARN, message, (Throwable) null);
        }
    }

    @Override // org.apache.commons.logging.Log
    public void warn(Object message, Throwable t) {
        if (is12) {
            getLogger().log(FQCN, Level.WARN, message, t);
        } else {
            getLogger().log(FQCN, Level.WARN, message, t);
        }
    }

    @Override // org.apache.commons.logging.Log
    public void error(Object message) {
        if (is12) {
            getLogger().log(FQCN, Level.ERROR, message, null);
        } else {
            getLogger().log(FQCN, Level.ERROR, message, (Throwable) null);
        }
    }

    @Override // org.apache.commons.logging.Log
    public void error(Object message, Throwable t) {
        if (is12) {
            getLogger().log(FQCN, Level.ERROR, message, t);
        } else {
            getLogger().log(FQCN, Level.ERROR, message, t);
        }
    }

    @Override // org.apache.commons.logging.Log
    public void fatal(Object message) {
        if (is12) {
            getLogger().log(FQCN, Level.FATAL, message, null);
        } else {
            getLogger().log(FQCN, Level.FATAL, message, (Throwable) null);
        }
    }

    @Override // org.apache.commons.logging.Log
    public void fatal(Object message, Throwable t) {
        if (is12) {
            getLogger().log(FQCN, Level.FATAL, message, t);
        } else {
            getLogger().log(FQCN, Level.FATAL, message, t);
        }
    }

    public Logger getLogger() {
        if (this.logger == null) {
            this.logger = Logger.getLogger(this.name);
        }
        return this.logger;
    }

    @Override // org.apache.commons.logging.Log
    public boolean isDebugEnabled() {
        return getLogger().isDebugEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isErrorEnabled() {
        if (is12) {
            return getLogger().isEnabledFor(Level.ERROR);
        }
        return getLogger().isEnabledFor(Level.ERROR);
    }

    @Override // org.apache.commons.logging.Log
    public boolean isFatalEnabled() {
        if (is12) {
            return getLogger().isEnabledFor(Level.FATAL);
        }
        return getLogger().isEnabledFor(Level.FATAL);
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
        if (is12) {
            return getLogger().isEnabledFor(Level.WARN);
        }
        return getLogger().isEnabledFor(Level.WARN);
    }
}
