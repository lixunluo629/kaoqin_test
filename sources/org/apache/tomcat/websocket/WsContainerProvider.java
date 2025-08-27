package org.apache.tomcat.websocket;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:org/apache/tomcat/websocket/WsContainerProvider.class */
public class WsContainerProvider extends ContainerProvider {
    @Override // javax.websocket.ContainerProvider
    protected WebSocketContainer getContainer() {
        return new WsWebSocketContainer();
    }
}
