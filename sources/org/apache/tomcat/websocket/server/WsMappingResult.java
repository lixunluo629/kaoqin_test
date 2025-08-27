package org.apache.tomcat.websocket.server;

import java.util.Map;
import javax.websocket.server.ServerEndpointConfig;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:org/apache/tomcat/websocket/server/WsMappingResult.class */
class WsMappingResult {
    private final ServerEndpointConfig config;
    private final Map<String, String> pathParams;

    WsMappingResult(ServerEndpointConfig config, Map<String, String> pathParams) {
        this.config = config;
        this.pathParams = pathParams;
    }

    ServerEndpointConfig getConfig() {
        return this.config;
    }

    Map<String, String> getPathParams() {
        return this.pathParams;
    }
}
