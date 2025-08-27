package org.apache.tomcat.util.net;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/net/DispatchType.class */
public enum DispatchType {
    NON_BLOCKING_READ(SocketEvent.OPEN_READ),
    NON_BLOCKING_WRITE(SocketEvent.OPEN_WRITE);

    private final SocketEvent status;

    DispatchType(SocketEvent status) {
        this.status = status;
    }

    public SocketEvent getSocketStatus() {
        return this.status;
    }
}
