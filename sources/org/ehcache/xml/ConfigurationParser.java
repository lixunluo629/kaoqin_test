package org.ehcache.xml;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.ehcache.config.ResourcePool;
import org.ehcache.config.ResourceType;
import org.ehcache.config.ResourceUnit;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.config.SizedResourcePoolImpl;
import org.ehcache.core.internal.util.ClassLoading;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.spi.service.ServiceCreationConfiguration;
import org.ehcache.xml.exceptions.XmlConfigurationException;
import org.ehcache.xml.model.BaseCacheType;
import org.ehcache.xml.model.CacheLoaderWriterType;
import org.ehcache.xml.model.CacheTemplateType;
import org.ehcache.xml.model.CacheType;
import org.ehcache.xml.model.ConfigType;
import org.ehcache.xml.model.CopierType;
import org.ehcache.xml.model.Disk;
import org.ehcache.xml.model.DiskStoreSettingsType;
import org.ehcache.xml.model.EventFiringType;
import org.ehcache.xml.model.EventOrderingType;
import org.ehcache.xml.model.EventType;
import org.ehcache.xml.model.ExpiryType;
import org.ehcache.xml.model.Heap;
import org.ehcache.xml.model.ListenersType;
import org.ehcache.xml.model.MemoryType;
import org.ehcache.xml.model.ObjectFactory;
import org.ehcache.xml.model.Offheap;
import org.ehcache.xml.model.PersistableMemoryType;
import org.ehcache.xml.model.PersistenceType;
import org.ehcache.xml.model.ResourceType;
import org.ehcache.xml.model.ResourcesType;
import org.ehcache.xml.model.SerializerType;
import org.ehcache.xml.model.ServiceType;
import org.ehcache.xml.model.SizeofType;
import org.ehcache.xml.model.ThreadPoolReferenceType;
import org.ehcache.xml.model.ThreadPoolsType;
import org.ehcache.xml.model.TimeType;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/ConfigurationParser.class */
class ConfigurationParser {
    private static final String CORE_SCHEMA_NAMESPACE = "http://www.ehcache.org/v3";
    private static final String CORE_SCHEMA_ROOT_ELEMENT = "config";
    private final Unmarshaller unmarshaller;
    private final ConfigType config;
    private static final Pattern SYSPROP = Pattern.compile("\\$\\{([^}]+)\\}");
    private static final SchemaFactory XSD_SCHEMA_FACTORY = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
    private static final URL CORE_SCHEMA_URL = XmlConfiguration.class.getResource("/ehcache-core.xsd");
    private static final String CORE_SCHEMA_JAXB_MODEL_PACKAGE = ConfigType.class.getPackage().getName();
    private final Map<URI, CacheManagerServiceConfigurationParser<?>> xmlParsers = new HashMap();
    private final Map<URI, CacheServiceConfigurationParser<?>> cacheXmlParsers = new HashMap();
    private final Map<URI, CacheResourceConfigurationParser> resourceXmlParsers = new HashMap();

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/ConfigurationParser$Batching.class */
    interface Batching {
        boolean isCoalesced();

        int batchSize();

        long maxDelay();

        TimeUnit maxDelayUnit();
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/ConfigurationParser$CacheDefinition.class */
    interface CacheDefinition extends CacheTemplate {
        String id();
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/ConfigurationParser$CacheTemplate.class */
    interface CacheTemplate {
        String keyType();

        String keySerializer();

        String keyCopier();

        String valueType();

        String valueSerializer();

        String valueCopier();

        String evictionAdvisor();

        Expiry expiry();

        String loaderWriter();

        ListenersConfig listenersConfig();

        Iterable<ServiceConfiguration<?>> serviceConfigs();

        Collection<ResourcePool> resourcePools();

        WriteBehind writeBehind();

        DiskStoreSettings diskStoreSettings();

        SizeOfEngineLimits heapStoreSettings();
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/ConfigurationParser$DiskStoreSettings.class */
    interface DiskStoreSettings {
        int writerConcurrency();

        String threadPool();
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/ConfigurationParser$Expiry.class */
    interface Expiry {
        boolean isUserDef();

        boolean isTTI();

        boolean isTTL();

        String type();

        long value();

        TimeUnit unit();
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/ConfigurationParser$Listener.class */
    interface Listener {
        String className();

        EventFiringType eventFiring();

        EventOrderingType eventOrdering();

        List<EventType> fireOn();
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/ConfigurationParser$ListenersConfig.class */
    interface ListenersConfig {
        int dispatcherConcurrency();

        String threadPool();

        Iterable<Listener> listeners();
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/ConfigurationParser$SizeOfEngineLimits.class */
    interface SizeOfEngineLimits {
        long getMaxObjectGraphSize();

        long getMaxObjectSize();

        MemoryUnit getUnit();
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/ConfigurationParser$WriteBehind.class */
    interface WriteBehind {
        int maxQueueSize();

        int concurrency();

        String threadPool();

        Batching batching();
    }

    private static Schema newSchema(Source[] schemas) throws SAXException {
        Schema schemaNewSchema;
        synchronized (XSD_SCHEMA_FACTORY) {
            schemaNewSchema = XSD_SCHEMA_FACTORY.newSchema(schemas);
        }
        return schemaNewSchema;
    }

    static String replaceProperties(String originalValue, Properties properties) {
        Matcher matcher = SYSPROP.matcher(originalValue);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String property = matcher.group(1);
            String value = properties.getProperty(property);
            if (value == null) {
                throw new IllegalStateException(String.format("Replacement for ${%s} not found!", property));
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(sb);
        String resolvedValue = sb.toString();
        if (resolvedValue.equals(originalValue)) {
            return null;
        }
        return resolvedValue;
    }

    public ConfigurationParser(String xml) throws ParserConfigurationException, DOMException, SAXException, IOException, JAXBException {
        Collection<Source> schemaSources = new ArrayList<>();
        schemaSources.add(new StreamSource(CORE_SCHEMA_URL.openStream()));
        Iterator i$ = ClassLoading.libraryServiceLoaderFor(CacheManagerServiceConfigurationParser.class).iterator();
        while (i$.hasNext()) {
            CacheManagerServiceConfigurationParser<?> parser = (CacheManagerServiceConfigurationParser) i$.next();
            schemaSources.add(parser.getXmlSchema());
            this.xmlParsers.put(parser.getNamespace(), parser);
        }
        Iterator i$2 = ClassLoading.libraryServiceLoaderFor(CacheServiceConfigurationParser.class).iterator();
        while (i$2.hasNext()) {
            CacheServiceConfigurationParser<?> parser2 = (CacheServiceConfigurationParser) i$2.next();
            schemaSources.add(parser2.getXmlSchema());
            this.cacheXmlParsers.put(parser2.getNamespace(), parser2);
        }
        Iterator i$3 = ClassLoading.libraryServiceLoaderFor(CacheResourceConfigurationParser.class).iterator();
        while (i$3.hasNext()) {
            CacheResourceConfigurationParser parser3 = (CacheResourceConfigurationParser) i$3.next();
            schemaSources.add(parser3.getXmlSchema());
            this.resourceXmlParsers.put(parser3.getNamespace(), parser3);
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setSchema(newSchema((Source[]) schemaSources.toArray(new Source[schemaSources.size()])));
        DocumentBuilder domBuilder = factory.newDocumentBuilder();
        domBuilder.setErrorHandler(new FatalErrorHandler());
        Element dom = domBuilder.parse(xml).getDocumentElement();
        substituteSystemProperties(dom);
        if (!CORE_SCHEMA_ROOT_ELEMENT.equals(dom.getLocalName()) || !CORE_SCHEMA_NAMESPACE.equals(dom.getNamespaceURI())) {
            throw new XmlConfigurationException("Expecting {http://www.ehcache.org/v3}config element; found {" + dom.getNamespaceURI() + "}" + dom.getLocalName());
        }
        JAXBContext jc = JAXBContext.newInstance(CORE_SCHEMA_JAXB_MODEL_PACKAGE, ConfigType.class.getClassLoader());
        this.unmarshaller = jc.createUnmarshaller();
        this.config = (ConfigType) this.unmarshaller.unmarshal(dom, ConfigType.class).getValue();
    }

    private void substituteSystemProperties(Element dom) throws DOMException {
        String newValue;
        Properties properties = System.getProperties();
        Stack<NodeList> nodeLists = new Stack<>();
        nodeLists.push(dom.getChildNodes());
        while (!nodeLists.isEmpty()) {
            NodeList nodeList = nodeLists.pop();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node currentNode = nodeList.item(i);
                if (currentNode.hasChildNodes()) {
                    nodeLists.push(currentNode.getChildNodes());
                }
                NamedNodeMap attributes = currentNode.getAttributes();
                if (attributes != null) {
                    for (int j = 0; j < attributes.getLength(); j++) {
                        Node attributeNode = attributes.item(j);
                        String newValue2 = replaceProperties(attributeNode.getNodeValue(), properties);
                        if (newValue2 != null) {
                            attributeNode.setNodeValue(newValue2);
                        }
                    }
                }
                if (currentNode.getNodeType() == 3 && (newValue = replaceProperties(currentNode.getNodeValue(), properties)) != null) {
                    currentNode.setNodeValue(newValue);
                }
            }
        }
    }

    public Iterable<ServiceType> getServiceElements() {
        return this.config.getService();
    }

    public SerializerType getDefaultSerializers() {
        return this.config.getDefaultSerializers();
    }

    public CopierType getDefaultCopiers() {
        return this.config.getDefaultCopiers();
    }

    public PersistenceType getPersistence() {
        return this.config.getPersistence();
    }

    public ThreadPoolReferenceType getEventDispatch() {
        return this.config.getEventDispatch();
    }

    public ThreadPoolReferenceType getWriteBehind() {
        return this.config.getWriteBehind();
    }

    public ThreadPoolReferenceType getDiskStore() {
        return this.config.getDiskStore();
    }

    public ThreadPoolsType getThreadPools() {
        return this.config.getThreadPools();
    }

    public SizeOfEngineLimits getHeapStore() {
        SizeofType type = this.config.getHeapStore();
        if (type == null) {
            return null;
        }
        return new XmlSizeOfEngineLimits(type);
    }

    public Iterable<CacheDefinition> getCacheElements() {
        BaseCacheType[] sources;
        List<CacheDefinition> cacheCfgs = new ArrayList<>();
        List<BaseCacheType> cacheOrCacheTemplate = this.config.getCacheOrCacheTemplate();
        for (BaseCacheType baseCacheType : cacheOrCacheTemplate) {
            if (baseCacheType instanceof CacheType) {
                final CacheType cacheType = (CacheType) baseCacheType;
                if (cacheType.getUsesTemplate() != null) {
                    sources = new BaseCacheType[]{cacheType, (BaseCacheType) cacheType.getUsesTemplate()};
                } else {
                    sources = new BaseCacheType[]{cacheType};
                }
                final BaseCacheType[] baseCacheTypeArr = sources;
                cacheCfgs.add(new CacheDefinition() { // from class: org.ehcache.xml.ConfigurationParser.1
                    @Override // org.ehcache.xml.ConfigurationParser.CacheDefinition
                    public String id() {
                        return cacheType.getAlias();
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public String keyType() throws NoSuchFieldException {
                        String value = null;
                        BaseCacheType[] arr$ = baseCacheTypeArr;
                        for (BaseCacheType source : arr$) {
                            value = source.getKeyType() != null ? source.getKeyType().getValue() : null;
                            if (value != null) {
                                break;
                            }
                        }
                        if (value == null) {
                            for (BaseCacheType baseCacheType2 : baseCacheTypeArr) {
                                value = JaxbHelper.findDefaultValue(baseCacheType2, "keyType");
                                if (value != null) {
                                    break;
                                }
                            }
                        }
                        return value;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public String keySerializer() {
                        String value = null;
                        BaseCacheType[] arr$ = baseCacheTypeArr;
                        for (BaseCacheType source : arr$) {
                            value = source.getKeyType() != null ? source.getKeyType().getSerializer() : null;
                            if (value != null) {
                                break;
                            }
                        }
                        return value;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public String keyCopier() {
                        String value = null;
                        BaseCacheType[] arr$ = baseCacheTypeArr;
                        for (BaseCacheType source : arr$) {
                            value = source.getKeyType() != null ? source.getKeyType().getCopier() : null;
                            if (value != null) {
                                break;
                            }
                        }
                        return value;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public String valueType() throws NoSuchFieldException {
                        String value = null;
                        BaseCacheType[] arr$ = baseCacheTypeArr;
                        for (BaseCacheType source : arr$) {
                            value = source.getValueType() != null ? source.getValueType().getValue() : null;
                            if (value != null) {
                                break;
                            }
                        }
                        if (value == null) {
                            for (BaseCacheType baseCacheType2 : baseCacheTypeArr) {
                                value = JaxbHelper.findDefaultValue(baseCacheType2, "valueType");
                                if (value != null) {
                                    break;
                                }
                            }
                        }
                        return value;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public String valueSerializer() {
                        String value = null;
                        BaseCacheType[] arr$ = baseCacheTypeArr;
                        for (BaseCacheType source : arr$) {
                            value = source.getValueType() != null ? source.getValueType().getSerializer() : null;
                            if (value != null) {
                                break;
                            }
                        }
                        return value;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public String valueCopier() {
                        String value = null;
                        BaseCacheType[] arr$ = baseCacheTypeArr;
                        for (BaseCacheType source : arr$) {
                            value = source.getValueType() != null ? source.getValueType().getCopier() : null;
                            if (value != null) {
                                break;
                            }
                        }
                        return value;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public String evictionAdvisor() {
                        String value = null;
                        BaseCacheType[] arr$ = baseCacheTypeArr;
                        for (BaseCacheType source : arr$) {
                            value = source.getEvictionAdvisor();
                            if (value != null) {
                                break;
                            }
                        }
                        return value;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public Expiry expiry() {
                        ExpiryType value = null;
                        BaseCacheType[] arr$ = baseCacheTypeArr;
                        for (BaseCacheType source : arr$) {
                            value = source.getExpiry();
                            if (value != null) {
                                break;
                            }
                        }
                        if (value != null) {
                            return new XmlExpiry(value);
                        }
                        return null;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public String loaderWriter() {
                        String configClass = null;
                        BaseCacheType[] arr$ = baseCacheTypeArr;
                        int len$ = arr$.length;
                        int i$ = 0;
                        while (true) {
                            if (i$ >= len$) {
                                break;
                            }
                            BaseCacheType source = arr$[i$];
                            CacheLoaderWriterType loaderWriter = source.getLoaderWriter();
                            if (loaderWriter == null) {
                                i$++;
                            } else {
                                configClass = loaderWriter.getClazz();
                                break;
                            }
                        }
                        return configClass;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public ListenersConfig listenersConfig() {
                        ListenersType base = null;
                        ArrayList<ListenersType> additionals = new ArrayList<>();
                        BaseCacheType[] arr$ = baseCacheTypeArr;
                        for (BaseCacheType source : arr$) {
                            if (source.getListeners() != null) {
                                if (base == null) {
                                    base = source.getListeners();
                                } else {
                                    additionals.add(source.getListeners());
                                }
                            }
                        }
                        if (base != null) {
                            return new XmlListenersConfig(base, (ListenersType[]) additionals.toArray(new ListenersType[0]));
                        }
                        return null;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public Iterable<ServiceConfiguration<?>> serviceConfigs() {
                        HashMap map = new HashMap();
                        BaseCacheType[] arr$ = baseCacheTypeArr;
                        for (BaseCacheType source : arr$) {
                            for (Element child : source.getServiceConfiguration()) {
                                ServiceConfiguration<?> serviceConfiguration = ConfigurationParser.this.parseCacheExtension(child);
                                if (!map.containsKey(serviceConfiguration.getClass())) {
                                    map.put(serviceConfiguration.getClass(), serviceConfiguration);
                                }
                            }
                        }
                        return map.values();
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public Collection<ResourcePool> resourcePools() {
                        BaseCacheType[] arr$ = baseCacheTypeArr;
                        for (BaseCacheType source : arr$) {
                            Heap heapResource = source.getHeap();
                            if (heapResource != null) {
                                return Collections.singleton(ConfigurationParser.this.parseResource(heapResource));
                            }
                            ResourcesType resources = source.getResources();
                            if (resources != null) {
                                return ConfigurationParser.this.parseResources(resources);
                            }
                        }
                        return Collections.emptySet();
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public WriteBehind writeBehind() {
                        BaseCacheType[] arr$ = baseCacheTypeArr;
                        for (BaseCacheType source : arr$) {
                            CacheLoaderWriterType loaderWriter = source.getLoaderWriter();
                            CacheLoaderWriterType.WriteBehind writebehind = loaderWriter != null ? loaderWriter.getWriteBehind() : null;
                            if (writebehind != null) {
                                return new XmlWriteBehind(writebehind);
                            }
                        }
                        return null;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public DiskStoreSettings diskStoreSettings() {
                        DiskStoreSettingsType value = null;
                        BaseCacheType[] arr$ = baseCacheTypeArr;
                        for (BaseCacheType source : arr$) {
                            value = source.getDiskStoreSettings();
                            if (value != null) {
                                break;
                            }
                        }
                        if (value != null) {
                            return new XmlDiskStoreSettings(value);
                        }
                        return null;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public SizeOfEngineLimits heapStoreSettings() {
                        SizeofType sizeofType = null;
                        BaseCacheType[] arr$ = baseCacheTypeArr;
                        for (BaseCacheType source : arr$) {
                            sizeofType = source.getHeapStoreSettings();
                            if (sizeofType != null) {
                                break;
                            }
                        }
                        if (sizeofType != null) {
                            return new XmlSizeOfEngineLimits(sizeofType);
                        }
                        return null;
                    }
                });
            }
        }
        return Collections.unmodifiableList(cacheCfgs);
    }

    public Map<String, CacheTemplate> getTemplates() {
        Map<String, CacheTemplate> templates = new HashMap<>();
        List<BaseCacheType> cacheOrCacheTemplate = this.config.getCacheOrCacheTemplate();
        for (BaseCacheType baseCacheType : cacheOrCacheTemplate) {
            if (baseCacheType instanceof CacheTemplateType) {
                final CacheTemplateType cacheTemplate = (CacheTemplateType) baseCacheType;
                templates.put(cacheTemplate.getName(), new CacheTemplate() { // from class: org.ehcache.xml.ConfigurationParser.2
                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public String keyType() throws NoSuchFieldException {
                        String keyType = cacheTemplate.getKeyType() != null ? cacheTemplate.getKeyType().getValue() : null;
                        if (keyType == null) {
                            keyType = JaxbHelper.findDefaultValue(cacheTemplate, "keyType");
                        }
                        return keyType;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public String keySerializer() {
                        if (cacheTemplate.getKeyType() != null) {
                            return cacheTemplate.getKeyType().getSerializer();
                        }
                        return null;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public String keyCopier() {
                        if (cacheTemplate.getKeyType() != null) {
                            return cacheTemplate.getKeyType().getCopier();
                        }
                        return null;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public String valueType() throws NoSuchFieldException {
                        String valueType = cacheTemplate.getValueType() != null ? cacheTemplate.getValueType().getValue() : null;
                        if (valueType == null) {
                            valueType = JaxbHelper.findDefaultValue(cacheTemplate, "valueType");
                        }
                        return valueType;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public String valueSerializer() {
                        if (cacheTemplate.getValueType() != null) {
                            return cacheTemplate.getValueType().getSerializer();
                        }
                        return null;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public String valueCopier() {
                        if (cacheTemplate.getValueType() != null) {
                            return cacheTemplate.getValueType().getCopier();
                        }
                        return null;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public String evictionAdvisor() {
                        return cacheTemplate.getEvictionAdvisor();
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public Expiry expiry() {
                        ExpiryType cacheTemplateExpiry = cacheTemplate.getExpiry();
                        if (cacheTemplateExpiry != null) {
                            return new XmlExpiry(cacheTemplateExpiry);
                        }
                        return null;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public ListenersConfig listenersConfig() {
                        ListenersType integration = cacheTemplate.getListeners();
                        if (integration != null) {
                            return new XmlListenersConfig(integration, new ListenersType[0]);
                        }
                        return null;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public String loaderWriter() {
                        CacheLoaderWriterType loaderWriter = cacheTemplate.getLoaderWriter();
                        if (loaderWriter != null) {
                            return loaderWriter.getClazz();
                        }
                        return null;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public Iterable<ServiceConfiguration<?>> serviceConfigs() {
                        Collection<ServiceConfiguration<?>> configs = new ArrayList<>();
                        for (Element child : cacheTemplate.getServiceConfiguration()) {
                            configs.add(ConfigurationParser.this.parseCacheExtension(child));
                        }
                        return configs;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public Collection<ResourcePool> resourcePools() {
                        Heap heapResource = cacheTemplate.getHeap();
                        if (heapResource != null) {
                            return Collections.singleton(ConfigurationParser.this.parseResource(heapResource));
                        }
                        ResourcesType resources = cacheTemplate.getResources();
                        if (resources != null) {
                            return ConfigurationParser.this.parseResources(resources);
                        }
                        return Collections.emptySet();
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public WriteBehind writeBehind() {
                        CacheLoaderWriterType loaderWriter = cacheTemplate.getLoaderWriter();
                        CacheLoaderWriterType.WriteBehind writebehind = loaderWriter != null ? loaderWriter.getWriteBehind() : null;
                        if (writebehind != null) {
                            return new XmlWriteBehind(writebehind);
                        }
                        return null;
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public DiskStoreSettings diskStoreSettings() {
                        DiskStoreSettingsType diskStoreSettings = cacheTemplate.getDiskStoreSettings();
                        if (diskStoreSettings == null) {
                            return null;
                        }
                        return new XmlDiskStoreSettings(diskStoreSettings);
                    }

                    @Override // org.ehcache.xml.ConfigurationParser.CacheTemplate
                    public SizeOfEngineLimits heapStoreSettings() {
                        SizeofType type = cacheTemplate.getHeapStoreSettings();
                        if (type == null) {
                            return null;
                        }
                        return new XmlSizeOfEngineLimits(type);
                    }
                });
            }
        }
        return Collections.unmodifiableMap(templates);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Collection<ResourcePool> parseResources(ResourcesType resources) {
        Collection<ResourcePool> resourcePools = new ArrayList<>();
        for (Element resource : resources.getResource()) {
            resourcePools.add(parseResource(resource));
        }
        return resourcePools;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ResourcePool parseResource(Heap resource) {
        ResourceType heapResource = (ResourceType) resource.getValue();
        return new SizedResourcePoolImpl(ResourceType.Core.HEAP, heapResource.getValue().longValue(), parseUnit(heapResource), false);
    }

    private ResourcePool parseResource(Element element) {
        if (!CORE_SCHEMA_NAMESPACE.equals(element.getNamespaceURI())) {
            return parseResourceExtension(element);
        }
        try {
            Object resource = this.unmarshaller.unmarshal(element);
            if (resource instanceof Heap) {
                org.ehcache.xml.model.ResourceType heapResource = (org.ehcache.xml.model.ResourceType) ((Heap) resource).getValue();
                return new SizedResourcePoolImpl(ResourceType.Core.HEAP, heapResource.getValue().longValue(), parseUnit(heapResource), false);
            }
            if (resource instanceof Offheap) {
                MemoryType offheapResource = (MemoryType) ((Offheap) resource).getValue();
                return new SizedResourcePoolImpl(ResourceType.Core.OFFHEAP, offheapResource.getValue().longValue(), parseMemory(offheapResource), false);
            }
            if (resource instanceof Disk) {
                PersistableMemoryType diskResource = (PersistableMemoryType) ((Disk) resource).getValue();
                return new SizedResourcePoolImpl(ResourceType.Core.DISK, diskResource.getValue().longValue(), parseMemory(diskResource), diskResource.isPersistent());
            }
            throw new AssertionError("Unrecognized resource: " + element + " / " + resource.getClass().getName());
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Can't find parser for resource: " + element, e);
        }
    }

    private static ResourceUnit parseUnit(org.ehcache.xml.model.ResourceType resourceType) {
        if (resourceType.getUnit().value().equalsIgnoreCase("entries")) {
            return EntryUnit.ENTRIES;
        }
        return MemoryUnit.valueOf(resourceType.getUnit().value().toUpperCase());
    }

    private static MemoryUnit parseMemory(MemoryType memoryType) {
        return MemoryUnit.valueOf(memoryType.getUnit().value().toUpperCase());
    }

    ServiceCreationConfiguration<?> parseExtension(Element element) {
        URI namespace = URI.create(element.getNamespaceURI());
        CacheManagerServiceConfigurationParser<?> cacheManagerServiceConfigurationParser = this.xmlParsers.get(namespace);
        if (cacheManagerServiceConfigurationParser == null) {
            throw new IllegalArgumentException("Can't find parser for namespace: " + namespace);
        }
        return cacheManagerServiceConfigurationParser.parseServiceCreationConfiguration(element);
    }

    ServiceConfiguration<?> parseCacheExtension(Element element) {
        URI namespace = URI.create(element.getNamespaceURI());
        CacheServiceConfigurationParser<?> xmlConfigurationParser = this.cacheXmlParsers.get(namespace);
        if (xmlConfigurationParser == null) {
            throw new IllegalArgumentException("Can't find parser for namespace: " + namespace);
        }
        return xmlConfigurationParser.parseServiceConfiguration(element);
    }

    ResourcePool parseResourceExtension(Element element) {
        URI namespace = URI.create(element.getNamespaceURI());
        CacheResourceConfigurationParser xmlConfigurationParser = this.resourceXmlParsers.get(namespace);
        if (xmlConfigurationParser == null) {
            throw new XmlConfigurationException("Can't find parser for namespace: " + namespace);
        }
        return xmlConfigurationParser.parseResourceConfiguration(element);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/ConfigurationParser$FatalErrorHandler.class */
    static class FatalErrorHandler implements ErrorHandler {
        FatalErrorHandler() {
        }

        @Override // org.xml.sax.ErrorHandler
        public void warning(SAXParseException exception) throws SAXException {
            throw exception;
        }

        @Override // org.xml.sax.ErrorHandler
        public void error(SAXParseException exception) throws SAXException {
            throw exception;
        }

        @Override // org.xml.sax.ErrorHandler
        public void fatalError(SAXParseException exception) throws SAXException {
            throw exception;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/ConfigurationParser$XmlListenersConfig.class */
    private static class XmlListenersConfig implements ListenersConfig {
        final int dispatcherConcurrency;
        final String threadPool;
        final Iterable<Listener> listeners;

        private XmlListenersConfig(ListenersType type, ListenersType... others) {
            this.dispatcherConcurrency = type.getDispatcherConcurrency().intValue();
            String threadPool = type.getDispatcherThreadPool();
            Set<Listener> listenerSet = new HashSet<>();
            List<ListenersType.Listener> xmlListeners = type.getListener();
            extractListeners(listenerSet, xmlListeners);
            for (ListenersType other : others) {
                if (threadPool == null && other.getDispatcherThreadPool() != null) {
                    threadPool = other.getDispatcherThreadPool();
                }
                extractListeners(listenerSet, other.getListener());
            }
            this.threadPool = threadPool;
            this.listeners = !listenerSet.isEmpty() ? listenerSet : null;
        }

        private void extractListeners(Set<Listener> listenerSet, List<ListenersType.Listener> xmlListeners) {
            if (xmlListeners != null) {
                for (final ListenersType.Listener listener : xmlListeners) {
                    listenerSet.add(new Listener() { // from class: org.ehcache.xml.ConfigurationParser.XmlListenersConfig.1
                        @Override // org.ehcache.xml.ConfigurationParser.Listener
                        public String className() {
                            return listener.getClazz();
                        }

                        @Override // org.ehcache.xml.ConfigurationParser.Listener
                        public EventFiringType eventFiring() {
                            return listener.getEventFiringMode();
                        }

                        @Override // org.ehcache.xml.ConfigurationParser.Listener
                        public EventOrderingType eventOrdering() {
                            return listener.getEventOrderingMode();
                        }

                        @Override // org.ehcache.xml.ConfigurationParser.Listener
                        public List<EventType> fireOn() {
                            return listener.getEventsToFireOn();
                        }
                    });
                }
            }
        }

        @Override // org.ehcache.xml.ConfigurationParser.ListenersConfig
        public int dispatcherConcurrency() {
            return this.dispatcherConcurrency;
        }

        @Override // org.ehcache.xml.ConfigurationParser.ListenersConfig
        public String threadPool() {
            return this.threadPool;
        }

        @Override // org.ehcache.xml.ConfigurationParser.ListenersConfig
        public Iterable<Listener> listeners() {
            return this.listeners;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/ConfigurationParser$XmlExpiry.class */
    private static class XmlExpiry implements Expiry {
        final ExpiryType type;

        private XmlExpiry(ExpiryType type) {
            this.type = type;
        }

        @Override // org.ehcache.xml.ConfigurationParser.Expiry
        public boolean isUserDef() {
            return (this.type == null || this.type.getClazz() == null) ? false : true;
        }

        @Override // org.ehcache.xml.ConfigurationParser.Expiry
        public boolean isTTI() {
            return (this.type == null || this.type.getTti() == null) ? false : true;
        }

        @Override // org.ehcache.xml.ConfigurationParser.Expiry
        public boolean isTTL() {
            return (this.type == null || this.type.getTtl() == null) ? false : true;
        }

        @Override // org.ehcache.xml.ConfigurationParser.Expiry
        public String type() {
            return this.type.getClazz();
        }

        @Override // org.ehcache.xml.ConfigurationParser.Expiry
        public long value() {
            TimeType time;
            if (isTTI()) {
                time = this.type.getTti();
            } else {
                time = this.type.getTtl();
            }
            if (time == null) {
                return 0L;
            }
            return time.getValue().longValue();
        }

        @Override // org.ehcache.xml.ConfigurationParser.Expiry
        public TimeUnit unit() {
            TimeType time;
            if (isTTI()) {
                time = this.type.getTti();
            } else {
                time = this.type.getTtl();
            }
            if (time != null) {
                return XmlModel.convertToJavaTimeUnit(time.getUnit());
            }
            return null;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/ConfigurationParser$XmlSizeOfEngineLimits.class */
    private static class XmlSizeOfEngineLimits implements SizeOfEngineLimits {
        private final SizeofType sizeoflimits;

        private XmlSizeOfEngineLimits(SizeofType sizeoflimits) {
            this.sizeoflimits = sizeoflimits;
        }

        @Override // org.ehcache.xml.ConfigurationParser.SizeOfEngineLimits
        public long getMaxObjectGraphSize() {
            SizeofType.MaxObjectGraphSize value = this.sizeoflimits.getMaxObjectGraphSize();
            if (value == null) {
                return new BigInteger(JaxbHelper.findDefaultValue(this.sizeoflimits, "maxObjectGraphSize")).longValue();
            }
            return value.getValue().longValue();
        }

        @Override // org.ehcache.xml.ConfigurationParser.SizeOfEngineLimits
        public long getMaxObjectSize() {
            MemoryType value = this.sizeoflimits.getMaxObjectSize();
            if (value == null) {
                return new BigInteger(JaxbHelper.findDefaultValue(this.sizeoflimits, "maxObjectSize")).longValue();
            }
            return value.getValue().longValue();
        }

        @Override // org.ehcache.xml.ConfigurationParser.SizeOfEngineLimits
        public MemoryUnit getUnit() {
            MemoryType value = this.sizeoflimits.getMaxObjectSize();
            if (value == null) {
                return MemoryUnit.valueOf(new ObjectFactory().createMemoryType().getUnit().value().toUpperCase());
            }
            return MemoryUnit.valueOf(value.getUnit().value().toUpperCase());
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/ConfigurationParser$XmlWriteBehind.class */
    private static class XmlWriteBehind implements WriteBehind {
        private final CacheLoaderWriterType.WriteBehind writebehind;

        private XmlWriteBehind(CacheLoaderWriterType.WriteBehind writebehind) {
            this.writebehind = writebehind;
        }

        @Override // org.ehcache.xml.ConfigurationParser.WriteBehind
        public int maxQueueSize() {
            return this.writebehind.getSize().intValue();
        }

        @Override // org.ehcache.xml.ConfigurationParser.WriteBehind
        public int concurrency() {
            return this.writebehind.getConcurrency().intValue();
        }

        @Override // org.ehcache.xml.ConfigurationParser.WriteBehind
        public String threadPool() {
            return this.writebehind.getThreadPool();
        }

        @Override // org.ehcache.xml.ConfigurationParser.WriteBehind
        public Batching batching() {
            CacheLoaderWriterType.WriteBehind.Batching batching = this.writebehind.getBatching();
            if (batching == null) {
                return null;
            }
            return new XmlBatching(batching);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/ConfigurationParser$XmlBatching.class */
    private static class XmlBatching implements Batching {
        private final CacheLoaderWriterType.WriteBehind.Batching batching;

        private XmlBatching(CacheLoaderWriterType.WriteBehind.Batching batching) {
            this.batching = batching;
        }

        @Override // org.ehcache.xml.ConfigurationParser.Batching
        public boolean isCoalesced() {
            return this.batching.isCoalesce();
        }

        @Override // org.ehcache.xml.ConfigurationParser.Batching
        public int batchSize() {
            return this.batching.getBatchSize().intValue();
        }

        @Override // org.ehcache.xml.ConfigurationParser.Batching
        public long maxDelay() {
            return this.batching.getMaxWriteDelay().getValue().longValue();
        }

        @Override // org.ehcache.xml.ConfigurationParser.Batching
        public TimeUnit maxDelayUnit() {
            return XmlModel.convertToJavaTimeUnit(this.batching.getMaxWriteDelay().getUnit());
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/ConfigurationParser$XmlDiskStoreSettings.class */
    private static class XmlDiskStoreSettings implements DiskStoreSettings {
        private final DiskStoreSettingsType diskStoreSettings;

        private XmlDiskStoreSettings(DiskStoreSettingsType diskStoreSettings) {
            this.diskStoreSettings = diskStoreSettings;
        }

        @Override // org.ehcache.xml.ConfigurationParser.DiskStoreSettings
        public int writerConcurrency() {
            return this.diskStoreSettings.getWriterConcurrency().intValue();
        }

        @Override // org.ehcache.xml.ConfigurationParser.DiskStoreSettings
        public String threadPool() {
            return this.diskStoreSettings.getThreadPool();
        }
    }
}
