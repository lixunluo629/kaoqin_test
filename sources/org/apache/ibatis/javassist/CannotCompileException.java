package org.apache.ibatis.javassist;

import org.apache.ibatis.javassist.compiler.CompileError;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/CannotCompileException.class */
public class CannotCompileException extends Exception {
    private Throwable myCause;
    private String message;

    @Override // java.lang.Throwable
    public Throwable getCause() {
        if (this.myCause == this) {
            return null;
        }
        return this.myCause;
    }

    @Override // java.lang.Throwable
    public synchronized Throwable initCause(Throwable cause) {
        this.myCause = cause;
        return this;
    }

    public String getReason() {
        if (this.message != null) {
            return this.message;
        }
        return toString();
    }

    public CannotCompileException(String msg) {
        super(msg);
        this.message = msg;
        initCause(null);
    }

    public CannotCompileException(Throwable e) {
        super("by " + e.toString());
        this.message = null;
        initCause(e);
    }

    public CannotCompileException(String msg, Throwable e) {
        this(msg);
        initCause(e);
    }

    public CannotCompileException(NotFoundException e) {
        this("cannot find " + e.getMessage(), e);
    }

    public CannotCompileException(CompileError e) {
        this("[source error] " + e.getMessage(), e);
    }

    public CannotCompileException(ClassNotFoundException e, String name) {
        this("cannot find " + name, e);
    }

    public CannotCompileException(ClassFormatError e, String name) {
        this("invalid class format: " + name, e);
    }
}
