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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STEighthPointMeasure.class */
public interface STEighthPointMeasure extends STUnsignedDecimalNumber {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STEighthPointMeasure.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("steighthpointmeasure3371type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STEighthPointMeasure$Factory.class */
    public static final class Factory {
        public static STEighthPointMeasure newValue(Object obj) {
            return (STEighthPointMeasure) STEighthPointMeasure.type.newValue(obj);
        }

        public static STEighthPointMeasure newInstance() {
            return (STEighthPointMeasure) POIXMLTypeLoader.newInstance(STEighthPointMeasure.type, null);
        }

        public static STEighthPointMeasure newInstance(XmlOptions xmlOptions) {
            return (STEighthPointMeasure) POIXMLTypeLoader.newInstance(STEighthPointMeasure.type, xmlOptions);
        }

        public static STEighthPointMeasure parse(String str) throws XmlException {
            return (STEighthPointMeasure) POIXMLTypeLoader.parse(str, STEighthPointMeasure.type, (XmlOptions) null);
        }

        public static STEighthPointMeasure parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STEighthPointMeasure) POIXMLTypeLoader.parse(str, STEighthPointMeasure.type, xmlOptions);
        }

        public static STEighthPointMeasure parse(File file) throws XmlException, IOException {
            return (STEighthPointMeasure) POIXMLTypeLoader.parse(file, STEighthPointMeasure.type, (XmlOptions) null);
        }

        public static STEighthPointMeasure parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STEighthPointMeasure) POIXMLTypeLoader.parse(file, STEighthPointMeasure.type, xmlOptions);
        }

        public static STEighthPointMeasure parse(URL url) throws XmlException, IOException {
            return (STEighthPointMeasure) POIXMLTypeLoader.parse(url, STEighthPointMeasure.type, (XmlOptions) null);
        }

        public static STEighthPointMeasure parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STEighthPointMeasure) POIXMLTypeLoader.parse(url, STEighthPointMeasure.type, xmlOptions);
        }

        public static STEighthPointMeasure parse(InputStream inputStream) throws XmlException, IOException {
            return (STEighthPointMeasure) POIXMLTypeLoader.parse(inputStream, STEighthPointMeasure.type, (XmlOptions) null);
        }

        public static STEighthPointMeasure parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STEighthPointMeasure) POIXMLTypeLoader.parse(inputStream, STEighthPointMeasure.type, xmlOptions);
        }

        public static STEighthPointMeasure parse(Reader reader) throws XmlException, IOException {
            return (STEighthPointMeasure) POIXMLTypeLoader.parse(reader, STEighthPointMeasure.type, (XmlOptions) null);
        }

        public static STEighthPointMeasure parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STEighthPointMeasure) POIXMLTypeLoader.parse(reader, STEighthPointMeasure.type, xmlOptions);
        }

        public static STEighthPointMeasure parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STEighthPointMeasure) POIXMLTypeLoader.parse(xMLStreamReader, STEighthPointMeasure.type, (XmlOptions) null);
        }

        public static STEighthPointMeasure parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STEighthPointMeasure) POIXMLTypeLoader.parse(xMLStreamReader, STEighthPointMeasure.type, xmlOptions);
        }

        public static STEighthPointMeasure parse(Node node) throws XmlException {
            return (STEighthPointMeasure) POIXMLTypeLoader.parse(node, STEighthPointMeasure.type, (XmlOptions) null);
        }

        public static STEighthPointMeasure parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STEighthPointMeasure) POIXMLTypeLoader.parse(node, STEighthPointMeasure.type, xmlOptions);
        }

        public static STEighthPointMeasure parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STEighthPointMeasure) POIXMLTypeLoader.parse(xMLInputStream, STEighthPointMeasure.type, (XmlOptions) null);
        }

        public static STEighthPointMeasure parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STEighthPointMeasure) POIXMLTypeLoader.parse(xMLInputStream, STEighthPointMeasure.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STEighthPointMeasure.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STEighthPointMeasure.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
