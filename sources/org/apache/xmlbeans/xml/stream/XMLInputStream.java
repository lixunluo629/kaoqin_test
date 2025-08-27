package org.apache.xmlbeans.xml.stream;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/xml/stream/XMLInputStream.class */
public interface XMLInputStream {
    XMLEvent next() throws XMLStreamException;

    boolean hasNext() throws XMLStreamException;

    void skip() throws XMLStreamException;

    void skipElement() throws XMLStreamException;

    XMLEvent peek() throws XMLStreamException;

    boolean skip(int i) throws XMLStreamException;

    boolean skip(XMLName xMLName) throws XMLStreamException;

    boolean skip(XMLName xMLName, int i) throws XMLStreamException;

    XMLInputStream getSubStream() throws XMLStreamException;

    void close() throws XMLStreamException;

    ReferenceResolver getReferenceResolver();

    void setReferenceResolver(ReferenceResolver referenceResolver);
}
