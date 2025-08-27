package org.springframework.beans.factory.config;

import org.springframework.util.Assert;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/RuntimeBeanReference.class */
public class RuntimeBeanReference implements BeanReference {
    private final String beanName;
    private final boolean toParent;
    private Object source;

    public RuntimeBeanReference(String beanName) {
        this(beanName, false);
    }

    public RuntimeBeanReference(String beanName, boolean toParent) {
        Assert.hasText(beanName, "'beanName' must not be empty");
        this.beanName = beanName;
        this.toParent = toParent;
    }

    @Override // org.springframework.beans.factory.config.BeanReference
    public String getBeanName() {
        return this.beanName;
    }

    public boolean isToParent() {
        return this.toParent;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    @Override // org.springframework.beans.BeanMetadataElement
    public Object getSource() {
        return this.source;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof RuntimeBeanReference)) {
            return false;
        }
        RuntimeBeanReference that = (RuntimeBeanReference) other;
        return this.beanName.equals(that.beanName) && this.toParent == that.toParent;
    }

    public int hashCode() {
        int result = this.beanName.hashCode();
        return (29 * result) + (this.toParent ? 1 : 0);
    }

    public String toString() {
        return '<' + getBeanName() + '>';
    }
}
