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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTNumFmts.class */
public interface CTNumFmts extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTNumFmts.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctnumfmtsb58btype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTNumFmts$Factory.class */
    public static final class Factory {
        public static CTNumFmts newInstance() {
            return (CTNumFmts) POIXMLTypeLoader.newInstance(CTNumFmts.type, null);
        }

        public static CTNumFmts newInstance(XmlOptions xmlOptions) {
            return (CTNumFmts) POIXMLTypeLoader.newInstance(CTNumFmts.type, xmlOptions);
        }

        public static CTNumFmts parse(String str) throws XmlException {
            return (CTNumFmts) POIXMLTypeLoader.parse(str, CTNumFmts.type, (XmlOptions) null);
        }

        public static CTNumFmts parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTNumFmts) POIXMLTypeLoader.parse(str, CTNumFmts.type, xmlOptions);
        }

        public static CTNumFmts parse(File file) throws XmlException, IOException {
            return (CTNumFmts) POIXMLTypeLoader.parse(file, CTNumFmts.type, (XmlOptions) null);
        }

        public static CTNumFmts parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumFmts) POIXMLTypeLoader.parse(file, CTNumFmts.type, xmlOptions);
        }

        public static CTNumFmts parse(URL url) throws XmlException, IOException {
            return (CTNumFmts) POIXMLTypeLoader.parse(url, CTNumFmts.type, (XmlOptions) null);
        }

        public static CTNumFmts parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumFmts) POIXMLTypeLoader.parse(url, CTNumFmts.type, xmlOptions);
        }

        public static CTNumFmts parse(InputStream inputStream) throws XmlException, IOException {
            return (CTNumFmts) POIXMLTypeLoader.parse(inputStream, CTNumFmts.type, (XmlOptions) null);
        }

        public static CTNumFmts parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumFmts) POIXMLTypeLoader.parse(inputStream, CTNumFmts.type, xmlOptions);
        }

        public static CTNumFmts parse(Reader reader) throws XmlException, IOException {
            return (CTNumFmts) POIXMLTypeLoader.parse(reader, CTNumFmts.type, (XmlOptions) null);
        }

        public static CTNumFmts parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumFmts) POIXMLTypeLoader.parse(reader, CTNumFmts.type, xmlOptions);
        }

        public static CTNumFmts parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTNumFmts) POIXMLTypeLoader.parse(xMLStreamReader, CTNumFmts.type, (XmlOptions) null);
        }

        public static CTNumFmts parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTNumFmts) POIXMLTypeLoader.parse(xMLStreamReader, CTNumFmts.type, xmlOptions);
        }

        public static CTNumFmts parse(Node node) throws XmlException {
            return (CTNumFmts) POIXMLTypeLoader.parse(node, CTNumFmts.type, (XmlOptions) null);
        }

        public static CTNumFmts parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTNumFmts) POIXMLTypeLoader.parse(node, CTNumFmts.type, xmlOptions);
        }

        public static CTNumFmts parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTNumFmts) POIXMLTypeLoader.parse(xMLInputStream, CTNumFmts.type, (XmlOptions) null);
        }

        public static CTNumFmts parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTNumFmts) POIXMLTypeLoader.parse(xMLInputStream, CTNumFmts.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTNumFmts.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTNumFmts.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTNumFmt> getNumFmtList();

    CTNumFmt[] getNumFmtArray();

    CTNumFmt getNumFmtArray(int i);

    int sizeOfNumFmtArray();

    void setNumFmtArray(CTNumFmt[] cTNumFmtArr);

    void setNumFmtArray(int i, CTNumFmt cTNumFmt);

    CTNumFmt insertNewNumFmt(int i);

    CTNumFmt addNewNumFmt();

    void removeNumFmt(int i);

    long getCount();

    XmlUnsignedInt xgetCount();

    boolean isSetCount();

    void setCount(long j);

    void xsetCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetCount();
}
