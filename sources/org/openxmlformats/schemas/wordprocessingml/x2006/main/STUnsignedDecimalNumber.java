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
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedLong;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STUnsignedDecimalNumber.class */
public interface STUnsignedDecimalNumber extends XmlUnsignedLong {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STUnsignedDecimalNumber.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stunsigneddecimalnumber74fdtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STUnsignedDecimalNumber$Factory.class */
    public static final class Factory {
        public static STUnsignedDecimalNumber newValue(Object obj) {
            return (STUnsignedDecimalNumber) STUnsignedDecimalNumber.type.newValue(obj);
        }

        public static STUnsignedDecimalNumber newInstance() {
            return (STUnsignedDecimalNumber) POIXMLTypeLoader.newInstance(STUnsignedDecimalNumber.type, null);
        }

        public static STUnsignedDecimalNumber newInstance(XmlOptions xmlOptions) {
            return (STUnsignedDecimalNumber) POIXMLTypeLoader.newInstance(STUnsignedDecimalNumber.type, xmlOptions);
        }

        public static STUnsignedDecimalNumber parse(String str) throws XmlException {
            return (STUnsignedDecimalNumber) POIXMLTypeLoader.parse(str, STUnsignedDecimalNumber.type, (XmlOptions) null);
        }

        public static STUnsignedDecimalNumber parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STUnsignedDecimalNumber) POIXMLTypeLoader.parse(str, STUnsignedDecimalNumber.type, xmlOptions);
        }

        public static STUnsignedDecimalNumber parse(File file) throws XmlException, IOException {
            return (STUnsignedDecimalNumber) POIXMLTypeLoader.parse(file, STUnsignedDecimalNumber.type, (XmlOptions) null);
        }

        public static STUnsignedDecimalNumber parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STUnsignedDecimalNumber) POIXMLTypeLoader.parse(file, STUnsignedDecimalNumber.type, xmlOptions);
        }

        public static STUnsignedDecimalNumber parse(URL url) throws XmlException, IOException {
            return (STUnsignedDecimalNumber) POIXMLTypeLoader.parse(url, STUnsignedDecimalNumber.type, (XmlOptions) null);
        }

        public static STUnsignedDecimalNumber parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STUnsignedDecimalNumber) POIXMLTypeLoader.parse(url, STUnsignedDecimalNumber.type, xmlOptions);
        }

        public static STUnsignedDecimalNumber parse(InputStream inputStream) throws XmlException, IOException {
            return (STUnsignedDecimalNumber) POIXMLTypeLoader.parse(inputStream, STUnsignedDecimalNumber.type, (XmlOptions) null);
        }

        public static STUnsignedDecimalNumber parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STUnsignedDecimalNumber) POIXMLTypeLoader.parse(inputStream, STUnsignedDecimalNumber.type, xmlOptions);
        }

        public static STUnsignedDecimalNumber parse(Reader reader) throws XmlException, IOException {
            return (STUnsignedDecimalNumber) POIXMLTypeLoader.parse(reader, STUnsignedDecimalNumber.type, (XmlOptions) null);
        }

        public static STUnsignedDecimalNumber parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STUnsignedDecimalNumber) POIXMLTypeLoader.parse(reader, STUnsignedDecimalNumber.type, xmlOptions);
        }

        public static STUnsignedDecimalNumber parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STUnsignedDecimalNumber) POIXMLTypeLoader.parse(xMLStreamReader, STUnsignedDecimalNumber.type, (XmlOptions) null);
        }

        public static STUnsignedDecimalNumber parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STUnsignedDecimalNumber) POIXMLTypeLoader.parse(xMLStreamReader, STUnsignedDecimalNumber.type, xmlOptions);
        }

        public static STUnsignedDecimalNumber parse(Node node) throws XmlException {
            return (STUnsignedDecimalNumber) POIXMLTypeLoader.parse(node, STUnsignedDecimalNumber.type, (XmlOptions) null);
        }

        public static STUnsignedDecimalNumber parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STUnsignedDecimalNumber) POIXMLTypeLoader.parse(node, STUnsignedDecimalNumber.type, xmlOptions);
        }

        public static STUnsignedDecimalNumber parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STUnsignedDecimalNumber) POIXMLTypeLoader.parse(xMLInputStream, STUnsignedDecimalNumber.type, (XmlOptions) null);
        }

        public static STUnsignedDecimalNumber parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STUnsignedDecimalNumber) POIXMLTypeLoader.parse(xMLInputStream, STUnsignedDecimalNumber.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STUnsignedDecimalNumber.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STUnsignedDecimalNumber.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
