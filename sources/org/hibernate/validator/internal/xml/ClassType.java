package org.hibernate.validator.internal.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "classType", propOrder = {"groupSequence", "constraint"})
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/ClassType.class */
public class ClassType {

    @XmlElement(name = "group-sequence")
    protected GroupSequenceType groupSequence;
    protected List<ConstraintType> constraint;

    @XmlAttribute(name = "ignore-annotations")
    protected Boolean ignoreAnnotations;

    public GroupSequenceType getGroupSequence() {
        return this.groupSequence;
    }

    public void setGroupSequence(GroupSequenceType value) {
        this.groupSequence = value;
    }

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
