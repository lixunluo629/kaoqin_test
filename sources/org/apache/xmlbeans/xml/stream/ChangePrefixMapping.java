package org.apache.xmlbeans.xml.stream;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/xml/stream/ChangePrefixMapping.class */
public interface ChangePrefixMapping extends XMLEvent {
    String getOldNamespaceUri();

    String getNewNamespaceUri();

    String getPrefix();
}
