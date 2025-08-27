package org.springframework.beans.factory.access.el;

import javax.el.ELContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.Assert;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/access/el/SimpleSpringBeanELResolver.class */
public class SimpleSpringBeanELResolver extends SpringBeanELResolver {
    private final BeanFactory beanFactory;

    public SimpleSpringBeanELResolver(BeanFactory beanFactory) {
        Assert.notNull(beanFactory, "BeanFactory must not be null");
        this.beanFactory = beanFactory;
    }

    @Override // org.springframework.beans.factory.access.el.SpringBeanELResolver
    protected BeanFactory getBeanFactory(ELContext elContext) {
        return this.beanFactory;
    }
}
