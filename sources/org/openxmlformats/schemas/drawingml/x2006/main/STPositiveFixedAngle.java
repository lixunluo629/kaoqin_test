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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STPositiveFixedAngle.class */
public interface STPositiveFixedAngle extends STAngle {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STPositiveFixedAngle.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stpositivefixedangle2503type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STPositiveFixedAngle$Factory.class */
    public static final class Factory {
        public static STPositiveFixedAngle newValue(Object obj) {
            return (STPositiveFixedAngle) STPositiveFixedAngle.type.newValue(obj);
        }

        public static STPositiveFixedAngle newInstance() {
            return (STPositiveFixedAngle) POIXMLTypeLoader.newInstance(STPositiveFixedAngle.type, null);
        }

        public static STPositiveFixedAngle newInstance(XmlOptions xmlOptions) {
            return (STPositiveFixedAngle) POIXMLTypeLoader.newInstance(STPositiveFixedAngle.type, xmlOptions);
        }

        public static STPositiveFixedAngle parse(String str) throws XmlException {
            return (STPositiveFixedAngle) POIXMLTypeLoader.parse(str, STPositiveFixedAngle.type, (XmlOptions) null);
        }

        public static STPositiveFixedAngle parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STPositiveFixedAngle) POIXMLTypeLoader.parse(str, STPositiveFixedAngle.type, xmlOptions);
        }

        public static STPositiveFixedAngle parse(File file) throws XmlException, IOException {
            return (STPositiveFixedAngle) POIXMLTypeLoader.parse(file, STPositiveFixedAngle.type, (XmlOptions) null);
        }

        public static STPositiveFixedAngle parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPositiveFixedAngle) POIXMLTypeLoader.parse(file, STPositiveFixedAngle.type, xmlOptions);
        }

        public static STPositiveFixedAngle parse(URL url) throws XmlException, IOException {
            return (STPositiveFixedAngle) POIXMLTypeLoader.parse(url, STPositiveFixedAngle.type, (XmlOptions) null);
        }

        public static STPositiveFixedAngle parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPositiveFixedAngle) POIXMLTypeLoader.parse(url, STPositiveFixedAngle.type, xmlOptions);
        }

        public static STPositiveFixedAngle parse(InputStream inputStream) throws XmlException, IOException {
            return (STPositiveFixedAngle) POIXMLTypeLoader.parse(inputStream, STPositiveFixedAngle.type, (XmlOptions) null);
        }

        public static STPositiveFixedAngle parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPositiveFixedAngle) POIXMLTypeLoader.parse(inputStream, STPositiveFixedAngle.type, xmlOptions);
        }

        public static STPositiveFixedAngle parse(Reader reader) throws XmlException, IOException {
            return (STPositiveFixedAngle) POIXMLTypeLoader.parse(reader, STPositiveFixedAngle.type, (XmlOptions) null);
        }

        public static STPositiveFixedAngle parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STPositiveFixedAngle) POIXMLTypeLoader.parse(reader, STPositiveFixedAngle.type, xmlOptions);
        }

        public static STPositiveFixedAngle parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STPositiveFixedAngle) POIXMLTypeLoader.parse(xMLStreamReader, STPositiveFixedAngle.type, (XmlOptions) null);
        }

        public static STPositiveFixedAngle parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STPositiveFixedAngle) POIXMLTypeLoader.parse(xMLStreamReader, STPositiveFixedAngle.type, xmlOptions);
        }

        public static STPositiveFixedAngle parse(Node node) throws XmlException {
            return (STPositiveFixedAngle) POIXMLTypeLoader.parse(node, STPositiveFixedAngle.type, (XmlOptions) null);
        }

        public static STPositiveFixedAngle parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STPositiveFixedAngle) POIXMLTypeLoader.parse(node, STPositiveFixedAngle.type, xmlOptions);
        }

        public static STPositiveFixedAngle parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STPositiveFixedAngle) POIXMLTypeLoader.parse(xMLInputStream, STPositiveFixedAngle.type, (XmlOptions) null);
        }

        public static STPositiveFixedAngle parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STPositiveFixedAngle) POIXMLTypeLoader.parse(xMLInputStream, STPositiveFixedAngle.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STPositiveFixedAngle.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STPositiveFixedAngle.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
