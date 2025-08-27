package org.apache.commons.httpclient;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/CircularRedirectException.class */
public class CircularRedirectException extends RedirectException {
    public CircularRedirectException() {
    }

    public CircularRedirectException(String message) {
        super(message);
    }

    public CircularRedirectException(String message, Throwable cause) {
        super(message, cause);
    }
}
