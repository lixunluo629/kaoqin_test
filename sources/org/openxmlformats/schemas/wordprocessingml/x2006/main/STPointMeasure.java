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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STPointMeasure.class */
public interface STPointMeasure extends STUnsignedDecimalNumber {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STPointMeasure.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stpointmeasurea96atype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STPointMeasure$Factory.class */
    public static final class Factory {
        public static STPointMeasure newValue(Object obj) {
            return (STPointMeasure) STPointMeasure.type.newValue(obj);
        }

        public static STPointMeasure newInstance() {
            return (STPointMeasure) POIXMLTypeLoader.newInstance(STPointMeasure.type, null);
        }

        public static STPointMeasure newInstance(XmlOptions xmlOptions) {
            return (STPointMeasure) POIXMLTypeLoader.newInstance(STPointMeasure.type, xmlOptions);
        }

        public static STPointMeasure parse(String str) throws XmlException {
            return (STPointMeasure) POIXMLTypeLoader.parse(str, STPointMeasure.type, (XmlOptions) null);
        }

        public static STPointMeasure parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STPointMeasure) POIXMLTypeLoader.parse(str, STPointMeasure.type, xmlOptions);
        }

        public static STPointMeasure parse(File file) throws XmlException, IOException {
            return (STPointMeasure) POIXMLTypeLoader.parse(file, STPointMeasure.type, (XmlOptions) null);
        }

        public static STPointMeasure parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPointMeasure) POIXMLTypeLoader.parse(file, STPointMeasure.type, xmlOptions);
        }

        public static STPointMeasure parse(URL url) throws XmlException, IOException {
            return (STPointMeasure) POIXMLTypeLoader.parse(url, STPointMeasure.type, (XmlOptions) null);
        }

        public static STPointMeasure parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPointMeasure) POIXMLTypeLoader.parse(url, STPointMeasure.type, xmlOptions);
        }

        public static STPointMeasure parse(InputStream inputStream) throws XmlException, IOException {
            return (STPointMeasure) POIXMLTypeLoader.parse(inputStream, STPointMeasure.type, (XmlOptions) null);
        }

        public static STPointMeasure parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPointMeasure) POIXMLTypeLoader.parse(inputStream, STPointMeasure.type, xmlOptions);
        }

        public static STPointMeasure parse(Reader reader) throws XmlException, IOException {
            return (STPointMeasure) POIXMLTypeLoader.parse(reader, STPointMeasure.type, (XmlOptions) null);
        }

        public static STPointMeasure parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPointMeasure) POIXMLTypeLoader.parse(reader, STPointMeasure.type, xmlOptions);
        }

        public static STPointMeasure parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STPointMeasure) POIXMLTypeLoader.parse(xMLStreamReader, STPointMeasure.type, (XmlOptions) null);
        }

        public static STPointMeasure parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STPointMeasure) POIXMLTypeLoader.parse(xMLStreamReader, STPointMeasure.type, xmlOptions);
        }

        public static STPointMeasure parse(Node node) throws XmlException {
            return (STPointMeasure) POIXMLTypeLoader.parse(node, STPointMeasure.type, (XmlOptions) null);
        }

        public static STPointMeasure parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STPointMeasure) POIXMLTypeLoader.parse(node, STPointMeasure.type, xmlOptions);
        }

        public static STPointMeasure parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STPointMeasure) POIXMLTypeLoader.parse(xMLInputStream, STPointMeasure.type, (XmlOptions) null);
        }

        public static STPointMeasure parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STPointMeasure) POIXMLTypeLoader.parse(xMLInputStream, STPointMeasure.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STPointMeasure.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STPointMeasure.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
