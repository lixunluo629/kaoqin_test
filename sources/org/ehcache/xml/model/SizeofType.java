package org.ehcache.xml.model;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sizeof-type", propOrder = {"maxObjectGraphSize", "maxObjectSize"})
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/SizeofType.class */
public class SizeofType {

    @XmlElement(name = "max-object-graph-size", defaultValue = "1000")
    protected MaxObjectGraphSize maxObjectGraphSize;

    @XmlElement(name = "max-object-size", defaultValue = "9223372036854775807")
    protected MemoryType maxObjectSize;

    public MaxObjectGraphSize getMaxObjectGraphSize() {
        return this.maxObjectGraphSize;
    }

    public void setMaxObjectGraphSize(MaxObjectGraphSize value) {
        this.maxObjectGraphSize = value;
    }

    public MemoryType getMaxObjectSize() {
        return this.maxObjectSize;
    }

    public void setMaxObjectSize(MemoryType value) {
        this.maxObjectSize = value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"value"})
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/SizeofType$MaxObjectGraphSize.class */
    public static class MaxObjectGraphSize {

        @XmlSchemaType(name = "positiveInteger")
        @XmlValue
        protected BigInteger value;

        public BigInteger getValue() {
            return this.value;
        }

        public void setValue(BigInteger value) {
            this.value = value;
        }
    }
}
