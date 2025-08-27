package javax.servlet;

import java.util.EventListener;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/ServletRequestAttributeListener.class */
public interface ServletRequestAttributeListener extends EventListener {
    void attributeAdded(ServletRequestAttributeEvent servletRequestAttributeEvent);

    void attributeRemoved(ServletRequestAttributeEvent servletRequestAttributeEvent);

    void attributeReplaced(ServletRequestAttributeEvent servletRequestAttributeEvent);
}
