package org.springframework.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/WebApplicationInitializer.class */
public interface WebApplicationInitializer {
    void onStartup(ServletContext servletContext) throws ServletException;
}
