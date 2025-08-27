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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STRef.class */
public interface STRef extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STRef.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("stref90a2type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STRef$Factory.class */
    public static final class Factory {
        public static STRef newValue(Object obj) {
            return (STRef) STRef.type.newValue(obj);
        }

        public static STRef newInstance() {
            return (STRef) POIXMLTypeLoader.newInstance(STRef.type, null);
        }

        public static STRef newInstance(XmlOptions xmlOptions) {
            return (STRef) POIXMLTypeLoader.newInstance(STRef.type, xmlOptions);
        }

        public static STRef parse(String str) throws XmlException {
            return (STRef) POIXMLTypeLoader.parse(str, STRef.type, (XmlOptions) null);
        }

        public static STRef parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STRef) POIXMLTypeLoader.parse(str, STRef.type, xmlOptions);
        }

        public static STRef parse(File file) throws XmlException, IOException {
            return (STRef) POIXMLTypeLoader.parse(file, STRef.type, (XmlOptions) null);
        }

        public static STRef parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STRef) POIXMLTypeLoader.parse(file, STRef.type, xmlOptions);
        }

        public static STRef parse(URL url) throws XmlException, IOException {
            return (STRef) POIXMLTypeLoader.parse(url, STRef.type, (XmlOptions) null);
        }

        public static STRef parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STRef) POIXMLTypeLoader.parse(url, STRef.type, xmlOptions);
        }

        public static STRef parse(InputStream inputStream) throws XmlException, IOException {
            return (STRef) POIXMLTypeLoader.parse(inputStream, STRef.type, (XmlOptions) null);
        }

        public static STRef parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STRef) POIXMLTypeLoader.parse(inputStream, STRef.type, xmlOptions);
        }

        public static STRef parse(Reader reader) throws XmlException, IOException {
            return (STRef) POIXMLTypeLoader.parse(reader, STRef.type, (XmlOptions) null);
        }

        public static STRef parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STRef) POIXMLTypeLoader.parse(reader, STRef.type, xmlOptions);
        }

        public static STRef parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STRef) POIXMLTypeLoader.parse(xMLStreamReader, STRef.type, (XmlOptions) null);
        }

        public static STRef parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STRef) POIXMLTypeLoader.parse(xMLStreamReader, STRef.type, xmlOptions);
        }

        public static STRef parse(Node node) throws XmlException {
            return (STRef) POIXMLTypeLoader.parse(node, STRef.type, (XmlOptions) null);
        }

        public static STRef parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STRef) POIXMLTypeLoader.parse(node, STRef.type, xmlOptions);
        }

        public static STRef parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STRef) POIXMLTypeLoader.parse(xMLInputStream, STRef.type, (XmlOptions) null);
        }

        public static STRef parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STRef) POIXMLTypeLoader.parse(xMLInputStream, STRef.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STRef.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STRef.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
