package org.apache.coyote;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/ProtocolException.class */
public class ProtocolException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public ProtocolException() {
    }

    public ProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProtocolException(String message) {
        super(message);
    }

    public ProtocolException(Throwable cause) {
        super(cause);
    }
}
