package org.ehcache.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cache-entry-type", propOrder = {"value"})
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/CacheEntryType.class */
public class CacheEntryType {

    @XmlValue
    protected String value;

    @XmlAttribute(name = "serializer")
    protected String serializer;

    @XmlAttribute(name = "copier")
    protected String copier;

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSerializer() {
        return this.serializer;
    }

    public void setSerializer(String value) {
        this.serializer = value;
    }

    public String getCopier() {
        return this.copier;
    }

    public void setCopier(String value) {
        this.copier = value;
    }
}
