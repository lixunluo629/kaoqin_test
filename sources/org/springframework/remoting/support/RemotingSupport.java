package org.springframework.remoting.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.util.ClassUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/remoting/support/RemotingSupport.class */
public abstract class RemotingSupport implements BeanClassLoaderAware {
    protected final Log logger = LogFactory.getLog(getClass());
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    protected ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }

    protected ClassLoader overrideThreadContextClassLoader() {
        return ClassUtils.overrideThreadContextClassLoader(getBeanClassLoader());
    }

    protected void resetThreadContextClassLoader(ClassLoader original) {
        if (original != null) {
            Thread.currentThread().setContextClassLoader(original);
        }
    }
}
