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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STHpsMeasure.class */
public interface STHpsMeasure extends STUnsignedDecimalNumber {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STHpsMeasure.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("sthpsmeasurec985type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STHpsMeasure$Factory.class */
    public static final class Factory {
        public static STHpsMeasure newValue(Object obj) {
            return (STHpsMeasure) STHpsMeasure.type.newValue(obj);
        }

        public static STHpsMeasure newInstance() {
            return (STHpsMeasure) POIXMLTypeLoader.newInstance(STHpsMeasure.type, null);
        }

        public static STHpsMeasure newInstance(XmlOptions xmlOptions) {
            return (STHpsMeasure) POIXMLTypeLoader.newInstance(STHpsMeasure.type, xmlOptions);
        }

        public static STHpsMeasure parse(String str) throws XmlException {
            return (STHpsMeasure) POIXMLTypeLoader.parse(str, STHpsMeasure.type, (XmlOptions) null);
        }

        public static STHpsMeasure parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STHpsMeasure) POIXMLTypeLoader.parse(str, STHpsMeasure.type, xmlOptions);
        }

        public static STHpsMeasure parse(File file) throws XmlException, IOException {
            return (STHpsMeasure) POIXMLTypeLoader.parse(file, STHpsMeasure.type, (XmlOptions) null);
        }

        public static STHpsMeasure parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STHpsMeasure) POIXMLTypeLoader.parse(file, STHpsMeasure.type, xmlOptions);
        }

        public static STHpsMeasure parse(URL url) throws XmlException, IOException {
            return (STHpsMeasure) POIXMLTypeLoader.parse(url, STHpsMeasure.type, (XmlOptions) null);
        }

        public static STHpsMeasure parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STHpsMeasure) POIXMLTypeLoader.parse(url, STHpsMeasure.type, xmlOptions);
        }

        public static STHpsMeasure parse(InputStream inputStream) throws XmlException, IOException {
            return (STHpsMeasure) POIXMLTypeLoader.parse(inputStream, STHpsMeasure.type, (XmlOptions) null);
        }

        public static STHpsMeasure parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STHpsMeasure) POIXMLTypeLoader.parse(inputStream, STHpsMeasure.type, xmlOptions);
        }

        public static STHpsMeasure parse(Reader reader) throws XmlException, IOException {
            return (STHpsMeasure) POIXMLTypeLoader.parse(reader, STHpsMeasure.type, (XmlOptions) null);
        }

        public static STHpsMeasure parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STHpsMeasure) POIXMLTypeLoader.parse(reader, STHpsMeasure.type, xmlOptions);
        }

        public static STHpsMeasure parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STHpsMeasure) POIXMLTypeLoader.parse(xMLStreamReader, STHpsMeasure.type, (XmlOptions) null);
        }

        public static STHpsMeasure parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STHpsMeasure) POIXMLTypeLoader.parse(xMLStreamReader, STHpsMeasure.type, xmlOptions);
        }

        public static STHpsMeasure parse(Node node) throws XmlException {
            return (STHpsMeasure) POIXMLTypeLoader.parse(node, STHpsMeasure.type, (XmlOptions) null);
        }

        public static STHpsMeasure parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STHpsMeasure) POIXMLTypeLoader.parse(node, STHpsMeasure.type, xmlOptions);
        }

        public static STHpsMeasure parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STHpsMeasure) POIXMLTypeLoader.parse(xMLInputStream, STHpsMeasure.type, (XmlOptions) null);
        }

        public static STHpsMeasure parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STHpsMeasure) POIXMLTypeLoader.parse(xMLInputStream, STHpsMeasure.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STHpsMeasure.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STHpsMeasure.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
