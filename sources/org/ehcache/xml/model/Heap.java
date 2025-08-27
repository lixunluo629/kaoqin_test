package org.ehcache.xml.model;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/Heap.class */
public class Heap extends JAXBElement<ResourceType> {
    protected static final QName NAME = new QName("http://www.ehcache.org/v3", "heap");

    public Heap(ResourceType value) {
        super(NAME, ResourceType.class, (Class) null, value);
    }

    public Heap() {
        super(NAME, ResourceType.class, (Class) null, (Object) null);
    }
}
