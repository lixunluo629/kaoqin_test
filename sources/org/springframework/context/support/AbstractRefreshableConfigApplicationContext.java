package org.springframework.context.support;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/support/AbstractRefreshableConfigApplicationContext.class */
public abstract class AbstractRefreshableConfigApplicationContext extends AbstractRefreshableApplicationContext implements BeanNameAware, InitializingBean {
    private String[] configLocations;
    private boolean setIdCalled;

    public AbstractRefreshableConfigApplicationContext() {
        this.setIdCalled = false;
    }

    public AbstractRefreshableConfigApplicationContext(ApplicationContext parent) {
        super(parent);
        this.setIdCalled = false;
    }

    public void setConfigLocation(String location) {
        setConfigLocations(StringUtils.tokenizeToStringArray(location, ",; \t\n"));
    }

    public void setConfigLocations(String... locations) {
        if (locations != null) {
            Assert.noNullElements(locations, "Config locations must not be null");
            this.configLocations = new String[locations.length];
            for (int i = 0; i < locations.length; i++) {
                this.configLocations[i] = resolvePath(locations[i]).trim();
            }
            return;
        }
        this.configLocations = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String[] getConfigLocations() {
        return this.configLocations != null ? this.configLocations : getDefaultConfigLocations();
    }

    protected String[] getDefaultConfigLocations() {
        return null;
    }

    protected String resolvePath(String path) {
        return getEnvironment().resolveRequiredPlaceholders(path);
    }

    @Override // org.springframework.context.support.AbstractApplicationContext, org.springframework.context.ConfigurableApplicationContext
    public void setId(String id) {
        super.setId(id);
        this.setIdCalled = true;
    }

    @Override // org.springframework.beans.factory.BeanNameAware
    public void setBeanName(String name) {
        if (!this.setIdCalled) {
            super.setId(name);
            setDisplayName("ApplicationContext '" + name + "'");
        }
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (!isActive()) {
            refresh();
        }
    }
}
