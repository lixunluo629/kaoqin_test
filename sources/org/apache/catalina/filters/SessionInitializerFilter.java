package org.apache.catalina.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/filters/SessionInitializerFilter.class */
public class SessionInitializerFilter implements Filter {
    @Override // javax.servlet.Filter
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        ((HttpServletRequest) request).getSession();
        chain.doFilter(request, response);
    }

    @Override // javax.servlet.Filter
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override // javax.servlet.Filter
    public void destroy() {
    }
}
