package org.springframework.oxm.xmlbeans;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.apache.xmlbeans.XMLStreamValidationException;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlSaxHandler;
import org.apache.xmlbeans.XmlValidationError;
import org.springframework.oxm.MarshallingFailureException;
import org.springframework.oxm.UncategorizedMappingException;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.oxm.ValidationFailureException;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.support.AbstractMarshaller;
import org.springframework.util.xml.StaxUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

@Deprecated
/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/xmlbeans/XmlBeansMarshaller.class */
public class XmlBeansMarshaller extends AbstractMarshaller {
    private XmlOptions xmlOptions;
    private boolean validating = false;

    public void setXmlOptions(XmlOptions xmlOptions) {
        this.xmlOptions = xmlOptions;
    }

    public XmlOptions getXmlOptions() {
        return this.xmlOptions;
    }

    public void setValidating(boolean validating) {
        this.validating = validating;
    }

    public boolean isValidating() {
        return this.validating;
    }

    @Override // org.springframework.oxm.Marshaller, org.springframework.oxm.Unmarshaller
    public boolean supports(Class<?> clazz) {
        return XmlObject.class.isAssignableFrom(clazz);
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected void marshalDomNode(Object graph, Node node) throws XmlMappingException, DOMException {
        Document document = node.getNodeType() == 9 ? (Document) node : node.getOwnerDocument();
        Node xmlBeansNode = ((XmlObject) graph).newDomNode(getXmlOptions());
        NodeList xmlBeansChildNodes = xmlBeansNode.getChildNodes();
        for (int i = 0; i < xmlBeansChildNodes.getLength(); i++) {
            Node xmlBeansChildNode = xmlBeansChildNodes.item(i);
            Node importedNode = document.importNode(xmlBeansChildNode, true);
            node.appendChild(importedNode);
        }
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
        ContentHandler contentHandler = StaxUtils.createContentHandler(streamWriter);
        LexicalHandler lexicalHandler = null;
        if (contentHandler instanceof LexicalHandler) {
            lexicalHandler = (LexicalHandler) contentHandler;
        }
        marshalSaxHandlers(graph, contentHandler, lexicalHandler);
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected void marshalSaxHandlers(Object graph, ContentHandler contentHandler, LexicalHandler lexicalHandler) throws XmlMappingException {
        try {
            ((XmlObject) graph).save(contentHandler, lexicalHandler, getXmlOptions());
        } catch (SAXException ex) {
            throw convertXmlBeansException(ex, true);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected void marshalOutputStream(Object graph, OutputStream outputStream) throws XmlMappingException, IOException {
        ((XmlObject) graph).save(outputStream, getXmlOptions());
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected void marshalWriter(Object graph, Writer writer) throws XmlMappingException, IOException {
        ((XmlObject) graph).save(writer, getXmlOptions());
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected Object unmarshalDomNode(Node node) throws XmlMappingException {
        try {
            XmlObject object = XmlObject.Factory.parse(node, getXmlOptions());
            validate(object);
            return object;
        } catch (XmlException ex) {
            throw convertXmlBeansException(ex, false);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected Object unmarshalXmlEventReader(XMLEventReader eventReader) throws XmlMappingException {
        XMLReader reader = StaxUtils.createXMLReader(eventReader);
        try {
            return unmarshalSaxReader(reader, new InputSource());
        } catch (IOException ex) {
            throw convertXmlBeansException(ex, false);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected Object unmarshalXmlStreamReader(XMLStreamReader streamReader) throws XmlMappingException {
        try {
            XmlObject object = XmlObject.Factory.parse(streamReader, getXmlOptions());
            validate(object);
            return object;
        } catch (XmlException ex) {
            throw convertXmlBeansException(ex, false);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected Object unmarshalSaxReader(XMLReader xmlReader, InputSource inputSource) throws XmlMappingException, SAXException, IOException {
        XmlSaxHandler saxHandler = XmlObject.Factory.newXmlSaxHandler(getXmlOptions());
        xmlReader.setContentHandler(saxHandler.getContentHandler());
        try {
            xmlReader.setProperty("http://xml.org/sax/properties/lexical-handler", saxHandler.getLexicalHandler());
        } catch (SAXNotRecognizedException e) {
        } catch (SAXNotSupportedException e2) {
        }
        try {
            xmlReader.parse(inputSource);
            XmlObject object = saxHandler.getObject();
            validate(object);
            return object;
        } catch (XmlException ex) {
            throw convertXmlBeansException(ex, false);
        } catch (SAXException ex2) {
            throw convertXmlBeansException(ex2, false);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected Object unmarshalInputStream(InputStream inputStream) throws XmlMappingException, IOException {
        try {
            InputStream nonClosingInputStream = new NonClosingInputStream(inputStream);
            XmlObject object = XmlObject.Factory.parse(nonClosingInputStream, getXmlOptions());
            validate(object);
            return object;
        } catch (XmlException ex) {
            throw convertXmlBeansException(ex, false);
        }
    }

    @Override // org.springframework.oxm.support.AbstractMarshaller
    protected Object unmarshalReader(Reader reader) throws XmlMappingException, IOException {
        try {
            Reader nonClosingReader = new NonClosingReader(reader);
            XmlObject object = XmlObject.Factory.parse(nonClosingReader, getXmlOptions());
            validate(object);
            return object;
        } catch (XmlException ex) {
            throw convertXmlBeansException(ex, false);
        }
    }

    protected void validate(XmlObject object) throws ValidationFailureException {
        if (isValidating() && object != null) {
            XmlOptions validateOptions = getXmlOptions();
            if (validateOptions == null) {
                validateOptions = new XmlOptions();
            }
            List<XmlError> errorsList = new ArrayList<>();
            validateOptions.setErrorListener(errorsList);
            if (!object.validate(validateOptions)) {
                StringBuilder sb = new StringBuilder("Failed to validate XmlObject: ");
                boolean first = true;
                for (XmlError error : errorsList) {
                    if (error instanceof XmlValidationError) {
                        if (!first) {
                            sb.append("; ");
                        }
                        sb.append(error.toString());
                        first = false;
                    }
                }
                throw new ValidationFailureException("XMLBeans validation failure", new XmlException(sb.toString(), (Throwable) null, errorsList));
            }
        }
    }

    protected XmlMappingException convertXmlBeansException(Exception ex, boolean marshalling) {
        if (ex instanceof XMLStreamValidationException) {
            return new ValidationFailureException("XMLBeans validation exception", ex);
        }
        if ((ex instanceof XmlException) || (ex instanceof SAXException)) {
            if (marshalling) {
                return new MarshallingFailureException("XMLBeans marshalling exception", ex);
            }
            return new UnmarshallingFailureException("XMLBeans unmarshalling exception", ex);
        }
        return new UncategorizedMappingException("Unknown XMLBeans exception", ex);
    }

    /* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/xmlbeans/XmlBeansMarshaller$NonClosingInputStream.class */
    private static class NonClosingInputStream extends InputStream {
        private final WeakReference<InputStream> in;

        public NonClosingInputStream(InputStream in) {
            this.in = new WeakReference<>(in);
        }

        private InputStream getInputStream() {
            return this.in.get();
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            InputStream in = getInputStream();
            if (in != null) {
                return in.read();
            }
            return -1;
        }

        @Override // java.io.InputStream
        public int read(byte[] b) throws IOException {
            InputStream in = getInputStream();
            if (in != null) {
                return in.read(b);
            }
            return -1;
        }

        @Override // java.io.InputStream
        public int read(byte[] b, int off, int len) throws IOException {
            InputStream in = getInputStream();
            if (in != null) {
                return in.read(b, off, len);
            }
            return -1;
        }

        @Override // java.io.InputStream
        public long skip(long n) throws IOException {
            InputStream in = getInputStream();
            if (in != null) {
                return in.skip(n);
            }
            return 0L;
        }

        @Override // java.io.InputStream
        public boolean markSupported() {
            InputStream in = getInputStream();
            return in != null && in.markSupported();
        }

        @Override // java.io.InputStream
        public void mark(int readlimit) {
            InputStream in = getInputStream();
            if (in != null) {
                in.mark(readlimit);
            }
        }

        @Override // java.io.InputStream
        public void reset() throws IOException {
            InputStream in = getInputStream();
            if (in != null) {
                in.reset();
            }
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            InputStream in = getInputStream();
            if (in != null) {
                return in.available();
            }
            return 0;
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            InputStream in = getInputStream();
            if (in != null) {
                this.in.clear();
            }
        }
    }

    /* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/xmlbeans/XmlBeansMarshaller$NonClosingReader.class */
    private static class NonClosingReader extends Reader {
        private final WeakReference<Reader> reader;

        public NonClosingReader(Reader reader) {
            this.reader = new WeakReference<>(reader);
        }

        private Reader getReader() {
            return this.reader.get();
        }

        @Override // java.io.Reader, java.lang.Readable
        public int read(CharBuffer target) throws IOException {
            Reader rdr = getReader();
            if (rdr != null) {
                return rdr.read(target);
            }
            return -1;
        }

        @Override // java.io.Reader
        public int read() throws IOException {
            Reader rdr = getReader();
            if (rdr != null) {
                return rdr.read();
            }
            return -1;
        }

        @Override // java.io.Reader
        public int read(char[] cbuf) throws IOException {
            Reader rdr = getReader();
            if (rdr != null) {
                return rdr.read(cbuf);
            }
            return -1;
        }

        @Override // java.io.Reader
        public int read(char[] cbuf, int off, int len) throws IOException {
            Reader rdr = getReader();
            if (rdr != null) {
                return rdr.read(cbuf, off, len);
            }
            return -1;
        }

        @Override // java.io.Reader
        public long skip(long n) throws IOException {
            Reader rdr = getReader();
            if (rdr != null) {
                return rdr.skip(n);
            }
            return 0L;
        }

        @Override // java.io.Reader
        public boolean ready() throws IOException {
            Reader rdr = getReader();
            return rdr != null && rdr.ready();
        }

        @Override // java.io.Reader
        public boolean markSupported() {
            Reader rdr = getReader();
            return rdr != null && rdr.markSupported();
        }

        @Override // java.io.Reader
        public void mark(int readAheadLimit) throws IOException {
            Reader rdr = getReader();
            if (rdr != null) {
                rdr.mark(readAheadLimit);
            }
        }

        @Override // java.io.Reader
        public void reset() throws IOException {
            Reader rdr = getReader();
            if (rdr != null) {
                rdr.reset();
            }
        }

        @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            Reader rdr = getReader();
            if (rdr != null) {
                this.reader.clear();
            }
        }
    }
}
