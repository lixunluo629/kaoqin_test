package org.springframework.web.context.support;

import javax.servlet.ServletContext;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.util.StringUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/support/ServletContextPropertySource.class */
public class ServletContextPropertySource extends EnumerablePropertySource<ServletContext> {
    public ServletContextPropertySource(String name, ServletContext servletContext) {
        super(name, servletContext);
    }

    @Override // org.springframework.core.env.EnumerablePropertySource
    public String[] getPropertyNames() {
        return StringUtils.toStringArray(((ServletContext) this.source).getInitParameterNames());
    }

    @Override // org.springframework.core.env.PropertySource
    public String getProperty(String name) {
        return ((ServletContext) this.source).getInitParameter(name);
    }
}
