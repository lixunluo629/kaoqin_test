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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCellStyleXfs.class */
public interface CTCellStyleXfs extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTCellStyleXfs.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctcellstylexfsa81ftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTCellStyleXfs$Factory.class */
    public static final class Factory {
        public static CTCellStyleXfs newInstance() {
            return (CTCellStyleXfs) POIXMLTypeLoader.newInstance(CTCellStyleXfs.type, null);
        }

        public static CTCellStyleXfs newInstance(XmlOptions xmlOptions) {
            return (CTCellStyleXfs) POIXMLTypeLoader.newInstance(CTCellStyleXfs.type, xmlOptions);
        }

        public static CTCellStyleXfs parse(String str) throws XmlException {
            return (CTCellStyleXfs) POIXMLTypeLoader.parse(str, CTCellStyleXfs.type, (XmlOptions) null);
        }

        public static CTCellStyleXfs parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTCellStyleXfs) POIXMLTypeLoader.parse(str, CTCellStyleXfs.type, xmlOptions);
        }

        public static CTCellStyleXfs parse(File file) throws XmlException, IOException {
            return (CTCellStyleXfs) POIXMLTypeLoader.parse(file, CTCellStyleXfs.type, (XmlOptions) null);
        }

        public static CTCellStyleXfs parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCellStyleXfs) POIXMLTypeLoader.parse(file, CTCellStyleXfs.type, xmlOptions);
        }

        public static CTCellStyleXfs parse(URL url) throws XmlException, IOException {
            return (CTCellStyleXfs) POIXMLTypeLoader.parse(url, CTCellStyleXfs.type, (XmlOptions) null);
        }

        public static CTCellStyleXfs parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCellStyleXfs) POIXMLTypeLoader.parse(url, CTCellStyleXfs.type, xmlOptions);
        }

        public static CTCellStyleXfs parse(InputStream inputStream) throws XmlException, IOException {
            return (CTCellStyleXfs) POIXMLTypeLoader.parse(inputStream, CTCellStyleXfs.type, (XmlOptions) null);
        }

        public static CTCellStyleXfs parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCellStyleXfs) POIXMLTypeLoader.parse(inputStream, CTCellStyleXfs.type, xmlOptions);
        }

        public static CTCellStyleXfs parse(Reader reader) throws XmlException, IOException {
            return (CTCellStyleXfs) POIXMLTypeLoader.parse(reader, CTCellStyleXfs.type, (XmlOptions) null);
        }

        public static CTCellStyleXfs parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTCellStyleXfs) POIXMLTypeLoader.parse(reader, CTCellStyleXfs.type, xmlOptions);
        }

        public static CTCellStyleXfs parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTCellStyleXfs) POIXMLTypeLoader.parse(xMLStreamReader, CTCellStyleXfs.type, (XmlOptions) null);
        }

        public static CTCellStyleXfs parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTCellStyleXfs) POIXMLTypeLoader.parse(xMLStreamReader, CTCellStyleXfs.type, xmlOptions);
        }

        public static CTCellStyleXfs parse(Node node) throws XmlException {
            return (CTCellStyleXfs) POIXMLTypeLoader.parse(node, CTCellStyleXfs.type, (XmlOptions) null);
        }

        public static CTCellStyleXfs parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTCellStyleXfs) POIXMLTypeLoader.parse(node, CTCellStyleXfs.type, xmlOptions);
        }

        public static CTCellStyleXfs parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTCellStyleXfs) POIXMLTypeLoader.parse(xMLInputStream, CTCellStyleXfs.type, (XmlOptions) null);
        }

        public static CTCellStyleXfs parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTCellStyleXfs) POIXMLTypeLoader.parse(xMLInputStream, CTCellStyleXfs.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCellStyleXfs.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTCellStyleXfs.type, xmlOptions);
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
