package org.apache.commons.httpclient;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/ConnectionPoolTimeoutException.class */
public class ConnectionPoolTimeoutException extends ConnectTimeoutException {
    public ConnectionPoolTimeoutException() {
    }

    public ConnectionPoolTimeoutException(String message) {
        super(message);
    }

    public ConnectionPoolTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
