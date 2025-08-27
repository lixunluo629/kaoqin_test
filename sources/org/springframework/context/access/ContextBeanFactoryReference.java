package org.springframework.context.access;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/access/ContextBeanFactoryReference.class */
public class ContextBeanFactoryReference implements BeanFactoryReference {
    private ApplicationContext applicationContext;

    public ContextBeanFactoryReference(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override // org.springframework.beans.factory.access.BeanFactoryReference
    public BeanFactory getFactory() {
        if (this.applicationContext == null) {
            throw new IllegalStateException("ApplicationContext owned by this BeanFactoryReference has been released");
        }
        return this.applicationContext;
    }

    @Override // org.springframework.beans.factory.access.BeanFactoryReference
    public void release() {
        ApplicationContext savedCtx;
        if (this.applicationContext != null) {
            synchronized (this) {
                savedCtx = this.applicationContext;
                this.applicationContext = null;
            }
            if (savedCtx != null && (savedCtx instanceof ConfigurableApplicationContext)) {
                ((ConfigurableApplicationContext) savedCtx).close();
            }
        }
    }
}
