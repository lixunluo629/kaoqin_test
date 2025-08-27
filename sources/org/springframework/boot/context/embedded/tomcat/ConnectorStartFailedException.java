package org.springframework.boot.context.embedded.tomcat;

import org.springframework.boot.context.embedded.EmbeddedServletContainerException;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/tomcat/ConnectorStartFailedException.class */
public class ConnectorStartFailedException extends EmbeddedServletContainerException {
    private final int port;

    public ConnectorStartFailedException(int port) {
        super("Connector configured to listen on port " + port + " failed to start", null);
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }
}
