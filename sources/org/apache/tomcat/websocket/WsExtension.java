package org.apache.tomcat.websocket;

import java.util.ArrayList;
import java.util.List;
import javax.websocket.Extension;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:org/apache/tomcat/websocket/WsExtension.class */
public class WsExtension implements Extension {
    private final String name;
    private final List<Extension.Parameter> parameters = new ArrayList();

    WsExtension(String name) {
        this.name = name;
    }

    void addParameter(Extension.Parameter parameter) {
        this.parameters.add(parameter);
    }

    @Override // javax.websocket.Extension
    public String getName() {
        return this.name;
    }

    @Override // javax.websocket.Extension
    public List<Extension.Parameter> getParameters() {
        return this.parameters;
    }
}
