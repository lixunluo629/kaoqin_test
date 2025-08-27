package org.ehcache.xml.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "thread-pools-type", propOrder = {"threadPool"})
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/ThreadPoolsType.class */
public class ThreadPoolsType {

    @XmlElement(name = "thread-pool", required = true)
    protected List<ThreadPool> threadPool;

    public List<ThreadPool> getThreadPool() {
        if (this.threadPool == null) {
            this.threadPool = new ArrayList();
        }
        return this.threadPool;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/ThreadPoolsType$ThreadPool.class */
    public static class ThreadPool {

        @XmlAttribute(name = "alias", required = true)
        protected String alias;

        @XmlAttribute(name = "default")
        protected Boolean _default;

        @XmlSchemaType(name = "nonNegativeInteger")
        @XmlAttribute(name = "min-size", required = true)
        protected BigInteger minSize;

        @XmlSchemaType(name = "positiveInteger")
        @XmlAttribute(name = "max-size", required = true)
        protected BigInteger maxSize;

        public String getAlias() {
            return this.alias;
        }

        public void setAlias(String value) {
            this.alias = value;
        }

        public boolean isDefault() {
            if (this._default == null) {
                return false;
            }
            return this._default.booleanValue();
        }

        public void setDefault(Boolean value) {
            this._default = value;
        }

        public BigInteger getMinSize() {
            return this.minSize;
        }

        public void setMinSize(BigInteger value) {
            this.minSize = value;
        }

        public BigInteger getMaxSize() {
            return this.maxSize;
        }

        public void setMaxSize(BigInteger value) {
            this.maxSize = value;
        }
    }
}
