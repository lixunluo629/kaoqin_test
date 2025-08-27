package org.ehcache.xml.model;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({PersistableMemoryType.class})
@XmlType(name = "memory-type", propOrder = {"value"})
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/MemoryType.class */
public class MemoryType {

    @XmlSchemaType(name = "positiveInteger")
    @XmlValue
    protected BigInteger value;

    @XmlAttribute(name = "unit")
    protected MemoryUnit unit;

    public BigInteger getValue() {
        return this.value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public MemoryUnit getUnit() {
        if (this.unit == null) {
            return MemoryUnit.B;
        }
        return this.unit;
    }

    public void setUnit(MemoryUnit value) {
        this.unit = value;
    }
}
