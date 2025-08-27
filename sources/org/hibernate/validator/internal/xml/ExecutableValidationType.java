package org.hibernate.validator.internal.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "executable-validationType", namespace = "http://jboss.org/xml/ns/javax/validation/configuration", propOrder = {"defaultValidatedExecutableTypes"})
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/ExecutableValidationType.class */
public class ExecutableValidationType {

    @XmlElement(name = "default-validated-executable-types")
    protected DefaultValidatedExecutableTypesType defaultValidatedExecutableTypes;

    @XmlAttribute(name = "enabled")
    protected Boolean enabled;

    public DefaultValidatedExecutableTypesType getDefaultValidatedExecutableTypes() {
        return this.defaultValidatedExecutableTypes;
    }

    public void setDefaultValidatedExecutableTypes(DefaultValidatedExecutableTypesType value) {
        this.defaultValidatedExecutableTypes = value;
    }

    public Boolean getEnabled() {
        if (this.enabled == null) {
            return true;
        }
        return this.enabled;
    }

    public void setEnabled(Boolean value) {
        this.enabled = value;
    }
}
