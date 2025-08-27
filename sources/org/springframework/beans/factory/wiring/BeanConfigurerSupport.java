package org.springframework.beans.factory.wiring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/wiring/BeanConfigurerSupport.class */
public class BeanConfigurerSupport implements BeanFactoryAware, InitializingBean, DisposableBean {
    protected final Log logger = LogFactory.getLog(getClass());
    private volatile BeanWiringInfoResolver beanWiringInfoResolver;
    private volatile ConfigurableListableBeanFactory beanFactory;

    public void setBeanWiringInfoResolver(BeanWiringInfoResolver beanWiringInfoResolver) {
        Assert.notNull(beanWiringInfoResolver, "BeanWiringInfoResolver must not be null");
        this.beanWiringInfoResolver = beanWiringInfoResolver;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new IllegalArgumentException("Bean configurer aspect needs to run in a ConfigurableListableBeanFactory: " + beanFactory);
        }
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
        if (this.beanWiringInfoResolver == null) {
            this.beanWiringInfoResolver = createDefaultBeanWiringInfoResolver();
        }
    }

    protected BeanWiringInfoResolver createDefaultBeanWiringInfoResolver() {
        return new ClassNameBeanWiringInfoResolver();
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        Assert.notNull(this.beanFactory, "BeanFactory must be set");
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() {
        this.beanFactory = null;
        this.beanWiringInfoResolver = null;
    }

    public void configureBean(Object beanInstance) {
        if (this.beanFactory == null) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("BeanFactory has not been set on " + ClassUtils.getShortName(getClass()) + ": Make sure this configurer runs in a Spring container. Unable to configure bean of type [" + ClassUtils.getDescriptiveType(beanInstance) + "]. Proceeding without injection.");
                return;
            }
            return;
        }
        BeanWiringInfo bwi = this.beanWiringInfoResolver.resolveWiringInfo(beanInstance);
        if (bwi == null) {
            return;
        }
        try {
            if (bwi.indicatesAutowiring() || (bwi.isDefaultBeanName() && !this.beanFactory.containsBean(bwi.getBeanName()))) {
                this.beanFactory.autowireBeanProperties(beanInstance, bwi.getAutowireMode(), bwi.getDependencyCheck());
                Object result = this.beanFactory.initializeBean(beanInstance, bwi.getBeanName());
                checkExposedObject(result, beanInstance);
            } else {
                Object result2 = this.beanFactory.configureBean(beanInstance, bwi.getBeanName());
                checkExposedObject(result2, beanInstance);
            }
        } catch (BeanCreationException ex) {
            Throwable rootCause = ex.getMostSpecificCause();
            if (rootCause instanceof BeanCurrentlyInCreationException) {
                BeanCreationException bce = (BeanCreationException) rootCause;
                if (this.beanFactory.isCurrentlyInCreation(bce.getBeanName())) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Failed to create target bean '" + bce.getBeanName() + "' while configuring object of type [" + beanInstance.getClass().getName() + "] - probably due to a circular reference. This is a common startup situation and usually not fatal. Proceeding without injection. Original exception: " + ex);
                        return;
                    }
                    return;
                }
            }
            throw ex;
        }
    }

    private void checkExposedObject(Object exposedObject, Object originalBeanInstance) {
        if (exposedObject != originalBeanInstance) {
            throw new IllegalStateException("Post-processor tried to replace bean instance of type [" + originalBeanInstance.getClass().getName() + "] with (proxy) object of type [" + exposedObject.getClass().getName() + "] - not supported for aspect-configured classes!");
        }
    }
}
