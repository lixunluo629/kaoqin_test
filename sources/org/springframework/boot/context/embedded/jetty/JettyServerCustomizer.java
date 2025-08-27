package org.springframework.boot.context.embedded.jetty;

import org.eclipse.jetty.server.Server;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyServerCustomizer.class */
public interface JettyServerCustomizer {
    void customize(Server server);
}
