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
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STAngle.class */
public interface STAngle extends XmlInt {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STAngle.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stangle8074type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STAngle$Factory.class */
    public static final class Factory {
        public static STAngle newValue(Object obj) {
            return (STAngle) STAngle.type.newValue(obj);
        }

        public static STAngle newInstance() {
            return (STAngle) POIXMLTypeLoader.newInstance(STAngle.type, null);
        }

        public static STAngle newInstance(XmlOptions xmlOptions) {
            return (STAngle) POIXMLTypeLoader.newInstance(STAngle.type, xmlOptions);
        }

        public static STAngle parse(String str) throws XmlException {
            return (STAngle) POIXMLTypeLoader.parse(str, STAngle.type, (XmlOptions) null);
        }

        public static STAngle parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STAngle) POIXMLTypeLoader.parse(str, STAngle.type, xmlOptions);
        }

        public static STAngle parse(File file) throws XmlException, IOException {
            return (STAngle) POIXMLTypeLoader.parse(file, STAngle.type, (XmlOptions) null);
        }

        public static STAngle parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAngle) POIXMLTypeLoader.parse(file, STAngle.type, xmlOptions);
        }

        public static STAngle parse(URL url) throws XmlException, IOException {
            return (STAngle) POIXMLTypeLoader.parse(url, STAngle.type, (XmlOptions) null);
        }

        public static STAngle parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAngle) POIXMLTypeLoader.parse(url, STAngle.type, xmlOptions);
        }

        public static STAngle parse(InputStream inputStream) throws XmlException, IOException {
            return (STAngle) POIXMLTypeLoader.parse(inputStream, STAngle.type, (XmlOptions) null);
        }

        public static STAngle parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAngle) POIXMLTypeLoader.parse(inputStream, STAngle.type, xmlOptions);
        }

        public static STAngle parse(Reader reader) throws XmlException, IOException {
            return (STAngle) POIXMLTypeLoader.parse(reader, STAngle.type, (XmlOptions) null);
        }

        public static STAngle parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STAngle) POIXMLTypeLoader.parse(reader, STAngle.type, xmlOptions);
        }

        public static STAngle parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STAngle) POIXMLTypeLoader.parse(xMLStreamReader, STAngle.type, (XmlOptions) null);
        }

        public static STAngle parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STAngle) POIXMLTypeLoader.parse(xMLStreamReader, STAngle.type, xmlOptions);
        }

        public static STAngle parse(Node node) throws XmlException {
            return (STAngle) POIXMLTypeLoader.parse(node, STAngle.type, (XmlOptions) null);
        }

        public static STAngle parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STAngle) POIXMLTypeLoader.parse(node, STAngle.type, xmlOptions);
        }

        public static STAngle parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STAngle) POIXMLTypeLoader.parse(xMLInputStream, STAngle.type, (XmlOptions) null);
        }

        public static STAngle parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STAngle) POIXMLTypeLoader.parse(xMLInputStream, STAngle.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STAngle.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STAngle.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
