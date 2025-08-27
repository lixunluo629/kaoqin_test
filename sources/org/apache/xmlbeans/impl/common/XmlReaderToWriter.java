package org.apache.xmlbeans.impl.common;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/XmlReaderToWriter.class */
public final class XmlReaderToWriter {
    private XmlReaderToWriter() {
    }

    public static void writeAll(XMLStreamReader xmlr, XMLStreamWriter writer) throws XMLStreamException {
        while (xmlr.hasNext()) {
            write(xmlr, writer);
            xmlr.next();
        }
        write(xmlr, writer);
        writer.flush();
    }

    public static void write(XMLStreamReader xmlr, XMLStreamWriter writer) throws XMLStreamException {
        switch (xmlr.getEventType()) {
            case 1:
                String localName = xmlr.getLocalName();
                String namespaceURI = xmlr.getNamespaceURI();
                if (namespaceURI != null && namespaceURI.length() > 0) {
                    String prefix = xmlr.getPrefix();
                    if (prefix != null) {
                        writer.writeStartElement(prefix, localName, namespaceURI);
                    } else {
                        writer.writeStartElement(namespaceURI, localName);
                    }
                } else {
                    writer.writeStartElement(localName);
                }
                int len = xmlr.getNamespaceCount();
                for (int i = 0; i < len; i++) {
                    writer.writeNamespace(xmlr.getNamespacePrefix(i), xmlr.getNamespaceURI(i));
                }
                int len2 = xmlr.getAttributeCount();
                for (int i2 = 0; i2 < len2; i2++) {
                    String attUri = xmlr.getAttributeNamespace(i2);
                    if (attUri != null) {
                        writer.writeAttribute(attUri, xmlr.getAttributeLocalName(i2), xmlr.getAttributeValue(i2));
                    } else {
                        writer.writeAttribute(xmlr.getAttributeLocalName(i2), xmlr.getAttributeValue(i2));
                    }
                }
                break;
            case 2:
                writer.writeEndElement();
                break;
            case 3:
                writer.writeProcessingInstruction(xmlr.getPITarget(), xmlr.getPIData());
                break;
            case 4:
            case 6:
                writer.writeCharacters(xmlr.getTextCharacters(), xmlr.getTextStart(), xmlr.getTextLength());
                break;
            case 5:
                writer.writeComment(xmlr.getText());
                break;
            case 7:
                String encoding = xmlr.getCharacterEncodingScheme();
                String version = xmlr.getVersion();
                if (encoding != null && version != null) {
                    writer.writeStartDocument(encoding, version);
                    break;
                } else if (version != null) {
                    writer.writeStartDocument(xmlr.getVersion());
                    break;
                }
                break;
            case 8:
                writer.writeEndDocument();
                break;
            case 9:
                writer.writeEntityRef(xmlr.getLocalName());
                break;
            case 11:
                writer.writeDTD(xmlr.getText());
                break;
            case 12:
                writer.writeCData(xmlr.getText());
                break;
        }
    }
}
