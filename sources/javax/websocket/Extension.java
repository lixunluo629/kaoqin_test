package javax.websocket;

import java.util.List;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:javax/websocket/Extension.class */
public interface Extension {

    /* loaded from: tomcat-embed-websocket-8.5.43.jar:javax/websocket/Extension$Parameter.class */
    public interface Parameter {
        String getName();

        String getValue();
    }

    String getName();

    List<Parameter> getParameters();
}
