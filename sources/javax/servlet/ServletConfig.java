package javax.servlet;

import java.util.Enumeration;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/ServletConfig.class */
public interface ServletConfig {
    String getServletName();

    ServletContext getServletContext();

    String getInitParameter(String str);

    Enumeration<String> getInitParameterNames();
}
