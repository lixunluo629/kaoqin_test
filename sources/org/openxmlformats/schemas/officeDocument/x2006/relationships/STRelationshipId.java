package org.openxmlformats.schemas.officeDocument.x2006.relationships;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/officeDocument/x2006/relationships/STRelationshipId.class */
public interface STRelationshipId extends XmlString {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(STRelationshipId.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("strelationshipid1e94type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/officeDocument/x2006/relationships/STRelationshipId$Factory.class */
    public static final class Factory {
        public static STRelationshipId newValue(Object obj) {
            return (STRelationshipId) STRelationshipId.type.newValue(obj);
        }

        public static STRelationshipId newInstance() {
            return (STRelationshipId) POIXMLTypeLoader.newInstance(STRelationshipId.type, null);
        }

        public static STRelationshipId newInstance(XmlOptions xmlOptions) {
            return (STRelationshipId) POIXMLTypeLoader.newInstance(STRelationshipId.type, xmlOptions);
        }

        public static STRelationshipId parse(String str) throws XmlException {
            return (STRelationshipId) POIXMLTypeLoader.parse(str, STRelationshipId.type, (XmlOptions) null);
        }

        public static STRelationshipId parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (STRelationshipId) POIXMLTypeLoader.parse(str, STRelationshipId.type, xmlOptions);
        }

        public static STRelationshipId parse(File file) throws XmlException, IOException {
            return (STRelationshipId) POIXMLTypeLoader.parse(file, STRelationshipId.type, (XmlOptions) null);
        }

        public static STRelationshipId parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STRelationshipId) POIXMLTypeLoader.parse(file, STRelationshipId.type, xmlOptions);
        }

        public static STRelationshipId parse(URL url) throws XmlException, IOException {
            return (STRelationshipId) POIXMLTypeLoader.parse(url, STRelationshipId.type, (XmlOptions) null);
        }

        public static STRelationshipId parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STRelationshipId) POIXMLTypeLoader.parse(url, STRelationshipId.type, xmlOptions);
        }

        public static STRelationshipId parse(InputStream inputStream) throws XmlException, IOException {
            return (STRelationshipId) POIXMLTypeLoader.parse(inputStream, STRelationshipId.type, (XmlOptions) null);
        }

        public static STRelationshipId parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STRelationshipId) POIXMLTypeLoader.parse(inputStream, STRelationshipId.type, xmlOptions);
        }

        public static STRelationshipId parse(Reader reader) throws XmlException, IOException {
            return (STRelationshipId) POIXMLTypeLoader.parse(reader, STRelationshipId.type, (XmlOptions) null);
        }

        public static STRelationshipId parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (STRelationshipId) POIXMLTypeLoader.parse(reader, STRelationshipId.type, xmlOptions);
        }

        public static STRelationshipId parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (STRelationshipId) POIXMLTypeLoader.parse(xMLStreamReader, STRelationshipId.type, (XmlOptions) null);
        }

        public static STRelationshipId parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (STRelationshipId) POIXMLTypeLoader.parse(xMLStreamReader, STRelationshipId.type, xmlOptions);
        }

        public static STRelationshipId parse(Node node) throws XmlException {
            return (STRelationshipId) POIXMLTypeLoader.parse(node, STRelationshipId.type, (XmlOptions) null);
        }

        public static STRelationshipId parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (STRelationshipId) POIXMLTypeLoader.parse(node, STRelationshipId.type, xmlOptions);
        }

        public static STRelationshipId parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (STRelationshipId) POIXMLTypeLoader.parse(xMLInputStream, STRelationshipId.type, (XmlOptions) null);
        }

        public static STRelationshipId parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (STRelationshipId) POIXMLTypeLoader.parse(xMLInputStream, STRelationshipId.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STRelationshipId.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, STRelationshipId.type, xmlOptions);
        }

        private Factory() {
        }
    }
}
