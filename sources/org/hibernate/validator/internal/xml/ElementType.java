package org.hibernate.validator.internal.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "elementType", propOrder = {"content"})
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/ElementType.class */
public class ElementType {

    @XmlElementRefs({@XmlElementRef(name = JamXmlElements.ANNOTATION, namespace = "http://jboss.org/xml/ns/javax/validation/mapping", type = JAXBElement.class), @XmlElementRef(name = "value", namespace = "http://jboss.org/xml/ns/javax/validation/mapping", type = JAXBElement.class)})
    @XmlMixed
    protected List<Serializable> content;

    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String name;

    public List<Serializable> getContent() {
        if (this.content == null) {
            this.content = new ArrayList();
        }
        return this.content;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }
}
