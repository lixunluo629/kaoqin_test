package com.microsoft.schemas.vml;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/STColorType.class */
public interface STColorType extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STColorType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stcolortype99c1type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/STColorType$Factory.class */
    public static final class Factory {
        public static STColorType newValue(Object obj) {
            return (STColorType) STColorType.type.newValue(obj);
        }

        public static STColorType newInstance() {
            return (STColorType) POIXMLTypeLoader.newInstance(STColorType.type, null);
        }

        public static STColorType newInstance(XmlOptions xmlOptions) {
            return (STColorType) POIXMLTypeLoader.newInstance(STColorType.type, xmlOptions);
        }

        public static STColorType parse(String str) throws XmlException {
            return (STColorType) POIXMLTypeLoader.parse(str, STColorType.type, (XmlOptions) null);
        }

        public static STColorType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STColorType) POIXMLTypeLoader.parse(str, STColorType.type, xmlOptions);
        }

        public static STColorType parse(File file) throws XmlException, IOException {
            return (STColorType) POIXMLTypeLoader.parse(file, STColorType.type, (XmlOptions) null);
        }

        public static STColorType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STColorType) POIXMLTypeLoader.parse(file, STColorType.type, xmlOptions);
        }

        public static STColorType parse(URL url) throws XmlException, IOException {
            return (STColorType) POIXMLTypeLoader.parse(url, STColorType.type, (XmlOptions) null);
        }

        public static STColorType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STColorType) POIXMLTypeLoader.parse(url, STColorType.type, xmlOptions);
        }

        public static STColorType parse(InputStream inputStream) throws XmlException, IOException {
            return (STColorType) POIXMLTypeLoader.parse(inputStream, STColorType.type, (XmlOptions) null);
        }

        public static STColorType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STColorType) POIXMLTypeLoader.parse(inputStream, STColorType.type, xmlOptions);
        }

        public static STColorType parse(Reader reader) throws XmlException, IOException {
            return (STColorType) POIXMLTypeLoader.parse(reader, STColorType.type, (XmlOptions) null);
        }

        public static STColorType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STColorType) POIXMLTypeLoader.parse(reader, STColorType.type, xmlOptions);
        }

        public static STColorType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STColorType) POIXMLTypeLoader.parse(xMLStreamReader, STColorType.type, (XmlOptions) null);
        }

        public static STColorType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STColorType) POIXMLTypeLoader.parse(xMLStreamReader, STColorType.type, xmlOptions);
        }

        public static STColorType parse(Node node) throws XmlException {
            return (STColorType) POIXMLTypeLoader.parse(node, STColorType.type, (XmlOptions) null);
        }

        public static STColorType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STColorType) POIXMLTypeLoader.parse(node, STColorType.type, xmlOptions);
        }

        public static STColorType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STColorType) POIXMLTypeLoader.parse(xMLInputStream, STColorType.type, (XmlOptions) null);
        }

        public static STColorType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STColorType) POIXMLTypeLoader.parse(xMLInputStream, STColorType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STColorType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STColorType.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
