package org.springframework.web.servlet.view.velocity;

import java.io.IOException;
import javax.servlet.ServletContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.ui.velocity.VelocityEngineFactory;
import org.springframework.web.context.ServletContextAware;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/velocity/VelocityConfigurer.class */
public class VelocityConfigurer extends VelocityEngineFactory implements VelocityConfig, InitializingBean, ResourceLoaderAware, ServletContextAware {
    private static final String SPRING_MACRO_RESOURCE_LOADER_NAME = "springMacro";
    private static final String SPRING_MACRO_RESOURCE_LOADER_CLASS = "springMacro.resource.loader.class";
    private static final String SPRING_MACRO_LIBRARY = "org/springframework/web/servlet/view/velocity/spring.vm";
    private VelocityEngine velocityEngine;
    private ServletContext servletContext;

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Override // org.springframework.web.context.ServletContextAware
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws IOException, VelocityException {
        if (this.velocityEngine == null) {
            this.velocityEngine = createVelocityEngine();
        }
    }

    @Override // org.springframework.ui.velocity.VelocityEngineFactory
    protected void postProcessVelocityEngine(VelocityEngine velocityEngine) {
        velocityEngine.setApplicationAttribute(ServletContext.class.getName(), this.servletContext);
        velocityEngine.setProperty(SPRING_MACRO_RESOURCE_LOADER_CLASS, ClasspathResourceLoader.class.getName());
        velocityEngine.addProperty("resource.loader", SPRING_MACRO_RESOURCE_LOADER_NAME);
        velocityEngine.addProperty("velocimacro.library", SPRING_MACRO_LIBRARY);
        if (this.logger.isInfoEnabled()) {
            this.logger.info("ClasspathResourceLoader with name 'springMacro' added to configured VelocityEngine");
        }
    }

    @Override // org.springframework.web.servlet.view.velocity.VelocityConfig
    public VelocityEngine getVelocityEngine() {
        return this.velocityEngine;
    }
}
