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
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTEmpty.class */
public interface CTEmpty extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTEmpty.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctempty3fa5type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTEmpty$Factory.class */
    public static final class Factory {
        public static CTEmpty newInstance() {
            return (CTEmpty) POIXMLTypeLoader.newInstance(CTEmpty.type, null);
        }

        public static CTEmpty newInstance(XmlOptions xmlOptions) {
            return (CTEmpty) POIXMLTypeLoader.newInstance(CTEmpty.type, xmlOptions);
        }

        public static CTEmpty parse(String str) throws XmlException {
            return (CTEmpty) POIXMLTypeLoader.parse(str, CTEmpty.type, (XmlOptions) null);
        }

        public static CTEmpty parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTEmpty) POIXMLTypeLoader.parse(str, CTEmpty.type, xmlOptions);
        }

        public static CTEmpty parse(File file) throws XmlException, IOException {
            return (CTEmpty) POIXMLTypeLoader.parse(file, CTEmpty.type, (XmlOptions) null);
        }

        public static CTEmpty parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTEmpty) POIXMLTypeLoader.parse(file, CTEmpty.type, xmlOptions);
        }

        public static CTEmpty parse(URL url) throws XmlException, IOException {
            return (CTEmpty) POIXMLTypeLoader.parse(url, CTEmpty.type, (XmlOptions) null);
        }

        public static CTEmpty parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTEmpty) POIXMLTypeLoader.parse(url, CTEmpty.type, xmlOptions);
        }

        public static CTEmpty parse(InputStream inputStream) throws XmlException, IOException {
            return (CTEmpty) POIXMLTypeLoader.parse(inputStream, CTEmpty.type, (XmlOptions) null);
        }

        public static CTEmpty parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTEmpty) POIXMLTypeLoader.parse(inputStream, CTEmpty.type, xmlOptions);
        }

        public static CTEmpty parse(Reader reader) throws XmlException, IOException {
            return (CTEmpty) POIXMLTypeLoader.parse(reader, CTEmpty.type, (XmlOptions) null);
        }

        public static CTEmpty parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTEmpty) POIXMLTypeLoader.parse(reader, CTEmpty.type, xmlOptions);
        }

        public static CTEmpty parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTEmpty) POIXMLTypeLoader.parse(xMLStreamReader, CTEmpty.type, (XmlOptions) null);
        }

        public static CTEmpty parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTEmpty) POIXMLTypeLoader.parse(xMLStreamReader, CTEmpty.type, xmlOptions);
        }

        public static CTEmpty parse(Node node) throws XmlException {
            return (CTEmpty) POIXMLTypeLoader.parse(node, CTEmpty.type, (XmlOptions) null);
        }

        public static CTEmpty parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTEmpty) POIXMLTypeLoader.parse(node, CTEmpty.type, xmlOptions);
        }

        public static CTEmpty parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTEmpty) POIXMLTypeLoader.parse(xMLInputStream, CTEmpty.type, (XmlOptions) null);
        }

        public static CTEmpty parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTEmpty) POIXMLTypeLoader.parse(xMLInputStream, CTEmpty.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTEmpty.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTEmpty.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
