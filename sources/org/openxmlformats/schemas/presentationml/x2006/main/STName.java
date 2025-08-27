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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/STName.class */
public interface STName extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STName.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stnameadaatype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/STName$Factory.class */
    public static final class Factory {
        public static STName newValue(Object obj) {
            return (STName) STName.type.newValue(obj);
        }

        public static STName newInstance() {
            return (STName) POIXMLTypeLoader.newInstance(STName.type, null);
        }

        public static STName newInstance(XmlOptions xmlOptions) {
            return (STName) POIXMLTypeLoader.newInstance(STName.type, xmlOptions);
        }

        public static STName parse(String str) throws XmlException {
            return (STName) POIXMLTypeLoader.parse(str, STName.type, (XmlOptions) null);
        }

        public static STName parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STName) POIXMLTypeLoader.parse(str, STName.type, xmlOptions);
        }

        public static STName parse(File file) throws XmlException, IOException {
            return (STName) POIXMLTypeLoader.parse(file, STName.type, (XmlOptions) null);
        }

        public static STName parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STName) POIXMLTypeLoader.parse(file, STName.type, xmlOptions);
        }

        public static STName parse(URL url) throws XmlException, IOException {
            return (STName) POIXMLTypeLoader.parse(url, STName.type, (XmlOptions) null);
        }

        public static STName parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STName) POIXMLTypeLoader.parse(url, STName.type, xmlOptions);
        }

        public static STName parse(InputStream inputStream) throws XmlException, IOException {
            return (STName) POIXMLTypeLoader.parse(inputStream, STName.type, (XmlOptions) null);
        }

        public static STName parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STName) POIXMLTypeLoader.parse(inputStream, STName.type, xmlOptions);
        }

        public static STName parse(Reader reader) throws XmlException, IOException {
            return (STName) POIXMLTypeLoader.parse(reader, STName.type, (XmlOptions) null);
        }

        public static STName parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STName) POIXMLTypeLoader.parse(reader, STName.type, xmlOptions);
        }

        public static STName parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STName) POIXMLTypeLoader.parse(xMLStreamReader, STName.type, (XmlOptions) null);
        }

        public static STName parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STName) POIXMLTypeLoader.parse(xMLStreamReader, STName.type, xmlOptions);
        }

        public static STName parse(Node node) throws XmlException {
            return (STName) POIXMLTypeLoader.parse(node, STName.type, (XmlOptions) null);
        }

        public static STName parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STName) POIXMLTypeLoader.parse(node, STName.type, xmlOptions);
        }

        public static STName parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STName) POIXMLTypeLoader.parse(xMLInputStream, STName.type, (XmlOptions) null);
        }

        public static STName parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STName) POIXMLTypeLoader.parse(xMLInputStream, STName.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STName.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STName.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
