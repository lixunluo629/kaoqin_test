package org.ehcache.xml.model;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "disk-store-settings-type")
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/DiskStoreSettingsType.class */
public class DiskStoreSettingsType {

    @XmlAttribute(name = "thread-pool")
    protected String threadPool;

    @XmlSchemaType(name = "positiveInteger")
    @XmlAttribute(name = "writer-concurrency")
    protected BigInteger writerConcurrency;

    public String getThreadPool() {
        return this.threadPool;
    }

    public void setThreadPool(String value) {
        this.threadPool = value;
    }

    public BigInteger getWriterConcurrency() {
        if (this.writerConcurrency == null) {
            return new BigInteger("1");
        }
        return this.writerConcurrency;
    }

    public void setWriterConcurrency(BigInteger value) {
        this.writerConcurrency = value;
    }
}
