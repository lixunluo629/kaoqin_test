package org.apache.ibatis.javassist.tools.reflect;

import java.lang.reflect.InvocationTargetException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/tools/reflect/CannotInvokeException.class */
public class CannotInvokeException extends RuntimeException {
    private Throwable err;

    public Throwable getReason() {
        return this.err;
    }

    public CannotInvokeException(String reason) {
        super(reason);
        this.err = null;
    }

    public CannotInvokeException(InvocationTargetException e) {
        super("by " + e.getTargetException().toString());
        this.err = null;
        this.err = e.getTargetException();
    }

    public CannotInvokeException(IllegalAccessException e) {
        super("by " + e.toString());
        this.err = null;
        this.err = e;
    }

    public CannotInvokeException(ClassNotFoundException e) {
        super("by " + e.toString());
        this.err = null;
        this.err = e;
    }
}
