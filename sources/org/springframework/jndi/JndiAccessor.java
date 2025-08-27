package org.springframework.jndi;

import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jndi/JndiAccessor.class */
public class JndiAccessor {
    protected final Log logger = LogFactory.getLog(getClass());
    private JndiTemplate jndiTemplate = new JndiTemplate();

    public void setJndiTemplate(JndiTemplate jndiTemplate) {
        this.jndiTemplate = jndiTemplate != null ? jndiTemplate : new JndiTemplate();
    }

    public JndiTemplate getJndiTemplate() {
        return this.jndiTemplate;
    }

    public void setJndiEnvironment(Properties jndiEnvironment) {
        this.jndiTemplate = new JndiTemplate(jndiEnvironment);
    }

    public Properties getJndiEnvironment() {
        return this.jndiTemplate.getEnvironment();
    }
}
