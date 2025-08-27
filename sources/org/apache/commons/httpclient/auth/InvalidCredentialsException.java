package org.apache.commons.httpclient.auth;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/auth/InvalidCredentialsException.class */
public class InvalidCredentialsException extends AuthenticationException {
    public InvalidCredentialsException() {
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
