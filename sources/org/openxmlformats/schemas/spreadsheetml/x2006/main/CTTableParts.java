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
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTTableParts.class */
public interface CTTableParts extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTableParts.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttablepartsf6bbtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTTableParts$Factory.class */
    public static final class Factory {
        public static CTTableParts newInstance() {
            return (CTTableParts) POIXMLTypeLoader.newInstance(CTTableParts.type, null);
        }

        public static CTTableParts newInstance(XmlOptions xmlOptions) {
            return (CTTableParts) POIXMLTypeLoader.newInstance(CTTableParts.type, xmlOptions);
        }

        public static CTTableParts parse(String str) throws XmlException {
            return (CTTableParts) POIXMLTypeLoader.parse(str, CTTableParts.type, (XmlOptions) null);
        }

        public static CTTableParts parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTableParts) POIXMLTypeLoader.parse(str, CTTableParts.type, xmlOptions);
        }

        public static CTTableParts parse(File file) throws XmlException, IOException {
            return (CTTableParts) POIXMLTypeLoader.parse(file, CTTableParts.type, (XmlOptions) null);
        }

        public static CTTableParts parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableParts) POIXMLTypeLoader.parse(file, CTTableParts.type, xmlOptions);
        }

        public static CTTableParts parse(URL url) throws XmlException, IOException {
            return (CTTableParts) POIXMLTypeLoader.parse(url, CTTableParts.type, (XmlOptions) null);
        }

        public static CTTableParts parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableParts) POIXMLTypeLoader.parse(url, CTTableParts.type, xmlOptions);
        }

        public static CTTableParts parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTableParts) POIXMLTypeLoader.parse(inputStream, CTTableParts.type, (XmlOptions) null);
        }

        public static CTTableParts parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableParts) POIXMLTypeLoader.parse(inputStream, CTTableParts.type, xmlOptions);
        }

        public static CTTableParts parse(Reader reader) throws XmlException, IOException {
            return (CTTableParts) POIXMLTypeLoader.parse(reader, CTTableParts.type, (XmlOptions) null);
        }

        public static CTTableParts parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableParts) POIXMLTypeLoader.parse(reader, CTTableParts.type, xmlOptions);
        }

        public static CTTableParts parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTableParts) POIXMLTypeLoader.parse(xMLStreamReader, CTTableParts.type, (XmlOptions) null);
        }

        public static CTTableParts parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTableParts) POIXMLTypeLoader.parse(xMLStreamReader, CTTableParts.type, xmlOptions);
        }

        public static CTTableParts parse(Node node) throws XmlException {
            return (CTTableParts) POIXMLTypeLoader.parse(node, CTTableParts.type, (XmlOptions) null);
        }

        public static CTTableParts parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTableParts) POIXMLTypeLoader.parse(node, CTTableParts.type, xmlOptions);
        }

        public static CTTableParts parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTableParts) POIXMLTypeLoader.parse(xMLInputStream, CTTableParts.type, (XmlOptions) null);
        }

        public static CTTableParts parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTableParts) POIXMLTypeLoader.parse(xMLInputStream, CTTableParts.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTableParts.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTableParts.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTTablePart> getTablePartList();

    CTTablePart[] getTablePartArray();

    CTTablePart getTablePartArray(int i);

    int sizeOfTablePartArray();

    void setTablePartArray(CTTablePart[] cTTablePartArr);

    void setTablePartArray(int i, CTTablePart cTTablePart);

    CTTablePart insertNewTablePart(int i);

    CTTablePart addNewTablePart();

    void removeTablePart(int i);

    long getCount();

    XmlUnsignedInt xgetCount();

    boolean isSetCount();

    void setCount(long j);

    void xsetCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetCount();
}
