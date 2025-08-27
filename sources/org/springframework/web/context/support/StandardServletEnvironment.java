package org.springframework.web.context.support;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.jndi.JndiLocatorDelegate;
import org.springframework.jndi.JndiPropertySource;
import org.springframework.web.context.ConfigurableWebEnvironment;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/support/StandardServletEnvironment.class */
public class StandardServletEnvironment extends StandardEnvironment implements ConfigurableWebEnvironment {
    public static final String SERVLET_CONTEXT_PROPERTY_SOURCE_NAME = "servletContextInitParams";
    public static final String SERVLET_CONFIG_PROPERTY_SOURCE_NAME = "servletConfigInitParams";
    public static final String JNDI_PROPERTY_SOURCE_NAME = "jndiProperties";

    @Override // org.springframework.core.env.StandardEnvironment, org.springframework.core.env.AbstractEnvironment
    protected void customizePropertySources(MutablePropertySources propertySources) {
        propertySources.addLast(new PropertySource.StubPropertySource(SERVLET_CONFIG_PROPERTY_SOURCE_NAME));
        propertySources.addLast(new PropertySource.StubPropertySource(SERVLET_CONTEXT_PROPERTY_SOURCE_NAME));
        if (JndiLocatorDelegate.isDefaultJndiEnvironmentAvailable()) {
            propertySources.addLast(new JndiPropertySource(JNDI_PROPERTY_SOURCE_NAME));
        }
        super.customizePropertySources(propertySources);
    }

    @Override // org.springframework.web.context.ConfigurableWebEnvironment
    public void initPropertySources(ServletContext servletContext, ServletConfig servletConfig) {
        WebApplicationContextUtils.initServletPropertySources(getPropertySources(), servletContext, servletConfig);
    }
}
