package org.springframework.context.access;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/access/ContextSingletonBeanFactoryLocator.class */
public class ContextSingletonBeanFactoryLocator extends SingletonBeanFactoryLocator {
    private static final String DEFAULT_RESOURCE_LOCATION = "classpath*:beanRefContext.xml";
    private static final Map<String, BeanFactoryLocator> instances = new HashMap();

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
                logger.trace("ContextSingletonBeanFactoryLocator.getInstance(): instances.hashCode=" + instances.hashCode() + ", instances=" + instances);
            }
            BeanFactoryLocator bfl = instances.get(resourceLocation);
            if (bfl == null) {
                bfl = new ContextSingletonBeanFactoryLocator(resourceLocation);
                instances.put(resourceLocation, bfl);
            }
            beanFactoryLocator = bfl;
        }
        return beanFactoryLocator;
    }

    protected ContextSingletonBeanFactoryLocator(String resourceLocation) {
        super(resourceLocation);
    }

    @Override // org.springframework.beans.factory.access.SingletonBeanFactoryLocator
    protected BeanFactory createDefinition(String resourceLocation, String factoryKey) {
        return new ClassPathXmlApplicationContext(new String[]{resourceLocation}, false);
    }

    @Override // org.springframework.beans.factory.access.SingletonBeanFactoryLocator
    protected void initializeDefinition(BeanFactory groupDef) throws IllegalStateException, BeansException {
        if (groupDef instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext) groupDef).refresh();
        }
    }

    @Override // org.springframework.beans.factory.access.SingletonBeanFactoryLocator
    protected void destroyDefinition(BeanFactory groupDef, String selector) {
        if (groupDef instanceof ConfigurableApplicationContext) {
            if (logger.isTraceEnabled()) {
                logger.trace("Context group with selector '" + selector + "' being released, as there are no more references to it");
            }
            ((ConfigurableApplicationContext) groupDef).close();
        }
    }
}
