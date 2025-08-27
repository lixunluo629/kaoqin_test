package org.springframework.hateoas.core;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.LinkBuilderFactory;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/ControllerEntityLinksFactoryBean.class */
public class ControllerEntityLinksFactoryBean extends AbstractFactoryBean<ControllerEntityLinks> implements ApplicationContextAware {
    private Class<? extends Annotation> annotation;
    private LinkBuilderFactory<? extends LinkBuilder> linkBuilderFactory;
    private ApplicationContext context;

    public void setAnnotation(Class<? extends Annotation> annotation) {
        Assert.notNull(annotation);
        this.annotation = annotation;
    }

    public void setLinkBuilderFactory(LinkBuilderFactory<? extends LinkBuilder> linkBuilderFactory) {
        this.linkBuilderFactory = linkBuilderFactory;
    }

    @Override // org.springframework.context.ApplicationContextAware
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    @Override // org.springframework.beans.factory.config.AbstractFactoryBean, org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        return ControllerEntityLinks.class;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.config.AbstractFactoryBean
    public ControllerEntityLinks createInstance() throws Exception {
        Collection<Class<?>> controllerTypes = new HashSet<>();
        for (Class<?> controllerType : getBeanTypesWithAnnotation(this.annotation)) {
            if (AnnotationUtils.findAnnotation(controllerType, ExposesResourceFor.class) != null) {
                controllerTypes.add(controllerType);
            }
        }
        return new ControllerEntityLinks(controllerTypes, this.linkBuilderFactory);
    }

    @Override // org.springframework.beans.factory.config.AbstractFactoryBean, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        Assert.state(this.annotation != null, "Annotation type must be configured!");
        Assert.state(this.linkBuilderFactory != null, "LinkBuilderFactory must be configured!");
        super.afterPropertiesSet();
    }

    private Iterable<Class<?>> getBeanTypesWithAnnotation(Class<? extends Annotation> type) {
        Set<Class<?>> annotatedTypes = new HashSet<>();
        for (String beanName : this.context.getBeanDefinitionNames()) {
            Annotation annotation = this.context.findAnnotationOnBean(beanName, type);
            if (annotation != null) {
                annotatedTypes.add(this.context.getType(beanName));
            }
        }
        return annotatedTypes;
    }
}
