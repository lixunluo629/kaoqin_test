package org.springframework.boot.autoconfigure.websocket;

import org.eclipse.jetty.util.thread.ShutdownThread;
import org.eclipse.jetty.webapp.AbstractConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/websocket/JettyWebSocketContainerCustomizer.class */
public class JettyWebSocketContainerCustomizer extends WebSocketContainerCustomizer<JettyEmbeddedServletContainerFactory> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.boot.autoconfigure.websocket.WebSocketContainerCustomizer
    public void doCustomize(JettyEmbeddedServletContainerFactory container) {
        container.addConfigurations(new AbstractConfiguration() { // from class: org.springframework.boot.autoconfigure.websocket.JettyWebSocketContainerCustomizer.1
            public void configure(WebAppContext context) throws Exception {
                ServerContainer serverContainer = WebSocketServerContainerInitializer.configureContext(context);
                ShutdownThread.deregister(serverContainer);
            }
        });
    }
}
