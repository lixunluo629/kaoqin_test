package javax.servlet;

import java.util.EventListener;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/ServletRequestListener.class */
public interface ServletRequestListener extends EventListener {
    void requestDestroyed(ServletRequestEvent servletRequestEvent);

    void requestInitialized(ServletRequestEvent servletRequestEvent);
}
