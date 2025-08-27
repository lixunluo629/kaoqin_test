package org.openxmlformats.schemas.wordprocessingml.x2006.main;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STString.class */
public interface STString extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STString.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ststringa627type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STString$Factory.class */
    public static final class Factory {
        public static STString newValue(Object obj) {
            return (STString) STString.type.newValue(obj);
        }

        public static STString newInstance() {
            return (STString) POIXMLTypeLoader.newInstance(STString.type, null);
        }

        public static STString newInstance(XmlOptions xmlOptions) {
            return (STString) POIXMLTypeLoader.newInstance(STString.type, xmlOptions);
        }

        public static STString parse(String str) throws XmlException {
            return (STString) POIXMLTypeLoader.parse(str, STString.type, (XmlOptions) null);
        }

        public static STString parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STString) POIXMLTypeLoader.parse(str, STString.type, xmlOptions);
        }

        public static STString parse(File file) throws XmlException, IOException {
            return (STString) POIXMLTypeLoader.parse(file, STString.type, (XmlOptions) null);
        }

        public static STString parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STString) POIXMLTypeLoader.parse(file, STString.type, xmlOptions);
        }

        public static STString parse(URL url) throws XmlException, IOException {
            return (STString) POIXMLTypeLoader.parse(url, STString.type, (XmlOptions) null);
        }

        public static STString parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STString) POIXMLTypeLoader.parse(url, STString.type, xmlOptions);
        }

        public static STString parse(InputStream inputStream) throws XmlException, IOException {
            return (STString) POIXMLTypeLoader.parse(inputStream, STString.type, (XmlOptions) null);
        }

        public static STString parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STString) POIXMLTypeLoader.parse(inputStream, STString.type, xmlOptions);
        }

        public static STString parse(Reader reader) throws XmlException, IOException {
            return (STString) POIXMLTypeLoader.parse(reader, STString.type, (XmlOptions) null);
        }

        public static STString parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STString) POIXMLTypeLoader.parse(reader, STString.type, xmlOptions);
        }

        public static STString parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STString) POIXMLTypeLoader.parse(xMLStreamReader, STString.type, (XmlOptions) null);
        }

        public static STString parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STString) POIXMLTypeLoader.parse(xMLStreamReader, STString.type, xmlOptions);
        }

        public static STString parse(Node node) throws XmlException {
            return (STString) POIXMLTypeLoader.parse(node, STString.type, (XmlOptions) null);
        }

        public static STString parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STString) POIXMLTypeLoader.parse(node, STString.type, xmlOptions);
        }

        public static STString parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STString) POIXMLTypeLoader.parse(xMLInputStream, STString.type, (XmlOptions) null);
        }

        public static STString parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STString) POIXMLTypeLoader.parse(xMLInputStream, STString.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STString.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STString.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
