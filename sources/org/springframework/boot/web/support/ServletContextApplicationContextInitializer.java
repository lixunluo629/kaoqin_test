package org.springframework.boot.web.support;

import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.Ordered;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/support/ServletContextApplicationContextInitializer.class */
public class ServletContextApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableWebApplicationContext>, Ordered {
    private int order;
    private final ServletContext servletContext;
    private final boolean addApplicationContextAttribute;

    public ServletContextApplicationContextInitializer(ServletContext servletContext) {
        this(servletContext, false);
    }

    public ServletContextApplicationContextInitializer(ServletContext servletContext, boolean addApplicationContextAttribute) {
        this.order = Integer.MIN_VALUE;
        this.servletContext = servletContext;
        this.addApplicationContextAttribute = addApplicationContextAttribute;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    @Override // org.springframework.context.ApplicationContextInitializer
    public void initialize(ConfigurableWebApplicationContext applicationContext) {
        applicationContext.setServletContext(this.servletContext);
        if (this.addApplicationContextAttribute) {
            this.servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
        }
    }
}
