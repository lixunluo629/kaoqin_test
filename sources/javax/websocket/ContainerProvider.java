package javax.websocket;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.ServiceLoader;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:javax/websocket/ContainerProvider.class */
public abstract class ContainerProvider {
    private static final String DEFAULT_PROVIDER_CLASS_NAME = "org.apache.tomcat.websocket.WsWebSocketContainer";

    protected abstract WebSocketContainer getContainer();

    public static WebSocketContainer getWebSocketContainer() {
        WebSocketContainer result = null;
        ServiceLoader<ContainerProvider> serviceLoader = ServiceLoader.load(ContainerProvider.class);
        Iterator<ContainerProvider> iter = serviceLoader.iterator();
        while (result == null && iter.hasNext()) {
            result = iter.next().getContainer();
        }
        if (result == null) {
            try {
                result = (WebSocketContainer) Class.forName(DEFAULT_PROVIDER_CLASS_NAME).getConstructor(new Class[0]).newInstance(new Object[0]);
            } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            }
        }
        return result;
    }
}
