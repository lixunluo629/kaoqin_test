package org.hibernate.validator.internal.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "constraint-mappingsType", propOrder = {"defaultPackage", "bean", "constraintDefinition"})
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/ConstraintMappingsType.class */
public class ConstraintMappingsType {

    @XmlElement(name = "default-package")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String defaultPackage;
    protected List<BeanType> bean;

    @XmlElement(name = "constraint-definition")
    protected List<ConstraintDefinitionType> constraintDefinition;

    @XmlAttribute(name = "version", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String version;

    public String getDefaultPackage() {
        return this.defaultPackage;
    }

    public void setDefaultPackage(String value) {
        this.defaultPackage = value;
    }

    public List<BeanType> getBean() {
        if (this.bean == null) {
            this.bean = new ArrayList();
        }
        return this.bean;
    }

    public List<ConstraintDefinitionType> getConstraintDefinition() {
        if (this.constraintDefinition == null) {
            this.constraintDefinition = new ArrayList();
        }
        return this.constraintDefinition;
    }

    public String getVersion() {
        if (this.version == null) {
            return "1.1";
        }
        return this.version;
    }

    public void setVersion(String value) {
        this.version = value;
    }
}
