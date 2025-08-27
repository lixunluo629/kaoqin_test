package org.springframework.jndi;

import javax.naming.NamingException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jndi/JndiObjectLocator.class */
public abstract class JndiObjectLocator extends JndiLocatorSupport implements InitializingBean {
    private String jndiName;
    private Class<?> expectedType;

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public String getJndiName() {
        return this.jndiName;
    }

    public void setExpectedType(Class<?> expectedType) {
        this.expectedType = expectedType;
    }

    public Class<?> getExpectedType() {
        return this.expectedType;
    }

    public void afterPropertiesSet() throws NamingException, IllegalArgumentException {
        if (!StringUtils.hasLength(getJndiName())) {
            throw new IllegalArgumentException("Property 'jndiName' is required");
        }
    }

    protected Object lookup() throws NamingException {
        return lookup(getJndiName(), getExpectedType());
    }
}
