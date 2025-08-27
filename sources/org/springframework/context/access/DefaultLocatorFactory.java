package org.springframework.context.access;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.access.BeanFactoryLocator;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/access/DefaultLocatorFactory.class */
public class DefaultLocatorFactory {
    public static BeanFactoryLocator getInstance() throws FatalBeanException {
        return ContextSingletonBeanFactoryLocator.getInstance();
    }

    public static BeanFactoryLocator getInstance(String selector) throws FatalBeanException {
        return ContextSingletonBeanFactoryLocator.getInstance(selector);
    }
}
