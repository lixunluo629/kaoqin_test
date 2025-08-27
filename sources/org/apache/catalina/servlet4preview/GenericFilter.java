package org.apache.catalina.servlet4preview;

import java.io.Serializable;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

@Deprecated
/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/servlet4preview/GenericFilter.class */
public abstract class GenericFilter implements Filter, FilterConfig, Serializable {
    private static final long serialVersionUID = 1;
    private volatile FilterConfig filterConfig;

    @Override // javax.servlet.FilterConfig
    public String getInitParameter(String name) {
        return getFilterConfig().getInitParameter(name);
    }

    @Override // javax.servlet.FilterConfig
    public Enumeration<String> getInitParameterNames() {
        return getFilterConfig().getInitParameterNames();
    }

    public FilterConfig getFilterConfig() {
        return this.filterConfig;
    }

    @Override // javax.servlet.FilterConfig
    public javax.servlet.ServletContext getServletContext() {
        return getFilterConfig().getServletContext();
    }

    @Override // javax.servlet.Filter
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        init();
    }

    public void init() throws ServletException {
    }

    @Override // javax.servlet.FilterConfig
    public String getFilterName() {
        return getFilterConfig().getFilterName();
    }
}
