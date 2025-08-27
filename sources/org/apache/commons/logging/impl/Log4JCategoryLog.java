package org.apache.commons.logging.impl;

import org.apache.commons.logging.Log;
import org.apache.log4j.Category;
import org.apache.log4j.Level;

/* loaded from: commons-logging-1.0.4.jar:org/apache/commons/logging/impl/Log4JCategoryLog.class */
public final class Log4JCategoryLog implements Log {
    private static final String FQCN;
    private Category category;
    static Class class$org$apache$commons$logging$impl$Log4JCategoryLog;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$logging$impl$Log4JCategoryLog == null) {
            clsClass$ = class$("org.apache.commons.logging.impl.Log4JCategoryLog");
            class$org$apache$commons$logging$impl$Log4JCategoryLog = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$logging$impl$Log4JCategoryLog;
        }
        FQCN = clsClass$.getName();
    }

    public Log4JCategoryLog() {
        this.category = null;
    }

    public Log4JCategoryLog(String name) {
        this.category = null;
        this.category = Category.getInstance(name);
    }

    public Log4JCategoryLog(Category category) {
        this.category = null;
        this.category = category;
    }

    @Override // org.apache.commons.logging.Log
    public void trace(Object message) {
        this.category.log(FQCN, Level.DEBUG, message, (Throwable) null);
    }

    @Override // org.apache.commons.logging.Log
    public void trace(Object message, Throwable t) {
        this.category.log(FQCN, Level.DEBUG, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void debug(Object message) {
        this.category.log(FQCN, Level.DEBUG, message, (Throwable) null);
    }

    @Override // org.apache.commons.logging.Log
    public void debug(Object message, Throwable t) {
        this.category.log(FQCN, Level.DEBUG, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void info(Object message) {
        this.category.log(FQCN, Level.INFO, message, (Throwable) null);
    }

    @Override // org.apache.commons.logging.Log
    public void info(Object message, Throwable t) {
        this.category.log(FQCN, Level.INFO, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void warn(Object message) {
        this.category.log(FQCN, Level.WARN, message, (Throwable) null);
    }

    @Override // org.apache.commons.logging.Log
    public void warn(Object message, Throwable t) {
        this.category.log(FQCN, Level.WARN, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void error(Object message) {
        this.category.log(FQCN, Level.ERROR, message, (Throwable) null);
    }

    @Override // org.apache.commons.logging.Log
    public void error(Object message, Throwable t) {
        this.category.log(FQCN, Level.ERROR, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void fatal(Object message) {
        this.category.log(FQCN, Level.FATAL, message, (Throwable) null);
    }

    @Override // org.apache.commons.logging.Log
    public void fatal(Object message, Throwable t) {
        this.category.log(FQCN, Level.FATAL, message, t);
    }

    public Category getCategory() {
        return this.category;
    }

    @Override // org.apache.commons.logging.Log
    public boolean isDebugEnabled() {
        return this.category.isDebugEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isErrorEnabled() {
        return this.category.isEnabledFor(Level.ERROR);
    }

    @Override // org.apache.commons.logging.Log
    public boolean isFatalEnabled() {
        return this.category.isEnabledFor(Level.FATAL);
    }

    @Override // org.apache.commons.logging.Log
    public boolean isInfoEnabled() {
        return this.category.isInfoEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isTraceEnabled() {
        return this.category.isDebugEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isWarnEnabled() {
        return this.category.isEnabledFor(Level.WARN);
    }
}
