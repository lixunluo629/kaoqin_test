package org.springframework.data.repository.init;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.support.Repositories;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/init/AbstractRepositoryPopulatorFactoryBean.class */
public abstract class AbstractRepositoryPopulatorFactoryBean extends AbstractFactoryBean<ResourceReaderRepositoryPopulator> implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {
    private Resource[] resources;
    private RepositoryPopulator populator;
    private ApplicationContext context;

    protected abstract ResourceReader getResourceReader();

    public void setResources(Resource[] resources) {
        Assert.notNull(resources, "Resources must not be null!");
        this.resources = (Resource[]) resources.clone();
    }

    @Override // org.springframework.context.ApplicationContextAware
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.context = applicationContext;
    }

    @Override // org.springframework.beans.factory.config.AbstractFactoryBean, org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        return ResourceReaderRepositoryPopulator.class;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.config.AbstractFactoryBean
    public ResourceReaderRepositoryPopulator createInstance() {
        ResourceReaderRepositoryPopulator initializer = new ResourceReaderRepositoryPopulator(getResourceReader());
        initializer.setResources(this.resources);
        initializer.setApplicationEventPublisher(this.context);
        this.populator = initializer;
        return initializer;
    }

    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().equals(this.context)) {
            Repositories repositories = new Repositories(event.getApplicationContext());
            this.populator.populate(repositories);
        }
    }
}
