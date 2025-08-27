package org.springframework.beans;

import org.springframework.core.AttributeAccessorSupport;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/BeanMetadataAttributeAccessor.class */
public class BeanMetadataAttributeAccessor extends AttributeAccessorSupport implements BeanMetadataElement {
    private Object source;

    public void setSource(Object source) {
        this.source = source;
    }

    @Override // org.springframework.beans.BeanMetadataElement
    public Object getSource() {
        return this.source;
    }

    public void addMetadataAttribute(BeanMetadataAttribute attribute) {
        super.setAttribute(attribute.getName(), attribute);
    }

    public BeanMetadataAttribute getMetadataAttribute(String name) {
        return (BeanMetadataAttribute) super.getAttribute(name);
    }

    @Override // org.springframework.core.AttributeAccessorSupport, org.springframework.core.AttributeAccessor
    public void setAttribute(String name, Object value) {
        super.setAttribute(name, new BeanMetadataAttribute(name, value));
    }

    @Override // org.springframework.core.AttributeAccessorSupport, org.springframework.core.AttributeAccessor
    public Object getAttribute(String name) {
        BeanMetadataAttribute attribute = (BeanMetadataAttribute) super.getAttribute(name);
        if (attribute != null) {
            return attribute.getValue();
        }
        return null;
    }

    @Override // org.springframework.core.AttributeAccessorSupport, org.springframework.core.AttributeAccessor
    public Object removeAttribute(String name) {
        BeanMetadataAttribute attribute = (BeanMetadataAttribute) super.removeAttribute(name);
        if (attribute != null) {
            return attribute.getValue();
        }
        return null;
    }
}
