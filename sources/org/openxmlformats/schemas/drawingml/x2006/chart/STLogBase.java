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
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/STLogBase.class */
public interface STLogBase extends XmlDouble {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STLogBase.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stlogbase11a1type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/STLogBase$Factory.class */
    public static final class Factory {
        public static STLogBase newValue(Object obj) {
            return (STLogBase) STLogBase.type.newValue(obj);
        }

        public static STLogBase newInstance() {
            return (STLogBase) POIXMLTypeLoader.newInstance(STLogBase.type, null);
        }

        public static STLogBase newInstance(XmlOptions xmlOptions) {
            return (STLogBase) POIXMLTypeLoader.newInstance(STLogBase.type, xmlOptions);
        }

        public static STLogBase parse(String str) throws XmlException {
            return (STLogBase) POIXMLTypeLoader.parse(str, STLogBase.type, (XmlOptions) null);
        }

        public static STLogBase parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STLogBase) POIXMLTypeLoader.parse(str, STLogBase.type, xmlOptions);
        }

        public static STLogBase parse(File file) throws XmlException, IOException {
            return (STLogBase) POIXMLTypeLoader.parse(file, STLogBase.type, (XmlOptions) null);
        }

        public static STLogBase parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLogBase) POIXMLTypeLoader.parse(file, STLogBase.type, xmlOptions);
        }

        public static STLogBase parse(URL url) throws XmlException, IOException {
            return (STLogBase) POIXMLTypeLoader.parse(url, STLogBase.type, (XmlOptions) null);
        }

        public static STLogBase parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLogBase) POIXMLTypeLoader.parse(url, STLogBase.type, xmlOptions);
        }

        public static STLogBase parse(InputStream inputStream) throws XmlException, IOException {
            return (STLogBase) POIXMLTypeLoader.parse(inputStream, STLogBase.type, (XmlOptions) null);
        }

        public static STLogBase parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLogBase) POIXMLTypeLoader.parse(inputStream, STLogBase.type, xmlOptions);
        }

        public static STLogBase parse(Reader reader) throws XmlException, IOException {
            return (STLogBase) POIXMLTypeLoader.parse(reader, STLogBase.type, (XmlOptions) null);
        }

        public static STLogBase parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STLogBase) POIXMLTypeLoader.parse(reader, STLogBase.type, xmlOptions);
        }

        public static STLogBase parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STLogBase) POIXMLTypeLoader.parse(xMLStreamReader, STLogBase.type, (XmlOptions) null);
        }

        public static STLogBase parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STLogBase) POIXMLTypeLoader.parse(xMLStreamReader, STLogBase.type, xmlOptions);
        }

        public static STLogBase parse(Node node) throws XmlException {
            return (STLogBase) POIXMLTypeLoader.parse(node, STLogBase.type, (XmlOptions) null);
        }

        public static STLogBase parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STLogBase) POIXMLTypeLoader.parse(node, STLogBase.type, xmlOptions);
        }

        public static STLogBase parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STLogBase) POIXMLTypeLoader.parse(xMLInputStream, STLogBase.type, (XmlOptions) null);
        }

        public static STLogBase parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STLogBase) POIXMLTypeLoader.parse(xMLInputStream, STLogBase.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STLogBase.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STLogBase.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
