package javax.websocket.server;

import java.util.Set;
import javax.websocket.Endpoint;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:javax/websocket/server/ServerApplicationConfig.class */
public interface ServerApplicationConfig {
    Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> set);

    Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> set);
}
