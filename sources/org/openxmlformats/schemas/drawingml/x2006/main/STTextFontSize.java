package org.openxmlformats.schemas.drawingml.x2006.main;

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
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextFontSize.class */
public interface STTextFontSize extends XmlInt {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STTextFontSize.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("sttextfontsizeb3a8type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextFontSize$Factory.class */
    public static final class Factory {
        public static STTextFontSize newValue(Object obj) {
            return (STTextFontSize) STTextFontSize.type.newValue(obj);
        }

        public static STTextFontSize newInstance() {
            return (STTextFontSize) POIXMLTypeLoader.newInstance(STTextFontSize.type, null);
        }

        public static STTextFontSize newInstance(XmlOptions xmlOptions) {
            return (STTextFontSize) POIXMLTypeLoader.newInstance(STTextFontSize.type, xmlOptions);
        }

        public static STTextFontSize parse(String str) throws XmlException {
            return (STTextFontSize) POIXMLTypeLoader.parse(str, STTextFontSize.type, (XmlOptions) null);
        }

        public static STTextFontSize parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STTextFontSize) POIXMLTypeLoader.parse(str, STTextFontSize.type, xmlOptions);
        }

        public static STTextFontSize parse(File file) throws XmlException, IOException {
            return (STTextFontSize) POIXMLTypeLoader.parse(file, STTextFontSize.type, (XmlOptions) null);
        }

        public static STTextFontSize parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextFontSize) POIXMLTypeLoader.parse(file, STTextFontSize.type, xmlOptions);
        }

        public static STTextFontSize parse(URL url) throws XmlException, IOException {
            return (STTextFontSize) POIXMLTypeLoader.parse(url, STTextFontSize.type, (XmlOptions) null);
        }

        public static STTextFontSize parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextFontSize) POIXMLTypeLoader.parse(url, STTextFontSize.type, xmlOptions);
        }

        public static STTextFontSize parse(InputStream inputStream) throws XmlException, IOException {
            return (STTextFontSize) POIXMLTypeLoader.parse(inputStream, STTextFontSize.type, (XmlOptions) null);
        }

        public static STTextFontSize parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextFontSize) POIXMLTypeLoader.parse(inputStream, STTextFontSize.type, xmlOptions);
        }

        public static STTextFontSize parse(Reader reader) throws XmlException, IOException {
            return (STTextFontSize) POIXMLTypeLoader.parse(reader, STTextFontSize.type, (XmlOptions) null);
        }

        public static STTextFontSize parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextFontSize) POIXMLTypeLoader.parse(reader, STTextFontSize.type, xmlOptions);
        }

        public static STTextFontSize parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STTextFontSize) POIXMLTypeLoader.parse(xMLStreamReader, STTextFontSize.type, (XmlOptions) null);
        }

        public static STTextFontSize parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STTextFontSize) POIXMLTypeLoader.parse(xMLStreamReader, STTextFontSize.type, xmlOptions);
        }

        public static STTextFontSize parse(Node node) throws XmlException {
            return (STTextFontSize) POIXMLTypeLoader.parse(node, STTextFontSize.type, (XmlOptions) null);
        }

        public static STTextFontSize parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STTextFontSize) POIXMLTypeLoader.parse(node, STTextFontSize.type, xmlOptions);
        }

        public static STTextFontSize parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STTextFontSize) POIXMLTypeLoader.parse(xMLInputStream, STTextFontSize.type, (XmlOptions) null);
        }

        public static STTextFontSize parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STTextFontSize) POIXMLTypeLoader.parse(xMLInputStream, STTextFontSize.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTextFontSize.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTextFontSize.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
