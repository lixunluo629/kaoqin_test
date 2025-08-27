package org.springframework.web.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/WebAppRootListener.class */
public class WebAppRootListener implements ServletContextListener {
    @Override // javax.servlet.ServletContextListener
    public void contextInitialized(ServletContextEvent event) throws IllegalStateException {
        WebUtils.setWebAppRootSystemProperty(event.getServletContext());
    }

    @Override // javax.servlet.ServletContextListener
    public void contextDestroyed(ServletContextEvent event) {
        WebUtils.removeWebAppRootSystemProperty(event.getServletContext());
    }
}
