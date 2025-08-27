package org.springframework.web.context;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.springframework.context.ConfigurableApplicationContext;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/ConfigurableWebApplicationContext.class */
public interface ConfigurableWebApplicationContext extends WebApplicationContext, ConfigurableApplicationContext {
    public static final String APPLICATION_CONTEXT_ID_PREFIX = WebApplicationContext.class.getName() + ":";
    public static final String SERVLET_CONFIG_BEAN_NAME = "servletConfig";

    void setServletContext(ServletContext servletContext);

    void setServletConfig(ServletConfig servletConfig);

    ServletConfig getServletConfig();

    void setNamespace(String str);

    String getNamespace();

    void setConfigLocation(String str);

    void setConfigLocations(String... strArr);

    String[] getConfigLocations();
}
