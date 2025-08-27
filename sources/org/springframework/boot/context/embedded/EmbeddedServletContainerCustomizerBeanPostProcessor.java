package org.springframework.boot.context.embedded;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.Assert;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/EmbeddedServletContainerCustomizerBeanPostProcessor.class */
public class EmbeddedServletContainerCustomizerBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {
    private ListableBeanFactory beanFactory;
    private List<EmbeddedServletContainerCustomizer> customizers;

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        Assert.isInstanceOf(ListableBeanFactory.class, beanFactory, "EmbeddedServletContainerCustomizerBeanPostProcessor can only be used with a ListableBeanFactory");
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }

    @Override // org.springframework.beans.factory.config.BeanPostProcessor
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ConfigurableEmbeddedServletContainer) {
            postProcessBeforeInitialization((ConfigurableEmbeddedServletContainer) bean);
        }
        return bean;
    }

    @Override // org.springframework.beans.factory.config.BeanPostProcessor
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private void postProcessBeforeInitialization(ConfigurableEmbeddedServletContainer bean) {
        for (EmbeddedServletContainerCustomizer customizer : getCustomizers()) {
            customizer.customize(bean);
        }
    }

    private Collection<EmbeddedServletContainerCustomizer> getCustomizers() {
        if (this.customizers == null) {
            this.customizers = new ArrayList(this.beanFactory.getBeansOfType(EmbeddedServletContainerCustomizer.class, false, false).values());
            Collections.sort(this.customizers, AnnotationAwareOrderComparator.INSTANCE);
            this.customizers = Collections.unmodifiableList(this.customizers);
        }
        return this.customizers;
    }
}
