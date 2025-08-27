package org.apache.commons.httpclient.cookie;

import org.apache.commons.httpclient.ProtocolException;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/MalformedCookieException.class */
public class MalformedCookieException extends ProtocolException {
    public MalformedCookieException() {
    }

    public MalformedCookieException(String message) {
        super(message);
    }

    public MalformedCookieException(String message, Throwable cause) {
        super(message, cause);
    }
}
