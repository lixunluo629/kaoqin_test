package org.springframework.web.servlet.config.annotation;

import java.util.Collections;
import javax.servlet.ServletContext;
import org.springframework.util.Assert;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/config/annotation/DefaultServletHandlerConfigurer.class */
public class DefaultServletHandlerConfigurer {
    private final ServletContext servletContext;
    private DefaultServletHttpRequestHandler handler;

    public DefaultServletHandlerConfigurer(ServletContext servletContext) {
        Assert.notNull(servletContext, "ServletContext is required");
        this.servletContext = servletContext;
    }

    public void enable() {
        enable(null);
    }

    public void enable(String defaultServletName) {
        this.handler = new DefaultServletHttpRequestHandler();
        this.handler.setDefaultServletName(defaultServletName);
        this.handler.setServletContext(this.servletContext);
    }

    protected SimpleUrlHandlerMapping buildHandlerMapping() {
        if (this.handler == null) {
            return null;
        }
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setUrlMap(Collections.singletonMap("/**", this.handler));
        handlerMapping.setOrder(Integer.MAX_VALUE);
        return handlerMapping;
    }

    @Deprecated
    protected AbstractHandlerMapping getHandlerMapping() {
        return buildHandlerMapping();
    }
}
