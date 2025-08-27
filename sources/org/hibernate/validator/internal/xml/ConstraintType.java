package org.hibernate.validator.internal.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "constraintType", propOrder = {ConstraintHelper.MESSAGE, ConstraintHelper.GROUPS, ConstraintHelper.PAYLOAD, "element"})
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/ConstraintType.class */
public class ConstraintType {

    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String message;
    protected GroupsType groups;
    protected PayloadType payload;
    protected List<ElementType> element;

    @XmlAttribute(name = JamXmlElements.ANNOTATION, required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String annotation;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public GroupsType getGroups() {
        return this.groups;
    }

    public void setGroups(GroupsType value) {
        this.groups = value;
    }

    public PayloadType getPayload() {
        return this.payload;
    }

    public void setPayload(PayloadType value) {
        this.payload = value;
    }

    public List<ElementType> getElement() {
        if (this.element == null) {
            this.element = new ArrayList();
        }
        return this.element;
    }

    public String getAnnotation() {
        return this.annotation;
    }

    public void setAnnotation(String value) {
        this.annotation = value;
    }
}
