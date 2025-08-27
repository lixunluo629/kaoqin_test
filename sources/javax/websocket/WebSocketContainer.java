package javax.websocket;

import java.io.IOException;
import java.net.URI;
import java.util.Set;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:javax/websocket/WebSocketContainer.class */
public interface WebSocketContainer {
    long getDefaultAsyncSendTimeout();

    void setAsyncSendTimeout(long j);

    Session connectToServer(Object obj, URI uri) throws IOException, DeploymentException;

    Session connectToServer(Class<?> cls, URI uri) throws IOException, DeploymentException;

    Session connectToServer(Endpoint endpoint, ClientEndpointConfig clientEndpointConfig, URI uri) throws IOException, DeploymentException;

    Session connectToServer(Class<? extends Endpoint> cls, ClientEndpointConfig clientEndpointConfig, URI uri) throws IOException, DeploymentException;

    long getDefaultMaxSessionIdleTimeout();

    void setDefaultMaxSessionIdleTimeout(long j);

    int getDefaultMaxBinaryMessageBufferSize();

    void setDefaultMaxBinaryMessageBufferSize(int i);

    int getDefaultMaxTextMessageBufferSize();

    void setDefaultMaxTextMessageBufferSize(int i);

    Set<Extension> getInstalledExtensions();
}
