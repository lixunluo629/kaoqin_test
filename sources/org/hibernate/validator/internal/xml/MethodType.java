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
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "methodType", propOrder = {JamXmlElements.PARAMETER, "crossParameter", "returnValue"})
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/MethodType.class */
public class MethodType {
    protected List<ParameterType> parameter;

    @XmlElement(name = "cross-parameter")
    protected CrossParameterType crossParameter;

    @XmlElement(name = "return-value")
    protected ReturnValueType returnValue;

    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String name;

    @XmlAttribute(name = "ignore-annotations")
    protected Boolean ignoreAnnotations;

    public List<ParameterType> getParameter() {
        if (this.parameter == null) {
            this.parameter = new ArrayList();
        }
        return this.parameter;
    }

    public CrossParameterType getCrossParameter() {
        return this.crossParameter;
    }

    public void setCrossParameter(CrossParameterType value) {
        this.crossParameter = value;
    }

    public ReturnValueType getReturnValue() {
        return this.returnValue;
    }

    public void setReturnValue(ReturnValueType value) {
        this.returnValue = value;
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
