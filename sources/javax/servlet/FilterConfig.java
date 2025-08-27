package javax.servlet;

import java.util.Enumeration;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/FilterConfig.class */
public interface FilterConfig {
    String getFilterName();

    ServletContext getServletContext();

    String getInitParameter(String str);

    Enumeration<String> getInitParameterNames();
}
