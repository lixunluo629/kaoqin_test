package javax.servlet;

import java.io.IOException;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/FilterChain.class */
public interface FilterChain {
    void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException;
}
