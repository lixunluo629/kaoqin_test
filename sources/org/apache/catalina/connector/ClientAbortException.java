package org.apache.catalina.connector;

import java.io.IOException;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/connector/ClientAbortException.class */
public final class ClientAbortException extends IOException {
    private static final long serialVersionUID = 1;

    public ClientAbortException() {
    }

    public ClientAbortException(String message) {
        super(message);
    }

    public ClientAbortException(Throwable throwable) {
        super(throwable);
    }

    public ClientAbortException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
