package org.springframework.boot.web.servlet;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Registration;
import org.springframework.core.Conventions;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/servlet/RegistrationBean.class */
public abstract class RegistrationBean implements ServletContextInitializer, Ordered {
    private String name;
    private int order = Integer.MAX_VALUE;
    private boolean asyncSupported = true;
    private boolean enabled = true;
    private Map<String, String> initParameters = new LinkedHashMap();

    public void setName(String name) {
        Assert.hasLength(name, "Name must not be empty");
        this.name = name;
    }

    public void setAsyncSupported(boolean asyncSupported) {
        this.asyncSupported = asyncSupported;
    }

    public boolean isAsyncSupported() {
        return this.asyncSupported;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setInitParameters(Map<String, String> initParameters) {
        Assert.notNull(initParameters, "InitParameters must not be null");
        this.initParameters = new LinkedHashMap(initParameters);
    }

    public Map<String, String> getInitParameters() {
        return this.initParameters;
    }

    public void addInitParameter(String name, String value) {
        Assert.notNull(name, "Name must not be null");
        this.initParameters.put(name, value);
    }

    protected final String getOrDeduceName(Object value) {
        return this.name != null ? this.name : Conventions.getVariableName(value);
    }

    protected void configure(Registration.Dynamic registration) {
        Assert.state(registration != null, "Registration is null. Was something already registered for name=[" + this.name + "]?");
        registration.setAsyncSupported(this.asyncSupported);
        if (!this.initParameters.isEmpty()) {
            registration.setInitParameters(this.initParameters);
        }
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }
}
