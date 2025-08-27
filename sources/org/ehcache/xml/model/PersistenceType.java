package org.ehcache.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "persistence-type")
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/PersistenceType.class */
public class PersistenceType {

    @XmlAttribute(name = "directory", required = true)
    protected String directory;

    public String getDirectory() {
        return this.directory;
    }

    public void setDirectory(String value) {
        this.directory = value;
    }
}
