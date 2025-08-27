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
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTSlideIdListEntry.class */
public interface CTSlideIdListEntry extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSlideIdListEntry.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctslideidlistentry427dtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTSlideIdListEntry$Factory.class */
    public static final class Factory {
        public static CTSlideIdListEntry newInstance() {
            return (CTSlideIdListEntry) POIXMLTypeLoader.newInstance(CTSlideIdListEntry.type, null);
        }

        public static CTSlideIdListEntry newInstance(XmlOptions xmlOptions) {
            return (CTSlideIdListEntry) POIXMLTypeLoader.newInstance(CTSlideIdListEntry.type, xmlOptions);
        }

        public static CTSlideIdListEntry parse(String str) throws XmlException {
            return (CTSlideIdListEntry) POIXMLTypeLoader.parse(str, CTSlideIdListEntry.type, (XmlOptions) null);
        }

        public static CTSlideIdListEntry parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSlideIdListEntry) POIXMLTypeLoader.parse(str, CTSlideIdListEntry.type, xmlOptions);
        }

        public static CTSlideIdListEntry parse(File file) throws XmlException, IOException {
            return (CTSlideIdListEntry) POIXMLTypeLoader.parse(file, CTSlideIdListEntry.type, (XmlOptions) null);
        }

        public static CTSlideIdListEntry parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideIdListEntry) POIXMLTypeLoader.parse(file, CTSlideIdListEntry.type, xmlOptions);
        }

        public static CTSlideIdListEntry parse(URL url) throws XmlException, IOException {
            return (CTSlideIdListEntry) POIXMLTypeLoader.parse(url, CTSlideIdListEntry.type, (XmlOptions) null);
        }

        public static CTSlideIdListEntry parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideIdListEntry) POIXMLTypeLoader.parse(url, CTSlideIdListEntry.type, xmlOptions);
        }

        public static CTSlideIdListEntry parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSlideIdListEntry) POIXMLTypeLoader.parse(inputStream, CTSlideIdListEntry.type, (XmlOptions) null);
        }

        public static CTSlideIdListEntry parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideIdListEntry) POIXMLTypeLoader.parse(inputStream, CTSlideIdListEntry.type, xmlOptions);
        }

        public static CTSlideIdListEntry parse(Reader reader) throws XmlException, IOException {
            return (CTSlideIdListEntry) POIXMLTypeLoader.parse(reader, CTSlideIdListEntry.type, (XmlOptions) null);
        }

        public static CTSlideIdListEntry parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideIdListEntry) POIXMLTypeLoader.parse(reader, CTSlideIdListEntry.type, xmlOptions);
        }

        public static CTSlideIdListEntry parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSlideIdListEntry) POIXMLTypeLoader.parse(xMLStreamReader, CTSlideIdListEntry.type, (XmlOptions) null);
        }

        public static CTSlideIdListEntry parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSlideIdListEntry) POIXMLTypeLoader.parse(xMLStreamReader, CTSlideIdListEntry.type, xmlOptions);
        }

        public static CTSlideIdListEntry parse(Node node) throws XmlException {
            return (CTSlideIdListEntry) POIXMLTypeLoader.parse(node, CTSlideIdListEntry.type, (XmlOptions) null);
        }

        public static CTSlideIdListEntry parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSlideIdListEntry) POIXMLTypeLoader.parse(node, CTSlideIdListEntry.type, xmlOptions);
        }

        public static CTSlideIdListEntry parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSlideIdListEntry) POIXMLTypeLoader.parse(xMLInputStream, CTSlideIdListEntry.type, (XmlOptions) null);
        }

        public static CTSlideIdListEntry parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSlideIdListEntry) POIXMLTypeLoader.parse(xMLInputStream, CTSlideIdListEntry.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSlideIdListEntry.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSlideIdListEntry.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    long getId();

    STSlideId xgetId();

    void setId(long j);

    void xsetId(STSlideId sTSlideId);

    String getId2();

    STRelationshipId xgetId2();

    void setId2(String str);

    void xsetId2(STRelationshipId sTRelationshipId);
}
