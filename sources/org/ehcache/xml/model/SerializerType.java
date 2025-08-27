package org.ehcache.xml.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serializer-type", propOrder = {"serializer"})
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/SerializerType.class */
public class SerializerType {
    protected List<Serializer> serializer;

    public List<Serializer> getSerializer() {
        if (this.serializer == null) {
            this.serializer = new ArrayList();
        }
        return this.serializer;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"value"})
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/SerializerType$Serializer.class */
    public static class Serializer {

        @XmlValue
        protected String value;

        @XmlAttribute(name = "type", required = true)
        protected String type;

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String value) {
            this.type = value;
        }
    }
}
