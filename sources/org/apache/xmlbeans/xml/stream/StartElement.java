package org.apache.xmlbeans.xml.stream;

import java.util.Map;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/xml/stream/StartElement.class */
public interface StartElement extends XMLEvent {
    AttributeIterator getAttributes();

    AttributeIterator getNamespaces();

    AttributeIterator getAttributesAndNamespaces();

    Attribute getAttributeByName(XMLName xMLName);

    String getNamespaceUri(String str);

    Map getNamespaceMap();
}
