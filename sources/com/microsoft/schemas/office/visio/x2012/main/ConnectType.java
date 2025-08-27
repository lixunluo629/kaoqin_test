package com.microsoft.schemas.office.visio.x2012.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/ConnectType.class */
public interface ConnectType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(ConnectType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("connecttypeea41type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/ConnectType$Factory.class */
    public static final class Factory {
        public static ConnectType newInstance() {
            return (ConnectType) POIXMLTypeLoader.newInstance(ConnectType.type, null);
        }

        public static ConnectType newInstance(XmlOptions xmlOptions) {
            return (ConnectType) POIXMLTypeLoader.newInstance(ConnectType.type, xmlOptions);
        }

        public static ConnectType parse(String str) throws XmlException {
            return (ConnectType) POIXMLTypeLoader.parse(str, ConnectType.type, (XmlOptions) null);
        }

        public static ConnectType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (ConnectType) POIXMLTypeLoader.parse(str, ConnectType.type, xmlOptions);
        }

        public static ConnectType parse(File file) throws XmlException, IOException {
            return (ConnectType) POIXMLTypeLoader.parse(file, ConnectType.type, (XmlOptions) null);
        }

        public static ConnectType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (ConnectType) POIXMLTypeLoader.parse(file, ConnectType.type, xmlOptions);
        }

        public static ConnectType parse(URL url) throws XmlException, IOException {
            return (ConnectType) POIXMLTypeLoader.parse(url, ConnectType.type, (XmlOptions) null);
        }

        public static ConnectType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (ConnectType) POIXMLTypeLoader.parse(url, ConnectType.type, xmlOptions);
        }

        public static ConnectType parse(InputStream inputStream) throws XmlException, IOException {
            return (ConnectType) POIXMLTypeLoader.parse(inputStream, ConnectType.type, (XmlOptions) null);
        }

        public static ConnectType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (ConnectType) POIXMLTypeLoader.parse(inputStream, ConnectType.type, xmlOptions);
        }

        public static ConnectType parse(Reader reader) throws XmlException, IOException {
            return (ConnectType) POIXMLTypeLoader.parse(reader, ConnectType.type, (XmlOptions) null);
        }

        public static ConnectType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (ConnectType) POIXMLTypeLoader.parse(reader, ConnectType.type, xmlOptions);
        }

        public static ConnectType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (ConnectType) POIXMLTypeLoader.parse(xMLStreamReader, ConnectType.type, (XmlOptions) null);
        }

        public static ConnectType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (ConnectType) POIXMLTypeLoader.parse(xMLStreamReader, ConnectType.type, xmlOptions);
        }

        public static ConnectType parse(Node node) throws XmlException {
            return (ConnectType) POIXMLTypeLoader.parse(node, ConnectType.type, (XmlOptions) null);
        }

        public static ConnectType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (ConnectType) POIXMLTypeLoader.parse(node, ConnectType.type, xmlOptions);
        }

        public static ConnectType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (ConnectType) POIXMLTypeLoader.parse(xMLInputStream, ConnectType.type, (XmlOptions) null);
        }

        public static ConnectType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (ConnectType) POIXMLTypeLoader.parse(xMLInputStream, ConnectType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, ConnectType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, ConnectType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    long getFromSheet();

    XmlUnsignedInt xgetFromSheet();

    void setFromSheet(long j);

    void xsetFromSheet(XmlUnsignedInt xmlUnsignedInt);

    String getFromCell();

    XmlString xgetFromCell();

    boolean isSetFromCell();

    void setFromCell(String str);

    void xsetFromCell(XmlString xmlString);

    void unsetFromCell();

    int getFromPart();

    XmlInt xgetFromPart();

    boolean isSetFromPart();

    void setFromPart(int i);

    void xsetFromPart(XmlInt xmlInt);

    void unsetFromPart();

    long getToSheet();

    XmlUnsignedInt xgetToSheet();

    void setToSheet(long j);

    void xsetToSheet(XmlUnsignedInt xmlUnsignedInt);

    String getToCell();

    XmlString xgetToCell();

    boolean isSetToCell();

    void setToCell(String str);

    void xsetToCell(XmlString xmlString);

    void unsetToCell();

    int getToPart();

    XmlInt xgetToPart();

    boolean isSetToPart();

    void setToPart(int i);

    void xsetToPart(XmlInt xmlInt);

    void unsetToPart();
}
