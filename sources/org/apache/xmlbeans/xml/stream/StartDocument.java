package org.apache.xmlbeans.xml.stream;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/xml/stream/StartDocument.class */
public interface StartDocument extends XMLEvent {
    String getSystemId();

    String getCharacterEncodingScheme();

    boolean isStandalone();

    String getVersion();
}
