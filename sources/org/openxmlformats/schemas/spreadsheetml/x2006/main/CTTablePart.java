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
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTTablePart.class */
public interface CTTablePart extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTablePart.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttablepart1140type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTTablePart$Factory.class */
    public static final class Factory {
        public static CTTablePart newInstance() {
            return (CTTablePart) POIXMLTypeLoader.newInstance(CTTablePart.type, null);
        }

        public static CTTablePart newInstance(XmlOptions xmlOptions) {
            return (CTTablePart) POIXMLTypeLoader.newInstance(CTTablePart.type, xmlOptions);
        }

        public static CTTablePart parse(String str) throws XmlException {
            return (CTTablePart) POIXMLTypeLoader.parse(str, CTTablePart.type, (XmlOptions) null);
        }

        public static CTTablePart parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTablePart) POIXMLTypeLoader.parse(str, CTTablePart.type, xmlOptions);
        }

        public static CTTablePart parse(File file) throws XmlException, IOException {
            return (CTTablePart) POIXMLTypeLoader.parse(file, CTTablePart.type, (XmlOptions) null);
        }

        public static CTTablePart parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTablePart) POIXMLTypeLoader.parse(file, CTTablePart.type, xmlOptions);
        }

        public static CTTablePart parse(URL url) throws XmlException, IOException {
            return (CTTablePart) POIXMLTypeLoader.parse(url, CTTablePart.type, (XmlOptions) null);
        }

        public static CTTablePart parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTablePart) POIXMLTypeLoader.parse(url, CTTablePart.type, xmlOptions);
        }

        public static CTTablePart parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTablePart) POIXMLTypeLoader.parse(inputStream, CTTablePart.type, (XmlOptions) null);
        }

        public static CTTablePart parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTablePart) POIXMLTypeLoader.parse(inputStream, CTTablePart.type, xmlOptions);
        }

        public static CTTablePart parse(Reader reader) throws XmlException, IOException {
            return (CTTablePart) POIXMLTypeLoader.parse(reader, CTTablePart.type, (XmlOptions) null);
        }

        public static CTTablePart parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTablePart) POIXMLTypeLoader.parse(reader, CTTablePart.type, xmlOptions);
        }

        public static CTTablePart parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTablePart) POIXMLTypeLoader.parse(xMLStreamReader, CTTablePart.type, (XmlOptions) null);
        }

        public static CTTablePart parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTablePart) POIXMLTypeLoader.parse(xMLStreamReader, CTTablePart.type, xmlOptions);
        }

        public static CTTablePart parse(Node node) throws XmlException {
            return (CTTablePart) POIXMLTypeLoader.parse(node, CTTablePart.type, (XmlOptions) null);
        }

        public static CTTablePart parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTablePart) POIXMLTypeLoader.parse(node, CTTablePart.type, xmlOptions);
        }

        public static CTTablePart parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTablePart) POIXMLTypeLoader.parse(xMLInputStream, CTTablePart.type, (XmlOptions) null);
        }

        public static CTTablePart parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTablePart) POIXMLTypeLoader.parse(xMLInputStream, CTTablePart.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTablePart.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTablePart.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getId();

    STRelationshipId xgetId();

    void setId(String str);

    void xsetId(STRelationshipId sTRelationshipId);
}
