package javax.servlet;

import java.util.EventListener;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/ServletContextAttributeListener.class */
public interface ServletContextAttributeListener extends EventListener {
    void attributeAdded(ServletContextAttributeEvent servletContextAttributeEvent);

    void attributeRemoved(ServletContextAttributeEvent servletContextAttributeEvent);

    void attributeReplaced(ServletContextAttributeEvent servletContextAttributeEvent);
}
