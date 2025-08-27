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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTSlideMasterIdListEntry.class */
public interface CTSlideMasterIdListEntry extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSlideMasterIdListEntry.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctslidemasteridlistentryae7ftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTSlideMasterIdListEntry$Factory.class */
    public static final class Factory {
        public static CTSlideMasterIdListEntry newInstance() {
            return (CTSlideMasterIdListEntry) POIXMLTypeLoader.newInstance(CTSlideMasterIdListEntry.type, null);
        }

        public static CTSlideMasterIdListEntry newInstance(XmlOptions xmlOptions) {
            return (CTSlideMasterIdListEntry) POIXMLTypeLoader.newInstance(CTSlideMasterIdListEntry.type, xmlOptions);
        }

        public static CTSlideMasterIdListEntry parse(String str) throws XmlException {
            return (CTSlideMasterIdListEntry) POIXMLTypeLoader.parse(str, CTSlideMasterIdListEntry.type, (XmlOptions) null);
        }

        public static CTSlideMasterIdListEntry parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSlideMasterIdListEntry) POIXMLTypeLoader.parse(str, CTSlideMasterIdListEntry.type, xmlOptions);
        }

        public static CTSlideMasterIdListEntry parse(File file) throws XmlException, IOException {
            return (CTSlideMasterIdListEntry) POIXMLTypeLoader.parse(file, CTSlideMasterIdListEntry.type, (XmlOptions) null);
        }

        public static CTSlideMasterIdListEntry parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideMasterIdListEntry) POIXMLTypeLoader.parse(file, CTSlideMasterIdListEntry.type, xmlOptions);
        }

        public static CTSlideMasterIdListEntry parse(URL url) throws XmlException, IOException {
            return (CTSlideMasterIdListEntry) POIXMLTypeLoader.parse(url, CTSlideMasterIdListEntry.type, (XmlOptions) null);
        }

        public static CTSlideMasterIdListEntry parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideMasterIdListEntry) POIXMLTypeLoader.parse(url, CTSlideMasterIdListEntry.type, xmlOptions);
        }

        public static CTSlideMasterIdListEntry parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSlideMasterIdListEntry) POIXMLTypeLoader.parse(inputStream, CTSlideMasterIdListEntry.type, (XmlOptions) null);
        }

        public static CTSlideMasterIdListEntry parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideMasterIdListEntry) POIXMLTypeLoader.parse(inputStream, CTSlideMasterIdListEntry.type, xmlOptions);
        }

        public static CTSlideMasterIdListEntry parse(Reader reader) throws XmlException, IOException {
            return (CTSlideMasterIdListEntry) POIXMLTypeLoader.parse(reader, CTSlideMasterIdListEntry.type, (XmlOptions) null);
        }

        public static CTSlideMasterIdListEntry parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideMasterIdListEntry) POIXMLTypeLoader.parse(reader, CTSlideMasterIdListEntry.type, xmlOptions);
        }

        public static CTSlideMasterIdListEntry parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSlideMasterIdListEntry) POIXMLTypeLoader.parse(xMLStreamReader, CTSlideMasterIdListEntry.type, (XmlOptions) null);
        }

        public static CTSlideMasterIdListEntry parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSlideMasterIdListEntry) POIXMLTypeLoader.parse(xMLStreamReader, CTSlideMasterIdListEntry.type, xmlOptions);
        }

        public static CTSlideMasterIdListEntry parse(Node node) throws XmlException {
            return (CTSlideMasterIdListEntry) POIXMLTypeLoader.parse(node, CTSlideMasterIdListEntry.type, (XmlOptions) null);
        }

        public static CTSlideMasterIdListEntry parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSlideMasterIdListEntry) POIXMLTypeLoader.parse(node, CTSlideMasterIdListEntry.type, xmlOptions);
        }

        public static CTSlideMasterIdListEntry parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSlideMasterIdListEntry) POIXMLTypeLoader.parse(xMLInputStream, CTSlideMasterIdListEntry.type, (XmlOptions) null);
        }

        public static CTSlideMasterIdListEntry parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSlideMasterIdListEntry) POIXMLTypeLoader.parse(xMLInputStream, CTSlideMasterIdListEntry.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSlideMasterIdListEntry.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSlideMasterIdListEntry.type, xmlOptions);
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

    STSlideMasterId xgetId();

    boolean isSetId();

    void setId(long j);

    void xsetId(STSlideMasterId sTSlideMasterId);

    void unsetId();

    String getId2();

    STRelationshipId xgetId2();

    void setId2(String str);

    void xsetId2(STRelationshipId sTRelationshipId);
}
