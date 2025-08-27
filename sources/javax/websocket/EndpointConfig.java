package javax.websocket;

import java.util.List;
import java.util.Map;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:javax/websocket/EndpointConfig.class */
public interface EndpointConfig {
    List<Class<? extends Encoder>> getEncoders();

    List<Class<? extends Decoder>> getDecoders();

    Map<String, Object> getUserProperties();
}
