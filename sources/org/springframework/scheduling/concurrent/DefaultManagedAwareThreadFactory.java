package org.springframework.scheduling.concurrent;

import java.util.Properties;
import java.util.concurrent.ThreadFactory;
import javax.naming.NamingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jndi.JndiLocatorDelegate;
import org.springframework.jndi.JndiTemplate;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/concurrent/DefaultManagedAwareThreadFactory.class */
public class DefaultManagedAwareThreadFactory extends CustomizableThreadFactory implements InitializingBean {
    protected final Log logger = LogFactory.getLog(getClass());
    private JndiLocatorDelegate jndiLocator = new JndiLocatorDelegate();
    private String jndiName = "java:comp/DefaultManagedThreadFactory";
    private ThreadFactory threadFactory;

    public void setJndiTemplate(JndiTemplate jndiTemplate) {
        this.jndiLocator.setJndiTemplate(jndiTemplate);
    }

    public void setJndiEnvironment(Properties jndiEnvironment) {
        this.jndiLocator.setJndiEnvironment(jndiEnvironment);
    }

    public void setResourceRef(boolean resourceRef) {
        this.jndiLocator.setResourceRef(resourceRef);
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws NamingException {
        if (this.jndiName != null) {
            try {
                this.threadFactory = (ThreadFactory) this.jndiLocator.lookup(this.jndiName, ThreadFactory.class);
            } catch (NamingException e) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Failed to retrieve [" + this.jndiName + "] from JNDI", e);
                }
                this.logger.info("Could not find default managed thread factory in JNDI - proceeding with default local thread factory");
            }
        }
    }

    @Override // org.springframework.scheduling.concurrent.CustomizableThreadFactory, java.util.concurrent.ThreadFactory
    public Thread newThread(Runnable runnable) {
        if (this.threadFactory != null) {
            return this.threadFactory.newThread(runnable);
        }
        return super.newThread(runnable);
    }
}
