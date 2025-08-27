package org.apache.coyote.http2;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/ConnectionException.class */
public class ConnectionException extends Http2Exception {
    private static final long serialVersionUID = 1;

    ConnectionException(String msg, Http2Error error) {
        super(msg, error);
    }

    ConnectionException(String msg, Http2Error error, Throwable cause) {
        super(msg, error, cause);
    }
}
