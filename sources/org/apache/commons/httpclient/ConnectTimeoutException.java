package org.apache.commons.httpclient;

import java.io.InterruptedIOException;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.httpclient.util.ExceptionUtil;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/ConnectTimeoutException.class */
public class ConnectTimeoutException extends InterruptedIOException {
    public ConnectTimeoutException() {
    }

    public ConnectTimeoutException(String message) {
        super(message);
    }

    public ConnectTimeoutException(String message, Throwable cause) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        super(message);
        ExceptionUtil.initCause(this, cause);
    }
}
