package org.ehcache.xml.model;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resource-type", propOrder = {"value"})
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/ResourceType.class */
public class ResourceType {

    @XmlSchemaType(name = "positiveInteger")
    @XmlValue
    protected BigInteger value;

    @XmlAttribute(name = "unit")
    protected ResourceUnit unit;

    public BigInteger getValue() {
        return this.value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public ResourceUnit getUnit() {
        if (this.unit == null) {
            return ResourceUnit.ENTRIES;
        }
        return this.unit;
    }

    public void setUnit(ResourceUnit value) {
        this.unit = value;
    }
}
