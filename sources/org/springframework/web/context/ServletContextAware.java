package org.springframework.web.context;

import javax.servlet.ServletContext;
import org.springframework.beans.factory.Aware;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/ServletContextAware.class */
public interface ServletContextAware extends Aware {
    void setServletContext(ServletContext servletContext);
}
