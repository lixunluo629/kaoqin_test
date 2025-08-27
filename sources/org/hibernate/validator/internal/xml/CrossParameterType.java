package org.hibernate.validator.internal.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "crossParameterType", propOrder = {"constraint"})
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/CrossParameterType.class */
public class CrossParameterType {
    protected List<ConstraintType> constraint;

    @XmlAttribute(name = "ignore-annotations")
    protected Boolean ignoreAnnotations;

    public List<ConstraintType> getConstraint() {
        if (this.constraint == null) {
            this.constraint = new ArrayList();
        }
        return this.constraint;
    }

    public Boolean getIgnoreAnnotations() {
        return this.ignoreAnnotations;
    }

    public void setIgnoreAnnotations(Boolean value) {
        this.ignoreAnnotations = value;
    }
}
