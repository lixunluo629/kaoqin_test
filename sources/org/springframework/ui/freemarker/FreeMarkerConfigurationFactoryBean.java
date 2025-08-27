package org.springframework.ui.freemarker;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/ui/freemarker/FreeMarkerConfigurationFactoryBean.class */
public class FreeMarkerConfigurationFactoryBean extends FreeMarkerConfigurationFactory implements FactoryBean<Configuration>, InitializingBean, ResourceLoaderAware {
    private Configuration configuration;

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws TemplateException, IOException {
        this.configuration = createConfiguration();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.FactoryBean
    public Configuration getObject() {
        return this.configuration;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<? extends Configuration> getObjectType() {
        return Configuration.class;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }
}
