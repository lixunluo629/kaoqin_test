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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/STSlideMasterId.class */
public interface STSlideMasterId extends XmlUnsignedInt {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STSlideMasterId.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stslidemasteridfe71type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/STSlideMasterId$Factory.class */
    public static final class Factory {
        public static STSlideMasterId newValue(Object obj) {
            return (STSlideMasterId) STSlideMasterId.type.newValue(obj);
        }

        public static STSlideMasterId newInstance() {
            return (STSlideMasterId) POIXMLTypeLoader.newInstance(STSlideMasterId.type, null);
        }

        public static STSlideMasterId newInstance(XmlOptions xmlOptions) {
            return (STSlideMasterId) POIXMLTypeLoader.newInstance(STSlideMasterId.type, xmlOptions);
        }

        public static STSlideMasterId parse(String str) throws XmlException {
            return (STSlideMasterId) POIXMLTypeLoader.parse(str, STSlideMasterId.type, (XmlOptions) null);
        }

        public static STSlideMasterId parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STSlideMasterId) POIXMLTypeLoader.parse(str, STSlideMasterId.type, xmlOptions);
        }

        public static STSlideMasterId parse(File file) throws XmlException, IOException {
            return (STSlideMasterId) POIXMLTypeLoader.parse(file, STSlideMasterId.type, (XmlOptions) null);
        }

        public static STSlideMasterId parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSlideMasterId) POIXMLTypeLoader.parse(file, STSlideMasterId.type, xmlOptions);
        }

        public static STSlideMasterId parse(URL url) throws XmlException, IOException {
            return (STSlideMasterId) POIXMLTypeLoader.parse(url, STSlideMasterId.type, (XmlOptions) null);
        }

        public static STSlideMasterId parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSlideMasterId) POIXMLTypeLoader.parse(url, STSlideMasterId.type, xmlOptions);
        }

        public static STSlideMasterId parse(InputStream inputStream) throws XmlException, IOException {
            return (STSlideMasterId) POIXMLTypeLoader.parse(inputStream, STSlideMasterId.type, (XmlOptions) null);
        }

        public static STSlideMasterId parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSlideMasterId) POIXMLTypeLoader.parse(inputStream, STSlideMasterId.type, xmlOptions);
        }

        public static STSlideMasterId parse(Reader reader) throws XmlException, IOException {
            return (STSlideMasterId) POIXMLTypeLoader.parse(reader, STSlideMasterId.type, (XmlOptions) null);
        }

        public static STSlideMasterId parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STSlideMasterId) POIXMLTypeLoader.parse(reader, STSlideMasterId.type, xmlOptions);
        }

        public static STSlideMasterId parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STSlideMasterId) POIXMLTypeLoader.parse(xMLStreamReader, STSlideMasterId.type, (XmlOptions) null);
        }

        public static STSlideMasterId parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STSlideMasterId) POIXMLTypeLoader.parse(xMLStreamReader, STSlideMasterId.type, xmlOptions);
        }

        public static STSlideMasterId parse(Node node) throws XmlException {
            return (STSlideMasterId) POIXMLTypeLoader.parse(node, STSlideMasterId.type, (XmlOptions) null);
        }

        public static STSlideMasterId parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STSlideMasterId) POIXMLTypeLoader.parse(node, STSlideMasterId.type, xmlOptions);
        }

        public static STSlideMasterId parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STSlideMasterId) POIXMLTypeLoader.parse(xMLInputStream, STSlideMasterId.type, (XmlOptions) null);
        }

        public static STSlideMasterId parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STSlideMasterId) POIXMLTypeLoader.parse(xMLInputStream, STSlideMasterId.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STSlideMasterId.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STSlideMasterId.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
