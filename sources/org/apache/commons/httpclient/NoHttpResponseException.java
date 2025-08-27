package org.apache.commons.httpclient;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.httpclient.util.ExceptionUtil;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/NoHttpResponseException.class */
public class NoHttpResponseException extends IOException {
    public NoHttpResponseException() {
    }

    public NoHttpResponseException(String message) {
        super(message);
    }

    public NoHttpResponseException(String message, Throwable cause) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        super(message);
        ExceptionUtil.initCause(this, cause);
    }
}
