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
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STDecimalNumber.class */
public interface STDecimalNumber extends XmlInteger {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STDecimalNumber.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stdecimalnumber8d28type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STDecimalNumber$Factory.class */
    public static final class Factory {
        public static STDecimalNumber newValue(Object obj) {
            return (STDecimalNumber) STDecimalNumber.type.newValue(obj);
        }

        public static STDecimalNumber newInstance() {
            return (STDecimalNumber) POIXMLTypeLoader.newInstance(STDecimalNumber.type, null);
        }

        public static STDecimalNumber newInstance(XmlOptions xmlOptions) {
            return (STDecimalNumber) POIXMLTypeLoader.newInstance(STDecimalNumber.type, xmlOptions);
        }

        public static STDecimalNumber parse(String str) throws XmlException {
            return (STDecimalNumber) POIXMLTypeLoader.parse(str, STDecimalNumber.type, (XmlOptions) null);
        }

        public static STDecimalNumber parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STDecimalNumber) POIXMLTypeLoader.parse(str, STDecimalNumber.type, xmlOptions);
        }

        public static STDecimalNumber parse(File file) throws XmlException, IOException {
            return (STDecimalNumber) POIXMLTypeLoader.parse(file, STDecimalNumber.type, (XmlOptions) null);
        }

        public static STDecimalNumber parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDecimalNumber) POIXMLTypeLoader.parse(file, STDecimalNumber.type, xmlOptions);
        }

        public static STDecimalNumber parse(URL url) throws XmlException, IOException {
            return (STDecimalNumber) POIXMLTypeLoader.parse(url, STDecimalNumber.type, (XmlOptions) null);
        }

        public static STDecimalNumber parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDecimalNumber) POIXMLTypeLoader.parse(url, STDecimalNumber.type, xmlOptions);
        }

        public static STDecimalNumber parse(InputStream inputStream) throws XmlException, IOException {
            return (STDecimalNumber) POIXMLTypeLoader.parse(inputStream, STDecimalNumber.type, (XmlOptions) null);
        }

        public static STDecimalNumber parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDecimalNumber) POIXMLTypeLoader.parse(inputStream, STDecimalNumber.type, xmlOptions);
        }

        public static STDecimalNumber parse(Reader reader) throws XmlException, IOException {
            return (STDecimalNumber) POIXMLTypeLoader.parse(reader, STDecimalNumber.type, (XmlOptions) null);
        }

        public static STDecimalNumber parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDecimalNumber) POIXMLTypeLoader.parse(reader, STDecimalNumber.type, xmlOptions);
        }

        public static STDecimalNumber parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STDecimalNumber) POIXMLTypeLoader.parse(xMLStreamReader, STDecimalNumber.type, (XmlOptions) null);
        }

        public static STDecimalNumber parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STDecimalNumber) POIXMLTypeLoader.parse(xMLStreamReader, STDecimalNumber.type, xmlOptions);
        }

        public static STDecimalNumber parse(Node node) throws XmlException {
            return (STDecimalNumber) POIXMLTypeLoader.parse(node, STDecimalNumber.type, (XmlOptions) null);
        }

        public static STDecimalNumber parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STDecimalNumber) POIXMLTypeLoader.parse(node, STDecimalNumber.type, xmlOptions);
        }

        public static STDecimalNumber parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STDecimalNumber) POIXMLTypeLoader.parse(xMLInputStream, STDecimalNumber.type, (XmlOptions) null);
        }

        public static STDecimalNumber parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STDecimalNumber) POIXMLTypeLoader.parse(xMLInputStream, STDecimalNumber.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STDecimalNumber.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STDecimalNumber.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
