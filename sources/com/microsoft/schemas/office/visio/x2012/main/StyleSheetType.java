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
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/StyleSheetType.class */
public interface StyleSheetType extends SheetType {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(StyleSheetType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stylesheettypeebcbtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/StyleSheetType$Factory.class */
    public static final class Factory {
        public static StyleSheetType newInstance() {
            return (StyleSheetType) POIXMLTypeLoader.newInstance(StyleSheetType.type, null);
        }

        public static StyleSheetType newInstance(XmlOptions xmlOptions) {
            return (StyleSheetType) POIXMLTypeLoader.newInstance(StyleSheetType.type, xmlOptions);
        }

        public static StyleSheetType parse(String str) throws XmlException {
            return (StyleSheetType) POIXMLTypeLoader.parse(str, StyleSheetType.type, (XmlOptions) null);
        }

        public static StyleSheetType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (StyleSheetType) POIXMLTypeLoader.parse(str, StyleSheetType.type, xmlOptions);
        }

        public static StyleSheetType parse(File file) throws XmlException, IOException {
            return (StyleSheetType) POIXMLTypeLoader.parse(file, StyleSheetType.type, (XmlOptions) null);
        }

        public static StyleSheetType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (StyleSheetType) POIXMLTypeLoader.parse(file, StyleSheetType.type, xmlOptions);
        }

        public static StyleSheetType parse(URL url) throws XmlException, IOException {
            return (StyleSheetType) POIXMLTypeLoader.parse(url, StyleSheetType.type, (XmlOptions) null);
        }

        public static StyleSheetType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (StyleSheetType) POIXMLTypeLoader.parse(url, StyleSheetType.type, xmlOptions);
        }

        public static StyleSheetType parse(InputStream inputStream) throws XmlException, IOException {
            return (StyleSheetType) POIXMLTypeLoader.parse(inputStream, StyleSheetType.type, (XmlOptions) null);
        }

        public static StyleSheetType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (StyleSheetType) POIXMLTypeLoader.parse(inputStream, StyleSheetType.type, xmlOptions);
        }

        public static StyleSheetType parse(Reader reader) throws XmlException, IOException {
            return (StyleSheetType) POIXMLTypeLoader.parse(reader, StyleSheetType.type, (XmlOptions) null);
        }

        public static StyleSheetType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (StyleSheetType) POIXMLTypeLoader.parse(reader, StyleSheetType.type, xmlOptions);
        }

        public static StyleSheetType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (StyleSheetType) POIXMLTypeLoader.parse(xMLStreamReader, StyleSheetType.type, (XmlOptions) null);
        }

        public static StyleSheetType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (StyleSheetType) POIXMLTypeLoader.parse(xMLStreamReader, StyleSheetType.type, xmlOptions);
        }

        public static StyleSheetType parse(Node node) throws XmlException {
            return (StyleSheetType) POIXMLTypeLoader.parse(node, StyleSheetType.type, (XmlOptions) null);
        }

        public static StyleSheetType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (StyleSheetType) POIXMLTypeLoader.parse(node, StyleSheetType.type, xmlOptions);
        }

        public static StyleSheetType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (StyleSheetType) POIXMLTypeLoader.parse(xMLInputStream, StyleSheetType.type, (XmlOptions) null);
        }

        public static StyleSheetType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (StyleSheetType) POIXMLTypeLoader.parse(xMLInputStream, StyleSheetType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, StyleSheetType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, StyleSheetType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    long getID();

    XmlUnsignedInt xgetID();

    void setID(long j);

    void xsetID(XmlUnsignedInt xmlUnsignedInt);

    String getName();

    XmlString xgetName();

    boolean isSetName();

    void setName(String str);

    void xsetName(XmlString xmlString);

    void unsetName();

    String getNameU();

    XmlString xgetNameU();

    boolean isSetNameU();

    void setNameU(String str);

    void xsetNameU(XmlString xmlString);

    void unsetNameU();

    boolean getIsCustomName();

    XmlBoolean xgetIsCustomName();

    boolean isSetIsCustomName();

    void setIsCustomName(boolean z);

    void xsetIsCustomName(XmlBoolean xmlBoolean);

    void unsetIsCustomName();

    boolean getIsCustomNameU();

    XmlBoolean xgetIsCustomNameU();

    boolean isSetIsCustomNameU();

    void setIsCustomNameU(boolean z);

    void xsetIsCustomNameU(XmlBoolean xmlBoolean);

    void unsetIsCustomNameU();
}
