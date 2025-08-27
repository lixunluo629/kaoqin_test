package org.springframework.util.xml;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.sax.SAXResult;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/xml/StaxResult.class */
class StaxResult extends SAXResult {
    private XMLEventWriter eventWriter;
    private XMLStreamWriter streamWriter;

    public StaxResult(XMLEventWriter eventWriter) {
        StaxEventHandler handler = new StaxEventHandler(eventWriter);
        super.setHandler(handler);
        super.setLexicalHandler(handler);
        this.eventWriter = eventWriter;
    }

    public StaxResult(XMLStreamWriter streamWriter) {
        StaxStreamHandler handler = new StaxStreamHandler(streamWriter);
        super.setHandler(handler);
        super.setLexicalHandler(handler);
        this.streamWriter = streamWriter;
    }

    public XMLEventWriter getXMLEventWriter() {
        return this.eventWriter;
    }

    public XMLStreamWriter getXMLStreamWriter() {
        return this.streamWriter;
    }

    @Override // javax.xml.transform.sax.SAXResult
    public void setHandler(ContentHandler handler) {
        throw new UnsupportedOperationException("setHandler is not supported");
    }

    @Override // javax.xml.transform.sax.SAXResult
    public void setLexicalHandler(LexicalHandler handler) {
        throw new UnsupportedOperationException("setLexicalHandler is not supported");
    }
}
