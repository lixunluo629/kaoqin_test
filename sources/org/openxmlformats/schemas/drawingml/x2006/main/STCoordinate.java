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
import org.apache.xmlbeans.XmlLong;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STCoordinate.class */
public interface STCoordinate extends XmlLong {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STCoordinate.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stcoordinatefae3type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STCoordinate$Factory.class */
    public static final class Factory {
        public static STCoordinate newValue(Object obj) {
            return (STCoordinate) STCoordinate.type.newValue(obj);
        }

        public static STCoordinate newInstance() {
            return (STCoordinate) POIXMLTypeLoader.newInstance(STCoordinate.type, null);
        }

        public static STCoordinate newInstance(XmlOptions xmlOptions) {
            return (STCoordinate) POIXMLTypeLoader.newInstance(STCoordinate.type, xmlOptions);
        }

        public static STCoordinate parse(String str) throws XmlException {
            return (STCoordinate) POIXMLTypeLoader.parse(str, STCoordinate.type, (XmlOptions) null);
        }

        public static STCoordinate parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STCoordinate) POIXMLTypeLoader.parse(str, STCoordinate.type, xmlOptions);
        }

        public static STCoordinate parse(File file) throws XmlException, IOException {
            return (STCoordinate) POIXMLTypeLoader.parse(file, STCoordinate.type, (XmlOptions) null);
        }

        public static STCoordinate parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCoordinate) POIXMLTypeLoader.parse(file, STCoordinate.type, xmlOptions);
        }

        public static STCoordinate parse(URL url) throws XmlException, IOException {
            return (STCoordinate) POIXMLTypeLoader.parse(url, STCoordinate.type, (XmlOptions) null);
        }

        public static STCoordinate parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCoordinate) POIXMLTypeLoader.parse(url, STCoordinate.type, xmlOptions);
        }

        public static STCoordinate parse(InputStream inputStream) throws XmlException, IOException {
            return (STCoordinate) POIXMLTypeLoader.parse(inputStream, STCoordinate.type, (XmlOptions) null);
        }

        public static STCoordinate parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCoordinate) POIXMLTypeLoader.parse(inputStream, STCoordinate.type, xmlOptions);
        }

        public static STCoordinate parse(Reader reader) throws XmlException, IOException {
            return (STCoordinate) POIXMLTypeLoader.parse(reader, STCoordinate.type, (XmlOptions) null);
        }

        public static STCoordinate parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STCoordinate) POIXMLTypeLoader.parse(reader, STCoordinate.type, xmlOptions);
        }

        public static STCoordinate parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STCoordinate) POIXMLTypeLoader.parse(xMLStreamReader, STCoordinate.type, (XmlOptions) null);
        }

        public static STCoordinate parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STCoordinate) POIXMLTypeLoader.parse(xMLStreamReader, STCoordinate.type, xmlOptions);
        }

        public static STCoordinate parse(Node node) throws XmlException {
            return (STCoordinate) POIXMLTypeLoader.parse(node, STCoordinate.type, (XmlOptions) null);
        }

        public static STCoordinate parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STCoordinate) POIXMLTypeLoader.parse(node, STCoordinate.type, xmlOptions);
        }

        public static STCoordinate parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STCoordinate) POIXMLTypeLoader.parse(xMLInputStream, STCoordinate.type, (XmlOptions) null);
        }

        public static STCoordinate parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STCoordinate) POIXMLTypeLoader.parse(xMLInputStream, STCoordinate.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STCoordinate.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STCoordinate.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
