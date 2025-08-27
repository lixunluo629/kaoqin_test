package org.springframework.data.repository.support;

import java.io.Serializable;
import java.util.Iterator;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.core.RepositoryInformation;

@Deprecated
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/DomainClassPropertyEditorRegistrar.class */
public class DomainClassPropertyEditorRegistrar implements PropertyEditorRegistrar, ApplicationContextAware {
    private Repositories repositories = Repositories.NONE;
    private RepositoryInvokerFactory repositoryInvokerFactory;

    @Override // org.springframework.beans.PropertyEditorRegistrar
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        Iterator<Class<?>> it = this.repositories.iterator();
        while (it.hasNext()) {
            Class<?> domainClass = it.next();
            RepositoryInformation repositoryInformation = this.repositories.getRepositoryInformationFor(domainClass);
            RepositoryInvoker invoker = this.repositoryInvokerFactory.getInvokerFor(domainClass);
            DomainClassPropertyEditor<Object, Serializable> editor = new DomainClassPropertyEditor<>(invoker, this.repositories.getEntityInformationFor(repositoryInformation.getDomainType()), registry);
            registry.registerCustomEditor(repositoryInformation.getDomainType(), editor);
        }
    }

    @Override // org.springframework.context.ApplicationContextAware
    public void setApplicationContext(ApplicationContext context) {
        this.repositories = new Repositories(context);
        this.repositoryInvokerFactory = new DefaultRepositoryInvokerFactory(this.repositories);
    }
}
