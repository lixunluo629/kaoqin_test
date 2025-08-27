package org.ehcache.xml.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.w3c.dom.Element;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({CacheTemplateType.class, CacheType.class})
@XmlType(name = "base-cache-type", propOrder = {"keyType", "valueType", "expiry", "evictionAdvisor", "loaderWriter", "listeners", "heap", "resources", "heapStoreSettings", "diskStoreSettings", "serviceConfiguration"})
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/BaseCacheType.class */
public abstract class BaseCacheType {

    @XmlElement(name = BeanDefinitionParserDelegate.KEY_TYPE_ATTRIBUTE, defaultValue = "java.lang.Object")
    protected CacheEntryType keyType;

    @XmlElement(name = BeanDefinitionParserDelegate.VALUE_TYPE_ATTRIBUTE, defaultValue = "java.lang.Object")
    protected CacheEntryType valueType;
    protected ExpiryType expiry;

    @XmlElement(name = "eviction-advisor")
    protected String evictionAdvisor;

    @XmlElement(name = "loader-writer")
    protected CacheLoaderWriterType loaderWriter;
    protected ListenersType listeners;

    @XmlElementRef(name = "heap", namespace = "http://www.ehcache.org/v3", type = Heap.class)
    protected Heap heap;
    protected ResourcesType resources;

    @XmlElement(name = "heap-store-settings")
    protected SizeofType heapStoreSettings;

    @XmlElement(name = "disk-store-settings")
    protected DiskStoreSettingsType diskStoreSettings;

    @XmlAnyElement
    protected List<Element> serviceConfiguration;

    public CacheEntryType getKeyType() {
        return this.keyType;
    }

    public void setKeyType(CacheEntryType value) {
        this.keyType = value;
    }

    public CacheEntryType getValueType() {
        return this.valueType;
    }

    public void setValueType(CacheEntryType value) {
        this.valueType = value;
    }

    public ExpiryType getExpiry() {
        return this.expiry;
    }

    public void setExpiry(ExpiryType value) {
        this.expiry = value;
    }

    public String getEvictionAdvisor() {
        return this.evictionAdvisor;
    }

    public void setEvictionAdvisor(String value) {
        this.evictionAdvisor = value;
    }

    public CacheLoaderWriterType getLoaderWriter() {
        return this.loaderWriter;
    }

    public void setLoaderWriter(CacheLoaderWriterType value) {
        this.loaderWriter = value;
    }

    public ListenersType getListeners() {
        return this.listeners;
    }

    public void setListeners(ListenersType value) {
        this.listeners = value;
    }

    public Heap getHeap() {
        return this.heap;
    }

    public void setHeap(Heap value) {
        this.heap = value;
    }

    public ResourcesType getResources() {
        return this.resources;
    }

    public void setResources(ResourcesType value) {
        this.resources = value;
    }

    public SizeofType getHeapStoreSettings() {
        return this.heapStoreSettings;
    }

    public void setHeapStoreSettings(SizeofType value) {
        this.heapStoreSettings = value;
    }

    public DiskStoreSettingsType getDiskStoreSettings() {
        return this.diskStoreSettings;
    }

    public void setDiskStoreSettings(DiskStoreSettingsType value) {
        this.diskStoreSettings = value;
    }

    public List<Element> getServiceConfiguration() {
        if (this.serviceConfiguration == null) {
            this.serviceConfiguration = new ArrayList();
        }
        return this.serviceConfiguration;
    }
}
