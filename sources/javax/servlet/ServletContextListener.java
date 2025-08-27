package javax.servlet;

import java.util.EventListener;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/ServletContextListener.class */
public interface ServletContextListener extends EventListener {
    void contextInitialized(ServletContextEvent servletContextEvent);

    void contextDestroyed(ServletContextEvent servletContextEvent);
}
