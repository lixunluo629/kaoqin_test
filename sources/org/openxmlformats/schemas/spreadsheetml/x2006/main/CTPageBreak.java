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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPageBreak.class */
public interface CTPageBreak extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPageBreak.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpagebreakeb4ftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPageBreak$Factory.class */
    public static final class Factory {
        public static CTPageBreak newInstance() {
            return (CTPageBreak) POIXMLTypeLoader.newInstance(CTPageBreak.type, null);
        }

        public static CTPageBreak newInstance(XmlOptions xmlOptions) {
            return (CTPageBreak) POIXMLTypeLoader.newInstance(CTPageBreak.type, xmlOptions);
        }

        public static CTPageBreak parse(String str) throws XmlException {
            return (CTPageBreak) POIXMLTypeLoader.parse(str, CTPageBreak.type, (XmlOptions) null);
        }

        public static CTPageBreak parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPageBreak) POIXMLTypeLoader.parse(str, CTPageBreak.type, xmlOptions);
        }

        public static CTPageBreak parse(File file) throws XmlException, IOException {
            return (CTPageBreak) POIXMLTypeLoader.parse(file, CTPageBreak.type, (XmlOptions) null);
        }

        public static CTPageBreak parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageBreak) POIXMLTypeLoader.parse(file, CTPageBreak.type, xmlOptions);
        }

        public static CTPageBreak parse(URL url) throws XmlException, IOException {
            return (CTPageBreak) POIXMLTypeLoader.parse(url, CTPageBreak.type, (XmlOptions) null);
        }

        public static CTPageBreak parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageBreak) POIXMLTypeLoader.parse(url, CTPageBreak.type, xmlOptions);
        }

        public static CTPageBreak parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPageBreak) POIXMLTypeLoader.parse(inputStream, CTPageBreak.type, (XmlOptions) null);
        }

        public static CTPageBreak parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageBreak) POIXMLTypeLoader.parse(inputStream, CTPageBreak.type, xmlOptions);
        }

        public static CTPageBreak parse(Reader reader) throws XmlException, IOException {
            return (CTPageBreak) POIXMLTypeLoader.parse(reader, CTPageBreak.type, (XmlOptions) null);
        }

        public static CTPageBreak parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPageBreak) POIXMLTypeLoader.parse(reader, CTPageBreak.type, xmlOptions);
        }

        public static CTPageBreak parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPageBreak) POIXMLTypeLoader.parse(xMLStreamReader, CTPageBreak.type, (XmlOptions) null);
        }

        public static CTPageBreak parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPageBreak) POIXMLTypeLoader.parse(xMLStreamReader, CTPageBreak.type, xmlOptions);
        }

        public static CTPageBreak parse(Node node) throws XmlException {
            return (CTPageBreak) POIXMLTypeLoader.parse(node, CTPageBreak.type, (XmlOptions) null);
        }

        public static CTPageBreak parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPageBreak) POIXMLTypeLoader.parse(node, CTPageBreak.type, xmlOptions);
        }

        public static CTPageBreak parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPageBreak) POIXMLTypeLoader.parse(xMLInputStream, CTPageBreak.type, (XmlOptions) null);
        }

        public static CTPageBreak parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPageBreak) POIXMLTypeLoader.parse(xMLInputStream, CTPageBreak.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPageBreak.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPageBreak.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTBreak> getBrkList();

    CTBreak[] getBrkArray();

    CTBreak getBrkArray(int i);

    int sizeOfBrkArray();

    void setBrkArray(CTBreak[] cTBreakArr);

    void setBrkArray(int i, CTBreak cTBreak);

    CTBreak insertNewBrk(int i);

    CTBreak addNewBrk();

    void removeBrk(int i);

    long getCount();

    XmlUnsignedInt xgetCount();

    boolean isSetCount();

    void setCount(long j);

    void xsetCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetCount();

    long getManualBreakCount();

    XmlUnsignedInt xgetManualBreakCount();

    boolean isSetManualBreakCount();

    void setManualBreakCount(long j);

    void xsetManualBreakCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetManualBreakCount();
}
