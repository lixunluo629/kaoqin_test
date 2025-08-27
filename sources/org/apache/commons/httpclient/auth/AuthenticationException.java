package org.apache.commons.httpclient.auth;

import org.apache.commons.httpclient.ProtocolException;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/auth/AuthenticationException.class */
public class AuthenticationException extends ProtocolException {
    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
