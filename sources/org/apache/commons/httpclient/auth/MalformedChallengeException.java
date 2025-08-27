package org.apache.commons.httpclient.auth;

import org.apache.commons.httpclient.ProtocolException;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/auth/MalformedChallengeException.class */
public class MalformedChallengeException extends ProtocolException {
    public MalformedChallengeException() {
    }

    public MalformedChallengeException(String message) {
        super(message);
    }

    public MalformedChallengeException(String message, Throwable cause) {
        super(message, cause);
    }
}
