package org.apache.commons.httpclient.auth;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/auth/CredentialsNotAvailableException.class */
public class CredentialsNotAvailableException extends AuthenticationException {
    public CredentialsNotAvailableException() {
    }

    public CredentialsNotAvailableException(String message) {
        super(message);
    }

    public CredentialsNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
