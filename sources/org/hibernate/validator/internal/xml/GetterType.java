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
@XmlType(name = "getterType", propOrder = {"valid", "convertGroup", "constraint"})
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/GetterType.class */
public class GetterType {

    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String valid;

    @XmlElement(name = "convert-group")
    protected List<GroupConversionType> convertGroup;
    protected List<ConstraintType> constraint;

    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String name;

    @XmlAttribute(name = "ignore-annotations")
    protected Boolean ignoreAnnotations;

    public String getValid() {
        return this.valid;
    }

    public void setValid(String value) {
        this.valid = value;
    }

    public List<GroupConversionType> getConvertGroup() {
        if (this.convertGroup == null) {
            this.convertGroup = new ArrayList();
        }
        return this.convertGroup;
    }

    public List<ConstraintType> getConstraint() {
        if (this.constraint == null) {
            this.constraint = new ArrayList();
        }
        return this.constraint;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public Boolean getIgnoreAnnotations() {
        return this.ignoreAnnotations;
    }

    public void setIgnoreAnnotations(Boolean value) {
        this.ignoreAnnotations = value;
    }
}
