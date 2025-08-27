package javax.websocket;

import java.util.List;
import java.util.Map;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:javax/websocket/HandshakeResponse.class */
public interface HandshakeResponse {
    public static final String SEC_WEBSOCKET_ACCEPT = "Sec-WebSocket-Accept";

    Map<String, List<String>> getHeaders();
}
