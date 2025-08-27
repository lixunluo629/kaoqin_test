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
@XmlType(name = "beanType", propOrder = {"classType", JamXmlElements.FIELD, "getter", "constructor", JamXmlElements.METHOD})
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/BeanType.class */
public class BeanType {

    @XmlElement(name = "class")
    protected ClassType classType;
    protected List<FieldType> field;
    protected List<GetterType> getter;
    protected List<ConstructorType> constructor;
    protected List<MethodType> method;

    @XmlAttribute(name = "class", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String clazz;

    @XmlAttribute(name = "ignore-annotations")
    protected Boolean ignoreAnnotations;

    public ClassType getClassType() {
        return this.classType;
    }

    public void setClassType(ClassType value) {
        this.classType = value;
    }

    public List<FieldType> getField() {
        if (this.field == null) {
            this.field = new ArrayList();
        }
        return this.field;
    }

    public List<GetterType> getGetter() {
        if (this.getter == null) {
            this.getter = new ArrayList();
        }
        return this.getter;
    }

    public List<ConstructorType> getConstructor() {
        if (this.constructor == null) {
            this.constructor = new ArrayList();
        }
        return this.constructor;
    }

    public List<MethodType> getMethod() {
        if (this.method == null) {
            this.method = new ArrayList();
        }
        return this.method;
    }

    public String getClazz() {
        return this.clazz;
    }

    public void setClazz(String value) {
        this.clazz = value;
    }

    public Boolean getIgnoreAnnotations() {
        return this.ignoreAnnotations;
    }

    public void setIgnoreAnnotations(Boolean value) {
        this.ignoreAnnotations = value;
    }
}
