package org.openxmlformats.schemas.drawingml.x2006.chart;

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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/STXstring.class */
public interface STXstring extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STXstring.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stxstringb8cdtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/STXstring$Factory.class */
    public static final class Factory {
        public static STXstring newValue(Object obj) {
            return (STXstring) STXstring.type.newValue(obj);
        }

        public static STXstring newInstance() {
            return (STXstring) POIXMLTypeLoader.newInstance(STXstring.type, null);
        }

        public static STXstring newInstance(XmlOptions xmlOptions) {
            return (STXstring) POIXMLTypeLoader.newInstance(STXstring.type, xmlOptions);
        }

        public static STXstring parse(String str) throws XmlException {
            return (STXstring) POIXMLTypeLoader.parse(str, STXstring.type, (XmlOptions) null);
        }

        public static STXstring parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STXstring) POIXMLTypeLoader.parse(str, STXstring.type, xmlOptions);
        }

        public static STXstring parse(File file) throws XmlException, IOException {
            return (STXstring) POIXMLTypeLoader.parse(file, STXstring.type, (XmlOptions) null);
        }

        public static STXstring parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STXstring) POIXMLTypeLoader.parse(file, STXstring.type, xmlOptions);
        }

        public static STXstring parse(URL url) throws XmlException, IOException {
            return (STXstring) POIXMLTypeLoader.parse(url, STXstring.type, (XmlOptions) null);
        }

        public static STXstring parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STXstring) POIXMLTypeLoader.parse(url, STXstring.type, xmlOptions);
        }

        public static STXstring parse(InputStream inputStream) throws XmlException, IOException {
            return (STXstring) POIXMLTypeLoader.parse(inputStream, STXstring.type, (XmlOptions) null);
        }

        public static STXstring parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STXstring) POIXMLTypeLoader.parse(inputStream, STXstring.type, xmlOptions);
        }

        public static STXstring parse(Reader reader) throws XmlException, IOException {
            return (STXstring) POIXMLTypeLoader.parse(reader, STXstring.type, (XmlOptions) null);
        }

        public static STXstring parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STXstring) POIXMLTypeLoader.parse(reader, STXstring.type, xmlOptions);
        }

        public static STXstring parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STXstring) POIXMLTypeLoader.parse(xMLStreamReader, STXstring.type, (XmlOptions) null);
        }

        public static STXstring parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STXstring) POIXMLTypeLoader.parse(xMLStreamReader, STXstring.type, xmlOptions);
        }

        public static STXstring parse(Node node) throws XmlException {
            return (STXstring) POIXMLTypeLoader.parse(node, STXstring.type, (XmlOptions) null);
        }

        public static STXstring parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STXstring) POIXMLTypeLoader.parse(node, STXstring.type, xmlOptions);
        }

        public static STXstring parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STXstring) POIXMLTypeLoader.parse(xMLInputStream, STXstring.type, (XmlOptions) null);
        }

        public static STXstring parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STXstring) POIXMLTypeLoader.parse(xMLInputStream, STXstring.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STXstring.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STXstring.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
