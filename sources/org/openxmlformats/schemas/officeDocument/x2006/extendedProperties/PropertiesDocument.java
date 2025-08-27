package org.openxmlformats.schemas.officeDocument.x2006.extendedProperties;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/officeDocument/x2006/extendedProperties/PropertiesDocument.class */
public interface PropertiesDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(PropertiesDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("propertiesee84doctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/officeDocument/x2006/extendedProperties/PropertiesDocument$Factory.class */
    public static final class Factory {
        public static PropertiesDocument newInstance() {
            return (PropertiesDocument) POIXMLTypeLoader.newInstance(PropertiesDocument.type, null);
        }

        public static PropertiesDocument newInstance(XmlOptions xmlOptions) {
            return (PropertiesDocument) POIXMLTypeLoader.newInstance(PropertiesDocument.type, xmlOptions);
        }

        public static PropertiesDocument parse(String str) throws XmlException {
            return (PropertiesDocument) POIXMLTypeLoader.parse(str, PropertiesDocument.type, (XmlOptions) null);
        }

        public static PropertiesDocument parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (PropertiesDocument) POIXMLTypeLoader.parse(str, PropertiesDocument.type, xmlOptions);
        }

        public static PropertiesDocument parse(File file) throws XmlException, IOException {
            return (PropertiesDocument) POIXMLTypeLoader.parse(file, PropertiesDocument.type, (XmlOptions) null);
        }

        public static PropertiesDocument parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (PropertiesDocument) POIXMLTypeLoader.parse(file, PropertiesDocument.type, xmlOptions);
        }

        public static PropertiesDocument parse(URL url) throws XmlException, IOException {
            return (PropertiesDocument) POIXMLTypeLoader.parse(url, PropertiesDocument.type, (XmlOptions) null);
        }

        public static PropertiesDocument parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (PropertiesDocument) POIXMLTypeLoader.parse(url, PropertiesDocument.type, xmlOptions);
        }

        public static PropertiesDocument parse(InputStream inputStream) throws XmlException, IOException {
            return (PropertiesDocument) POIXMLTypeLoader.parse(inputStream, PropertiesDocument.type, (XmlOptions) null);
        }

        public static PropertiesDocument parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (PropertiesDocument) POIXMLTypeLoader.parse(inputStream, PropertiesDocument.type, xmlOptions);
        }

        public static PropertiesDocument parse(Reader reader) throws XmlException, IOException {
            return (PropertiesDocument) POIXMLTypeLoader.parse(reader, PropertiesDocument.type, (XmlOptions) null);
        }

        public static PropertiesDocument parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (PropertiesDocument) POIXMLTypeLoader.parse(reader, PropertiesDocument.type, xmlOptions);
        }

        public static PropertiesDocument parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (PropertiesDocument) POIXMLTypeLoader.parse(xMLStreamReader, PropertiesDocument.type, (XmlOptions) null);
        }

        public static PropertiesDocument parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (PropertiesDocument) POIXMLTypeLoader.parse(xMLStreamReader, PropertiesDocument.type, xmlOptions);
        }

        public static PropertiesDocument parse(Node node) throws XmlException {
            return (PropertiesDocument) POIXMLTypeLoader.parse(node, PropertiesDocument.type, (XmlOptions) null);
        }

        public static PropertiesDocument parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (PropertiesDocument) POIXMLTypeLoader.parse(node, PropertiesDocument.type, xmlOptions);
        }

        public static PropertiesDocument parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (PropertiesDocument) POIXMLTypeLoader.parse(xMLInputStream, PropertiesDocument.type, (XmlOptions) null);
        }

        public static PropertiesDocument parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (PropertiesDocument) POIXMLTypeLoader.parse(xMLInputStream, PropertiesDocument.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, PropertiesDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, PropertiesDocument.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTProperties getProperties();

    void setProperties(CTProperties cTProperties);

    CTProperties addNewProperties();
}
