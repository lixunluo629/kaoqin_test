package org.springframework.beans.factory.config;

import java.io.Serializable;
import javax.inject.Provider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.Assert;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/ProviderCreatingFactoryBean.class */
public class ProviderCreatingFactoryBean extends AbstractFactoryBean<Provider<Object>> {
    private String targetBeanName;

    public void setTargetBeanName(String targetBeanName) {
        this.targetBeanName = targetBeanName;
    }

    @Override // org.springframework.beans.factory.config.AbstractFactoryBean, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(this.targetBeanName, "Property 'targetBeanName' is required");
        super.afterPropertiesSet();
    }

    @Override // org.springframework.beans.factory.config.AbstractFactoryBean, org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        return Provider.class;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.config.AbstractFactoryBean
    public Provider<Object> createInstance() {
        return new TargetBeanProvider(getBeanFactory(), this.targetBeanName);
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/ProviderCreatingFactoryBean$TargetBeanProvider.class */
    private static class TargetBeanProvider implements Provider<Object>, Serializable {
        private final BeanFactory beanFactory;
        private final String targetBeanName;

        public TargetBeanProvider(BeanFactory beanFactory, String targetBeanName) {
            this.beanFactory = beanFactory;
            this.targetBeanName = targetBeanName;
        }

        public Object get() throws BeansException {
            return this.beanFactory.getBean(this.targetBeanName);
        }
    }
}
