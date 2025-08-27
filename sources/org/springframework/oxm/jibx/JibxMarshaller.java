package org.springframework.oxm.jibx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.IXMLWriter;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.ValidationException;
import org.jibx.runtime.impl.MarshallingContext;
import org.jibx.runtime.impl.StAXReaderWrapper;
import org.jibx.runtime.impl.StAXWriter;
import org.jibx.runtime.impl.UnmarshallingContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.oxm.MarshallingFailureException;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.oxm.ValidationFailureException;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.support.AbstractMarshaller;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.StaxUtils;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/jibx/JibxMarshaller.class */
public class JibxMarshaller extends AbstractMarshaller implements InitializingBean {
    private static final String DEFAULT_BINDING_NAME = "binding";
    private Class<?> targetClass;
    private String targetPackage;
    private String bindingName;
    private Boolean standalone;
    private String docTypeRootElementName;
    private String docTypeSystemId;
    private String docTypePublicId;
    private String docTypeInternalSubset;
    private IBindingFactory bindingFactory;
    private int indent = -1;
    private String encoding = "UTF-8";
    private final TransformerFactory transformerFactory = TransformerFactory.newInstance();

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public void setBindingName(String bindingName) {
        this.bindingName = bindingName;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected String getDefaultEncoding() {
        return this.encoding;
    }

    public void setStandalone(Boolean standalone) {
        this.standalone = standalone;
    }

    public void setDocTypeRootElementName(String docTypeRootElementName) {
        this.docTypeRootElementName = docTypeRootElementName;
    }

    public void setDocTypeSystemId(String docTypeSystemId) {
        this.docTypeSystemId = docTypeSystemId;
    }

    public void setDocTypePublicId(String docTypePublicId) {
        this.docTypePublicId = docTypePublicId;
    }

    public void setDocTypeInternalSubset(String docTypeInternalSubset) {
        this.docTypeInternalSubset = docTypeInternalSubset;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws JiBXException {
        if (this.targetClass != null) {
            if (StringUtils.hasLength(this.bindingName)) {
                if (this.logger.isInfoEnabled()) {
                    this.logger.info("Configured for target class [" + this.targetClass + "] using binding [" + this.bindingName + "]");
                }
                this.bindingFactory = BindingDirectory.getFactory(this.bindingName, this.targetClass);
                return;
            } else {
                if (this.logger.isInfoEnabled()) {
                    this.logger.info("Configured for target class [" + this.targetClass + "]");
                }
                this.bindingFactory = BindingDirectory.getFactory(this.targetClass);
                return;
            }
        }
        if (this.targetPackage != null) {
            if (!StringUtils.hasLength(this.bindingName)) {
                this.bindingName = DEFAULT_BINDING_NAME;
            }
            if (this.logger.isInfoEnabled()) {
                this.logger.info("Configured for target package [" + this.targetPackage + "] using binding [" + this.bindingName + "]");
            }
            this.bindingFactory = BindingDirectory.getFactory(this.bindingName, this.targetPackage);
            return;
        }
        throw new IllegalArgumentException("Either 'targetClass' or 'targetPackage' is required");
    }

    @Override // org.springframework.oxm.Marshaller, org.springframework.oxm.Unmarshaller
    public boolean supports(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        if (this.targetClass != null) {
            return this.targetClass == clazz;
        }
        String[] mappedClasses = this.bindingFactory.getMappedClasses();
        String className = clazz.getName();
        for (String mappedClass : mappedClasses) {
            if (className.equals(mappedClass)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected void marshalOutputStream(Object graph, OutputStream outputStream) throws XmlMappingException, IOException {
        try {
            IMarshallingContext marshallingContext = createMarshallingContext();
            marshallingContext.startDocument(this.encoding, this.standalone, outputStream);
            marshalDocument(marshallingContext, graph);
        } catch (JiBXException ex) {
            throw convertJibxException(ex, true);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected void marshalWriter(Object graph, Writer writer) throws XmlMappingException, IOException {
        try {
            IMarshallingContext marshallingContext = createMarshallingContext();
            marshallingContext.startDocument(this.encoding, this.standalone, writer);
            marshalDocument(marshallingContext, graph);
        } catch (JiBXException ex) {
            throw convertJibxException(ex, true);
        }
    }

    private void marshalDocument(IMarshallingContext marshallingContext, Object graph) throws JiBXException, IOException {
        if (StringUtils.hasLength(this.docTypeRootElementName)) {
            IXMLWriter xmlWriter = marshallingContext.getXmlWriter();
            xmlWriter.writeDocType(this.docTypeRootElementName, this.docTypeSystemId, this.docTypePublicId, this.docTypeInternalSubset);
        }
        marshallingContext.marshalDocument(graph);
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected void marshalDomNode(Object graph, Node node) throws XmlMappingException, TransformerException {
        try {
            Result result = new DOMResult(node);
            transformAndMarshal(graph, result);
        } catch (IOException ex) {
            throw new MarshallingFailureException("JiBX marshalling exception", ex);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected void marshalXmlEventWriter(Object graph, XMLEventWriter eventWriter) throws XmlMappingException {
        XMLStreamWriter streamWriter = StaxUtils.createEventStreamWriter(eventWriter);
        marshalXmlStreamWriter(graph, streamWriter);
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected void marshalXmlStreamWriter(Object graph, XMLStreamWriter streamWriter) throws XmlMappingException {
        try {
            MarshallingContext marshallingContext = createMarshallingContext();
            marshallingContext.setXmlWriter(new StAXWriter(marshallingContext.getNamespaces(), streamWriter));
            marshallingContext.marshalDocument(graph);
        } catch (JiBXException ex) {
            throw convertJibxException(ex, false);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected void marshalSaxHandlers(Object graph, ContentHandler contentHandler, LexicalHandler lexicalHandler) throws XmlMappingException, TransformerException {
        try {
            SAXResult saxResult = new SAXResult(contentHandler);
            saxResult.setLexicalHandler(lexicalHandler);
            transformAndMarshal(graph, saxResult);
        } catch (IOException ex) {
            throw new MarshallingFailureException("JiBX marshalling exception", ex);
        }
    }

    private void transformAndMarshal(Object graph, Result result) throws XmlMappingException, TransformerException, IOException {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
            marshalOutputStream(graph, os);
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
            Transformer transformer = this.transformerFactory.newTransformer();
            transformer.transform(new StreamSource(is), result);
        } catch (TransformerException ex) {
            throw new MarshallingFailureException("Could not transform to [" + ClassUtils.getShortName(result.getClass()) + "]", ex);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected Object unmarshalXmlEventReader(XMLEventReader eventReader) {
        try {
            XMLStreamReader streamReader = StaxUtils.createEventStreamReader(eventReader);
            return unmarshalXmlStreamReader(streamReader);
        } catch (XMLStreamException ex) {
            return new UnmarshallingFailureException("JiBX unmarshalling exception", ex);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected Object unmarshalXmlStreamReader(XMLStreamReader streamReader) {
        try {
            UnmarshallingContext unmarshallingContext = createUnmarshallingContext();
            unmarshallingContext.setDocument(new StAXReaderWrapper(streamReader, (String) null, true));
            return unmarshallingContext.unmarshalElement();
        } catch (JiBXException ex) {
            throw convertJibxException(ex, false);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected Object unmarshalInputStream(InputStream inputStream) throws XmlMappingException, IOException {
        try {
            IUnmarshallingContext unmarshallingContext = createUnmarshallingContext();
            return unmarshallingContext.unmarshalDocument(inputStream, this.encoding);
        } catch (JiBXException ex) {
            throw convertJibxException(ex, false);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected Object unmarshalReader(Reader reader) throws XmlMappingException, IOException {
        try {
            IUnmarshallingContext unmarshallingContext = createUnmarshallingContext();
            return unmarshallingContext.unmarshalDocument(reader);
        } catch (JiBXException ex) {
            throw convertJibxException(ex, false);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected Object unmarshalDomNode(Node node) throws XmlMappingException {
        try {
            return transformAndUnmarshal(new DOMSource(node), null);
        } catch (IOException ex) {
            throw new UnmarshallingFailureException("JiBX unmarshalling exception", ex);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected Object unmarshalSaxReader(XMLReader xmlReader, InputSource inputSource) throws XmlMappingException, IOException {
        return transformAndUnmarshal(new SAXSource(xmlReader, inputSource), inputSource.getEncoding());
    }

    private Object transformAndUnmarshal(Source source, String encoding) throws TransformerException, IOException, IllegalArgumentException {
        try {
            Transformer transformer = this.transformerFactory.newTransformer();
            if (encoding != null) {
                transformer.setOutputProperty("encoding", encoding);
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
            transformer.transform(source, new StreamResult(os));
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
            return unmarshalInputStream(is);
        } catch (TransformerException ex) {
            throw new MarshallingFailureException("Could not transform from [" + ClassUtils.getShortName(source.getClass()) + "]", ex);
        }
    }

    protected IMarshallingContext createMarshallingContext() throws JiBXException {
        IMarshallingContext marshallingContext = this.bindingFactory.createMarshallingContext();
        marshallingContext.setIndent(this.indent);
        return marshallingContext;
    }

    protected IUnmarshallingContext createUnmarshallingContext() throws JiBXException {
        return this.bindingFactory.createUnmarshallingContext();
    }

    public XmlMappingException convertJibxException(JiBXException ex, boolean marshalling) {
        if (ex instanceof ValidationException) {
            return new ValidationFailureException("JiBX validation exception", ex);
        }
        if (marshalling) {
            return new MarshallingFailureException("JiBX marshalling exception", ex);
        }
        return new UnmarshallingFailureException("JiBX unmarshalling exception", ex);
    }
}
