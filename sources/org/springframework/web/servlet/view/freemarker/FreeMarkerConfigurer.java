package org.springframework.web.servlet.view.freemarker;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.ext.jsp.TaglibFactory;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.context.ServletContextAware;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/freemarker/FreeMarkerConfigurer.class */
public class FreeMarkerConfigurer extends FreeMarkerConfigurationFactory implements FreeMarkerConfig, InitializingBean, ResourceLoaderAware, ServletContextAware {
    private Configuration configuration;
    private TaglibFactory taglibFactory;

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override // org.springframework.web.context.ServletContextAware
    public void setServletContext(ServletContext servletContext) {
        this.taglibFactory = new TaglibFactory(servletContext);
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws TemplateException, IOException {
        if (this.configuration == null) {
            this.configuration = createConfiguration();
        }
    }

    @Override // org.springframework.ui.freemarker.FreeMarkerConfigurationFactory
    protected void postProcessTemplateLoaders(List<TemplateLoader> templateLoaders) {
        templateLoaders.add(new ClassTemplateLoader(FreeMarkerConfigurer.class, ""));
        this.logger.info("ClassTemplateLoader for Spring macros added to FreeMarker configuration");
    }

    @Override // org.springframework.web.servlet.view.freemarker.FreeMarkerConfig
    public Configuration getConfiguration() {
        return this.configuration;
    }

    @Override // org.springframework.web.servlet.view.freemarker.FreeMarkerConfig
    public TaglibFactory getTaglibFactory() {
        return this.taglibFactory;
    }
}
