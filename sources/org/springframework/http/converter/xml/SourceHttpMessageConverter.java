package org.springframework.http.converter.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/xml/SourceHttpMessageConverter.class */
public class SourceHttpMessageConverter<T extends Source> extends AbstractHttpMessageConverter<T> {
    private static final Set<Class<?>> SUPPORTED_CLASSES = new HashSet(5);
    private final TransformerFactory transformerFactory;
    private boolean supportDtd;
    private boolean processExternalEntities;
    private static final EntityResolver NO_OP_ENTITY_RESOLVER;
    private static final XMLResolver NO_OP_XML_RESOLVER;

    static {
        SUPPORTED_CLASSES.add(DOMSource.class);
        SUPPORTED_CLASSES.add(SAXSource.class);
        SUPPORTED_CLASSES.add(StAXSource.class);
        SUPPORTED_CLASSES.add(StreamSource.class);
        SUPPORTED_CLASSES.add(Source.class);
        NO_OP_ENTITY_RESOLVER = new EntityResolver() { // from class: org.springframework.http.converter.xml.SourceHttpMessageConverter.1
            @Override // org.xml.sax.EntityResolver
            public InputSource resolveEntity(String publicId, String systemId) {
                return new InputSource(new StringReader(""));
            }
        };
        NO_OP_XML_RESOLVER = new XMLResolver() { // from class: org.springframework.http.converter.xml.SourceHttpMessageConverter.2
            @Override // javax.xml.stream.XMLResolver
            public Object resolveEntity(String publicID, String systemID, String base, String ns) {
                return StreamUtils.emptyInput();
            }
        };
    }

    public SourceHttpMessageConverter() {
        super(MediaType.APPLICATION_XML, MediaType.TEXT_XML, new MediaType("application", "*+xml"));
        this.transformerFactory = TransformerFactory.newInstance();
        this.supportDtd = false;
        this.processExternalEntities = false;
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

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    public boolean supports(Class<?> clazz) {
        return SUPPORTED_CLASSES.contains(clazz);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    public T readInternal(Class<? extends T> cls, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        InputStream body = httpInputMessage.getBody();
        if (DOMSource.class == cls) {
            return readDOMSource(body);
        }
        if (SAXSource.class == cls) {
            return readSAXSource(body);
        }
        if (StAXSource.class == cls) {
            return (T) readStAXSource(body);
        }
        if (StreamSource.class == cls || Source.class == cls) {
            return readStreamSource(body);
        }
        throw new HttpMessageConversionException("Could not read class [" + cls + "]. Only DOMSource, SAXSource, StAXSource, and StreamSource are supported.");
    }

    private DOMSource readDOMSource(InputStream body) throws ParserConfigurationException, SAXException, IOException {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", !isSupportDtd());
            documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", isProcessExternalEntities());
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            if (!isProcessExternalEntities()) {
                documentBuilder.setEntityResolver(NO_OP_ENTITY_RESOLVER);
            }
            Document document = documentBuilder.parse(body);
            return new DOMSource(document);
        } catch (NullPointerException ex) {
            if (!isSupportDtd()) {
                throw new HttpMessageNotReadableException("NPE while unmarshalling: This can happen due to the presence of DTD declarations which are disabled.", ex);
            }
            throw ex;
        } catch (ParserConfigurationException ex2) {
            throw new HttpMessageNotReadableException("Could not set feature: " + ex2.getMessage(), ex2);
        } catch (SAXException ex3) {
            throw new HttpMessageNotReadableException("Could not parse document: " + ex3.getMessage(), ex3);
        }
    }

    private SAXSource readSAXSource(InputStream body) throws SAXException, IOException {
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", !isSupportDtd());
            xmlReader.setFeature("http://xml.org/sax/features/external-general-entities", isProcessExternalEntities());
            if (!isProcessExternalEntities()) {
                xmlReader.setEntityResolver(NO_OP_ENTITY_RESOLVER);
            }
            byte[] bytes = StreamUtils.copyToByteArray(body);
            return new SAXSource(xmlReader, new InputSource(new ByteArrayInputStream(bytes)));
        } catch (SAXException ex) {
            throw new HttpMessageNotReadableException("Could not parse document: " + ex.getMessage(), ex);
        }
    }

    private Source readStAXSource(InputStream body) throws IllegalArgumentException, FactoryConfigurationError {
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.valueOf(isSupportDtd()));
            inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.valueOf(isProcessExternalEntities()));
            if (!isProcessExternalEntities()) {
                inputFactory.setXMLResolver(NO_OP_XML_RESOLVER);
            }
            XMLStreamReader streamReader = inputFactory.createXMLStreamReader(body);
            return new StAXSource(streamReader);
        } catch (XMLStreamException ex) {
            throw new HttpMessageNotReadableException("Could not parse document: " + ex.getMessage(), ex);
        }
    }

    private StreamSource readStreamSource(InputStream body) throws IOException {
        byte[] bytes = StreamUtils.copyToByteArray(body);
        return new StreamSource(new ByteArrayInputStream(bytes));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    public Long getContentLength(T t, MediaType contentType) {
        if (t instanceof DOMSource) {
            try {
                CountingOutputStream os = new CountingOutputStream();
                transform(t, new StreamResult(os));
                return Long.valueOf(os.count);
            } catch (TransformerException e) {
                return null;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    public void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        try {
            Result result = new StreamResult(outputMessage.getBody());
            transform(t, result);
        } catch (TransformerException ex) {
            throw new HttpMessageNotWritableException("Could not transform [" + t + "] to output message", ex);
        }
    }

    private void transform(Source source, Result result) throws TransformerException {
        this.transformerFactory.newTransformer().transform(source, result);
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/xml/SourceHttpMessageConverter$CountingOutputStream.class */
    private static class CountingOutputStream extends OutputStream {
        long count;

        private CountingOutputStream() {
            this.count = 0L;
        }

        @Override // java.io.OutputStream
        public void write(int b) throws IOException {
            this.count++;
        }

        @Override // java.io.OutputStream
        public void write(byte[] b) throws IOException {
            this.count += b.length;
        }

        @Override // java.io.OutputStream
        public void write(byte[] b, int off, int len) throws IOException {
            this.count += len;
        }
    }
}
