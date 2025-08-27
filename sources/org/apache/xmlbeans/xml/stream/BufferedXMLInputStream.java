package org.apache.xmlbeans.xml.stream;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/xml/stream/BufferedXMLInputStream.class */
public interface BufferedXMLInputStream extends XMLInputStream {
    void mark() throws XMLStreamException;

    void reset() throws XMLStreamException;
}
