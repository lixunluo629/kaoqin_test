package org.apache.ibatis.ognl;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/OgnlException.class */
public class OgnlException extends Exception {
    static Method _initCause;
    private Evaluation _evaluation;
    private Throwable _reason;

    static {
        try {
            _initCause = OgnlException.class.getMethod("initCause", Throwable.class);
        } catch (NoSuchMethodException e) {
        }
    }

    public OgnlException() {
        this(null, null);
    }

    public OgnlException(String msg) {
        this(msg, null);
    }

    public OgnlException(String msg, Throwable reason) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        super(msg);
        this._reason = reason;
        if (_initCause != null) {
            try {
                _initCause.invoke(this, reason);
            } catch (Exception e) {
            }
        }
    }

    public Throwable getReason() {
        return this._reason;
    }

    public Evaluation getEvaluation() {
        return this._evaluation;
    }

    public void setEvaluation(Evaluation value) {
        this._evaluation = value;
    }

    @Override // java.lang.Throwable
    public String toString() {
        if (this._reason == null) {
            return super.toString();
        }
        return super.toString() + " [" + this._reason + "]";
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream s) {
        synchronized (s) {
            super.printStackTrace(s);
            if (this._reason != null) {
                s.println("/-- Encapsulated exception ------------\\");
                this._reason.printStackTrace(s);
                s.println("\\--------------------------------------/");
            }
        }
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter s) {
        synchronized (s) {
            super.printStackTrace(s);
            if (this._reason != null) {
                s.println("/-- Encapsulated exception ------------\\");
                this._reason.printStackTrace(s);
                s.println("\\--------------------------------------/");
            }
        }
    }
}
