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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STLangCode.class */
public interface STLangCode extends XmlHexBinary {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STLangCode.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stlangcode02bdtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STLangCode$Factory.class */
    public static final class Factory {
        public static STLangCode newValue(Object obj) {
            return (STLangCode) STLangCode.type.newValue(obj);
        }

        public static STLangCode newInstance() {
            return (STLangCode) POIXMLTypeLoader.newInstance(STLangCode.type, null);
        }

        public static STLangCode newInstance(XmlOptions xmlOptions) {
            return (STLangCode) POIXMLTypeLoader.newInstance(STLangCode.type, xmlOptions);
        }

        public static STLangCode parse(String str) throws XmlException {
            return (STLangCode) POIXMLTypeLoader.parse(str, STLangCode.type, (XmlOptions) null);
        }

        public static STLangCode parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STLangCode) POIXMLTypeLoader.parse(str, STLangCode.type, xmlOptions);
        }

        public static STLangCode parse(File file) throws XmlException, IOException {
            return (STLangCode) POIXMLTypeLoader.parse(file, STLangCode.type, (XmlOptions) null);
        }

        public static STLangCode parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLangCode) POIXMLTypeLoader.parse(file, STLangCode.type, xmlOptions);
        }

        public static STLangCode parse(URL url) throws XmlException, IOException {
            return (STLangCode) POIXMLTypeLoader.parse(url, STLangCode.type, (XmlOptions) null);
        }

        public static STLangCode parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLangCode) POIXMLTypeLoader.parse(url, STLangCode.type, xmlOptions);
        }

        public static STLangCode parse(InputStream inputStream) throws XmlException, IOException {
            return (STLangCode) POIXMLTypeLoader.parse(inputStream, STLangCode.type, (XmlOptions) null);
        }

        public static STLangCode parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLangCode) POIXMLTypeLoader.parse(inputStream, STLangCode.type, xmlOptions);
        }

        public static STLangCode parse(Reader reader) throws XmlException, IOException {
            return (STLangCode) POIXMLTypeLoader.parse(reader, STLangCode.type, (XmlOptions) null);
        }

        public static STLangCode parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLangCode) POIXMLTypeLoader.parse(reader, STLangCode.type, xmlOptions);
        }

        public static STLangCode parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STLangCode) POIXMLTypeLoader.parse(xMLStreamReader, STLangCode.type, (XmlOptions) null);
        }

        public static STLangCode parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STLangCode) POIXMLTypeLoader.parse(xMLStreamReader, STLangCode.type, xmlOptions);
        }

        public static STLangCode parse(Node node) throws XmlException {
            return (STLangCode) POIXMLTypeLoader.parse(node, STLangCode.type, (XmlOptions) null);
        }

        public static STLangCode parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STLangCode) POIXMLTypeLoader.parse(node, STLangCode.type, xmlOptions);
        }

        public static STLangCode parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STLangCode) POIXMLTypeLoader.parse(xMLInputStream, STLangCode.type, (XmlOptions) null);
        }

        public static STLangCode parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STLangCode) POIXMLTypeLoader.parse(xMLInputStream, STLangCode.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STLangCode.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STLangCode.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
