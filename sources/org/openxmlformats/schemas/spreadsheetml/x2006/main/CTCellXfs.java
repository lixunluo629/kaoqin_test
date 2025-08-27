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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCellXfs.class */
public interface CTCellXfs extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTCellXfs.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcellxfs1322type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCellXfs$Factory.class */
    public static final class Factory {
        public static CTCellXfs newInstance() {
            return (CTCellXfs) POIXMLTypeLoader.newInstance(CTCellXfs.type, null);
        }

        public static CTCellXfs newInstance(XmlOptions xmlOptions) {
            return (CTCellXfs) POIXMLTypeLoader.newInstance(CTCellXfs.type, xmlOptions);
        }

        public static CTCellXfs parse(String str) throws XmlException {
            return (CTCellXfs) POIXMLTypeLoader.parse(str, CTCellXfs.type, (XmlOptions) null);
        }

        public static CTCellXfs parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTCellXfs) POIXMLTypeLoader.parse(str, CTCellXfs.type, xmlOptions);
        }

        public static CTCellXfs parse(File file) throws XmlException, IOException {
            return (CTCellXfs) POIXMLTypeLoader.parse(file, CTCellXfs.type, (XmlOptions) null);
        }

        public static CTCellXfs parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCellXfs) POIXMLTypeLoader.parse(file, CTCellXfs.type, xmlOptions);
        }

        public static CTCellXfs parse(URL url) throws XmlException, IOException {
            return (CTCellXfs) POIXMLTypeLoader.parse(url, CTCellXfs.type, (XmlOptions) null);
        }

        public static CTCellXfs parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCellXfs) POIXMLTypeLoader.parse(url, CTCellXfs.type, xmlOptions);
        }

        public static CTCellXfs parse(InputStream inputStream) throws XmlException, IOException {
            return (CTCellXfs) POIXMLTypeLoader.parse(inputStream, CTCellXfs.type, (XmlOptions) null);
        }

        public static CTCellXfs parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCellXfs) POIXMLTypeLoader.parse(inputStream, CTCellXfs.type, xmlOptions);
        }

        public static CTCellXfs parse(Reader reader) throws XmlException, IOException {
            return (CTCellXfs) POIXMLTypeLoader.parse(reader, CTCellXfs.type, (XmlOptions) null);
        }

        public static CTCellXfs parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCellXfs) POIXMLTypeLoader.parse(reader, CTCellXfs.type, xmlOptions);
        }

        public static CTCellXfs parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTCellXfs) POIXMLTypeLoader.parse(xMLStreamReader, CTCellXfs.type, (XmlOptions) null);
        }

        public static CTCellXfs parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTCellXfs) POIXMLTypeLoader.parse(xMLStreamReader, CTCellXfs.type, xmlOptions);
        }

        public static CTCellXfs parse(Node node) throws XmlException {
            return (CTCellXfs) POIXMLTypeLoader.parse(node, CTCellXfs.type, (XmlOptions) null);
        }

        public static CTCellXfs parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTCellXfs) POIXMLTypeLoader.parse(node, CTCellXfs.type, xmlOptions);
        }

        public static CTCellXfs parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTCellXfs) POIXMLTypeLoader.parse(xMLInputStream, CTCellXfs.type, (XmlOptions) null);
        }

        public static CTCellXfs parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTCellXfs) POIXMLTypeLoader.parse(xMLInputStream, CTCellXfs.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCellXfs.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCellXfs.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTXf> getXfList();

    CTXf[] getXfArray();

    CTXf getXfArray(int i);

    int sizeOfXfArray();

    void setXfArray(CTXf[] cTXfArr);

    void setXfArray(int i, CTXf cTXf);

    CTXf insertNewXf(int i);

    CTXf addNewXf();

    void removeXf(int i);

    long getCount();

    XmlUnsignedInt xgetCount();

    boolean isSetCount();

    void setCount(long j);

    void xsetCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetCount();
}
