package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/wordprocessingDrawing/STWrapDistance.class */
public interface STWrapDistance extends XmlUnsignedInt {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STWrapDistance.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stwrapdistanceea50type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/wordprocessingDrawing/STWrapDistance$Factory.class */
    public static final class Factory {
        public static STWrapDistance newValue(Object obj) {
            return (STWrapDistance) STWrapDistance.type.newValue(obj);
        }

        public static STWrapDistance newInstance() {
            return (STWrapDistance) POIXMLTypeLoader.newInstance(STWrapDistance.type, null);
        }

        public static STWrapDistance newInstance(XmlOptions xmlOptions) {
            return (STWrapDistance) POIXMLTypeLoader.newInstance(STWrapDistance.type, xmlOptions);
        }

        public static STWrapDistance parse(String str) throws XmlException {
            return (STWrapDistance) POIXMLTypeLoader.parse(str, STWrapDistance.type, (XmlOptions) null);
        }

        public static STWrapDistance parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STWrapDistance) POIXMLTypeLoader.parse(str, STWrapDistance.type, xmlOptions);
        }

        public static STWrapDistance parse(File file) throws XmlException, IOException {
            return (STWrapDistance) POIXMLTypeLoader.parse(file, STWrapDistance.type, (XmlOptions) null);
        }

        public static STWrapDistance parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STWrapDistance) POIXMLTypeLoader.parse(file, STWrapDistance.type, xmlOptions);
        }

        public static STWrapDistance parse(URL url) throws XmlException, IOException {
            return (STWrapDistance) POIXMLTypeLoader.parse(url, STWrapDistance.type, (XmlOptions) null);
        }

        public static STWrapDistance parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STWrapDistance) POIXMLTypeLoader.parse(url, STWrapDistance.type, xmlOptions);
        }

        public static STWrapDistance parse(InputStream inputStream) throws XmlException, IOException {
            return (STWrapDistance) POIXMLTypeLoader.parse(inputStream, STWrapDistance.type, (XmlOptions) null);
        }

        public static STWrapDistance parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STWrapDistance) POIXMLTypeLoader.parse(inputStream, STWrapDistance.type, xmlOptions);
        }

        public static STWrapDistance parse(Reader reader) throws XmlException, IOException {
            return (STWrapDistance) POIXMLTypeLoader.parse(reader, STWrapDistance.type, (XmlOptions) null);
        }

        public static STWrapDistance parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STWrapDistance) POIXMLTypeLoader.parse(reader, STWrapDistance.type, xmlOptions);
        }

        public static STWrapDistance parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STWrapDistance) POIXMLTypeLoader.parse(xMLStreamReader, STWrapDistance.type, (XmlOptions) null);
        }

        public static STWrapDistance parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STWrapDistance) POIXMLTypeLoader.parse(xMLStreamReader, STWrapDistance.type, xmlOptions);
        }

        public static STWrapDistance parse(Node node) throws XmlException {
            return (STWrapDistance) POIXMLTypeLoader.parse(node, STWrapDistance.type, (XmlOptions) null);
        }

        public static STWrapDistance parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STWrapDistance) POIXMLTypeLoader.parse(node, STWrapDistance.type, xmlOptions);
        }

        public static STWrapDistance parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STWrapDistance) POIXMLTypeLoader.parse(xMLInputStream, STWrapDistance.type, (XmlOptions) null);
        }

        public static STWrapDistance parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STWrapDistance) POIXMLTypeLoader.parse(xMLInputStream, STWrapDistance.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STWrapDistance.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STWrapDistance.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
