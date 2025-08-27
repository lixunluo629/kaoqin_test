package javax.websocket.server;

import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:javax/websocket/server/ServerContainer.class */
public interface ServerContainer extends WebSocketContainer {
    void addEndpoint(Class<?> cls) throws DeploymentException;

    void addEndpoint(ServerEndpointConfig serverEndpointConfig) throws DeploymentException;
}
