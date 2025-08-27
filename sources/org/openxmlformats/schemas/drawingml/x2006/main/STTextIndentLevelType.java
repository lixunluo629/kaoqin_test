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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextIndentLevelType.class */
public interface STTextIndentLevelType extends XmlInt {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STTextIndentLevelType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("sttextindentleveltypeaf86type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextIndentLevelType$Factory.class */
    public static final class Factory {
        public static STTextIndentLevelType newValue(Object obj) {
            return (STTextIndentLevelType) STTextIndentLevelType.type.newValue(obj);
        }

        public static STTextIndentLevelType newInstance() {
            return (STTextIndentLevelType) POIXMLTypeLoader.newInstance(STTextIndentLevelType.type, null);
        }

        public static STTextIndentLevelType newInstance(XmlOptions xmlOptions) {
            return (STTextIndentLevelType) POIXMLTypeLoader.newInstance(STTextIndentLevelType.type, xmlOptions);
        }

        public static STTextIndentLevelType parse(String str) throws XmlException {
            return (STTextIndentLevelType) POIXMLTypeLoader.parse(str, STTextIndentLevelType.type, (XmlOptions) null);
        }

        public static STTextIndentLevelType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STTextIndentLevelType) POIXMLTypeLoader.parse(str, STTextIndentLevelType.type, xmlOptions);
        }

        public static STTextIndentLevelType parse(File file) throws XmlException, IOException {
            return (STTextIndentLevelType) POIXMLTypeLoader.parse(file, STTextIndentLevelType.type, (XmlOptions) null);
        }

        public static STTextIndentLevelType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextIndentLevelType) POIXMLTypeLoader.parse(file, STTextIndentLevelType.type, xmlOptions);
        }

        public static STTextIndentLevelType parse(URL url) throws XmlException, IOException {
            return (STTextIndentLevelType) POIXMLTypeLoader.parse(url, STTextIndentLevelType.type, (XmlOptions) null);
        }

        public static STTextIndentLevelType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextIndentLevelType) POIXMLTypeLoader.parse(url, STTextIndentLevelType.type, xmlOptions);
        }

        public static STTextIndentLevelType parse(InputStream inputStream) throws XmlException, IOException {
            return (STTextIndentLevelType) POIXMLTypeLoader.parse(inputStream, STTextIndentLevelType.type, (XmlOptions) null);
        }

        public static STTextIndentLevelType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextIndentLevelType) POIXMLTypeLoader.parse(inputStream, STTextIndentLevelType.type, xmlOptions);
        }

        public static STTextIndentLevelType parse(Reader reader) throws XmlException, IOException {
            return (STTextIndentLevelType) POIXMLTypeLoader.parse(reader, STTextIndentLevelType.type, (XmlOptions) null);
        }

        public static STTextIndentLevelType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextIndentLevelType) POIXMLTypeLoader.parse(reader, STTextIndentLevelType.type, xmlOptions);
        }

        public static STTextIndentLevelType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STTextIndentLevelType) POIXMLTypeLoader.parse(xMLStreamReader, STTextIndentLevelType.type, (XmlOptions) null);
        }

        public static STTextIndentLevelType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STTextIndentLevelType) POIXMLTypeLoader.parse(xMLStreamReader, STTextIndentLevelType.type, xmlOptions);
        }

        public static STTextIndentLevelType parse(Node node) throws XmlException {
            return (STTextIndentLevelType) POIXMLTypeLoader.parse(node, STTextIndentLevelType.type, (XmlOptions) null);
        }

        public static STTextIndentLevelType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STTextIndentLevelType) POIXMLTypeLoader.parse(node, STTextIndentLevelType.type, xmlOptions);
        }

        public static STTextIndentLevelType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STTextIndentLevelType) POIXMLTypeLoader.parse(xMLInputStream, STTextIndentLevelType.type, (XmlOptions) null);
        }

        public static STTextIndentLevelType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STTextIndentLevelType) POIXMLTypeLoader.parse(xMLInputStream, STTextIndentLevelType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTextIndentLevelType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTextIndentLevelType.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
