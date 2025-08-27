package org.ehcache.xml.model;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/Disk.class */
public class Disk extends JAXBElement<PersistableMemoryType> {
    protected static final QName NAME = new QName("http://www.ehcache.org/v3", "disk");

    public Disk(PersistableMemoryType value) {
        super(NAME, PersistableMemoryType.class, (Class) null, value);
    }

    public Disk() {
        super(NAME, PersistableMemoryType.class, (Class) null, (Object) null);
    }
}
