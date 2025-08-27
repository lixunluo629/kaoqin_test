package org.apache.ibatis.executor;

import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/ErrorContext.class */
public class ErrorContext {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator", ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
    private static final ThreadLocal<ErrorContext> LOCAL = new ThreadLocal<>();
    private ErrorContext stored;
    private String resource;
    private String activity;
    private String object;
    private String message;
    private String sql;
    private Throwable cause;

    private ErrorContext() {
    }

    public static ErrorContext instance() {
        ErrorContext context = LOCAL.get();
        if (context == null) {
            context = new ErrorContext();
            LOCAL.set(context);
        }
        return context;
    }

    public ErrorContext store() {
        this.stored = this;
        LOCAL.set(new ErrorContext());
        return LOCAL.get();
    }

    public ErrorContext recall() {
        if (this.stored != null) {
            LOCAL.set(this.stored);
            this.stored = null;
        }
        return LOCAL.get();
    }

    public ErrorContext resource(String resource) {
        this.resource = resource;
        return this;
    }

    public ErrorContext activity(String activity) {
        this.activity = activity;
        return this;
    }

    public ErrorContext object(String object) {
        this.object = object;
        return this;
    }

    public ErrorContext message(String message) {
        this.message = message;
        return this;
    }

    public ErrorContext sql(String sql) {
        this.sql = sql;
        return this;
    }

    public ErrorContext cause(Throwable cause) {
        this.cause = cause;
        return this;
    }

    public ErrorContext reset() {
        this.resource = null;
        this.activity = null;
        this.object = null;
        this.message = null;
        this.sql = null;
        this.cause = null;
        LOCAL.remove();
        return this;
    }

    public String toString() {
        StringBuilder description = new StringBuilder();
        if (this.message != null) {
            description.append(LINE_SEPARATOR);
            description.append("### ");
            description.append(this.message);
        }
        if (this.resource != null) {
            description.append(LINE_SEPARATOR);
            description.append("### The error may exist in ");
            description.append(this.resource);
        }
        if (this.object != null) {
            description.append(LINE_SEPARATOR);
            description.append("### The error may involve ");
            description.append(this.object);
        }
        if (this.activity != null) {
            description.append(LINE_SEPARATOR);
            description.append("### The error occurred while ");
            description.append(this.activity);
        }
        if (this.sql != null) {
            description.append(LINE_SEPARATOR);
            description.append("### SQL: ");
            description.append(this.sql.replace('\n', ' ').replace('\r', ' ').replace('\t', ' ').trim());
        }
        if (this.cause != null) {
            description.append(LINE_SEPARATOR);
            description.append("### Cause: ");
            description.append(this.cause.toString());
        }
        return description.toString();
    }
}
