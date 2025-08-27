package org.springframework.boot.context.embedded.jetty;

import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.webapp.WebAppContext;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedWebAppContext.class */
class JettyEmbeddedWebAppContext extends WebAppContext {
    JettyEmbeddedWebAppContext() {
    }

    protected ServletHandler newServletHandler() {
        return new JettyEmbeddedServletHandler();
    }

    public void deferredInitialize() throws Exception {
        ((JettyEmbeddedServletHandler) getServletHandler()).deferredInitialize();
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/jetty/JettyEmbeddedWebAppContext$JettyEmbeddedServletHandler.class */
    private static class JettyEmbeddedServletHandler extends ServletHandler {
        private JettyEmbeddedServletHandler() {
        }

        public void initialize() throws Exception {
        }

        public void deferredInitialize() throws Exception {
            super.initialize();
        }
    }
}
