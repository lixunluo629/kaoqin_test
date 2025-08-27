package org.springframework.boot.context.embedded;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/EmbeddedServletContainer.class */
public interface EmbeddedServletContainer {
    void start() throws EmbeddedServletContainerException;

    void stop() throws EmbeddedServletContainerException;

    int getPort();
}
