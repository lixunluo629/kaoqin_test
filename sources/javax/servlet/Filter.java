package javax.servlet;

import java.io.IOException;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/Filter.class */
public interface Filter {
    void init(FilterConfig filterConfig) throws ServletException;

    void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException;

    void destroy();
}
