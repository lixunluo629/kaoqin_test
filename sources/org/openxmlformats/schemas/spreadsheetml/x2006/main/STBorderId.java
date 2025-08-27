package org.openxmlformats.schemas.spreadsheetml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STBorderId.class */
public interface STBorderId extends XmlUnsignedInt {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STBorderId.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stborderid1a80type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STBorderId$Factory.class */
    public static final class Factory {
        public static STBorderId newValue(Object obj) {
            return (STBorderId) STBorderId.type.newValue(obj);
        }

        public static STBorderId newInstance() {
            return (STBorderId) POIXMLTypeLoader.newInstance(STBorderId.type, null);
        }

        public static STBorderId newInstance(XmlOptions xmlOptions) {
            return (STBorderId) POIXMLTypeLoader.newInstance(STBorderId.type, xmlOptions);
        }

        public static STBorderId parse(String str) throws XmlException {
            return (STBorderId) POIXMLTypeLoader.parse(str, STBorderId.type, (XmlOptions) null);
        }

        public static STBorderId parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STBorderId) POIXMLTypeLoader.parse(str, STBorderId.type, xmlOptions);
        }

        public static STBorderId parse(File file) throws XmlException, IOException {
            return (STBorderId) POIXMLTypeLoader.parse(file, STBorderId.type, (XmlOptions) null);
        }

        public static STBorderId parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STBorderId) POIXMLTypeLoader.parse(file, STBorderId.type, xmlOptions);
        }

        public static STBorderId parse(URL url) throws XmlException, IOException {
            return (STBorderId) POIXMLTypeLoader.parse(url, STBorderId.type, (XmlOptions) null);
        }

        public static STBorderId parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STBorderId) POIXMLTypeLoader.parse(url, STBorderId.type, xmlOptions);
        }

        public static STBorderId parse(InputStream inputStream) throws XmlException, IOException {
            return (STBorderId) POIXMLTypeLoader.parse(inputStream, STBorderId.type, (XmlOptions) null);
        }

        public static STBorderId parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STBorderId) POIXMLTypeLoader.parse(inputStream, STBorderId.type, xmlOptions);
        }

        public static STBorderId parse(Reader reader) throws XmlException, IOException {
            return (STBorderId) POIXMLTypeLoader.parse(reader, STBorderId.type, (XmlOptions) null);
        }

        public static STBorderId parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STBorderId) POIXMLTypeLoader.parse(reader, STBorderId.type, xmlOptions);
        }

        public static STBorderId parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STBorderId) POIXMLTypeLoader.parse(xMLStreamReader, STBorderId.type, (XmlOptions) null);
        }

        public static STBorderId parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STBorderId) POIXMLTypeLoader.parse(xMLStreamReader, STBorderId.type, xmlOptions);
        }

        public static STBorderId parse(Node node) throws XmlException {
            return (STBorderId) POIXMLTypeLoader.parse(node, STBorderId.type, (XmlOptions) null);
        }

        public static STBorderId parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STBorderId) POIXMLTypeLoader.parse(node, STBorderId.type, xmlOptions);
        }

        public static STBorderId parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STBorderId) POIXMLTypeLoader.parse(xMLInputStream, STBorderId.type, (XmlOptions) null);
        }

        public static STBorderId parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STBorderId) POIXMLTypeLoader.parse(xMLInputStream, STBorderId.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STBorderId.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STBorderId.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
