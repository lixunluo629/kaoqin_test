package org.openxmlformats.schemas.wordprocessingml.x2006.main;

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
import org.apache.xmlbeans.XmlHexBinary;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STLongHexNumber.class */
public interface STLongHexNumber extends XmlHexBinary {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STLongHexNumber.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stlonghexnumberd6batype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STLongHexNumber$Factory.class */
    public static final class Factory {
        public static STLongHexNumber newValue(Object obj) {
            return (STLongHexNumber) STLongHexNumber.type.newValue(obj);
        }

        public static STLongHexNumber newInstance() {
            return (STLongHexNumber) POIXMLTypeLoader.newInstance(STLongHexNumber.type, null);
        }

        public static STLongHexNumber newInstance(XmlOptions xmlOptions) {
            return (STLongHexNumber) POIXMLTypeLoader.newInstance(STLongHexNumber.type, xmlOptions);
        }

        public static STLongHexNumber parse(String str) throws XmlException {
            return (STLongHexNumber) POIXMLTypeLoader.parse(str, STLongHexNumber.type, (XmlOptions) null);
        }

        public static STLongHexNumber parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STLongHexNumber) POIXMLTypeLoader.parse(str, STLongHexNumber.type, xmlOptions);
        }

        public static STLongHexNumber parse(File file) throws XmlException, IOException {
            return (STLongHexNumber) POIXMLTypeLoader.parse(file, STLongHexNumber.type, (XmlOptions) null);
        }

        public static STLongHexNumber parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLongHexNumber) POIXMLTypeLoader.parse(file, STLongHexNumber.type, xmlOptions);
        }

        public static STLongHexNumber parse(URL url) throws XmlException, IOException {
            return (STLongHexNumber) POIXMLTypeLoader.parse(url, STLongHexNumber.type, (XmlOptions) null);
        }

        public static STLongHexNumber parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLongHexNumber) POIXMLTypeLoader.parse(url, STLongHexNumber.type, xmlOptions);
        }

        public static STLongHexNumber parse(InputStream inputStream) throws XmlException, IOException {
            return (STLongHexNumber) POIXMLTypeLoader.parse(inputStream, STLongHexNumber.type, (XmlOptions) null);
        }

        public static STLongHexNumber parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLongHexNumber) POIXMLTypeLoader.parse(inputStream, STLongHexNumber.type, xmlOptions);
        }

        public static STLongHexNumber parse(Reader reader) throws XmlException, IOException {
            return (STLongHexNumber) POIXMLTypeLoader.parse(reader, STLongHexNumber.type, (XmlOptions) null);
        }

        public static STLongHexNumber parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLongHexNumber) POIXMLTypeLoader.parse(reader, STLongHexNumber.type, xmlOptions);
        }

        public static STLongHexNumber parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STLongHexNumber) POIXMLTypeLoader.parse(xMLStreamReader, STLongHexNumber.type, (XmlOptions) null);
        }

        public static STLongHexNumber parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STLongHexNumber) POIXMLTypeLoader.parse(xMLStreamReader, STLongHexNumber.type, xmlOptions);
        }

        public static STLongHexNumber parse(Node node) throws XmlException {
            return (STLongHexNumber) POIXMLTypeLoader.parse(node, STLongHexNumber.type, (XmlOptions) null);
        }

        public static STLongHexNumber parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STLongHexNumber) POIXMLTypeLoader.parse(node, STLongHexNumber.type, xmlOptions);
        }

        public static STLongHexNumber parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STLongHexNumber) POIXMLTypeLoader.parse(xMLInputStream, STLongHexNumber.type, (XmlOptions) null);
        }

        public static STLongHexNumber parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STLongHexNumber) POIXMLTypeLoader.parse(xMLInputStream, STLongHexNumber.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STLongHexNumber.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STLongHexNumber.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
