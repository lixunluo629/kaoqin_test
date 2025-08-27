package org.springframework.web.context.support;

import java.io.File;
import javax.servlet.ServletContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.WebUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/support/WebApplicationObjectSupport.class */
public abstract class WebApplicationObjectSupport extends ApplicationObjectSupport implements ServletContextAware {
    private ServletContext servletContext;

    @Override // org.springframework.web.context.ServletContextAware
    public final void setServletContext(ServletContext servletContext) {
        if (servletContext != this.servletContext) {
            this.servletContext = servletContext;
            if (servletContext != null) {
                initServletContext(servletContext);
            }
        }
    }

    @Override // org.springframework.context.support.ApplicationObjectSupport
    protected boolean isContextRequired() {
        return true;
    }

    @Override // org.springframework.context.support.ApplicationObjectSupport
    protected void initApplicationContext(ApplicationContext context) throws BeansException {
        super.initApplicationContext(context);
        if (this.servletContext == null && (context instanceof WebApplicationContext)) {
            this.servletContext = ((WebApplicationContext) context).getServletContext();
            if (this.servletContext != null) {
                initServletContext(this.servletContext);
            }
        }
    }

    protected void initServletContext(ServletContext servletContext) {
    }

    protected final WebApplicationContext getWebApplicationContext() throws IllegalStateException {
        ApplicationContext ctx = getApplicationContext();
        if (ctx instanceof WebApplicationContext) {
            return (WebApplicationContext) getApplicationContext();
        }
        if (isContextRequired()) {
            throw new IllegalStateException("WebApplicationObjectSupport instance [" + this + "] does not run in a WebApplicationContext but in: " + ctx);
        }
        return null;
    }

    protected final ServletContext getServletContext() throws IllegalStateException {
        if (this.servletContext != null) {
            return this.servletContext;
        }
        WebApplicationContext wac = getWebApplicationContext();
        if (wac == null) {
            return null;
        }
        ServletContext servletContext = wac.getServletContext();
        if (servletContext == null && isContextRequired()) {
            throw new IllegalStateException("WebApplicationObjectSupport instance [" + this + "] does not run within a ServletContext. Make sure the object is fully configured!");
        }
        return servletContext;
    }

    protected final File getTempDir() throws IllegalStateException {
        return WebUtils.getTempDir(getServletContext());
    }
}
