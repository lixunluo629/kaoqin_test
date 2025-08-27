package org.hibernate.validator.internal.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "groupConversionType")
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/GroupConversionType.class */
public class GroupConversionType {

    @XmlAttribute(name = "from", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String from;

    @XmlAttribute(name = "to", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String to;

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String value) {
        this.from = value;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String value) {
        this.to = value;
    }
}
