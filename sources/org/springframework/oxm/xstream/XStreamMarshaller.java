package org.springframework.oxm.xstream;

import com.thoughtworks.xstream.MarshallingStrategy;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.ConverterMatcher;
import com.thoughtworks.xstream.converters.ConverterRegistry;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import com.thoughtworks.xstream.core.DefaultConverterLookup;
import com.thoughtworks.xstream.core.util.CompositeClassLoader;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.DomReader;
import com.thoughtworks.xstream.io.xml.DomWriter;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.SaxWriter;
import com.thoughtworks.xstream.io.xml.StaxReader;
import com.thoughtworks.xstream.io.xml.StaxWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamSource;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.oxm.MarshallingFailureException;
import org.springframework.oxm.UncategorizedMappingException;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.support.AbstractMarshaller;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.StaxUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/xstream/XStreamMarshaller.class */
public class XStreamMarshaller extends AbstractMarshaller implements BeanClassLoaderAware, InitializingBean {
    public static final String DEFAULT_ENCODING = "UTF-8";
    private ReflectionProvider reflectionProvider;
    private HierarchicalStreamDriver streamDriver;
    private HierarchicalStreamDriver defaultDriver;
    private Mapper mapper;
    private Class<? extends MapperWrapper>[] mapperWrappers;
    private ConverterMatcher[] converters;
    private MarshallingStrategy marshallingStrategy;
    private Integer mode;
    private Map<String, ?> aliases;
    private Map<String, ?> aliasesByType;
    private Map<String, String> fieldAliases;
    private Class<?>[] useAttributeForTypes;
    private Map<?, ?> useAttributeFor;
    private Map<Class<?>, String> implicitCollections;
    private Map<Class<?>, String> omittedFields;
    private Class<?>[] annotatedClasses;
    private boolean autodetectAnnotations;
    private Class<?>[] supportedClasses;
    private XStream xstream;
    private ConverterLookup converterLookup = new DefaultConverterLookup();
    private ConverterRegistry converterRegistry = this.converterLookup;
    private String encoding = "UTF-8";
    private NameCoder nameCoder = new XmlFriendlyNameCoder();
    private ClassLoader beanClassLoader = new CompositeClassLoader();

    public void setReflectionProvider(ReflectionProvider reflectionProvider) {
        this.reflectionProvider = reflectionProvider;
    }

    public void setStreamDriver(HierarchicalStreamDriver streamDriver) {
        this.streamDriver = streamDriver;
        this.defaultDriver = streamDriver;
    }

    private HierarchicalStreamDriver getDefaultDriver() {
        if (this.defaultDriver == null) {
            this.defaultDriver = new XppDriver();
        }
        return this.defaultDriver;
    }

    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public void setMapperWrappers(Class<? extends MapperWrapper>... mapperWrappers) {
        this.mapperWrappers = mapperWrappers;
    }

    public void setConverterLookup(ConverterLookup converterLookup) {
        this.converterLookup = converterLookup;
        if (converterLookup instanceof ConverterRegistry) {
            this.converterRegistry = (ConverterRegistry) converterLookup;
        }
    }

    public void setConverterRegistry(ConverterRegistry converterRegistry) {
        this.converterRegistry = converterRegistry;
    }

    public void setConverters(ConverterMatcher... converters) {
        this.converters = converters;
    }

    public void setMarshallingStrategy(MarshallingStrategy marshallingStrategy) {
        this.marshallingStrategy = marshallingStrategy;
    }

    public void setMode(int mode) {
        this.mode = Integer.valueOf(mode);
    }

    public void setAliases(Map<String, ?> aliases) {
        this.aliases = aliases;
    }

    public void setAliasesByType(Map<String, ?> aliasesByType) {
        this.aliasesByType = aliasesByType;
    }

    public void setFieldAliases(Map<String, String> fieldAliases) {
        this.fieldAliases = fieldAliases;
    }

    public void setUseAttributeForTypes(Class<?>... useAttributeForTypes) {
        this.useAttributeForTypes = useAttributeForTypes;
    }

    public void setUseAttributeFor(Map<?, ?> useAttributeFor) {
        this.useAttributeFor = useAttributeFor;
    }

    public void setImplicitCollections(Map<Class<?>, String> implicitCollections) {
        this.implicitCollections = implicitCollections;
    }

    public void setOmittedFields(Map<Class<?>, String> omittedFields) {
        this.omittedFields = omittedFields;
    }

    public void setAnnotatedClasses(Class<?>... annotatedClasses) {
        this.annotatedClasses = annotatedClasses;
    }

    public void setAutodetectAnnotations(boolean autodetectAnnotations) {
        this.autodetectAnnotations = autodetectAnnotations;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected String getDefaultEncoding() {
        return this.encoding;
    }

    public void setNameCoder(NameCoder nameCoder) {
        this.nameCoder = nameCoder;
    }

    public void setSupportedClasses(Class<?>... supportedClasses) {
        this.supportedClasses = supportedClasses;
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        this.xstream = buildXStream();
    }

    protected XStream buildXStream() throws LinkageError {
        XStream xstream = constructXStream();
        configureXStream(xstream);
        customizeXStream(xstream);
        return xstream;
    }

    protected XStream constructXStream() {
        return new XStream(this.reflectionProvider, getDefaultDriver(), new ClassLoaderReference(this.beanClassLoader), this.mapper, this.converterLookup, this.converterRegistry) { // from class: org.springframework.oxm.xstream.XStreamMarshaller.1
            protected MapperWrapper wrapMapper(MapperWrapper next) throws NoSuchMethodException, SecurityException {
                Constructor<? extends MapperWrapper> ctor;
                MapperWrapper mapperToWrap = next;
                if (XStreamMarshaller.this.mapperWrappers != null) {
                    for (Class<? extends MapperWrapper> mapperWrapper : XStreamMarshaller.this.mapperWrappers) {
                        try {
                            ctor = mapperWrapper.getConstructor(Mapper.class);
                        } catch (NoSuchMethodException e) {
                            try {
                                ctor = mapperWrapper.getConstructor(MapperWrapper.class);
                            } catch (NoSuchMethodException e2) {
                                throw new IllegalStateException("No appropriate MapperWrapper constructor found: " + mapperWrapper);
                            }
                        }
                        try {
                            mapperToWrap = (MapperWrapper) ctor.newInstance(mapperToWrap);
                        } catch (Throwable th) {
                            throw new IllegalStateException("Failed to construct MapperWrapper: " + mapperWrapper);
                        }
                    }
                }
                return mapperToWrap;
            }
        };
    }

    protected void configureXStream(XStream xstream) throws LinkageError {
        if (this.converters != null) {
            for (int i = 0; i < this.converters.length; i++) {
                if (this.converters[i] instanceof Converter) {
                    xstream.registerConverter(this.converters[i], i);
                } else if (this.converters[i] instanceof SingleValueConverter) {
                    xstream.registerConverter(this.converters[i], i);
                } else {
                    throw new IllegalArgumentException("Invalid ConverterMatcher [" + this.converters[i] + "]");
                }
            }
        }
        if (this.marshallingStrategy != null) {
            xstream.setMarshallingStrategy(this.marshallingStrategy);
        }
        if (this.mode != null) {
            xstream.setMode(this.mode.intValue());
        }
        try {
            if (this.aliases != null) {
                Map<String, Class<?>> classMap = toClassMap(this.aliases);
                for (Map.Entry<String, Class<?>> entry : classMap.entrySet()) {
                    xstream.alias(entry.getKey(), entry.getValue());
                }
            }
            if (this.aliasesByType != null) {
                Map<String, Class<?>> classMap2 = toClassMap(this.aliasesByType);
                for (Map.Entry<String, Class<?>> entry2 : classMap2.entrySet()) {
                    xstream.aliasType(entry2.getKey(), entry2.getValue());
                }
            }
            if (this.fieldAliases != null) {
                for (Map.Entry<String, String> entry3 : this.fieldAliases.entrySet()) {
                    String alias = entry3.getValue();
                    String field = entry3.getKey();
                    int idx = field.lastIndexOf(46);
                    if (idx != -1) {
                        String className = field.substring(0, idx);
                        Class<?> clazz = ClassUtils.forName(className, this.beanClassLoader);
                        String fieldName = field.substring(idx + 1);
                        xstream.aliasField(alias, clazz, fieldName);
                    } else {
                        throw new IllegalArgumentException("Field name [" + field + "] does not contain '.'");
                    }
                }
            }
            if (this.useAttributeForTypes != null) {
                for (Class<?> type : this.useAttributeForTypes) {
                    xstream.useAttributeFor(type);
                }
            }
            if (this.useAttributeFor != null) {
                for (Map.Entry<?, ?> entry4 : this.useAttributeFor.entrySet()) {
                    if (entry4.getKey() instanceof String) {
                        if (entry4.getValue() instanceof Class) {
                            xstream.useAttributeFor((String) entry4.getKey(), (Class) entry4.getValue());
                        } else {
                            throw new IllegalArgumentException("'useAttributesFor' takes Map<String, Class> when using a map key of type String");
                        }
                    } else if (entry4.getKey() instanceof Class) {
                        Class<?> key = (Class) entry4.getKey();
                        if (entry4.getValue() instanceof String) {
                            xstream.useAttributeFor(key, (String) entry4.getValue());
                        } else if (entry4.getValue() instanceof List) {
                            List<Object> listValue = (List) entry4.getValue();
                            for (Object element : listValue) {
                                if (element instanceof String) {
                                    xstream.useAttributeFor(key, (String) element);
                                }
                            }
                        } else {
                            throw new IllegalArgumentException("'useAttributesFor' property takes either Map<Class, String> or Map<Class, List<String>> when using a map key of type Class");
                        }
                    } else {
                        throw new IllegalArgumentException("'useAttributesFor' property takes either a map key of type String or Class");
                    }
                }
            }
            if (this.implicitCollections != null) {
                for (Map.Entry<Class<?>, String> entry5 : this.implicitCollections.entrySet()) {
                    String[] collectionFields = StringUtils.commaDelimitedListToStringArray(entry5.getValue());
                    for (String collectionField : collectionFields) {
                        xstream.addImplicitCollection(entry5.getKey(), collectionField);
                    }
                }
            }
            if (this.omittedFields != null) {
                for (Map.Entry<Class<?>, String> entry6 : this.omittedFields.entrySet()) {
                    String[] fields = StringUtils.commaDelimitedListToStringArray(entry6.getValue());
                    for (String str : fields) {
                        xstream.omitField(entry6.getKey(), str);
                    }
                }
            }
            if (this.annotatedClasses != null) {
                xstream.processAnnotations(this.annotatedClasses);
            }
            if (this.autodetectAnnotations) {
                xstream.autodetectAnnotations(true);
            }
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Failed to load specified alias class", ex);
        }
    }

    private Map<String, Class<?>> toClassMap(Map<String, ?> map) throws LinkageError, ClassNotFoundException {
        Class<?> clsForName;
        Map<String, Class<?>> result = new LinkedHashMap<>(map.size());
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Class) {
                clsForName = (Class) value;
            } else if (value instanceof String) {
                String className = (String) value;
                clsForName = ClassUtils.forName(className, this.beanClassLoader);
            } else {
                throw new IllegalArgumentException("Unknown value [" + value + "] - expected String or Class");
            }
            Class<?> type = clsForName;
            result.put(key, type);
        }
        return result;
    }

    protected void customizeXStream(XStream xstream) {
    }

    public final XStream getXStream() {
        if (this.xstream == null) {
            this.xstream = buildXStream();
        }
        return this.xstream;
    }

    @Override // org.springframework.oxm.Marshaller, org.springframework.oxm.Unmarshaller
    public boolean supports(Class<?> clazz) {
        if (ObjectUtils.isEmpty((Object[]) this.supportedClasses)) {
            return true;
        }
        for (Class<?> supportedClass : this.supportedClasses) {
            if (supportedClass.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected void marshalDomNode(Object graph, Node node) throws XmlMappingException {
        DomWriter domWriter;
        if (node instanceof Document) {
            domWriter = new DomWriter((Document) node, this.nameCoder);
        } else if (node instanceof Element) {
            domWriter = new DomWriter((Element) node, node.getOwnerDocument(), this.nameCoder);
        } else {
            throw new IllegalArgumentException("DOMResult contains neither Document nor Element");
        }
        doMarshal(graph, domWriter, null);
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected void marshalXmlEventWriter(Object graph, XMLEventWriter eventWriter) throws XmlMappingException {
        ContentHandler contentHandler = StaxUtils.createContentHandler(eventWriter);
        LexicalHandler lexicalHandler = null;
        if (contentHandler instanceof LexicalHandler) {
            lexicalHandler = (LexicalHandler) contentHandler;
        }
        marshalSaxHandlers(graph, contentHandler, lexicalHandler);
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected void marshalXmlStreamWriter(Object graph, XMLStreamWriter streamWriter) throws XmlMappingException {
        try {
            doMarshal(graph, new StaxWriter(new QNameMap(), streamWriter, this.nameCoder), null);
        } catch (XMLStreamException ex) {
            throw convertXStreamException(ex, true);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected void marshalSaxHandlers(Object graph, ContentHandler contentHandler, LexicalHandler lexicalHandler) throws XmlMappingException {
        SaxWriter saxWriter = new SaxWriter(this.nameCoder);
        saxWriter.setContentHandler(contentHandler);
        doMarshal(graph, saxWriter, null);
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    public void marshalOutputStream(Object graph, OutputStream outputStream) throws XmlMappingException, IOException {
        marshalOutputStream(graph, outputStream, null);
    }

    public void marshalOutputStream(Object graph, OutputStream outputStream, DataHolder dataHolder) throws XmlMappingException, IOException {
        if (this.streamDriver != null) {
            doMarshal(graph, this.streamDriver.createWriter(outputStream), dataHolder);
        } else {
            marshalWriter(graph, new OutputStreamWriter(outputStream, this.encoding), dataHolder);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    public void marshalWriter(Object graph, Writer writer) throws XmlMappingException, IOException {
        marshalWriter(graph, writer, null);
    }

    public void marshalWriter(Object graph, Writer writer, DataHolder dataHolder) throws XmlMappingException, IOException {
        if (this.streamDriver != null) {
            doMarshal(graph, this.streamDriver.createWriter(writer), dataHolder);
        } else {
            doMarshal(graph, new CompactWriter(writer), dataHolder);
        }
    }

    private void doMarshal(Object graph, HierarchicalStreamWriter streamWriter, DataHolder dataHolder) {
        try {
            try {
                getXStream().marshal(graph, streamWriter, dataHolder);
            } finally {
                try {
                    streamWriter.flush();
                } catch (Exception ex) {
                    this.logger.debug("Could not flush HierarchicalStreamWriter", ex);
                }
            }
        } catch (Exception ex2) {
            throw convertXStreamException(ex2, true);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected Object unmarshalStreamSource(StreamSource streamSource) throws XmlMappingException, IOException {
        if (streamSource.getInputStream() != null) {
            return unmarshalInputStream(streamSource.getInputStream());
        }
        if (streamSource.getReader() != null) {
            return unmarshalReader(streamSource.getReader());
        }
        throw new IllegalArgumentException("StreamSource contains neither InputStream nor Reader");
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected Object unmarshalDomNode(Node node) throws XmlMappingException {
        DomReader domReader;
        if (node instanceof Document) {
            domReader = new DomReader((Document) node, this.nameCoder);
        } else if (node instanceof Element) {
            domReader = new DomReader((Element) node, this.nameCoder);
        } else {
            throw new IllegalArgumentException("DOMSource contains neither Document nor Element");
        }
        return doUnmarshal(domReader, null);
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected Object unmarshalXmlEventReader(XMLEventReader eventReader) throws XmlMappingException {
        try {
            XMLStreamReader streamReader = StaxUtils.createEventStreamReader(eventReader);
            return unmarshalXmlStreamReader(streamReader);
        } catch (XMLStreamException ex) {
            throw convertXStreamException(ex, false);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected Object unmarshalXmlStreamReader(XMLStreamReader streamReader) throws XmlMappingException {
        return doUnmarshal(new StaxReader(new QNameMap(), streamReader, this.nameCoder), null);
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected Object unmarshalSaxReader(XMLReader xmlReader, InputSource inputSource) throws XmlMappingException, IOException {
        throw new UnsupportedOperationException("XStreamMarshaller does not support unmarshalling using SAX XMLReaders");
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    public Object unmarshalInputStream(InputStream inputStream) throws XmlMappingException, IOException {
        return unmarshalInputStream(inputStream, null);
    }

    public Object unmarshalInputStream(InputStream inputStream, DataHolder dataHolder) throws XmlMappingException, IOException {
        if (this.streamDriver != null) {
            return doUnmarshal(this.streamDriver.createReader(inputStream), dataHolder);
        }
        return unmarshalReader(new InputStreamReader(inputStream, this.encoding), dataHolder);
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    public Object unmarshalReader(Reader reader) throws XmlMappingException, IOException {
        return unmarshalReader(reader, null);
    }

    public Object unmarshalReader(Reader reader, DataHolder dataHolder) throws XmlMappingException, IOException {
        return doUnmarshal(getDefaultDriver().createReader(reader), dataHolder);
    }

    private Object doUnmarshal(HierarchicalStreamReader streamReader, DataHolder dataHolder) {
        try {
            return getXStream().unmarshal(streamReader, (Object) null, dataHolder);
        } catch (Exception ex) {
            throw convertXStreamException(ex, false);
        }
    }

    protected XmlMappingException convertXStreamException(Exception ex, boolean marshalling) {
        if ((ex instanceof StreamException) || (ex instanceof CannotResolveClassException) || (ex instanceof ConversionException)) {
            if (marshalling) {
                return new MarshallingFailureException("XStream marshalling exception", ex);
            }
            return new UnmarshallingFailureException("XStream unmarshalling exception", ex);
        }
        return new UncategorizedMappingException("Unknown XStream exception", ex);
    }
}
