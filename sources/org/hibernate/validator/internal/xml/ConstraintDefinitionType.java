package org.hibernate.validator.internal.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "constraint-definitionType", propOrder = {"validatedBy"})
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/ConstraintDefinitionType.class */
public class ConstraintDefinitionType {

    @XmlElement(name = "validated-by", required = true)
    protected ValidatedByType validatedBy;

    @XmlAttribute(name = JamXmlElements.ANNOTATION, required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String annotation;

    public ValidatedByType getValidatedBy() {
        return this.validatedBy;
    }

    public void setValidatedBy(ValidatedByType value) {
        this.validatedBy = value;
    }

    public String getAnnotation() {
        return this.annotation;
    }

    public void setAnnotation(String value) {
        this.annotation = value;
    }
}
