package org.springframework.boot.context.embedded;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.springframework.util.Assert;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.support.ServletContextAwareProcessor;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/WebApplicationContextServletContextAwareProcessor.class */
public class WebApplicationContextServletContextAwareProcessor extends ServletContextAwareProcessor {
    private final ConfigurableWebApplicationContext webApplicationContext;

    public WebApplicationContextServletContextAwareProcessor(ConfigurableWebApplicationContext webApplicationContext) {
        Assert.notNull(webApplicationContext, "WebApplicationContext must not be null");
        this.webApplicationContext = webApplicationContext;
    }

    @Override // org.springframework.web.context.support.ServletContextAwareProcessor
    protected ServletContext getServletContext() {
        ServletContext servletContext = this.webApplicationContext.getServletContext();
        return servletContext != null ? servletContext : super.getServletContext();
    }

    @Override // org.springframework.web.context.support.ServletContextAwareProcessor
    protected ServletConfig getServletConfig() {
        ServletConfig servletConfig = this.webApplicationContext.getServletConfig();
        return servletConfig != null ? servletConfig : super.getServletConfig();
    }
}
