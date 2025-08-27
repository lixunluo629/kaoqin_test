package org.springframework.web.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.FactoryConfigurationError;

@Deprecated
/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/Log4jConfigListener.class */
public class Log4jConfigListener implements ServletContextListener {
    @Override // javax.servlet.ServletContextListener
    public void contextInitialized(ServletContextEvent event) throws IllegalStateException, NumberFormatException, FactoryConfigurationError {
        Log4jWebConfigurer.initLogging(event.getServletContext());
    }

    @Override // javax.servlet.ServletContextListener
    public void contextDestroyed(ServletContextEvent event) {
        Log4jWebConfigurer.shutdownLogging(event.getServletContext());
    }
}
