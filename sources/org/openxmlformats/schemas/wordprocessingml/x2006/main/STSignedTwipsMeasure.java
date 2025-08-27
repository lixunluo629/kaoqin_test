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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STSignedTwipsMeasure.class */
public interface STSignedTwipsMeasure extends XmlInteger {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STSignedTwipsMeasure.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stsignedtwipsmeasureb227type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STSignedTwipsMeasure$Factory.class */
    public static final class Factory {
        public static STSignedTwipsMeasure newValue(Object obj) {
            return (STSignedTwipsMeasure) STSignedTwipsMeasure.type.newValue(obj);
        }

        public static STSignedTwipsMeasure newInstance() {
            return (STSignedTwipsMeasure) POIXMLTypeLoader.newInstance(STSignedTwipsMeasure.type, null);
        }

        public static STSignedTwipsMeasure newInstance(XmlOptions xmlOptions) {
            return (STSignedTwipsMeasure) POIXMLTypeLoader.newInstance(STSignedTwipsMeasure.type, xmlOptions);
        }

        public static STSignedTwipsMeasure parse(String str) throws XmlException {
            return (STSignedTwipsMeasure) POIXMLTypeLoader.parse(str, STSignedTwipsMeasure.type, (XmlOptions) null);
        }

        public static STSignedTwipsMeasure parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STSignedTwipsMeasure) POIXMLTypeLoader.parse(str, STSignedTwipsMeasure.type, xmlOptions);
        }

        public static STSignedTwipsMeasure parse(File file) throws XmlException, IOException {
            return (STSignedTwipsMeasure) POIXMLTypeLoader.parse(file, STSignedTwipsMeasure.type, (XmlOptions) null);
        }

        public static STSignedTwipsMeasure parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSignedTwipsMeasure) POIXMLTypeLoader.parse(file, STSignedTwipsMeasure.type, xmlOptions);
        }

        public static STSignedTwipsMeasure parse(URL url) throws XmlException, IOException {
            return (STSignedTwipsMeasure) POIXMLTypeLoader.parse(url, STSignedTwipsMeasure.type, (XmlOptions) null);
        }

        public static STSignedTwipsMeasure parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSignedTwipsMeasure) POIXMLTypeLoader.parse(url, STSignedTwipsMeasure.type, xmlOptions);
        }

        public static STSignedTwipsMeasure parse(InputStream inputStream) throws XmlException, IOException {
            return (STSignedTwipsMeasure) POIXMLTypeLoader.parse(inputStream, STSignedTwipsMeasure.type, (XmlOptions) null);
        }

        public static STSignedTwipsMeasure parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSignedTwipsMeasure) POIXMLTypeLoader.parse(inputStream, STSignedTwipsMeasure.type, xmlOptions);
        }

        public static STSignedTwipsMeasure parse(Reader reader) throws XmlException, IOException {
            return (STSignedTwipsMeasure) POIXMLTypeLoader.parse(reader, STSignedTwipsMeasure.type, (XmlOptions) null);
        }

        public static STSignedTwipsMeasure parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSignedTwipsMeasure) POIXMLTypeLoader.parse(reader, STSignedTwipsMeasure.type, xmlOptions);
        }

        public static STSignedTwipsMeasure parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STSignedTwipsMeasure) POIXMLTypeLoader.parse(xMLStreamReader, STSignedTwipsMeasure.type, (XmlOptions) null);
        }

        public static STSignedTwipsMeasure parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STSignedTwipsMeasure) POIXMLTypeLoader.parse(xMLStreamReader, STSignedTwipsMeasure.type, xmlOptions);
        }

        public static STSignedTwipsMeasure parse(Node node) throws XmlException {
            return (STSignedTwipsMeasure) POIXMLTypeLoader.parse(node, STSignedTwipsMeasure.type, (XmlOptions) null);
        }

        public static STSignedTwipsMeasure parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STSignedTwipsMeasure) POIXMLTypeLoader.parse(node, STSignedTwipsMeasure.type, xmlOptions);
        }

        public static STSignedTwipsMeasure parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STSignedTwipsMeasure) POIXMLTypeLoader.parse(xMLInputStream, STSignedTwipsMeasure.type, (XmlOptions) null);
        }

        public static STSignedTwipsMeasure parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STSignedTwipsMeasure) POIXMLTypeLoader.parse(xMLInputStream, STSignedTwipsMeasure.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STSignedTwipsMeasure.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STSignedTwipsMeasure.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
