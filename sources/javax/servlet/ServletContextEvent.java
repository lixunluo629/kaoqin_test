package javax.servlet;

import java.util.EventObject;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/ServletContextEvent.class */
public class ServletContextEvent extends EventObject {
    private static final long serialVersionUID = 1;

    public ServletContextEvent(ServletContext source) {
        super(source);
    }

    public ServletContext getServletContext() {
        return (ServletContext) super.getSource();
    }
}
