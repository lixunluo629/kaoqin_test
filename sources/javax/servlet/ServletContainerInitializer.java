package javax.servlet;

import java.util.Set;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/ServletContainerInitializer.class */
public interface ServletContainerInitializer {
    void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException;
}
