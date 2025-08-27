package org.apache.commons.httpclient;

import ch.qos.logback.core.CoreConstants;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/HttpException.class */
public class HttpException extends IOException {
    private String reason;
    private int reasonCode;
    private final Throwable cause;
    static Class class$java$lang$Throwable;

    public HttpException() {
        this.reasonCode = 200;
        this.cause = null;
    }

    public HttpException(String message) {
        super(message);
        this.reasonCode = 200;
        this.cause = null;
    }

    public HttpException(String message, Throwable cause) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Class clsClass$;
        Class clsClass$2;
        super(message);
        this.reasonCode = 200;
        this.cause = cause;
        try {
            Class[] paramsClasses = new Class[1];
            if (class$java$lang$Throwable == null) {
                clsClass$ = class$("java.lang.Throwable");
                class$java$lang$Throwable = clsClass$;
            } else {
                clsClass$ = class$java$lang$Throwable;
            }
            paramsClasses[0] = clsClass$;
            if (class$java$lang$Throwable == null) {
                clsClass$2 = class$("java.lang.Throwable");
                class$java$lang$Throwable = clsClass$2;
            } else {
                clsClass$2 = class$java$lang$Throwable;
            }
            Method initCause = clsClass$2.getMethod("initCause", paramsClasses);
            initCause.invoke(this, cause);
        } catch (Exception e) {
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }

    @Override // java.lang.Throwable
    public void printStackTrace() throws NoSuchMethodException, SecurityException {
        printStackTrace(System.err);
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream s) throws NoSuchMethodException, SecurityException {
        try {
            Class[] paramsClasses = new Class[0];
            getClass().getMethod("getStackTrace", paramsClasses);
            super.printStackTrace(s);
        } catch (Exception e) {
            super.printStackTrace(s);
            if (this.cause != null) {
                s.print(CoreConstants.CAUSED_BY);
                this.cause.printStackTrace(s);
            }
        }
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter s) throws NoSuchMethodException, SecurityException {
        try {
            Class[] paramsClasses = new Class[0];
            getClass().getMethod("getStackTrace", paramsClasses);
            super.printStackTrace(s);
        } catch (Exception e) {
            super.printStackTrace(s);
            if (this.cause != null) {
                s.print(CoreConstants.CAUSED_BY);
                this.cause.printStackTrace(s);
            }
        }
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReasonCode(int code) {
        this.reasonCode = code;
    }

    public int getReasonCode() {
        return this.reasonCode;
    }
}
