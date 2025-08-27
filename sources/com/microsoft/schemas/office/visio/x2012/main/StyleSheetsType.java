package com.microsoft.schemas.office.visio.x2012.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/StyleSheetsType.class */
public interface StyleSheetsType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(StyleSheetsType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stylesheetstypeb706type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/visio/x2012/main/StyleSheetsType$Factory.class */
    public static final class Factory {
        public static StyleSheetsType newInstance() {
            return (StyleSheetsType) POIXMLTypeLoader.newInstance(StyleSheetsType.type, null);
        }

        public static StyleSheetsType newInstance(XmlOptions xmlOptions) {
            return (StyleSheetsType) POIXMLTypeLoader.newInstance(StyleSheetsType.type, xmlOptions);
        }

        public static StyleSheetsType parse(String str) throws XmlException {
            return (StyleSheetsType) POIXMLTypeLoader.parse(str, StyleSheetsType.type, (XmlOptions) null);
        }

        public static StyleSheetsType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (StyleSheetsType) POIXMLTypeLoader.parse(str, StyleSheetsType.type, xmlOptions);
        }

        public static StyleSheetsType parse(File file) throws XmlException, IOException {
            return (StyleSheetsType) POIXMLTypeLoader.parse(file, StyleSheetsType.type, (XmlOptions) null);
        }

        public static StyleSheetsType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (StyleSheetsType) POIXMLTypeLoader.parse(file, StyleSheetsType.type, xmlOptions);
        }

        public static StyleSheetsType parse(URL url) throws XmlException, IOException {
            return (StyleSheetsType) POIXMLTypeLoader.parse(url, StyleSheetsType.type, (XmlOptions) null);
        }

        public static StyleSheetsType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (StyleSheetsType) POIXMLTypeLoader.parse(url, StyleSheetsType.type, xmlOptions);
        }

        public static StyleSheetsType parse(InputStream inputStream) throws XmlException, IOException {
            return (StyleSheetsType) POIXMLTypeLoader.parse(inputStream, StyleSheetsType.type, (XmlOptions) null);
        }

        public static StyleSheetsType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (StyleSheetsType) POIXMLTypeLoader.parse(inputStream, StyleSheetsType.type, xmlOptions);
        }

        public static StyleSheetsType parse(Reader reader) throws XmlException, IOException {
            return (StyleSheetsType) POIXMLTypeLoader.parse(reader, StyleSheetsType.type, (XmlOptions) null);
        }

        public static StyleSheetsType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (StyleSheetsType) POIXMLTypeLoader.parse(reader, StyleSheetsType.type, xmlOptions);
        }

        public static StyleSheetsType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (StyleSheetsType) POIXMLTypeLoader.parse(xMLStreamReader, StyleSheetsType.type, (XmlOptions) null);
        }

        public static StyleSheetsType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (StyleSheetsType) POIXMLTypeLoader.parse(xMLStreamReader, StyleSheetsType.type, xmlOptions);
        }

        public static StyleSheetsType parse(Node node) throws XmlException {
            return (StyleSheetsType) POIXMLTypeLoader.parse(node, StyleSheetsType.type, (XmlOptions) null);
        }

        public static StyleSheetsType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (StyleSheetsType) POIXMLTypeLoader.parse(node, StyleSheetsType.type, xmlOptions);
        }

        public static StyleSheetsType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (StyleSheetsType) POIXMLTypeLoader.parse(xMLInputStream, StyleSheetsType.type, (XmlOptions) null);
        }

        public static StyleSheetsType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (StyleSheetsType) POIXMLTypeLoader.parse(xMLInputStream, StyleSheetsType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, StyleSheetsType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, StyleSheetsType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<StyleSheetType> getStyleSheetList();

    StyleSheetType[] getStyleSheetArray();

    StyleSheetType getStyleSheetArray(int i);

    int sizeOfStyleSheetArray();

    void setStyleSheetArray(StyleSheetType[] styleSheetTypeArr);

    void setStyleSheetArray(int i, StyleSheetType styleSheetType);

    StyleSheetType insertNewStyleSheet(int i);

    StyleSheetType addNewStyleSheet();

    void removeStyleSheet(int i);
}
