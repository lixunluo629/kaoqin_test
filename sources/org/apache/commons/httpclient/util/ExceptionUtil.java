package org.apache.commons.httpclient.util;

import java.io.InterruptedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/util/ExceptionUtil.class */
public class ExceptionUtil {
    private static final Log LOG;
    private static final Method INIT_CAUSE_METHOD;
    private static final Class SOCKET_TIMEOUT_CLASS;
    static Class class$org$apache$commons$httpclient$util$ExceptionUtil;
    static Class class$java$lang$Throwable;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$util$ExceptionUtil == null) {
            clsClass$ = class$("org.apache.commons.httpclient.util.ExceptionUtil");
            class$org$apache$commons$httpclient$util$ExceptionUtil = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$util$ExceptionUtil;
        }
        LOG = LogFactory.getLog(clsClass$);
        INIT_CAUSE_METHOD = getInitCauseMethod();
        SOCKET_TIMEOUT_CLASS = SocketTimeoutExceptionClass();
    }

    private static Method getInitCauseMethod() {
        Class clsClass$;
        Class clsClass$2;
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
            return clsClass$2.getMethod("initCause", paramsClasses);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static Class SocketTimeoutExceptionClass() {
        try {
            return Class.forName("java.net.SocketTimeoutException");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static void initCause(Throwable throwable, Throwable cause) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (INIT_CAUSE_METHOD != null) {
            try {
                INIT_CAUSE_METHOD.invoke(throwable, cause);
            } catch (Exception e) {
                LOG.warn("Exception invoking Throwable.initCause", e);
            }
        }
    }

    public static boolean isSocketTimeoutException(InterruptedIOException e) {
        if (SOCKET_TIMEOUT_CLASS != null) {
            return SOCKET_TIMEOUT_CLASS.isInstance(e);
        }
        return true;
    }
}
