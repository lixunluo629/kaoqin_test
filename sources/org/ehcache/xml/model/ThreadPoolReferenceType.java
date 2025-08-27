package org.ehcache.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "thread-pool-reference-type")
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/ThreadPoolReferenceType.class */
public class ThreadPoolReferenceType {

    @XmlAttribute(name = "thread-pool", required = true)
    protected String threadPool;

    public String getThreadPool() {
        return this.threadPool;
    }

    public void setThreadPool(String value) {
        this.threadPool = value;
    }
}
