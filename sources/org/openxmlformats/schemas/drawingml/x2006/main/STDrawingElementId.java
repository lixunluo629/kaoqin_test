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
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STDrawingElementId.class */
public interface STDrawingElementId extends XmlUnsignedInt {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STDrawingElementId.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stdrawingelementid75a4type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STDrawingElementId$Factory.class */
    public static final class Factory {
        public static STDrawingElementId newValue(Object obj) {
            return (STDrawingElementId) STDrawingElementId.type.newValue(obj);
        }

        public static STDrawingElementId newInstance() {
            return (STDrawingElementId) POIXMLTypeLoader.newInstance(STDrawingElementId.type, null);
        }

        public static STDrawingElementId newInstance(XmlOptions xmlOptions) {
            return (STDrawingElementId) POIXMLTypeLoader.newInstance(STDrawingElementId.type, xmlOptions);
        }

        public static STDrawingElementId parse(String str) throws XmlException {
            return (STDrawingElementId) POIXMLTypeLoader.parse(str, STDrawingElementId.type, (XmlOptions) null);
        }

        public static STDrawingElementId parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STDrawingElementId) POIXMLTypeLoader.parse(str, STDrawingElementId.type, xmlOptions);
        }

        public static STDrawingElementId parse(File file) throws XmlException, IOException {
            return (STDrawingElementId) POIXMLTypeLoader.parse(file, STDrawingElementId.type, (XmlOptions) null);
        }

        public static STDrawingElementId parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDrawingElementId) POIXMLTypeLoader.parse(file, STDrawingElementId.type, xmlOptions);
        }

        public static STDrawingElementId parse(URL url) throws XmlException, IOException {
            return (STDrawingElementId) POIXMLTypeLoader.parse(url, STDrawingElementId.type, (XmlOptions) null);
        }

        public static STDrawingElementId parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDrawingElementId) POIXMLTypeLoader.parse(url, STDrawingElementId.type, xmlOptions);
        }

        public static STDrawingElementId parse(InputStream inputStream) throws XmlException, IOException {
            return (STDrawingElementId) POIXMLTypeLoader.parse(inputStream, STDrawingElementId.type, (XmlOptions) null);
        }

        public static STDrawingElementId parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDrawingElementId) POIXMLTypeLoader.parse(inputStream, STDrawingElementId.type, xmlOptions);
        }

        public static STDrawingElementId parse(Reader reader) throws XmlException, IOException {
            return (STDrawingElementId) POIXMLTypeLoader.parse(reader, STDrawingElementId.type, (XmlOptions) null);
        }

        public static STDrawingElementId parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STDrawingElementId) POIXMLTypeLoader.parse(reader, STDrawingElementId.type, xmlOptions);
        }

        public static STDrawingElementId parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STDrawingElementId) POIXMLTypeLoader.parse(xMLStreamReader, STDrawingElementId.type, (XmlOptions) null);
        }

        public static STDrawingElementId parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STDrawingElementId) POIXMLTypeLoader.parse(xMLStreamReader, STDrawingElementId.type, xmlOptions);
        }

        public static STDrawingElementId parse(Node node) throws XmlException {
            return (STDrawingElementId) POIXMLTypeLoader.parse(node, STDrawingElementId.type, (XmlOptions) null);
        }

        public static STDrawingElementId parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STDrawingElementId) POIXMLTypeLoader.parse(node, STDrawingElementId.type, xmlOptions);
        }

        public static STDrawingElementId parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STDrawingElementId) POIXMLTypeLoader.parse(xMLInputStream, STDrawingElementId.type, (XmlOptions) null);
        }

        public static STDrawingElementId parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STDrawingElementId) POIXMLTypeLoader.parse(xMLInputStream, STDrawingElementId.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STDrawingElementId.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STDrawingElementId.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
