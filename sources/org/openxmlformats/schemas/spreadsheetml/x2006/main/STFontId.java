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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STFontId.class */
public interface STFontId extends XmlUnsignedInt {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STFontId.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stfontid9d63type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STFontId$Factory.class */
    public static final class Factory {
        public static STFontId newValue(Object obj) {
            return (STFontId) STFontId.type.newValue(obj);
        }

        public static STFontId newInstance() {
            return (STFontId) POIXMLTypeLoader.newInstance(STFontId.type, null);
        }

        public static STFontId newInstance(XmlOptions xmlOptions) {
            return (STFontId) POIXMLTypeLoader.newInstance(STFontId.type, xmlOptions);
        }

        public static STFontId parse(String str) throws XmlException {
            return (STFontId) POIXMLTypeLoader.parse(str, STFontId.type, (XmlOptions) null);
        }

        public static STFontId parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STFontId) POIXMLTypeLoader.parse(str, STFontId.type, xmlOptions);
        }

        public static STFontId parse(File file) throws XmlException, IOException {
            return (STFontId) POIXMLTypeLoader.parse(file, STFontId.type, (XmlOptions) null);
        }

        public static STFontId parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFontId) POIXMLTypeLoader.parse(file, STFontId.type, xmlOptions);
        }

        public static STFontId parse(URL url) throws XmlException, IOException {
            return (STFontId) POIXMLTypeLoader.parse(url, STFontId.type, (XmlOptions) null);
        }

        public static STFontId parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFontId) POIXMLTypeLoader.parse(url, STFontId.type, xmlOptions);
        }

        public static STFontId parse(InputStream inputStream) throws XmlException, IOException {
            return (STFontId) POIXMLTypeLoader.parse(inputStream, STFontId.type, (XmlOptions) null);
        }

        public static STFontId parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFontId) POIXMLTypeLoader.parse(inputStream, STFontId.type, xmlOptions);
        }

        public static STFontId parse(Reader reader) throws XmlException, IOException {
            return (STFontId) POIXMLTypeLoader.parse(reader, STFontId.type, (XmlOptions) null);
        }

        public static STFontId parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STFontId) POIXMLTypeLoader.parse(reader, STFontId.type, xmlOptions);
        }

        public static STFontId parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STFontId) POIXMLTypeLoader.parse(xMLStreamReader, STFontId.type, (XmlOptions) null);
        }

        public static STFontId parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STFontId) POIXMLTypeLoader.parse(xMLStreamReader, STFontId.type, xmlOptions);
        }

        public static STFontId parse(Node node) throws XmlException {
            return (STFontId) POIXMLTypeLoader.parse(node, STFontId.type, (XmlOptions) null);
        }

        public static STFontId parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STFontId) POIXMLTypeLoader.parse(node, STFontId.type, xmlOptions);
        }

        public static STFontId parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STFontId) POIXMLTypeLoader.parse(xMLInputStream, STFontId.type, (XmlOptions) null);
        }

        public static STFontId parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STFontId) POIXMLTypeLoader.parse(xMLInputStream, STFontId.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STFontId.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STFontId.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
