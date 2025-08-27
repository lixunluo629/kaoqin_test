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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTWorksheetSource.class */
public interface CTWorksheetSource extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTWorksheetSource.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctworksheetsourced4c8type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTWorksheetSource$Factory.class */
    public static final class Factory {
        public static CTWorksheetSource newInstance() {
            return (CTWorksheetSource) POIXMLTypeLoader.newInstance(CTWorksheetSource.type, null);
        }

        public static CTWorksheetSource newInstance(XmlOptions xmlOptions) {
            return (CTWorksheetSource) POIXMLTypeLoader.newInstance(CTWorksheetSource.type, xmlOptions);
        }

        public static CTWorksheetSource parse(String str) throws XmlException {
            return (CTWorksheetSource) POIXMLTypeLoader.parse(str, CTWorksheetSource.type, (XmlOptions) null);
        }

        public static CTWorksheetSource parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTWorksheetSource) POIXMLTypeLoader.parse(str, CTWorksheetSource.type, xmlOptions);
        }

        public static CTWorksheetSource parse(File file) throws XmlException, IOException {
            return (CTWorksheetSource) POIXMLTypeLoader.parse(file, CTWorksheetSource.type, (XmlOptions) null);
        }

        public static CTWorksheetSource parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorksheetSource) POIXMLTypeLoader.parse(file, CTWorksheetSource.type, xmlOptions);
        }

        public static CTWorksheetSource parse(URL url) throws XmlException, IOException {
            return (CTWorksheetSource) POIXMLTypeLoader.parse(url, CTWorksheetSource.type, (XmlOptions) null);
        }

        public static CTWorksheetSource parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorksheetSource) POIXMLTypeLoader.parse(url, CTWorksheetSource.type, xmlOptions);
        }

        public static CTWorksheetSource parse(InputStream inputStream) throws XmlException, IOException {
            return (CTWorksheetSource) POIXMLTypeLoader.parse(inputStream, CTWorksheetSource.type, (XmlOptions) null);
        }

        public static CTWorksheetSource parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorksheetSource) POIXMLTypeLoader.parse(inputStream, CTWorksheetSource.type, xmlOptions);
        }

        public static CTWorksheetSource parse(Reader reader) throws XmlException, IOException {
            return (CTWorksheetSource) POIXMLTypeLoader.parse(reader, CTWorksheetSource.type, (XmlOptions) null);
        }

        public static CTWorksheetSource parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorksheetSource) POIXMLTypeLoader.parse(reader, CTWorksheetSource.type, xmlOptions);
        }

        public static CTWorksheetSource parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTWorksheetSource) POIXMLTypeLoader.parse(xMLStreamReader, CTWorksheetSource.type, (XmlOptions) null);
        }

        public static CTWorksheetSource parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTWorksheetSource) POIXMLTypeLoader.parse(xMLStreamReader, CTWorksheetSource.type, xmlOptions);
        }

        public static CTWorksheetSource parse(Node node) throws XmlException {
            return (CTWorksheetSource) POIXMLTypeLoader.parse(node, CTWorksheetSource.type, (XmlOptions) null);
        }

        public static CTWorksheetSource parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTWorksheetSource) POIXMLTypeLoader.parse(node, CTWorksheetSource.type, xmlOptions);
        }

        public static CTWorksheetSource parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTWorksheetSource) POIXMLTypeLoader.parse(xMLInputStream, CTWorksheetSource.type, (XmlOptions) null);
        }

        public static CTWorksheetSource parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTWorksheetSource) POIXMLTypeLoader.parse(xMLInputStream, CTWorksheetSource.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTWorksheetSource.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTWorksheetSource.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getRef();

    STRef xgetRef();

    boolean isSetRef();

    void setRef(String str);

    void xsetRef(STRef sTRef);

    void unsetRef();

    String getName();

    STXstring xgetName();

    boolean isSetName();

    void setName(String str);

    void xsetName(STXstring sTXstring);

    void unsetName();

    String getSheet();

    STXstring xgetSheet();

    boolean isSetSheet();

    void setSheet(String str);

    void xsetSheet(STXstring sTXstring);

    void unsetSheet();

    String getId();

    STRelationshipId xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(STRelationshipId sTRelationshipId);

    void unsetId();
}
