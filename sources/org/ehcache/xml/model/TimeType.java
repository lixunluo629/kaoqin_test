package org.ehcache.xml.model;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "time-type", propOrder = {"value"})
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/TimeType.class */
public class TimeType {

    @XmlSchemaType(name = "positiveInteger")
    @XmlValue
    protected BigInteger value;

    @XmlAttribute(name = "unit")
    protected TimeUnit unit;

    public BigInteger getValue() {
        return this.value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public TimeUnit getUnit() {
        if (this.unit == null) {
            return TimeUnit.SECONDS;
        }
        return this.unit;
    }

    public void setUnit(TimeUnit value) {
        this.unit = value;
    }
}
