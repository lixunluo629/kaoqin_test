package org.apache.coyote.http2;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/HpackException.class */
public class HpackException extends Exception {
    private static final long serialVersionUID = 1;

    public HpackException(String message, Throwable cause) {
        super(message, cause);
    }

    public HpackException(String message) {
        super(message);
    }

    public HpackException() {
    }
}
