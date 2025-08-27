package org.ehcache.xml.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "config-type", propOrder = {"service", "defaultSerializers", "defaultCopiers", "persistence", "threadPools", "eventDispatch", "writeBehind", "heapStore", "diskStore", "cacheOrCacheTemplate"})
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/ConfigType.class */
public class ConfigType {
    protected List<ServiceType> service;

    @XmlElement(name = "default-serializers")
    protected SerializerType defaultSerializers;

    @XmlElement(name = "default-copiers")
    protected CopierType defaultCopiers;
    protected PersistenceType persistence;

    @XmlElement(name = "thread-pools")
    protected ThreadPoolsType threadPools;

    @XmlElement(name = "event-dispatch")
    protected ThreadPoolReferenceType eventDispatch;

    @XmlElement(name = "write-behind")
    protected ThreadPoolReferenceType writeBehind;

    @XmlElement(name = "heap-store")
    protected SizeofType heapStore;

    @XmlElement(name = "disk-store")
    protected ThreadPoolReferenceType diskStore;

    @XmlElements({@XmlElement(name = "cache", type = CacheType.class), @XmlElement(name = "cache-template", type = CacheTemplateType.class)})
    protected List<BaseCacheType> cacheOrCacheTemplate;

    public List<ServiceType> getService() {
        if (this.service == null) {
            this.service = new ArrayList();
        }
        return this.service;
    }

    public SerializerType getDefaultSerializers() {
        return this.defaultSerializers;
    }

    public void setDefaultSerializers(SerializerType value) {
        this.defaultSerializers = value;
    }

    public CopierType getDefaultCopiers() {
        return this.defaultCopiers;
    }

    public void setDefaultCopiers(CopierType value) {
        this.defaultCopiers = value;
    }

    public PersistenceType getPersistence() {
        return this.persistence;
    }

    public void setPersistence(PersistenceType value) {
        this.persistence = value;
    }

    public ThreadPoolsType getThreadPools() {
        return this.threadPools;
    }

    public void setThreadPools(ThreadPoolsType value) {
        this.threadPools = value;
    }

    public ThreadPoolReferenceType getEventDispatch() {
        return this.eventDispatch;
    }

    public void setEventDispatch(ThreadPoolReferenceType value) {
        this.eventDispatch = value;
    }

    public ThreadPoolReferenceType getWriteBehind() {
        return this.writeBehind;
    }

    public void setWriteBehind(ThreadPoolReferenceType value) {
        this.writeBehind = value;
    }

    public SizeofType getHeapStore() {
        return this.heapStore;
    }

    public void setHeapStore(SizeofType value) {
        this.heapStore = value;
    }

    public ThreadPoolReferenceType getDiskStore() {
        return this.diskStore;
    }

    public void setDiskStore(ThreadPoolReferenceType value) {
        this.diskStore = value;
    }

    public List<BaseCacheType> getCacheOrCacheTemplate() {
        if (this.cacheOrCacheTemplate == null) {
            this.cacheOrCacheTemplate = new ArrayList();
        }
        return this.cacheOrCacheTemplate;
    }
}
