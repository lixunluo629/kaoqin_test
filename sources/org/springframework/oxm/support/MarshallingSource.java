package org.springframework.oxm.support;

import java.io.IOException;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.util.Assert;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/support/MarshallingSource.class */
public class MarshallingSource extends SAXSource {
    private final Marshaller marshaller;
    private final Object content;

    public MarshallingSource(Marshaller marshaller, Object content) {
        super(new MarshallingXMLReader(marshaller, content), new InputSource());
        Assert.notNull(marshaller, "'marshaller' must not be null");
        Assert.notNull(content, "'content' must not be null");
        this.marshaller = marshaller;
        this.content = content;
    }

    public Marshaller getMarshaller() {
        return this.marshaller;
    }

    public Object getContent() {
        return this.content;
    }

    @Override // javax.xml.transform.sax.SAXSource
    public void setInputSource(InputSource inputSource) {
        throw new UnsupportedOperationException("setInputSource is not supported");
    }

    @Override // javax.xml.transform.sax.SAXSource
    public void setXMLReader(XMLReader reader) {
        throw new UnsupportedOperationException("setXMLReader is not supported");
    }

    /* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/support/MarshallingSource$MarshallingXMLReader.class */
    private static class MarshallingXMLReader implements XMLReader {
        private final Marshaller marshaller;
        private final Object content;
        private DTDHandler dtdHandler;
        private ContentHandler contentHandler;
        private EntityResolver entityResolver;
        private ErrorHandler errorHandler;
        private LexicalHandler lexicalHandler;

        private MarshallingXMLReader(Marshaller marshaller, Object content) {
            Assert.notNull(marshaller, "'marshaller' must not be null");
            Assert.notNull(content, "'content' must not be null");
            this.marshaller = marshaller;
            this.content = content;
        }

        @Override // org.xml.sax.XMLReader
        public void setContentHandler(ContentHandler contentHandler) {
            this.contentHandler = contentHandler;
        }

        @Override // org.xml.sax.XMLReader
        public ContentHandler getContentHandler() {
            return this.contentHandler;
        }

        @Override // org.xml.sax.XMLReader
        public void setDTDHandler(DTDHandler dtdHandler) {
            this.dtdHandler = dtdHandler;
        }

        @Override // org.xml.sax.XMLReader
        public DTDHandler getDTDHandler() {
            return this.dtdHandler;
        }

        @Override // org.xml.sax.XMLReader
        public void setEntityResolver(EntityResolver entityResolver) {
            this.entityResolver = entityResolver;
        }

        @Override // org.xml.sax.XMLReader
        public EntityResolver getEntityResolver() {
            return this.entityResolver;
        }

        @Override // org.xml.sax.XMLReader
        public void setErrorHandler(ErrorHandler errorHandler) {
            this.errorHandler = errorHandler;
        }

        @Override // org.xml.sax.XMLReader
        public ErrorHandler getErrorHandler() {
            return this.errorHandler;
        }

        protected LexicalHandler getLexicalHandler() {
            return this.lexicalHandler;
        }

        @Override // org.xml.sax.XMLReader
        public boolean getFeature(String name) throws SAXNotRecognizedException {
            throw new SAXNotRecognizedException(name);
        }

        @Override // org.xml.sax.XMLReader
        public void setFeature(String name, boolean value) throws SAXNotRecognizedException {
            throw new SAXNotRecognizedException(name);
        }

        @Override // org.xml.sax.XMLReader
        public Object getProperty(String name) throws SAXNotRecognizedException {
            if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
                return this.lexicalHandler;
            }
            throw new SAXNotRecognizedException(name);
        }

        @Override // org.xml.sax.XMLReader
        public void setProperty(String name, Object value) throws SAXNotRecognizedException {
            if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
                this.lexicalHandler = (LexicalHandler) value;
                return;
            }
            throw new SAXNotRecognizedException(name);
        }

        @Override // org.xml.sax.XMLReader
        public void parse(InputSource input) throws XmlMappingException, SAXException {
            parse();
        }

        @Override // org.xml.sax.XMLReader
        public void parse(String systemId) throws XmlMappingException, SAXException {
            parse();
        }

        private void parse() throws XmlMappingException, SAXException {
            SAXResult result = new SAXResult(getContentHandler());
            result.setLexicalHandler(getLexicalHandler());
            try {
                this.marshaller.marshal(this.content, result);
            } catch (IOException ex) {
                SAXParseException saxException = new SAXParseException(ex.getMessage(), null, null, -1, -1, ex);
                ErrorHandler errorHandler = getErrorHandler();
                if (errorHandler != null) {
                    errorHandler.fatalError(saxException);
                    return;
                }
                throw saxException;
            }
        }
    }
}
