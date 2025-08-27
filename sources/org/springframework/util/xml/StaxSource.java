package org.springframework.util.xml;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/xml/StaxSource.class */
class StaxSource extends SAXSource {
    private XMLEventReader eventReader;
    private XMLStreamReader streamReader;

    StaxSource(XMLEventReader eventReader) {
        super(new StaxEventXMLReader(eventReader), new InputSource());
        this.eventReader = eventReader;
    }

    StaxSource(XMLStreamReader streamReader) {
        super(new StaxStreamXMLReader(streamReader), new InputSource());
        this.streamReader = streamReader;
    }

    XMLEventReader getXMLEventReader() {
        return this.eventReader;
    }

    XMLStreamReader getXMLStreamReader() {
        return this.streamReader;
    }

    @Override // javax.xml.transform.sax.SAXSource
    public void setInputSource(InputSource inputSource) {
        throw new UnsupportedOperationException("setInputSource is not supported");
    }

    @Override // javax.xml.transform.sax.SAXSource
    public void setXMLReader(XMLReader reader) {
        throw new UnsupportedOperationException("setXMLReader is not supported");
    }
}
