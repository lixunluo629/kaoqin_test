package org.springframework.context.access;

import javax.naming.NamingException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.BootstrapException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jndi.JndiLocatorSupport;
import org.springframework.util.StringUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/access/ContextJndiBeanFactoryLocator.class */
public class ContextJndiBeanFactoryLocator extends JndiLocatorSupport implements BeanFactoryLocator {
    public static final String BEAN_FACTORY_PATH_DELIMITERS = ",; \t\n";

    @Override // org.springframework.beans.factory.access.BeanFactoryLocator
    public BeanFactoryReference useBeanFactory(String factoryKey) throws BeansException {
        try {
            String beanFactoryPath = (String) lookup(factoryKey, String.class);
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Bean factory path from JNDI environment variable [" + factoryKey + "] is: " + beanFactoryPath);
            }
            String[] paths = StringUtils.tokenizeToStringArray(beanFactoryPath, ",; \t\n");
            return createBeanFactory(paths);
        } catch (NamingException ex) {
            throw new BootstrapException("Define an environment variable [" + factoryKey + "] containing the class path locations of XML bean definition files", ex);
        }
    }

    protected BeanFactoryReference createBeanFactory(String[] resources) throws BeansException {
        ApplicationContext ctx = createApplicationContext(resources);
        return new ContextBeanFactoryReference(ctx);
    }

    protected ApplicationContext createApplicationContext(String[] resources) throws BeansException {
        return new ClassPathXmlApplicationContext(resources);
    }
}
