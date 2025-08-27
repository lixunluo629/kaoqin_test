package org.openxmlformats.schemas.presentationml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTSlideMasterIdList.class */
public interface CTSlideMasterIdList extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSlideMasterIdList.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctslidemasteridlist0b63type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/CTSlideMasterIdList$Factory.class */
    public static final class Factory {
        public static CTSlideMasterIdList newInstance() {
            return (CTSlideMasterIdList) POIXMLTypeLoader.newInstance(CTSlideMasterIdList.type, null);
        }

        public static CTSlideMasterIdList newInstance(XmlOptions xmlOptions) {
            return (CTSlideMasterIdList) POIXMLTypeLoader.newInstance(CTSlideMasterIdList.type, xmlOptions);
        }

        public static CTSlideMasterIdList parse(String str) throws XmlException {
            return (CTSlideMasterIdList) POIXMLTypeLoader.parse(str, CTSlideMasterIdList.type, (XmlOptions) null);
        }

        public static CTSlideMasterIdList parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSlideMasterIdList) POIXMLTypeLoader.parse(str, CTSlideMasterIdList.type, xmlOptions);
        }

        public static CTSlideMasterIdList parse(File file) throws XmlException, IOException {
            return (CTSlideMasterIdList) POIXMLTypeLoader.parse(file, CTSlideMasterIdList.type, (XmlOptions) null);
        }

        public static CTSlideMasterIdList parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideMasterIdList) POIXMLTypeLoader.parse(file, CTSlideMasterIdList.type, xmlOptions);
        }

        public static CTSlideMasterIdList parse(URL url) throws XmlException, IOException {
            return (CTSlideMasterIdList) POIXMLTypeLoader.parse(url, CTSlideMasterIdList.type, (XmlOptions) null);
        }

        public static CTSlideMasterIdList parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideMasterIdList) POIXMLTypeLoader.parse(url, CTSlideMasterIdList.type, xmlOptions);
        }

        public static CTSlideMasterIdList parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSlideMasterIdList) POIXMLTypeLoader.parse(inputStream, CTSlideMasterIdList.type, (XmlOptions) null);
        }

        public static CTSlideMasterIdList parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideMasterIdList) POIXMLTypeLoader.parse(inputStream, CTSlideMasterIdList.type, xmlOptions);
        }

        public static CTSlideMasterIdList parse(Reader reader) throws XmlException, IOException {
            return (CTSlideMasterIdList) POIXMLTypeLoader.parse(reader, CTSlideMasterIdList.type, (XmlOptions) null);
        }

        public static CTSlideMasterIdList parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSlideMasterIdList) POIXMLTypeLoader.parse(reader, CTSlideMasterIdList.type, xmlOptions);
        }

        public static CTSlideMasterIdList parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSlideMasterIdList) POIXMLTypeLoader.parse(xMLStreamReader, CTSlideMasterIdList.type, (XmlOptions) null);
        }

        public static CTSlideMasterIdList parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSlideMasterIdList) POIXMLTypeLoader.parse(xMLStreamReader, CTSlideMasterIdList.type, xmlOptions);
        }

        public static CTSlideMasterIdList parse(Node node) throws XmlException {
            return (CTSlideMasterIdList) POIXMLTypeLoader.parse(node, CTSlideMasterIdList.type, (XmlOptions) null);
        }

        public static CTSlideMasterIdList parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSlideMasterIdList) POIXMLTypeLoader.parse(node, CTSlideMasterIdList.type, xmlOptions);
        }

        public static CTSlideMasterIdList parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSlideMasterIdList) POIXMLTypeLoader.parse(xMLInputStream, CTSlideMasterIdList.type, (XmlOptions) null);
        }

        public static CTSlideMasterIdList parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSlideMasterIdList) POIXMLTypeLoader.parse(xMLInputStream, CTSlideMasterIdList.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSlideMasterIdList.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSlideMasterIdList.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTSlideMasterIdListEntry> getSldMasterIdList();

    CTSlideMasterIdListEntry[] getSldMasterIdArray();

    CTSlideMasterIdListEntry getSldMasterIdArray(int i);

    int sizeOfSldMasterIdArray();

    void setSldMasterIdArray(CTSlideMasterIdListEntry[] cTSlideMasterIdListEntryArr);

    void setSldMasterIdArray(int i, CTSlideMasterIdListEntry cTSlideMasterIdListEntry);

    CTSlideMasterIdListEntry insertNewSldMasterId(int i);

    CTSlideMasterIdListEntry addNewSldMasterId();

    void removeSldMasterId(int i);
}
