package org.openxmlformats.schemas.spreadsheetml.x2006.main;

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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTMapInfo.class */
public interface CTMapInfo extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTMapInfo.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctmapinfo1a09type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTMapInfo$Factory.class */
    public static final class Factory {
        public static CTMapInfo newInstance() {
            return (CTMapInfo) POIXMLTypeLoader.newInstance(CTMapInfo.type, null);
        }

        public static CTMapInfo newInstance(XmlOptions xmlOptions) {
            return (CTMapInfo) POIXMLTypeLoader.newInstance(CTMapInfo.type, xmlOptions);
        }

        public static CTMapInfo parse(String str) throws XmlException {
            return (CTMapInfo) POIXMLTypeLoader.parse(str, CTMapInfo.type, (XmlOptions) null);
        }

        public static CTMapInfo parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTMapInfo) POIXMLTypeLoader.parse(str, CTMapInfo.type, xmlOptions);
        }

        public static CTMapInfo parse(File file) throws XmlException, IOException {
            return (CTMapInfo) POIXMLTypeLoader.parse(file, CTMapInfo.type, (XmlOptions) null);
        }

        public static CTMapInfo parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMapInfo) POIXMLTypeLoader.parse(file, CTMapInfo.type, xmlOptions);
        }

        public static CTMapInfo parse(URL url) throws XmlException, IOException {
            return (CTMapInfo) POIXMLTypeLoader.parse(url, CTMapInfo.type, (XmlOptions) null);
        }

        public static CTMapInfo parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMapInfo) POIXMLTypeLoader.parse(url, CTMapInfo.type, xmlOptions);
        }

        public static CTMapInfo parse(InputStream inputStream) throws XmlException, IOException {
            return (CTMapInfo) POIXMLTypeLoader.parse(inputStream, CTMapInfo.type, (XmlOptions) null);
        }

        public static CTMapInfo parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMapInfo) POIXMLTypeLoader.parse(inputStream, CTMapInfo.type, xmlOptions);
        }

        public static CTMapInfo parse(Reader reader) throws XmlException, IOException {
            return (CTMapInfo) POIXMLTypeLoader.parse(reader, CTMapInfo.type, (XmlOptions) null);
        }

        public static CTMapInfo parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMapInfo) POIXMLTypeLoader.parse(reader, CTMapInfo.type, xmlOptions);
        }

        public static CTMapInfo parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTMapInfo) POIXMLTypeLoader.parse(xMLStreamReader, CTMapInfo.type, (XmlOptions) null);
        }

        public static CTMapInfo parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTMapInfo) POIXMLTypeLoader.parse(xMLStreamReader, CTMapInfo.type, xmlOptions);
        }

        public static CTMapInfo parse(Node node) throws XmlException {
            return (CTMapInfo) POIXMLTypeLoader.parse(node, CTMapInfo.type, (XmlOptions) null);
        }

        public static CTMapInfo parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTMapInfo) POIXMLTypeLoader.parse(node, CTMapInfo.type, xmlOptions);
        }

        public static CTMapInfo parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTMapInfo) POIXMLTypeLoader.parse(xMLInputStream, CTMapInfo.type, (XmlOptions) null);
        }

        public static CTMapInfo parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTMapInfo) POIXMLTypeLoader.parse(xMLInputStream, CTMapInfo.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTMapInfo.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTMapInfo.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTSchema> getSchemaList();

    CTSchema[] getSchemaArray();

    CTSchema getSchemaArray(int i);

    int sizeOfSchemaArray();

    void setSchemaArray(CTSchema[] cTSchemaArr);

    void setSchemaArray(int i, CTSchema cTSchema);

    CTSchema insertNewSchema(int i);

    CTSchema addNewSchema();

    void removeSchema(int i);

    List<CTMap> getMapList();

    CTMap[] getMapArray();

    CTMap getMapArray(int i);

    int sizeOfMapArray();

    void setMapArray(CTMap[] cTMapArr);

    void setMapArray(int i, CTMap cTMap);

    CTMap insertNewMap(int i);

    CTMap addNewMap();

    void removeMap(int i);

    String getSelectionNamespaces();

    XmlString xgetSelectionNamespaces();

    void setSelectionNamespaces(String str);

    void xsetSelectionNamespaces(XmlString xmlString);
}
