package javax.servlet;

import java.util.EventObject;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/ServletRequestEvent.class */
public class ServletRequestEvent extends EventObject {
    private static final long serialVersionUID = 1;
    private final transient ServletRequest request;

    public ServletRequestEvent(ServletContext sc, ServletRequest request) {
        super(sc);
        this.request = request;
    }

    public ServletRequest getServletRequest() {
        return this.request;
    }

    public ServletContext getServletContext() {
        return (ServletContext) super.getSource();
    }
}
