package org.springframework.boot.context.embedded;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/PortInUseException.class */
public class PortInUseException extends EmbeddedServletContainerException {
    private final int port;

    public PortInUseException(int port) {
        super("Port " + port + " is already in use", null);
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }
}
