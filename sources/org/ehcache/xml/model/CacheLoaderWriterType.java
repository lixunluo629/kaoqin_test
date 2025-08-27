package org.ehcache.xml.model;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import org.springframework.web.servlet.tags.form.InputTag;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cache-loader-writer-type", propOrder = {"clazz", "writeBehind"})
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/CacheLoaderWriterType.class */
public class CacheLoaderWriterType {

    @XmlElement(name = "class", required = true)
    protected String clazz;

    @XmlElement(name = "write-behind")
    protected WriteBehind writeBehind;

    public String getClazz() {
        return this.clazz;
    }

    public void setClazz(String value) {
        this.clazz = value;
    }

    public WriteBehind getWriteBehind() {
        return this.writeBehind;
    }

    public void setWriteBehind(WriteBehind value) {
        this.writeBehind = value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"batching", "nonBatching"})
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/CacheLoaderWriterType$WriteBehind.class */
    public static class WriteBehind {
        protected Batching batching;

        @XmlElement(name = "non-batching")
        protected Object nonBatching;

        @XmlSchemaType(name = "positiveInteger")
        @XmlAttribute(name = "concurrency")
        protected BigInteger concurrency;

        @XmlSchemaType(name = "positiveInteger")
        @XmlAttribute(name = InputTag.SIZE_ATTRIBUTE)
        protected BigInteger size;

        @XmlAttribute(name = "thread-pool")
        protected String threadPool;

        public Batching getBatching() {
            return this.batching;
        }

        public void setBatching(Batching value) {
            this.batching = value;
        }

        public Object getNonBatching() {
            return this.nonBatching;
        }

        public void setNonBatching(Object value) {
            this.nonBatching = value;
        }

        public BigInteger getConcurrency() {
            if (this.concurrency == null) {
                return new BigInteger("1");
            }
            return this.concurrency;
        }

        public void setConcurrency(BigInteger value) {
            this.concurrency = value;
        }

        public BigInteger getSize() {
            if (this.size == null) {
                return new BigInteger("2147483647");
            }
            return this.size;
        }

        public void setSize(BigInteger value) {
            this.size = value;
        }

        public String getThreadPool() {
            return this.threadPool;
        }

        public void setThreadPool(String value) {
            this.threadPool = value;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {})
        /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/CacheLoaderWriterType$WriteBehind$Batching.class */
        public static class Batching {

            @XmlElement(name = "max-write-delay", required = true)
            protected TimeType maxWriteDelay;

            @XmlSchemaType(name = "positiveInteger")
            @XmlAttribute(name = "batch-size", required = true)
            protected BigInteger batchSize;

            @XmlAttribute(name = "coalesce")
            protected Boolean coalesce;

            public TimeType getMaxWriteDelay() {
                return this.maxWriteDelay;
            }

            public void setMaxWriteDelay(TimeType value) {
                this.maxWriteDelay = value;
            }

            public BigInteger getBatchSize() {
                return this.batchSize;
            }

            public void setBatchSize(BigInteger value) {
                this.batchSize = value;
            }

            public boolean isCoalesce() {
                if (this.coalesce == null) {
                    return false;
                }
                return this.coalesce.booleanValue();
            }

            public void setCoalesce(Boolean value) {
                this.coalesce = value;
            }
        }
    }
}
