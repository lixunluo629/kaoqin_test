package org.springframework.oxm.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.oxm.XmlMappingException;
import org.springframework.util.Assert;
import org.springframework.util.xml.StaxUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/support/AbstractMarshaller.class */
public abstract class AbstractMarshaller implements Marshaller, Unmarshaller {
    private DocumentBuilderFactory documentBuilderFactory;
    private static final EntityResolver NO_OP_ENTITY_RESOLVER = new EntityResolver() { // from class: org.springframework.oxm.support.AbstractMarshaller.1
        @Override // org.xml.sax.EntityResolver
        public InputSource resolveEntity(String publicId, String systemId) {
            return new InputSource(new StringReader(""));
        }
    };
    protected final Log logger = LogFactory.getLog(getClass());
    private boolean supportDtd = false;
    private boolean processExternalEntities = false;
    private final Object documentBuilderFactoryMonitor = new Object();

    protected abstract void marshalDomNode(Object obj, Node node) throws XmlMappingException;

    protected abstract void marshalXmlEventWriter(Object obj, XMLEventWriter xMLEventWriter) throws XmlMappingException;

    protected abstract void marshalXmlStreamWriter(Object obj, XMLStreamWriter xMLStreamWriter) throws XmlMappingException;

    protected abstract void marshalSaxHandlers(Object obj, ContentHandler contentHandler, LexicalHandler lexicalHandler) throws XmlMappingException;

    protected abstract void marshalOutputStream(Object obj, OutputStream outputStream) throws XmlMappingException, IOException;

    protected abstract void marshalWriter(Object obj, Writer writer) throws XmlMappingException, IOException;

    protected abstract Object unmarshalDomNode(Node node) throws XmlMappingException;

    protected abstract Object unmarshalXmlEventReader(XMLEventReader xMLEventReader) throws XmlMappingException;

    protected abstract Object unmarshalXmlStreamReader(XMLStreamReader xMLStreamReader) throws XmlMappingException;

    protected abstract Object unmarshalSaxReader(XMLReader xMLReader, InputSource inputSource) throws XmlMappingException, IOException;

    protected abstract Object unmarshalInputStream(InputStream inputStream) throws XmlMappingException, IOException;

    protected abstract Object unmarshalReader(Reader reader) throws XmlMappingException, IOException;

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

    protected Document buildDocument() {
        DocumentBuilder documentBuilder;
        try {
            synchronized (this.documentBuilderFactoryMonitor) {
                if (this.documentBuilderFactory == null) {
                    this.documentBuilderFactory = createDocumentBuilderFactory();
                }
                documentBuilder = createDocumentBuilder(this.documentBuilderFactory);
            }
            return documentBuilder.newDocument();
        } catch (ParserConfigurationException ex) {
            throw new UnmarshallingFailureException("Could not create document placeholder: " + ex.getMessage(), ex);
        }
    }

    protected DocumentBuilderFactory createDocumentBuilderFactory() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", !isSupportDtd());
        factory.setFeature("http://xml.org/sax/features/external-general-entities", isProcessExternalEntities());
        return factory;
    }

    protected DocumentBuilder createDocumentBuilder(DocumentBuilderFactory factory) throws ParserConfigurationException {
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        if (!isProcessExternalEntities()) {
            documentBuilder.setEntityResolver(NO_OP_ENTITY_RESOLVER);
        }
        return documentBuilder;
    }

    protected XMLReader createXmlReader() throws SAXException {
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", !isSupportDtd());
        xmlReader.setFeature("http://xml.org/sax/features/external-general-entities", isProcessExternalEntities());
        if (!isProcessExternalEntities()) {
            xmlReader.setEntityResolver(NO_OP_ENTITY_RESOLVER);
        }
        return xmlReader;
    }

    protected String getDefaultEncoding() {
        return null;
    }

    @Override // org.springframework.oxm.Marshaller
    public final void marshal(Object graph, Result result) throws XmlMappingException, IOException {
        if (result instanceof DOMResult) {
            marshalDomResult(graph, (DOMResult) result);
            return;
        }
        if (StaxUtils.isStaxResult(result)) {
            marshalStaxResult(graph, result);
        } else if (result instanceof SAXResult) {
            marshalSaxResult(graph, (SAXResult) result);
        } else {
            if (result instanceof StreamResult) {
                marshalStreamResult(graph, (StreamResult) result);
                return;
            }
            throw new IllegalArgumentException("Unknown Result type: " + result.getClass());
        }
    }

    protected void marshalDomResult(Object graph, DOMResult domResult) throws XmlMappingException {
        if (domResult.getNode() == null) {
            domResult.setNode(buildDocument());
        }
        marshalDomNode(graph, domResult.getNode());
    }

    protected void marshalStaxResult(Object graph, Result staxResult) throws XmlMappingException {
        XMLStreamWriter streamWriter = StaxUtils.getXMLStreamWriter(staxResult);
        if (streamWriter != null) {
            marshalXmlStreamWriter(graph, streamWriter);
            return;
        }
        XMLEventWriter eventWriter = StaxUtils.getXMLEventWriter(staxResult);
        if (eventWriter != null) {
            marshalXmlEventWriter(graph, eventWriter);
            return;
        }
        throw new IllegalArgumentException("StaxResult contains neither XMLStreamWriter nor XMLEventConsumer");
    }

    protected void marshalSaxResult(Object graph, SAXResult saxResult) throws XmlMappingException {
        ContentHandler contentHandler = saxResult.getHandler();
        Assert.notNull(contentHandler, "ContentHandler not set on SAXResult");
        LexicalHandler lexicalHandler = saxResult.getLexicalHandler();
        marshalSaxHandlers(graph, contentHandler, lexicalHandler);
    }

    protected void marshalStreamResult(Object graph, StreamResult streamResult) throws XmlMappingException, IOException {
        if (streamResult.getOutputStream() != null) {
            marshalOutputStream(graph, streamResult.getOutputStream());
        } else {
            if (streamResult.getWriter() != null) {
                marshalWriter(graph, streamResult.getWriter());
                return;
            }
            throw new IllegalArgumentException("StreamResult contains neither OutputStream nor Writer");
        }
    }

    @Override // org.springframework.oxm.Unmarshaller
    public final Object unmarshal(Source source) throws XmlMappingException, IOException {
        if (source instanceof DOMSource) {
            return unmarshalDomSource((DOMSource) source);
        }
        if (StaxUtils.isStaxSource(source)) {
            return unmarshalStaxSource(source);
        }
        if (source instanceof SAXSource) {
            return unmarshalSaxSource((SAXSource) source);
        }
        if (source instanceof StreamSource) {
            return unmarshalStreamSource((StreamSource) source);
        }
        throw new IllegalArgumentException("Unknown Source type: " + source.getClass());
    }

    protected Object unmarshalDomSource(DOMSource domSource) throws XmlMappingException {
        if (domSource.getNode() == null) {
            domSource.setNode(buildDocument());
        }
        try {
            return unmarshalDomNode(domSource.getNode());
        } catch (NullPointerException ex) {
            if (!isSupportDtd()) {
                throw new UnmarshallingFailureException("NPE while unmarshalling. This can happen on JDK 1.6 due to the presence of DTD declarations, which are disabled.", ex);
            }
            throw ex;
        }
    }

    protected Object unmarshalStaxSource(Source staxSource) throws XmlMappingException {
        XMLStreamReader streamReader = StaxUtils.getXMLStreamReader(staxSource);
        if (streamReader != null) {
            return unmarshalXmlStreamReader(streamReader);
        }
        XMLEventReader eventReader = StaxUtils.getXMLEventReader(staxSource);
        if (eventReader != null) {
            return unmarshalXmlEventReader(eventReader);
        }
        throw new IllegalArgumentException("StaxSource contains neither XMLStreamReader nor XMLEventReader");
    }

    protected Object unmarshalSaxSource(SAXSource saxSource) throws XmlMappingException, IOException {
        if (saxSource.getXMLReader() == null) {
            try {
                saxSource.setXMLReader(createXmlReader());
            } catch (SAXException ex) {
                throw new UnmarshallingFailureException("Could not create XMLReader for SAXSource", ex);
            }
        }
        if (saxSource.getInputSource() == null) {
            saxSource.setInputSource(new InputSource());
        }
        try {
            return unmarshalSaxReader(saxSource.getXMLReader(), saxSource.getInputSource());
        } catch (NullPointerException ex2) {
            if (!isSupportDtd()) {
                throw new UnmarshallingFailureException("NPE while unmarshalling. This can happen on JDK 1.6 due to the presence of DTD declarations, which are disabled.");
            }
            throw ex2;
        }
    }

    protected Object unmarshalStreamSource(StreamSource streamSource) throws XmlMappingException, IOException {
        if (streamSource.getInputStream() != null) {
            if (isProcessExternalEntities() && isSupportDtd()) {
                return unmarshalInputStream(streamSource.getInputStream());
            }
            InputSource inputSource = new InputSource(streamSource.getInputStream());
            inputSource.setEncoding(getDefaultEncoding());
            return unmarshalSaxSource(new SAXSource(inputSource));
        }
        if (streamSource.getReader() != null) {
            if (isProcessExternalEntities() && isSupportDtd()) {
                return unmarshalReader(streamSource.getReader());
            }
            return unmarshalSaxSource(new SAXSource(new InputSource(streamSource.getReader())));
        }
        return unmarshalSaxSource(new SAXSource(new InputSource(streamSource.getSystemId())));
    }
}
