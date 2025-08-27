package org.apache.commons.lang.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/exception/NestableError.class */
public class NestableError extends Error implements Nestable {
    private static final long serialVersionUID = 1;
    protected NestableDelegate delegate;
    private Throwable cause;

    public NestableError() {
        this.delegate = new NestableDelegate(this);
        this.cause = null;
    }

    public NestableError(String msg) {
        super(msg);
        this.delegate = new NestableDelegate(this);
        this.cause = null;
    }

    public NestableError(Throwable cause) {
        this.delegate = new NestableDelegate(this);
        this.cause = null;
        this.cause = cause;
    }

    public NestableError(String msg, Throwable cause) {
        super(msg);
        this.delegate = new NestableDelegate(this);
        this.cause = null;
        this.cause = cause;
    }

    @Override // java.lang.Throwable, org.apache.commons.lang.exception.Nestable
    public Throwable getCause() {
        return this.cause;
    }

    @Override // java.lang.Throwable, org.apache.commons.lang.exception.Nestable
    public String getMessage() {
        if (super.getMessage() != null) {
            return super.getMessage();
        }
        if (this.cause != null) {
            return this.cause.toString();
        }
        return null;
    }

    @Override // org.apache.commons.lang.exception.Nestable
    public String getMessage(int index) {
        if (index == 0) {
            return super.getMessage();
        }
        return this.delegate.getMessage(index);
    }

    @Override // org.apache.commons.lang.exception.Nestable
    public String[] getMessages() {
        return this.delegate.getMessages();
    }

    @Override // org.apache.commons.lang.exception.Nestable
    public Throwable getThrowable(int index) {
        return this.delegate.getThrowable(index);
    }

    @Override // org.apache.commons.lang.exception.Nestable
    public int getThrowableCount() {
        return this.delegate.getThrowableCount();
    }

    @Override // org.apache.commons.lang.exception.Nestable
    public Throwable[] getThrowables() {
        return this.delegate.getThrowables();
    }

    @Override // org.apache.commons.lang.exception.Nestable
    public int indexOfThrowable(Class type) {
        return this.delegate.indexOfThrowable(type, 0);
    }

    @Override // org.apache.commons.lang.exception.Nestable
    public int indexOfThrowable(Class type, int fromIndex) {
        return this.delegate.indexOfThrowable(type, fromIndex);
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        this.delegate.printStackTrace();
    }

    @Override // java.lang.Throwable, org.apache.commons.lang.exception.Nestable
    public void printStackTrace(PrintStream out) {
        this.delegate.printStackTrace(out);
    }

    @Override // java.lang.Throwable, org.apache.commons.lang.exception.Nestable
    public void printStackTrace(PrintWriter out) {
        this.delegate.printStackTrace(out);
    }

    @Override // org.apache.commons.lang.exception.Nestable
    public final void printPartialStackTrace(PrintWriter out) {
        super.printStackTrace(out);
    }
}
