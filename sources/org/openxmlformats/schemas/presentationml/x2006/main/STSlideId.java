package org.openxmlformats.schemas.presentationml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/STSlideId.class */
public interface STSlideId extends XmlUnsignedInt {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STSlideId.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stslideida0b3type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/STSlideId$Factory.class */
    public static final class Factory {
        public static STSlideId newValue(Object obj) {
            return (STSlideId) STSlideId.type.newValue(obj);
        }

        public static STSlideId newInstance() {
            return (STSlideId) POIXMLTypeLoader.newInstance(STSlideId.type, null);
        }

        public static STSlideId newInstance(XmlOptions xmlOptions) {
            return (STSlideId) POIXMLTypeLoader.newInstance(STSlideId.type, xmlOptions);
        }

        public static STSlideId parse(String str) throws XmlException {
            return (STSlideId) POIXMLTypeLoader.parse(str, STSlideId.type, (XmlOptions) null);
        }

        public static STSlideId parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STSlideId) POIXMLTypeLoader.parse(str, STSlideId.type, xmlOptions);
        }

        public static STSlideId parse(File file) throws XmlException, IOException {
            return (STSlideId) POIXMLTypeLoader.parse(file, STSlideId.type, (XmlOptions) null);
        }

        public static STSlideId parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSlideId) POIXMLTypeLoader.parse(file, STSlideId.type, xmlOptions);
        }

        public static STSlideId parse(URL url) throws XmlException, IOException {
            return (STSlideId) POIXMLTypeLoader.parse(url, STSlideId.type, (XmlOptions) null);
        }

        public static STSlideId parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSlideId) POIXMLTypeLoader.parse(url, STSlideId.type, xmlOptions);
        }

        public static STSlideId parse(InputStream inputStream) throws XmlException, IOException {
            return (STSlideId) POIXMLTypeLoader.parse(inputStream, STSlideId.type, (XmlOptions) null);
        }

        public static STSlideId parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSlideId) POIXMLTypeLoader.parse(inputStream, STSlideId.type, xmlOptions);
        }

        public static STSlideId parse(Reader reader) throws XmlException, IOException {
            return (STSlideId) POIXMLTypeLoader.parse(reader, STSlideId.type, (XmlOptions) null);
        }

        public static STSlideId parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSlideId) POIXMLTypeLoader.parse(reader, STSlideId.type, xmlOptions);
        }

        public static STSlideId parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STSlideId) POIXMLTypeLoader.parse(xMLStreamReader, STSlideId.type, (XmlOptions) null);
        }

        public static STSlideId parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STSlideId) POIXMLTypeLoader.parse(xMLStreamReader, STSlideId.type, xmlOptions);
        }

        public static STSlideId parse(Node node) throws XmlException {
            return (STSlideId) POIXMLTypeLoader.parse(node, STSlideId.type, (XmlOptions) null);
        }

        public static STSlideId parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STSlideId) POIXMLTypeLoader.parse(node, STSlideId.type, xmlOptions);
        }

        public static STSlideId parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STSlideId) POIXMLTypeLoader.parse(xMLInputStream, STSlideId.type, (XmlOptions) null);
        }

        public static STSlideId parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STSlideId) POIXMLTypeLoader.parse(xMLInputStream, STSlideId.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STSlideId.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STSlideId.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
