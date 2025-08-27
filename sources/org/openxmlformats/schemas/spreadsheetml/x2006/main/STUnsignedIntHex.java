package org.openxmlformats.schemas.spreadsheetml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STUnsignedIntHex.class */
public interface STUnsignedIntHex extends XmlHexBinary {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STUnsignedIntHex.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stunsignedinthex27datype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STUnsignedIntHex$Factory.class */
    public static final class Factory {
        public static STUnsignedIntHex newValue(Object obj) {
            return (STUnsignedIntHex) STUnsignedIntHex.type.newValue(obj);
        }

        public static STUnsignedIntHex newInstance() {
            return (STUnsignedIntHex) POIXMLTypeLoader.newInstance(STUnsignedIntHex.type, null);
        }

        public static STUnsignedIntHex newInstance(XmlOptions xmlOptions) {
            return (STUnsignedIntHex) POIXMLTypeLoader.newInstance(STUnsignedIntHex.type, xmlOptions);
        }

        public static STUnsignedIntHex parse(String str) throws XmlException {
            return (STUnsignedIntHex) POIXMLTypeLoader.parse(str, STUnsignedIntHex.type, (XmlOptions) null);
        }

        public static STUnsignedIntHex parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STUnsignedIntHex) POIXMLTypeLoader.parse(str, STUnsignedIntHex.type, xmlOptions);
        }

        public static STUnsignedIntHex parse(File file) throws XmlException, IOException {
            return (STUnsignedIntHex) POIXMLTypeLoader.parse(file, STUnsignedIntHex.type, (XmlOptions) null);
        }

        public static STUnsignedIntHex parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STUnsignedIntHex) POIXMLTypeLoader.parse(file, STUnsignedIntHex.type, xmlOptions);
        }

        public static STUnsignedIntHex parse(URL url) throws XmlException, IOException {
            return (STUnsignedIntHex) POIXMLTypeLoader.parse(url, STUnsignedIntHex.type, (XmlOptions) null);
        }

        public static STUnsignedIntHex parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STUnsignedIntHex) POIXMLTypeLoader.parse(url, STUnsignedIntHex.type, xmlOptions);
        }

        public static STUnsignedIntHex parse(InputStream inputStream) throws XmlException, IOException {
            return (STUnsignedIntHex) POIXMLTypeLoader.parse(inputStream, STUnsignedIntHex.type, (XmlOptions) null);
        }

        public static STUnsignedIntHex parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STUnsignedIntHex) POIXMLTypeLoader.parse(inputStream, STUnsignedIntHex.type, xmlOptions);
        }

        public static STUnsignedIntHex parse(Reader reader) throws XmlException, IOException {
            return (STUnsignedIntHex) POIXMLTypeLoader.parse(reader, STUnsignedIntHex.type, (XmlOptions) null);
        }

        public static STUnsignedIntHex parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STUnsignedIntHex) POIXMLTypeLoader.parse(reader, STUnsignedIntHex.type, xmlOptions);
        }

        public static STUnsignedIntHex parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STUnsignedIntHex) POIXMLTypeLoader.parse(xMLStreamReader, STUnsignedIntHex.type, (XmlOptions) null);
        }

        public static STUnsignedIntHex parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STUnsignedIntHex) POIXMLTypeLoader.parse(xMLStreamReader, STUnsignedIntHex.type, xmlOptions);
        }

        public static STUnsignedIntHex parse(Node node) throws XmlException {
            return (STUnsignedIntHex) POIXMLTypeLoader.parse(node, STUnsignedIntHex.type, (XmlOptions) null);
        }

        public static STUnsignedIntHex parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STUnsignedIntHex) POIXMLTypeLoader.parse(node, STUnsignedIntHex.type, xmlOptions);
        }

        public static STUnsignedIntHex parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STUnsignedIntHex) POIXMLTypeLoader.parse(xMLInputStream, STUnsignedIntHex.type, (XmlOptions) null);
        }

        public static STUnsignedIntHex parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STUnsignedIntHex) POIXMLTypeLoader.parse(xMLInputStream, STUnsignedIntHex.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STUnsignedIntHex.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STUnsignedIntHex.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
