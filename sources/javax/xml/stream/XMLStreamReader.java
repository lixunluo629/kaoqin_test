package javax.xml.stream;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

/* loaded from: stax-api-1.0.1.jar:javax/xml/stream/XMLStreamReader.class */
public interface XMLStreamReader extends XMLStreamConstants {
    Object getProperty(String str) throws IllegalArgumentException;

    int next() throws XMLStreamException;

    void require(int i, String str, String str2) throws XMLStreamException;

    String getElementText() throws XMLStreamException;

    int nextTag() throws XMLStreamException;

    boolean hasNext() throws XMLStreamException;

    void close() throws XMLStreamException;

    String getNamespaceURI(String str);

    boolean isStartElement();

    boolean isEndElement();

    boolean isCharacters();

    boolean isWhiteSpace();

    String getAttributeValue(String str, String str2);

    int getAttributeCount();

    QName getAttributeName(int i);

    String getAttributeNamespace(int i);

    String getAttributeLocalName(int i);

    String getAttributePrefix(int i);

    String getAttributeType(int i);

    String getAttributeValue(int i);

    boolean isAttributeSpecified(int i);

    int getNamespaceCount();

    String getNamespacePrefix(int i);

    String getNamespaceURI(int i);

    NamespaceContext getNamespaceContext();

    int getEventType();

    String getText();

    char[] getTextCharacters();

    int getTextCharacters(int i, char[] cArr, int i2, int i3) throws XMLStreamException;

    int getTextStart();

    int getTextLength();

    String getEncoding();

    boolean hasText();

    Location getLocation();

    QName getName();

    String getLocalName();

    boolean hasName();

    String getNamespaceURI();

    String getPrefix();

    String getVersion();

    boolean isStandalone();

    boolean standaloneSet();

    String getCharacterEncodingScheme();

    String getPITarget();

    String getPIData();
}
