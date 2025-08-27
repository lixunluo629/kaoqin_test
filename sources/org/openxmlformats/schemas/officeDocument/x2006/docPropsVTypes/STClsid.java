package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/officeDocument/x2006/docPropsVTypes/STClsid.class */
public interface STClsid extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STClsid.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stclsida7datype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/officeDocument/x2006/docPropsVTypes/STClsid$Factory.class */
    public static final class Factory {
        public static STClsid newValue(Object obj) {
            return (STClsid) STClsid.type.newValue(obj);
        }

        public static STClsid newInstance() {
            return (STClsid) POIXMLTypeLoader.newInstance(STClsid.type, null);
        }

        public static STClsid newInstance(XmlOptions xmlOptions) {
            return (STClsid) POIXMLTypeLoader.newInstance(STClsid.type, xmlOptions);
        }

        public static STClsid parse(String str) throws XmlException {
            return (STClsid) POIXMLTypeLoader.parse(str, STClsid.type, (XmlOptions) null);
        }

        public static STClsid parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STClsid) POIXMLTypeLoader.parse(str, STClsid.type, xmlOptions);
        }

        public static STClsid parse(File file) throws XmlException, IOException {
            return (STClsid) POIXMLTypeLoader.parse(file, STClsid.type, (XmlOptions) null);
        }

        public static STClsid parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STClsid) POIXMLTypeLoader.parse(file, STClsid.type, xmlOptions);
        }

        public static STClsid parse(URL url) throws XmlException, IOException {
            return (STClsid) POIXMLTypeLoader.parse(url, STClsid.type, (XmlOptions) null);
        }

        public static STClsid parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STClsid) POIXMLTypeLoader.parse(url, STClsid.type, xmlOptions);
        }

        public static STClsid parse(InputStream inputStream) throws XmlException, IOException {
            return (STClsid) POIXMLTypeLoader.parse(inputStream, STClsid.type, (XmlOptions) null);
        }

        public static STClsid parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STClsid) POIXMLTypeLoader.parse(inputStream, STClsid.type, xmlOptions);
        }

        public static STClsid parse(Reader reader) throws XmlException, IOException {
            return (STClsid) POIXMLTypeLoader.parse(reader, STClsid.type, (XmlOptions) null);
        }

        public static STClsid parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STClsid) POIXMLTypeLoader.parse(reader, STClsid.type, xmlOptions);
        }

        public static STClsid parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STClsid) POIXMLTypeLoader.parse(xMLStreamReader, STClsid.type, (XmlOptions) null);
        }

        public static STClsid parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STClsid) POIXMLTypeLoader.parse(xMLStreamReader, STClsid.type, xmlOptions);
        }

        public static STClsid parse(Node node) throws XmlException {
            return (STClsid) POIXMLTypeLoader.parse(node, STClsid.type, (XmlOptions) null);
        }

        public static STClsid parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STClsid) POIXMLTypeLoader.parse(node, STClsid.type, xmlOptions);
        }

        public static STClsid parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STClsid) POIXMLTypeLoader.parse(xMLInputStream, STClsid.type, (XmlOptions) null);
        }

        public static STClsid parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STClsid) POIXMLTypeLoader.parse(xMLInputStream, STClsid.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STClsid.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STClsid.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
