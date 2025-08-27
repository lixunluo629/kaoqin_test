package org.springframework.oxm.jaxb;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationException;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.oxm.GenericMarshaller;
import org.springframework.oxm.GenericUnmarshaller;
import org.springframework.oxm.MarshallingFailureException;
import org.springframework.oxm.UncategorizedMappingException;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.oxm.ValidationFailureException;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.mime.MimeContainer;
import org.springframework.oxm.mime.MimeMarshaller;
import org.springframework.oxm.mime.MimeUnmarshaller;
import org.springframework.oxm.support.SaxResourceUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.StaxUtils;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/jaxb/Jaxb2Marshaller.class */
public class Jaxb2Marshaller implements MimeMarshaller, MimeUnmarshaller, GenericMarshaller, GenericUnmarshaller, BeanClassLoaderAware, InitializingBean {
    private static final String CID = "cid:";
    private static final EntityResolver NO_OP_ENTITY_RESOLVER = new EntityResolver() { // from class: org.springframework.oxm.jaxb.Jaxb2Marshaller.1
        @Override // org.xml.sax.EntityResolver
        public InputSource resolveEntity(String publicId, String systemId) {
            return new InputSource(new StringReader(""));
        }
    };
    private String contextPath;
    private Class<?>[] classesToBeBound;
    private String[] packagesToScan;
    private Map<String, ?> jaxbContextProperties;
    private Map<String, ?> marshallerProperties;
    private Map<String, ?> unmarshallerProperties;
    private Marshaller.Listener marshallerListener;
    private Unmarshaller.Listener unmarshallerListener;
    private ValidationEventHandler validationEventHandler;
    private XmlAdapter<?, ?>[] adapters;
    private Resource[] schemaResources;
    private LSResourceResolver schemaResourceResolver;
    private Class<?> mappedClass;
    private ClassLoader beanClassLoader;
    private volatile JAXBContext jaxbContext;
    private Schema schema;
    protected final Log logger = LogFactory.getLog(getClass());
    private String schemaLanguage = "http://www.w3.org/2001/XMLSchema";
    private boolean lazyInit = false;
    private boolean mtomEnabled = false;
    private boolean supportJaxbElementClass = false;
    private boolean checkForXmlRootElement = true;
    private final Object jaxbContextMonitor = new Object();
    private boolean supportDtd = false;
    private boolean processExternalEntities = false;

    public void setContextPaths(String... contextPaths) {
        Assert.notEmpty(contextPaths, "'contextPaths' must not be empty");
        this.contextPath = StringUtils.arrayToDelimitedString(contextPaths, ":");
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getContextPath() {
        return this.contextPath;
    }

    public void setClassesToBeBound(Class<?>... classesToBeBound) {
        this.classesToBeBound = classesToBeBound;
    }

    public Class<?>[] getClassesToBeBound() {
        return this.classesToBeBound;
    }

    public void setPackagesToScan(String... packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    public String[] getPackagesToScan() {
        return this.packagesToScan;
    }

    public void setJaxbContextProperties(Map<String, ?> jaxbContextProperties) {
        this.jaxbContextProperties = jaxbContextProperties;
    }

    public void setMarshallerProperties(Map<String, ?> properties) {
        this.marshallerProperties = properties;
    }

    public void setUnmarshallerProperties(Map<String, ?> properties) {
        this.unmarshallerProperties = properties;
    }

    public void setMarshallerListener(Marshaller.Listener marshallerListener) {
        this.marshallerListener = marshallerListener;
    }

    public void setUnmarshallerListener(Unmarshaller.Listener unmarshallerListener) {
        this.unmarshallerListener = unmarshallerListener;
    }

    public void setValidationEventHandler(ValidationEventHandler validationEventHandler) {
        this.validationEventHandler = validationEventHandler;
    }

    public void setAdapters(XmlAdapter<?, ?>... adapters) {
        this.adapters = adapters;
    }

    public void setSchema(Resource schemaResource) {
        this.schemaResources = new Resource[]{schemaResource};
    }

    public void setSchemas(Resource... schemaResources) {
        this.schemaResources = schemaResources;
    }

    public void setSchemaLanguage(String schemaLanguage) {
        this.schemaLanguage = schemaLanguage;
    }

    public void setSchemaResourceResolver(LSResourceResolver schemaResourceResolver) {
        this.schemaResourceResolver = schemaResourceResolver;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public void setMtomEnabled(boolean mtomEnabled) {
        this.mtomEnabled = mtomEnabled;
    }

    public void setSupportJaxbElementClass(boolean supportJaxbElementClass) {
        this.supportJaxbElementClass = supportJaxbElementClass;
    }

    public void setCheckForXmlRootElement(boolean checkForXmlRootElement) {
        this.checkForXmlRootElement = checkForXmlRootElement;
    }

    public void setMappedClass(Class<?> mappedClass) {
        this.mappedClass = mappedClass;
    }

    public void setSupportDtd(boolean supportDtd) {
        this.supportDtd = supportDtd;
    }

    public boolean isSupportDtd() {
        return this.supportDtd;
    }

    public void setProcessExternalEntities(boolean processExternalEntities) {
        this.processExternalEntities = processExternalEntities;
        if (processExternalEntities) {
            setSupportDtd(true);
        }
    }

    public boolean isProcessExternalEntities() {
        return this.processExternalEntities;
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        boolean hasContextPath = StringUtils.hasLength(this.contextPath);
        boolean hasClassesToBeBound = !ObjectUtils.isEmpty((Object[]) this.classesToBeBound);
        boolean hasPackagesToScan = !ObjectUtils.isEmpty((Object[]) this.packagesToScan);
        if ((hasContextPath && (hasClassesToBeBound || hasPackagesToScan)) || (hasClassesToBeBound && hasPackagesToScan)) {
            throw new IllegalArgumentException("Specify either 'contextPath', 'classesToBeBound', or 'packagesToScan'");
        }
        if (!hasContextPath && !hasClassesToBeBound && !hasPackagesToScan) {
            throw new IllegalArgumentException("Setting either 'contextPath', 'classesToBeBound', or 'packagesToScan' is required");
        }
        if (!this.lazyInit) {
            getJaxbContext();
        }
        if (!ObjectUtils.isEmpty((Object[]) this.schemaResources)) {
            this.schema = loadSchema(this.schemaResources, this.schemaLanguage);
        }
    }

    public JAXBContext getJaxbContext() {
        JAXBContext jAXBContext;
        if (this.jaxbContext != null) {
            return this.jaxbContext;
        }
        synchronized (this.jaxbContextMonitor) {
            if (this.jaxbContext == null) {
                try {
                    if (StringUtils.hasLength(this.contextPath)) {
                        this.jaxbContext = createJaxbContextFromContextPath();
                    } else if (!ObjectUtils.isEmpty((Object[]) this.classesToBeBound)) {
                        this.jaxbContext = createJaxbContextFromClasses();
                    } else if (!ObjectUtils.isEmpty((Object[]) this.packagesToScan)) {
                        this.jaxbContext = createJaxbContextFromPackages();
                    }
                } catch (JAXBException ex) {
                    throw convertJaxbException(ex);
                }
            }
            jAXBContext = this.jaxbContext;
        }
        return jAXBContext;
    }

    private JAXBContext createJaxbContextFromContextPath() throws JAXBException {
        if (this.logger.isInfoEnabled()) {
            this.logger.info("Creating JAXBContext with context path [" + this.contextPath + "]");
        }
        if (this.jaxbContextProperties != null) {
            if (this.beanClassLoader != null) {
                return JAXBContext.newInstance(this.contextPath, this.beanClassLoader, this.jaxbContextProperties);
            }
            return JAXBContext.newInstance(this.contextPath, Thread.currentThread().getContextClassLoader(), this.jaxbContextProperties);
        }
        if (this.beanClassLoader != null) {
            return JAXBContext.newInstance(this.contextPath, this.beanClassLoader);
        }
        return JAXBContext.newInstance(this.contextPath);
    }

    private JAXBContext createJaxbContextFromClasses() throws JAXBException {
        if (this.logger.isInfoEnabled()) {
            this.logger.info("Creating JAXBContext with classes to be bound [" + StringUtils.arrayToCommaDelimitedString(this.classesToBeBound) + "]");
        }
        if (this.jaxbContextProperties != null) {
            return JAXBContext.newInstance(this.classesToBeBound, this.jaxbContextProperties);
        }
        return JAXBContext.newInstance(this.classesToBeBound);
    }

    private JAXBContext createJaxbContextFromPackages() throws ClassNotFoundException, UncategorizedMappingException, JAXBException {
        if (this.logger.isInfoEnabled()) {
            this.logger.info("Creating JAXBContext by scanning packages [" + StringUtils.arrayToCommaDelimitedString(this.packagesToScan) + "]");
        }
        ClassPathJaxb2TypeScanner scanner = new ClassPathJaxb2TypeScanner(this.beanClassLoader, this.packagesToScan);
        Class<?>[] jaxb2Classes = scanner.scanPackages();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Found JAXB2 classes: [" + StringUtils.arrayToCommaDelimitedString(jaxb2Classes) + "]");
        }
        this.classesToBeBound = jaxb2Classes;
        if (this.jaxbContextProperties != null) {
            return JAXBContext.newInstance(jaxb2Classes, this.jaxbContextProperties);
        }
        return JAXBContext.newInstance(jaxb2Classes);
    }

    private Schema loadSchema(Resource[] resources, String schemaLanguage) throws SAXException, IOException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Setting validation schema to " + StringUtils.arrayToCommaDelimitedString(this.schemaResources));
        }
        Assert.notEmpty(resources, "No resources given");
        Assert.hasLength(schemaLanguage, "No schema language provided");
        Source[] schemaSources = new Source[resources.length];
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        for (int i = 0; i < resources.length; i++) {
            Resource resource = resources[i];
            if (resource == null || !resource.exists()) {
                throw new IllegalArgumentException("Resource does not exist: " + resource);
            }
            InputSource inputSource = SaxResourceUtils.createInputSource(resource);
            schemaSources[i] = new SAXSource(xmlReader, inputSource);
        }
        SchemaFactory schemaFactory = SchemaFactory.newInstance(schemaLanguage);
        if (this.schemaResourceResolver != null) {
            schemaFactory.setResourceResolver(this.schemaResourceResolver);
        }
        return schemaFactory.newSchema(schemaSources);
    }

    @Override // org.springframework.oxm.Marshaller, org.springframework.oxm.Unmarshaller
    public boolean supports(Class<?> clazz) {
        return (this.supportJaxbElementClass && JAXBElement.class.isAssignableFrom(clazz)) || supportsInternal(clazz, this.checkForXmlRootElement);
    }

    @Override // org.springframework.oxm.GenericMarshaller, org.springframework.oxm.GenericUnmarshaller
    public boolean supports(Type genericType) {
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            if (JAXBElement.class == parameterizedType.getRawType() && parameterizedType.getActualTypeArguments().length == 1) {
                Type typeArgument = parameterizedType.getActualTypeArguments()[0];
                if (typeArgument instanceof Class) {
                    Class<?> classArgument = (Class) typeArgument;
                    return (classArgument.isArray() && Byte.TYPE == classArgument.getComponentType()) || isPrimitiveWrapper(classArgument) || isStandardClass(classArgument) || supportsInternal(classArgument, false);
                }
                if (typeArgument instanceof GenericArrayType) {
                    GenericArrayType arrayType = (GenericArrayType) typeArgument;
                    return Byte.TYPE == arrayType.getGenericComponentType();
                }
                return false;
            }
            return false;
        }
        if (genericType instanceof Class) {
            Class<?> clazz = (Class) genericType;
            return supportsInternal(clazz, this.checkForXmlRootElement);
        }
        return false;
    }

    private boolean supportsInternal(Class<?> clazz, boolean checkForXmlRootElement) {
        if (checkForXmlRootElement && AnnotationUtils.findAnnotation(clazz, XmlRootElement.class) == null) {
            return false;
        }
        if (StringUtils.hasLength(this.contextPath)) {
            String packageName = ClassUtils.getPackageName(clazz);
            String[] contextPaths = StringUtils.tokenizeToStringArray(this.contextPath, ":");
            for (String contextPath : contextPaths) {
                if (contextPath.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
        if (!ObjectUtils.isEmpty((Object[]) this.classesToBeBound)) {
            return Arrays.asList(this.classesToBeBound).contains(clazz);
        }
        return false;
    }

    private boolean isPrimitiveWrapper(Class<?> clazz) {
        return Boolean.class == clazz || Byte.class == clazz || Short.class == clazz || Integer.class == clazz || Long.class == clazz || Float.class == clazz || Double.class == clazz;
    }

    private boolean isStandardClass(Class<?> clazz) {
        return String.class == clazz || BigInteger.class.isAssignableFrom(clazz) || BigDecimal.class.isAssignableFrom(clazz) || Calendar.class.isAssignableFrom(clazz) || Date.class.isAssignableFrom(clazz) || QName.class.isAssignableFrom(clazz) || URI.class == clazz || XMLGregorianCalendar.class.isAssignableFrom(clazz) || Duration.class.isAssignableFrom(clazz) || Image.class == clazz || DataHandler.class == clazz || UUID.class == clazz;
    }

    @Override // org.springframework.oxm.Marshaller
    public void marshal(Object graph, Result result) throws XmlMappingException {
        marshal(graph, result, null);
    }

    @Override // org.springframework.oxm.mime.MimeMarshaller
    public void marshal(Object graph, Result result, MimeContainer mimeContainer) throws XmlMappingException {
        try {
            Marshaller marshaller = createMarshaller();
            if (this.mtomEnabled && mimeContainer != null) {
                marshaller.setAttachmentMarshaller(new Jaxb2AttachmentMarshaller(mimeContainer));
            }
            if (StaxUtils.isStaxResult(result)) {
                marshalStaxResult(marshaller, graph, result);
            } else {
                marshaller.marshal(graph, result);
            }
        } catch (JAXBException ex) {
            throw convertJaxbException(ex);
        }
    }

    protected Marshaller createMarshaller() {
        try {
            Marshaller marshaller = getJaxbContext().createMarshaller();
            initJaxbMarshaller(marshaller);
            return marshaller;
        } catch (JAXBException ex) {
            throw convertJaxbException(ex);
        }
    }

    private void marshalStaxResult(Marshaller jaxbMarshaller, Object graph, Result staxResult) throws JAXBException {
        XMLStreamWriter streamWriter = StaxUtils.getXMLStreamWriter(staxResult);
        if (streamWriter != null) {
            jaxbMarshaller.marshal(graph, streamWriter);
            return;
        }
        XMLEventWriter eventWriter = StaxUtils.getXMLEventWriter(staxResult);
        if (eventWriter != null) {
            jaxbMarshaller.marshal(graph, eventWriter);
            return;
        }
        throw new IllegalArgumentException("StAX Result contains neither XMLStreamWriter nor XMLEventConsumer");
    }

    protected void initJaxbMarshaller(Marshaller marshaller) throws JAXBException {
        if (this.marshallerProperties != null) {
            for (String name : this.marshallerProperties.keySet()) {
                marshaller.setProperty(name, this.marshallerProperties.get(name));
            }
        }
        if (this.marshallerListener != null) {
            marshaller.setListener(this.marshallerListener);
        }
        if (this.validationEventHandler != null) {
            marshaller.setEventHandler(this.validationEventHandler);
        }
        if (this.adapters != null) {
            for (XmlAdapter<?, ?> adapter : this.adapters) {
                marshaller.setAdapter(adapter);
            }
        }
        if (this.schema != null) {
            marshaller.setSchema(this.schema);
        }
    }

    @Override // org.springframework.oxm.Unmarshaller
    public Object unmarshal(Source source) throws XmlMappingException {
        return unmarshal(source, null);
    }

    @Override // org.springframework.oxm.mime.MimeUnmarshaller
    public Object unmarshal(Source source, MimeContainer mimeContainer) throws XmlMappingException, SAXException {
        Source source2 = processSource(source);
        try {
            Unmarshaller unmarshaller = createUnmarshaller();
            if (this.mtomEnabled && mimeContainer != null) {
                unmarshaller.setAttachmentUnmarshaller(new Jaxb2AttachmentUnmarshaller(mimeContainer));
            }
            if (StaxUtils.isStaxSource(source2)) {
                return unmarshalStaxSource(unmarshaller, source2);
            }
            if (this.mappedClass != null) {
                return unmarshaller.unmarshal(source2, this.mappedClass).getValue();
            }
            return unmarshaller.unmarshal(source2);
        } catch (JAXBException ex) {
            throw convertJaxbException(ex);
        } catch (NullPointerException ex2) {
            if (!isSupportDtd()) {
                throw new UnmarshallingFailureException("NPE while unmarshalling: This can happen due to the presence of DTD declarations which are disabled.", ex2);
            }
            throw ex2;
        }
    }

    protected Unmarshaller createUnmarshaller() {
        try {
            Unmarshaller unmarshaller = getJaxbContext().createUnmarshaller();
            initJaxbUnmarshaller(unmarshaller);
            return unmarshaller;
        } catch (JAXBException ex) {
            throw convertJaxbException(ex);
        }
    }

    protected Object unmarshalStaxSource(Unmarshaller jaxbUnmarshaller, Source staxSource) throws JAXBException {
        XMLStreamReader streamReader = StaxUtils.getXMLStreamReader(staxSource);
        if (streamReader != null) {
            if (this.mappedClass != null) {
                return jaxbUnmarshaller.unmarshal(streamReader, this.mappedClass).getValue();
            }
            return jaxbUnmarshaller.unmarshal(streamReader);
        }
        XMLEventReader eventReader = StaxUtils.getXMLEventReader(staxSource);
        if (eventReader != null) {
            if (this.mappedClass != null) {
                return jaxbUnmarshaller.unmarshal(eventReader, this.mappedClass).getValue();
            }
            return jaxbUnmarshaller.unmarshal(eventReader);
        }
        throw new IllegalArgumentException("StaxSource contains neither XMLStreamReader nor XMLEventReader");
    }

    private Source processSource(Source source) throws SAXException {
        if (StaxUtils.isStaxSource(source) || (source instanceof DOMSource)) {
            return source;
        }
        XMLReader xmlReader = null;
        InputSource inputSource = null;
        if (source instanceof SAXSource) {
            SAXSource saxSource = (SAXSource) source;
            xmlReader = saxSource.getXMLReader();
            inputSource = saxSource.getInputSource();
        } else if (source instanceof StreamSource) {
            StreamSource streamSource = (StreamSource) source;
            if (streamSource.getInputStream() != null) {
                inputSource = new InputSource(streamSource.getInputStream());
            } else if (streamSource.getReader() != null) {
                inputSource = new InputSource(streamSource.getReader());
            } else {
                inputSource = new InputSource(streamSource.getSystemId());
            }
        }
        if (xmlReader == null) {
            try {
                xmlReader = XMLReaderFactory.createXMLReader();
            } catch (SAXException ex) {
                this.logger.warn("Processing of external entities could not be disabled", ex);
                return source;
            }
        }
        xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", !isSupportDtd());
        xmlReader.setFeature("http://xml.org/sax/features/external-general-entities", isProcessExternalEntities());
        if (!isProcessExternalEntities()) {
            xmlReader.setEntityResolver(NO_OP_ENTITY_RESOLVER);
        }
        return new SAXSource(xmlReader, inputSource);
    }

    protected void initJaxbUnmarshaller(Unmarshaller unmarshaller) throws JAXBException {
        if (this.unmarshallerProperties != null) {
            for (String name : this.unmarshallerProperties.keySet()) {
                unmarshaller.setProperty(name, this.unmarshallerProperties.get(name));
            }
        }
        if (this.unmarshallerListener != null) {
            unmarshaller.setListener(this.unmarshallerListener);
        }
        if (this.validationEventHandler != null) {
            unmarshaller.setEventHandler(this.validationEventHandler);
        }
        if (this.adapters != null) {
            for (XmlAdapter<?, ?> adapter : this.adapters) {
                unmarshaller.setAdapter(adapter);
            }
        }
        if (this.schema != null) {
            unmarshaller.setSchema(this.schema);
        }
    }

    protected XmlMappingException convertJaxbException(JAXBException ex) {
        if (ex instanceof ValidationException) {
            return new ValidationFailureException("JAXB validation exception", ex);
        }
        if (ex instanceof MarshalException) {
            return new MarshallingFailureException("JAXB marshalling exception", ex);
        }
        if (ex instanceof UnmarshalException) {
            return new UnmarshallingFailureException("JAXB unmarshalling exception", ex);
        }
        return new UncategorizedMappingException("Unknown JAXB exception", ex);
    }

    /* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/jaxb/Jaxb2Marshaller$Jaxb2AttachmentMarshaller.class */
    private static class Jaxb2AttachmentMarshaller extends AttachmentMarshaller {
        private final MimeContainer mimeContainer;

        public Jaxb2AttachmentMarshaller(MimeContainer mimeContainer) {
            this.mimeContainer = mimeContainer;
        }

        public String addMtomAttachment(byte[] data, int offset, int length, String mimeType, String elementNamespace, String elementLocalName) {
            ByteArrayDataSource dataSource = new ByteArrayDataSource(mimeType, data, offset, length);
            return addMtomAttachment(new DataHandler(dataSource), elementNamespace, elementLocalName);
        }

        public String addMtomAttachment(DataHandler dataHandler, String elementNamespace, String elementLocalName) throws UnsupportedEncodingException {
            String host = getHost(elementNamespace, dataHandler);
            String contentId = UUID.randomUUID() + "@" + host;
            this.mimeContainer.addAttachment("<" + contentId + ">", dataHandler);
            try {
                contentId = URLEncoder.encode(contentId, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
            return Jaxb2Marshaller.CID + contentId;
        }

        private String getHost(String elementNamespace, DataHandler dataHandler) {
            try {
                URI uri = new URI(elementNamespace);
                return uri.getHost();
            } catch (URISyntaxException e) {
                return dataHandler.getName();
            }
        }

        public String addSwaRefAttachment(DataHandler dataHandler) {
            String contentId = UUID.randomUUID() + "@" + dataHandler.getName();
            this.mimeContainer.addAttachment(contentId, dataHandler);
            return contentId;
        }

        public boolean isXOPPackage() {
            return this.mimeContainer.convertToXopPackage();
        }
    }

    /* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/jaxb/Jaxb2Marshaller$Jaxb2AttachmentUnmarshaller.class */
    private static class Jaxb2AttachmentUnmarshaller extends AttachmentUnmarshaller {
        private final MimeContainer mimeContainer;

        public Jaxb2AttachmentUnmarshaller(MimeContainer mimeContainer) {
            this.mimeContainer = mimeContainer;
        }

        public byte[] getAttachmentAsByteArray(String cid) {
            try {
                DataHandler dataHandler = getAttachmentAsDataHandler(cid);
                return FileCopyUtils.copyToByteArray(dataHandler.getInputStream());
            } catch (IOException ex) {
                throw new UnmarshallingFailureException("Couldn't read attachment", ex);
            }
        }

        public DataHandler getAttachmentAsDataHandler(String contentId) throws UnsupportedEncodingException {
            if (contentId.startsWith(Jaxb2Marshaller.CID)) {
                String contentId2 = contentId.substring(Jaxb2Marshaller.CID.length());
                try {
                    contentId2 = URLDecoder.decode(contentId2, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                }
                contentId = '<' + contentId2 + '>';
            }
            return this.mimeContainer.getAttachment(contentId);
        }

        public boolean isXOPPackage() {
            return this.mimeContainer.isXopPackage();
        }
    }

    /* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/jaxb/Jaxb2Marshaller$ByteArrayDataSource.class */
    private static class ByteArrayDataSource implements DataSource {
        private final byte[] data;
        private final String contentType;
        private final int offset;
        private final int length;

        public ByteArrayDataSource(String contentType, byte[] data, int offset, int length) {
            this.contentType = contentType;
            this.data = data;
            this.offset = offset;
            this.length = length;
        }

        public InputStream getInputStream() {
            return new ByteArrayInputStream(this.data, this.offset, this.length);
        }

        public OutputStream getOutputStream() {
            throw new UnsupportedOperationException();
        }

        public String getContentType() {
            return this.contentType;
        }

        public String getName() {
            return "ByteArrayDataSource";
        }
    }
}
