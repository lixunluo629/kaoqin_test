package javax.servlet.http;

import java.util.EventObject;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/http/HttpSessionEvent.class */
public class HttpSessionEvent extends EventObject {
    private static final long serialVersionUID = 1;

    public HttpSessionEvent(HttpSession source) {
        super(source);
    }

    public HttpSession getSession() {
        return (HttpSession) super.getSource();
    }
}
