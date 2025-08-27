package org.springframework.web.context;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.springframework.core.env.ConfigurableEnvironment;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/ConfigurableWebEnvironment.class */
public interface ConfigurableWebEnvironment extends ConfigurableEnvironment {
    void initPropertySources(ServletContext servletContext, ServletConfig servletConfig);
}
