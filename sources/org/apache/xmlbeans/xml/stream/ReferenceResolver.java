package org.apache.xmlbeans.xml.stream;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/xml/stream/ReferenceResolver.class */
public interface ReferenceResolver {
    XMLInputStream resolve(String str) throws XMLStreamException;

    String getId(String str);
}
