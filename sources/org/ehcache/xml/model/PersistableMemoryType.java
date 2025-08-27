package org.ehcache.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "persistable-memory-type")
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/PersistableMemoryType.class */
public class PersistableMemoryType extends MemoryType {

    @XmlAttribute(name = "persistent")
    protected Boolean persistent;

    public boolean isPersistent() {
        if (this.persistent == null) {
            return false;
        }
        return this.persistent.booleanValue();
    }

    public void setPersistent(Boolean value) {
        this.persistent = value;
    }
}
