package org.springframework.beans.factory.access;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/access/SingletonBeanFactoryLocator.class */
public class SingletonBeanFactoryLocator implements BeanFactoryLocator {
    private static final String DEFAULT_RESOURCE_LOCATION = "classpath*:beanRefFactory.xml";
    protected static final Log logger = LogFactory.getLog(SingletonBeanFactoryLocator.class);
    private static final Map<String, BeanFactoryLocator> instances = new HashMap();
    private final Map<String, BeanFactoryGroup> bfgInstancesByKey = new HashMap();
    private final Map<BeanFactory, BeanFactoryGroup> bfgInstancesByObj = new HashMap();
    private final String resourceLocation;

    public static BeanFactoryLocator getInstance() throws BeansException {
        return getInstance(null);
    }

    public static BeanFactoryLocator getInstance(String selector) throws BeansException {
        BeanFactoryLocator beanFactoryLocator;
        String resourceLocation = selector;
        if (resourceLocation == null) {
            resourceLocation = DEFAULT_RESOURCE_LOCATION;
        }
        if (!ResourcePatternUtils.isUrl(resourceLocation)) {
            resourceLocation = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resourceLocation;
        }
        synchronized (instances) {
            if (logger.isTraceEnabled()) {
                logger.trace("SingletonBeanFactoryLocator.getInstance(): instances.hashCode=" + instances.hashCode() + ", instances=" + instances);
            }
            BeanFactoryLocator bfl = instances.get(resourceLocation);
            if (bfl == null) {
                bfl = new SingletonBeanFactoryLocator(resourceLocation);
                instances.put(resourceLocation, bfl);
            }
            beanFactoryLocator = bfl;
        }
        return beanFactoryLocator;
    }

    protected SingletonBeanFactoryLocator(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    @Override // org.springframework.beans.factory.access.BeanFactoryLocator
    public BeanFactoryReference useBeanFactory(String factoryKey) throws BeansException {
        CountingBeanFactoryReference countingBeanFactoryReference;
        synchronized (this.bfgInstancesByKey) {
            BeanFactoryGroup bfg = this.bfgInstancesByKey.get(this.resourceLocation);
            if (bfg != null) {
                BeanFactoryGroup.access$008(bfg);
            } else {
                if (logger.isTraceEnabled()) {
                    logger.trace("Factory group with resource name [" + this.resourceLocation + "] requested. Creating new instance.");
                }
                BeanFactory groupContext = createDefinition(this.resourceLocation, factoryKey);
                bfg = new BeanFactoryGroup();
                bfg.definition = groupContext;
                bfg.refCount = 1;
                this.bfgInstancesByKey.put(this.resourceLocation, bfg);
                this.bfgInstancesByObj.put(groupContext, bfg);
                try {
                    initializeDefinition(groupContext);
                } catch (BeansException ex) {
                    this.bfgInstancesByKey.remove(this.resourceLocation);
                    this.bfgInstancesByObj.remove(groupContext);
                    throw new BootstrapException("Unable to initialize group definition. Group resource name [" + this.resourceLocation + "], factory key [" + factoryKey + "]", ex);
                }
            }
            try {
                BeanFactory beanFactory = factoryKey != null ? (BeanFactory) bfg.definition.getBean(factoryKey, BeanFactory.class) : (BeanFactory) bfg.definition.getBean(BeanFactory.class);
                countingBeanFactoryReference = new CountingBeanFactoryReference(beanFactory, bfg.definition);
            } catch (BeansException ex2) {
                throw new BootstrapException("Unable to return specified BeanFactory instance: factory key [" + factoryKey + "], from group with resource name [" + this.resourceLocation + "]", ex2);
            }
        }
        return countingBeanFactoryReference;
    }

    protected BeanFactory createDefinition(String resourceLocation, String factoryKey) {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] configResources = resourcePatternResolver.getResources(resourceLocation);
            if (configResources.length == 0) {
                throw new FatalBeanException("Unable to find resource for specified definition. Group resource name [" + this.resourceLocation + "], factory key [" + factoryKey + "]");
            }
            reader.loadBeanDefinitions(configResources);
            return factory;
        } catch (IOException ex) {
            throw new BeanDefinitionStoreException("Error accessing bean definition resource [" + this.resourceLocation + "]", ex);
        } catch (BeanDefinitionStoreException ex2) {
            throw new FatalBeanException("Unable to load group definition: group resource name [" + this.resourceLocation + "], factory key [" + factoryKey + "]", ex2);
        }
    }

    protected void initializeDefinition(BeanFactory groupDef) throws BeansException {
        if (groupDef instanceof ConfigurableListableBeanFactory) {
            ((ConfigurableListableBeanFactory) groupDef).preInstantiateSingletons();
        }
    }

    protected void destroyDefinition(BeanFactory groupDef, String selector) {
        if (groupDef instanceof ConfigurableBeanFactory) {
            if (logger.isTraceEnabled()) {
                logger.trace("Factory group with selector '" + selector + "' being released, as there are no more references to it");
            }
            ((ConfigurableBeanFactory) groupDef).destroySingletons();
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/access/SingletonBeanFactoryLocator$BeanFactoryGroup.class */
    private static class BeanFactoryGroup {
        private BeanFactory definition;
        private int refCount;

        private BeanFactoryGroup() {
            this.refCount = 0;
        }

        static /* synthetic */ int access$008(BeanFactoryGroup x0) {
            int i = x0.refCount;
            x0.refCount = i + 1;
            return i;
        }

        static /* synthetic */ int access$010(BeanFactoryGroup x0) {
            int i = x0.refCount;
            x0.refCount = i - 1;
            return i;
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/access/SingletonBeanFactoryLocator$CountingBeanFactoryReference.class */
    private class CountingBeanFactoryReference implements BeanFactoryReference {
        private BeanFactory beanFactory;
        private BeanFactory groupContextRef;

        public CountingBeanFactoryReference(BeanFactory beanFactory, BeanFactory groupContext) {
            this.beanFactory = beanFactory;
            this.groupContextRef = groupContext;
        }

        @Override // org.springframework.beans.factory.access.BeanFactoryReference
        public BeanFactory getFactory() {
            return this.beanFactory;
        }

        @Override // org.springframework.beans.factory.access.BeanFactoryReference
        public void release() throws FatalBeanException {
            synchronized (SingletonBeanFactoryLocator.this.bfgInstancesByKey) {
                BeanFactory savedRef = this.groupContextRef;
                if (savedRef != null) {
                    this.groupContextRef = null;
                    BeanFactoryGroup bfg = (BeanFactoryGroup) SingletonBeanFactoryLocator.this.bfgInstancesByObj.get(savedRef);
                    if (bfg != null) {
                        BeanFactoryGroup.access$010(bfg);
                        if (bfg.refCount == 0) {
                            SingletonBeanFactoryLocator.this.destroyDefinition(savedRef, SingletonBeanFactoryLocator.this.resourceLocation);
                            SingletonBeanFactoryLocator.this.bfgInstancesByKey.remove(SingletonBeanFactoryLocator.this.resourceLocation);
                            SingletonBeanFactoryLocator.this.bfgInstancesByObj.remove(savedRef);
                        }
                    } else {
                        SingletonBeanFactoryLocator.logger.warn("Tried to release a SingletonBeanFactoryLocator group definition more times than it has actually been used. Resource name [" + SingletonBeanFactoryLocator.this.resourceLocation + "]");
                    }
                }
            }
        }
    }
}
