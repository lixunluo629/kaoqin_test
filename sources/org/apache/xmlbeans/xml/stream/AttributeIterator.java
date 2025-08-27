package org.apache.xmlbeans.xml.stream;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/xml/stream/AttributeIterator.class */
public interface AttributeIterator {
    Attribute next();

    boolean hasNext();

    Attribute peek();

    void skip();
}
