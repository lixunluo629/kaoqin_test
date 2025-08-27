package org.ehcache.xml.model;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/Offheap.class */
public class Offheap extends JAXBElement<MemoryType> {
    protected static final QName NAME = new QName("http://www.ehcache.org/v3", "offheap");

    public Offheap(MemoryType value) {
        super(NAME, MemoryType.class, (Class) null, value);
    }

    public Offheap() {
        super(NAME, MemoryType.class, (Class) null, (Object) null);
    }
}
