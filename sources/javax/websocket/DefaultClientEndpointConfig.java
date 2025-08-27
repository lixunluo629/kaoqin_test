package javax.websocket;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.ClientEndpointConfig;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:javax/websocket/DefaultClientEndpointConfig.class */
final class DefaultClientEndpointConfig implements ClientEndpointConfig {
    private final List<String> preferredSubprotocols;
    private final List<Extension> extensions;
    private final List<Class<? extends Encoder>> encoders;
    private final List<Class<? extends Decoder>> decoders;
    private final Map<String, Object> userProperties = new ConcurrentHashMap();
    private final ClientEndpointConfig.Configurator configurator;

    DefaultClientEndpointConfig(List<String> preferredSubprotocols, List<Extension> extensions, List<Class<? extends Encoder>> encoders, List<Class<? extends Decoder>> decoders, ClientEndpointConfig.Configurator configurator) {
        this.preferredSubprotocols = preferredSubprotocols;
        this.extensions = extensions;
        this.decoders = decoders;
        this.encoders = encoders;
        this.configurator = configurator;
    }

    @Override // javax.websocket.ClientEndpointConfig
    public List<String> getPreferredSubprotocols() {
        return this.preferredSubprotocols;
    }

    @Override // javax.websocket.ClientEndpointConfig
    public List<Extension> getExtensions() {
        return this.extensions;
    }

    @Override // javax.websocket.EndpointConfig
    public List<Class<? extends Encoder>> getEncoders() {
        return this.encoders;
    }

    @Override // javax.websocket.EndpointConfig
    public List<Class<? extends Decoder>> getDecoders() {
        return this.decoders;
    }

    @Override // javax.websocket.EndpointConfig
    public final Map<String, Object> getUserProperties() {
        return this.userProperties;
    }

    @Override // javax.websocket.ClientEndpointConfig
    public ClientEndpointConfig.Configurator getConfigurator() {
        return this.configurator;
    }
}
