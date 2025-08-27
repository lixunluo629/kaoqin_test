package org.openxmlformats.schemas.drawingml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextTypeface.class */
public interface STTextTypeface extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STTextTypeface.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("sttexttypefacea80ftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextTypeface$Factory.class */
    public static final class Factory {
        public static STTextTypeface newValue(Object obj) {
            return (STTextTypeface) STTextTypeface.type.newValue(obj);
        }

        public static STTextTypeface newInstance() {
            return (STTextTypeface) POIXMLTypeLoader.newInstance(STTextTypeface.type, null);
        }

        public static STTextTypeface newInstance(XmlOptions xmlOptions) {
            return (STTextTypeface) POIXMLTypeLoader.newInstance(STTextTypeface.type, xmlOptions);
        }

        public static STTextTypeface parse(String str) throws XmlException {
            return (STTextTypeface) POIXMLTypeLoader.parse(str, STTextTypeface.type, (XmlOptions) null);
        }

        public static STTextTypeface parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STTextTypeface) POIXMLTypeLoader.parse(str, STTextTypeface.type, xmlOptions);
        }

        public static STTextTypeface parse(File file) throws XmlException, IOException {
            return (STTextTypeface) POIXMLTypeLoader.parse(file, STTextTypeface.type, (XmlOptions) null);
        }

        public static STTextTypeface parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextTypeface) POIXMLTypeLoader.parse(file, STTextTypeface.type, xmlOptions);
        }

        public static STTextTypeface parse(URL url) throws XmlException, IOException {
            return (STTextTypeface) POIXMLTypeLoader.parse(url, STTextTypeface.type, (XmlOptions) null);
        }

        public static STTextTypeface parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextTypeface) POIXMLTypeLoader.parse(url, STTextTypeface.type, xmlOptions);
        }

        public static STTextTypeface parse(InputStream inputStream) throws XmlException, IOException {
            return (STTextTypeface) POIXMLTypeLoader.parse(inputStream, STTextTypeface.type, (XmlOptions) null);
        }

        public static STTextTypeface parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextTypeface) POIXMLTypeLoader.parse(inputStream, STTextTypeface.type, xmlOptions);
        }

        public static STTextTypeface parse(Reader reader) throws XmlException, IOException {
            return (STTextTypeface) POIXMLTypeLoader.parse(reader, STTextTypeface.type, (XmlOptions) null);
        }

        public static STTextTypeface parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextTypeface) POIXMLTypeLoader.parse(reader, STTextTypeface.type, xmlOptions);
        }

        public static STTextTypeface parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STTextTypeface) POIXMLTypeLoader.parse(xMLStreamReader, STTextTypeface.type, (XmlOptions) null);
        }

        public static STTextTypeface parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STTextTypeface) POIXMLTypeLoader.parse(xMLStreamReader, STTextTypeface.type, xmlOptions);
        }

        public static STTextTypeface parse(Node node) throws XmlException {
            return (STTextTypeface) POIXMLTypeLoader.parse(node, STTextTypeface.type, (XmlOptions) null);
        }

        public static STTextTypeface parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STTextTypeface) POIXMLTypeLoader.parse(node, STTextTypeface.type, xmlOptions);
        }

        public static STTextTypeface parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STTextTypeface) POIXMLTypeLoader.parse(xMLInputStream, STTextTypeface.type, (XmlOptions) null);
        }

        public static STTextTypeface parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STTextTypeface) POIXMLTypeLoader.parse(xMLInputStream, STTextTypeface.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTextTypeface.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTextTypeface.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
