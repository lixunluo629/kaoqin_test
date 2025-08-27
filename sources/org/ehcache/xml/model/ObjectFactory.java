package org.ehcache.xml.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.ehcache.xml.model.CacheLoaderWriterType;
import org.ehcache.xml.model.CopierType;
import org.ehcache.xml.model.ExpiryType;
import org.ehcache.xml.model.ListenersType;
import org.ehcache.xml.model.SerializerType;
import org.ehcache.xml.model.SizeofType;
import org.ehcache.xml.model.ThreadPoolsType;

@XmlRegistry
/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/model/ObjectFactory.class */
public class ObjectFactory {
    private static final QName _Resource_QNAME = new QName("http://www.ehcache.org/v3", "resource");
    private static final QName _Config_QNAME = new QName("http://www.ehcache.org/v3", "config");
    private static final QName _ServiceConfiguration_QNAME = new QName("http://www.ehcache.org/v3", "service-configuration");
    private static final QName _ServiceCreationConfiguration_QNAME = new QName("http://www.ehcache.org/v3", "service-creation-configuration");

    public ThreadPoolsType createThreadPoolsType() {
        return new ThreadPoolsType();
    }

    public CacheLoaderWriterType createCacheLoaderWriterType() {
        return new CacheLoaderWriterType();
    }

    public CacheLoaderWriterType.WriteBehind createCacheLoaderWriterTypeWriteBehind() {
        return new CacheLoaderWriterType.WriteBehind();
    }

    public CopierType createCopierType() {
        return new CopierType();
    }

    public ExpiryType createExpiryType() {
        return new ExpiryType();
    }

    public SizeofType createSizeofType() {
        return new SizeofType();
    }

    public ListenersType createListenersType() {
        return new ListenersType();
    }

    public SerializerType createSerializerType() {
        return new SerializerType();
    }

    public PersistableMemoryType createPersistableMemoryType() {
        return new PersistableMemoryType();
    }

    public MemoryType createMemoryType() {
        return new MemoryType();
    }

    public ResourceType createResourceType() {
        return new ResourceType();
    }

    public ConfigType createConfigType() {
        return new ConfigType();
    }

    public ThreadPoolReferenceType createThreadPoolReferenceType() {
        return new ThreadPoolReferenceType();
    }

    public CacheTemplateType createCacheTemplateType() {
        return new CacheTemplateType();
    }

    public CacheEntryType createCacheEntryType() {
        return new CacheEntryType();
    }

    public TimeType createTimeType() {
        return new TimeType();
    }

    public PersistenceType createPersistenceType() {
        return new PersistenceType();
    }

    public CacheType createCacheType() {
        return new CacheType();
    }

    public ServiceType createServiceType() {
        return new ServiceType();
    }

    public ResourcesType createResourcesType() {
        return new ResourcesType();
    }

    public DiskStoreSettingsType createDiskStoreSettingsType() {
        return new DiskStoreSettingsType();
    }

    public ThreadPoolsType.ThreadPool createThreadPoolsTypeThreadPool() {
        return new ThreadPoolsType.ThreadPool();
    }

    public CacheLoaderWriterType.WriteBehind.Batching createCacheLoaderWriterTypeWriteBehindBatching() {
        return new CacheLoaderWriterType.WriteBehind.Batching();
    }

    public CopierType.Copier createCopierTypeCopier() {
        return new CopierType.Copier();
    }

    public ExpiryType.None createExpiryTypeNone() {
        return new ExpiryType.None();
    }

    public SizeofType.MaxObjectGraphSize createSizeofTypeMaxObjectGraphSize() {
        return new SizeofType.MaxObjectGraphSize();
    }

    public ListenersType.Listener createListenersTypeListener() {
        return new ListenersType.Listener();
    }

    public SerializerType.Serializer createSerializerTypeSerializer() {
        return new SerializerType.Serializer();
    }

    @XmlElementDecl(namespace = "http://www.ehcache.org/v3", name = "resource")
    public JAXBElement<Object> createResource(Object value) {
        return new JAXBElement<>(_Resource_QNAME, Object.class, (Class) null, value);
    }

    @XmlElementDecl(namespace = "http://www.ehcache.org/v3", name = "disk", substitutionHeadNamespace = "http://www.ehcache.org/v3", substitutionHeadName = "resource")
    public Disk createDisk(PersistableMemoryType value) {
        return new Disk(value);
    }

    @XmlElementDecl(namespace = "http://www.ehcache.org/v3", name = "config")
    public JAXBElement<ConfigType> createConfig(ConfigType value) {
        return new JAXBElement<>(_Config_QNAME, ConfigType.class, (Class) null, value);
    }

    @XmlElementDecl(namespace = "http://www.ehcache.org/v3", name = "service-configuration")
    public JAXBElement<Object> createServiceConfiguration(Object value) {
        return new JAXBElement<>(_ServiceConfiguration_QNAME, Object.class, (Class) null, value);
    }

    @XmlElementDecl(namespace = "http://www.ehcache.org/v3", name = "heap", substitutionHeadNamespace = "http://www.ehcache.org/v3", substitutionHeadName = "resource")
    public Heap createHeap(ResourceType value) {
        return new Heap(value);
    }

    @XmlElementDecl(namespace = "http://www.ehcache.org/v3", name = "service-creation-configuration")
    public JAXBElement<Object> createServiceCreationConfiguration(Object value) {
        return new JAXBElement<>(_ServiceCreationConfiguration_QNAME, Object.class, (Class) null, value);
    }

    @XmlElementDecl(namespace = "http://www.ehcache.org/v3", name = "offheap", substitutionHeadNamespace = "http://www.ehcache.org/v3", substitutionHeadName = "resource")
    public Offheap createOffheap(MemoryType value) {
        return new Offheap(value);
    }
}
