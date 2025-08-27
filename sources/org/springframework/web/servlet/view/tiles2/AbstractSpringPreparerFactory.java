package org.springframework.web.servlet.view.tiles2;

import org.apache.tiles.TilesException;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerFactory;
import org.apache.tiles.preparer.ViewPreparer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/tiles2/AbstractSpringPreparerFactory.class */
public abstract class AbstractSpringPreparerFactory implements PreparerFactory {
    protected abstract ViewPreparer getPreparer(String str, WebApplicationContext webApplicationContext) throws TilesException;

    public ViewPreparer getPreparer(String name, TilesRequestContext context) throws TilesException {
        WebApplicationContext webApplicationContext = (WebApplicationContext) context.getRequestScope().get(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        if (webApplicationContext == null) {
            webApplicationContext = (WebApplicationContext) context.getApplicationContext().getApplicationScope().get(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            if (webApplicationContext == null) {
                throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener registered?");
            }
        }
        return getPreparer(name, webApplicationContext);
    }
}
