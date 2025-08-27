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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextSpacingPoint.class */
public interface STTextSpacingPoint extends XmlInt {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STTextSpacingPoint.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("sttextspacingpointdd05type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextSpacingPoint$Factory.class */
    public static final class Factory {
        public static STTextSpacingPoint newValue(Object obj) {
            return (STTextSpacingPoint) STTextSpacingPoint.type.newValue(obj);
        }

        public static STTextSpacingPoint newInstance() {
            return (STTextSpacingPoint) POIXMLTypeLoader.newInstance(STTextSpacingPoint.type, null);
        }

        public static STTextSpacingPoint newInstance(XmlOptions xmlOptions) {
            return (STTextSpacingPoint) POIXMLTypeLoader.newInstance(STTextSpacingPoint.type, xmlOptions);
        }

        public static STTextSpacingPoint parse(String str) throws XmlException {
            return (STTextSpacingPoint) POIXMLTypeLoader.parse(str, STTextSpacingPoint.type, (XmlOptions) null);
        }

        public static STTextSpacingPoint parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STTextSpacingPoint) POIXMLTypeLoader.parse(str, STTextSpacingPoint.type, xmlOptions);
        }

        public static STTextSpacingPoint parse(File file) throws XmlException, IOException {
            return (STTextSpacingPoint) POIXMLTypeLoader.parse(file, STTextSpacingPoint.type, (XmlOptions) null);
        }

        public static STTextSpacingPoint parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextSpacingPoint) POIXMLTypeLoader.parse(file, STTextSpacingPoint.type, xmlOptions);
        }

        public static STTextSpacingPoint parse(URL url) throws XmlException, IOException {
            return (STTextSpacingPoint) POIXMLTypeLoader.parse(url, STTextSpacingPoint.type, (XmlOptions) null);
        }

        public static STTextSpacingPoint parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextSpacingPoint) POIXMLTypeLoader.parse(url, STTextSpacingPoint.type, xmlOptions);
        }

        public static STTextSpacingPoint parse(InputStream inputStream) throws XmlException, IOException {
            return (STTextSpacingPoint) POIXMLTypeLoader.parse(inputStream, STTextSpacingPoint.type, (XmlOptions) null);
        }

        public static STTextSpacingPoint parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextSpacingPoint) POIXMLTypeLoader.parse(inputStream, STTextSpacingPoint.type, xmlOptions);
        }

        public static STTextSpacingPoint parse(Reader reader) throws XmlException, IOException {
            return (STTextSpacingPoint) POIXMLTypeLoader.parse(reader, STTextSpacingPoint.type, (XmlOptions) null);
        }

        public static STTextSpacingPoint parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STTextSpacingPoint) POIXMLTypeLoader.parse(reader, STTextSpacingPoint.type, xmlOptions);
        }

        public static STTextSpacingPoint parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STTextSpacingPoint) POIXMLTypeLoader.parse(xMLStreamReader, STTextSpacingPoint.type, (XmlOptions) null);
        }

        public static STTextSpacingPoint parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STTextSpacingPoint) POIXMLTypeLoader.parse(xMLStreamReader, STTextSpacingPoint.type, xmlOptions);
        }

        public static STTextSpacingPoint parse(Node node) throws XmlException {
            return (STTextSpacingPoint) POIXMLTypeLoader.parse(node, STTextSpacingPoint.type, (XmlOptions) null);
        }

        public static STTextSpacingPoint parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STTextSpacingPoint) POIXMLTypeLoader.parse(node, STTextSpacingPoint.type, xmlOptions);
        }

        public static STTextSpacingPoint parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STTextSpacingPoint) POIXMLTypeLoader.parse(xMLInputStream, STTextSpacingPoint.type, (XmlOptions) null);
        }

        public static STTextSpacingPoint parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STTextSpacingPoint) POIXMLTypeLoader.parse(xMLInputStream, STTextSpacingPoint.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTextSpacingPoint.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STTextSpacingPoint.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
