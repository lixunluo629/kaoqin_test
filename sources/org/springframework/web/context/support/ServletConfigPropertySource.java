package org.springframework.web.context.support;

import javax.servlet.ServletConfig;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.util.StringUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/support/ServletConfigPropertySource.class */
public class ServletConfigPropertySource extends EnumerablePropertySource<ServletConfig> {
    public ServletConfigPropertySource(String name, ServletConfig servletConfig) {
        super(name, servletConfig);
    }

    @Override // org.springframework.core.env.EnumerablePropertySource
    public String[] getPropertyNames() {
        return StringUtils.toStringArray(((ServletConfig) this.source).getInitParameterNames());
    }

    @Override // org.springframework.core.env.PropertySource
    public String getProperty(String name) {
        return ((ServletConfig) this.source).getInitParameter(name);
    }
}
