package org.springframework.boot.web.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/servlet/ServletContextInitializer.class */
public interface ServletContextInitializer {
    void onStartup(ServletContext servletContext) throws ServletException;
}
