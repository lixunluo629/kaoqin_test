package org.ehcache.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cache-type")
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/CacheType.class */
public class CacheType extends BaseCacheType {

    @XmlAttribute(name = "alias", required = true)
    protected String alias;

    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    @XmlAttribute(name = "uses-template")
    protected Object usesTemplate;

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String value) {
        this.alias = value;
    }

    public Object getUsesTemplate() {
        return this.usesTemplate;
    }

    public void setUsesTemplate(Object value) {
        this.usesTemplate = value;
    }
}
