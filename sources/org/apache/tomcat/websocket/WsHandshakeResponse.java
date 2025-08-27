package org.apache.tomcat.websocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.websocket.HandshakeResponse;
import org.apache.tomcat.util.collections.CaseInsensitiveKeyMap;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:org/apache/tomcat/websocket/WsHandshakeResponse.class */
public class WsHandshakeResponse implements HandshakeResponse {
    private final Map<String, List<String>> headers = new CaseInsensitiveKeyMap();

    public WsHandshakeResponse() {
    }

    public WsHandshakeResponse(Map<String, List<String>> headers) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if (this.headers.containsKey(entry.getKey())) {
                this.headers.get(entry.getKey()).addAll(entry.getValue());
            } else {
                List<String> values = new ArrayList<>(entry.getValue());
                this.headers.put(entry.getKey(), values);
            }
        }
    }

    @Override // javax.websocket.HandshakeResponse
    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }
}
