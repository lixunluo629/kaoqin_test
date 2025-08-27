package org.springframework.web.context;

import javax.servlet.ServletConfig;
import org.springframework.beans.factory.Aware;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/ServletConfigAware.class */
public interface ServletConfigAware extends Aware {
    void setServletConfig(ServletConfig servletConfig);
}
