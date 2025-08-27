package org.springframework.context.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/support/ApplicationObjectSupport.class */
public abstract class ApplicationObjectSupport implements ApplicationContextAware {
    protected final Log logger = LogFactory.getLog(getClass());
    private ApplicationContext applicationContext;
    private MessageSourceAccessor messageSourceAccessor;

    @Override // org.springframework.context.ApplicationContextAware
    public final void setApplicationContext(ApplicationContext context) throws BeansException {
        if (context == null && !isContextRequired()) {
            this.applicationContext = null;
            this.messageSourceAccessor = null;
        } else {
            if (this.applicationContext == null) {
                if (!requiredContextClass().isInstance(context)) {
                    throw new ApplicationContextException("Invalid application context: needs to be of type [" + requiredContextClass().getName() + "]");
                }
                this.applicationContext = context;
                this.messageSourceAccessor = new MessageSourceAccessor(context);
                initApplicationContext(context);
                return;
            }
            if (this.applicationContext != context) {
                throw new ApplicationContextException("Cannot reinitialize with different application context: current one is [" + this.applicationContext + "], passed-in one is [" + context + "]");
            }
        }
    }

    protected boolean isContextRequired() {
        return false;
    }

    protected Class<?> requiredContextClass() {
        return ApplicationContext.class;
    }

    protected void initApplicationContext(ApplicationContext context) throws BeansException {
        initApplicationContext();
    }

    protected void initApplicationContext() throws BeansException {
    }

    public final ApplicationContext getApplicationContext() throws IllegalStateException {
        if (this.applicationContext == null && isContextRequired()) {
            throw new IllegalStateException("ApplicationObjectSupport instance [" + this + "] does not run in an ApplicationContext");
        }
        return this.applicationContext;
    }

    protected final MessageSourceAccessor getMessageSourceAccessor() throws IllegalStateException {
        if (this.messageSourceAccessor == null && isContextRequired()) {
            throw new IllegalStateException("ApplicationObjectSupport instance [" + this + "] does not run in an ApplicationContext");
        }
        return this.messageSourceAccessor;
    }
}
