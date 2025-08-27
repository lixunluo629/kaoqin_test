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
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTLocation.class */
public interface CTLocation extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTLocation.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctlocationc23etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTLocation$Factory.class */
    public static final class Factory {
        public static CTLocation newInstance() {
            return (CTLocation) POIXMLTypeLoader.newInstance(CTLocation.type, null);
        }

        public static CTLocation newInstance(XmlOptions xmlOptions) {
            return (CTLocation) POIXMLTypeLoader.newInstance(CTLocation.type, xmlOptions);
        }

        public static CTLocation parse(String str) throws XmlException {
            return (CTLocation) POIXMLTypeLoader.parse(str, CTLocation.type, (XmlOptions) null);
        }

        public static CTLocation parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTLocation) POIXMLTypeLoader.parse(str, CTLocation.type, xmlOptions);
        }

        public static CTLocation parse(File file) throws XmlException, IOException {
            return (CTLocation) POIXMLTypeLoader.parse(file, CTLocation.type, (XmlOptions) null);
        }

        public static CTLocation parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLocation) POIXMLTypeLoader.parse(file, CTLocation.type, xmlOptions);
        }

        public static CTLocation parse(URL url) throws XmlException, IOException {
            return (CTLocation) POIXMLTypeLoader.parse(url, CTLocation.type, (XmlOptions) null);
        }

        public static CTLocation parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLocation) POIXMLTypeLoader.parse(url, CTLocation.type, xmlOptions);
        }

        public static CTLocation parse(InputStream inputStream) throws XmlException, IOException {
            return (CTLocation) POIXMLTypeLoader.parse(inputStream, CTLocation.type, (XmlOptions) null);
        }

        public static CTLocation parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLocation) POIXMLTypeLoader.parse(inputStream, CTLocation.type, xmlOptions);
        }

        public static CTLocation parse(Reader reader) throws XmlException, IOException {
            return (CTLocation) POIXMLTypeLoader.parse(reader, CTLocation.type, (XmlOptions) null);
        }

        public static CTLocation parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLocation) POIXMLTypeLoader.parse(reader, CTLocation.type, xmlOptions);
        }

        public static CTLocation parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTLocation) POIXMLTypeLoader.parse(xMLStreamReader, CTLocation.type, (XmlOptions) null);
        }

        public static CTLocation parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTLocation) POIXMLTypeLoader.parse(xMLStreamReader, CTLocation.type, xmlOptions);
        }

        public static CTLocation parse(Node node) throws XmlException {
            return (CTLocation) POIXMLTypeLoader.parse(node, CTLocation.type, (XmlOptions) null);
        }

        public static CTLocation parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTLocation) POIXMLTypeLoader.parse(node, CTLocation.type, xmlOptions);
        }

        public static CTLocation parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTLocation) POIXMLTypeLoader.parse(xMLInputStream, CTLocation.type, (XmlOptions) null);
        }

        public static CTLocation parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTLocation) POIXMLTypeLoader.parse(xMLInputStream, CTLocation.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLocation.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLocation.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getRef();

    STRef xgetRef();

    void setRef(String str);

    void xsetRef(STRef sTRef);

    long getFirstHeaderRow();

    XmlUnsignedInt xgetFirstHeaderRow();

    void setFirstHeaderRow(long j);

    void xsetFirstHeaderRow(XmlUnsignedInt xmlUnsignedInt);

    long getFirstDataRow();

    XmlUnsignedInt xgetFirstDataRow();

    void setFirstDataRow(long j);

    void xsetFirstDataRow(XmlUnsignedInt xmlUnsignedInt);

    long getFirstDataCol();

    XmlUnsignedInt xgetFirstDataCol();

    void setFirstDataCol(long j);

    void xsetFirstDataCol(XmlUnsignedInt xmlUnsignedInt);

    long getRowPageCount();

    XmlUnsignedInt xgetRowPageCount();

    boolean isSetRowPageCount();

    void setRowPageCount(long j);

    void xsetRowPageCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetRowPageCount();

    long getColPageCount();

    XmlUnsignedInt xgetColPageCount();

    boolean isSetColPageCount();

    void setColPageCount(long j);

    void xsetColPageCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetColPageCount();
}
