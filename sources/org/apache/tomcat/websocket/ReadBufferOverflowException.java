package org.apache.tomcat.websocket;

import java.io.IOException;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:org/apache/tomcat/websocket/ReadBufferOverflowException.class */
public class ReadBufferOverflowException extends IOException {
    private static final long serialVersionUID = 1;
    private final int minBufferSize;

    public ReadBufferOverflowException(int minBufferSize) {
        this.minBufferSize = minBufferSize;
    }

    public int getMinBufferSize() {
        return this.minBufferSize;
    }
}
