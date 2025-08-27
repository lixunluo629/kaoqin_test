package javax.websocket;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:javax/websocket/Endpoint.class */
public abstract class Endpoint {
    public abstract void onOpen(Session session, EndpointConfig endpointConfig);

    public void onClose(Session session, CloseReason closeReason) {
    }

    public void onError(Session session, Throwable throwable) {
    }
}
