package org.openxmlformats.schemas.wordprocessingml.x2006.main;

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
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTcPrInner.class */
public interface CTTcPrInner extends CTTcPrBase {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTcPrInner.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttcprinnerc56dtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTTcPrInner$Factory.class */
    public static final class Factory {
        public static CTTcPrInner newInstance() {
            return (CTTcPrInner) POIXMLTypeLoader.newInstance(CTTcPrInner.type, null);
        }

        public static CTTcPrInner newInstance(XmlOptions xmlOptions) {
            return (CTTcPrInner) POIXMLTypeLoader.newInstance(CTTcPrInner.type, xmlOptions);
        }

        public static CTTcPrInner parse(String str) throws XmlException {
            return (CTTcPrInner) POIXMLTypeLoader.parse(str, CTTcPrInner.type, (XmlOptions) null);
        }

        public static CTTcPrInner parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTcPrInner) POIXMLTypeLoader.parse(str, CTTcPrInner.type, xmlOptions);
        }

        public static CTTcPrInner parse(File file) throws XmlException, IOException {
            return (CTTcPrInner) POIXMLTypeLoader.parse(file, CTTcPrInner.type, (XmlOptions) null);
        }

        public static CTTcPrInner parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTcPrInner) POIXMLTypeLoader.parse(file, CTTcPrInner.type, xmlOptions);
        }

        public static CTTcPrInner parse(URL url) throws XmlException, IOException {
            return (CTTcPrInner) POIXMLTypeLoader.parse(url, CTTcPrInner.type, (XmlOptions) null);
        }

        public static CTTcPrInner parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTcPrInner) POIXMLTypeLoader.parse(url, CTTcPrInner.type, xmlOptions);
        }

        public static CTTcPrInner parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTcPrInner) POIXMLTypeLoader.parse(inputStream, CTTcPrInner.type, (XmlOptions) null);
        }

        public static CTTcPrInner parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTcPrInner) POIXMLTypeLoader.parse(inputStream, CTTcPrInner.type, xmlOptions);
        }

        public static CTTcPrInner parse(Reader reader) throws XmlException, IOException {
            return (CTTcPrInner) POIXMLTypeLoader.parse(reader, CTTcPrInner.type, (XmlOptions) null);
        }

        public static CTTcPrInner parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTcPrInner) POIXMLTypeLoader.parse(reader, CTTcPrInner.type, xmlOptions);
        }

        public static CTTcPrInner parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTcPrInner) POIXMLTypeLoader.parse(xMLStreamReader, CTTcPrInner.type, (XmlOptions) null);
        }

        public static CTTcPrInner parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTcPrInner) POIXMLTypeLoader.parse(xMLStreamReader, CTTcPrInner.type, xmlOptions);
        }

        public static CTTcPrInner parse(Node node) throws XmlException {
            return (CTTcPrInner) POIXMLTypeLoader.parse(node, CTTcPrInner.type, (XmlOptions) null);
        }

        public static CTTcPrInner parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTcPrInner) POIXMLTypeLoader.parse(node, CTTcPrInner.type, xmlOptions);
        }

        public static CTTcPrInner parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTcPrInner) POIXMLTypeLoader.parse(xMLInputStream, CTTcPrInner.type, (XmlOptions) null);
        }

        public static CTTcPrInner parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTcPrInner) POIXMLTypeLoader.parse(xMLInputStream, CTTcPrInner.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTcPrInner.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTcPrInner.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTTrackChange getCellIns();

    boolean isSetCellIns();

    void setCellIns(CTTrackChange cTTrackChange);

    CTTrackChange addNewCellIns();

    void unsetCellIns();

    CTTrackChange getCellDel();

    boolean isSetCellDel();

    void setCellDel(CTTrackChange cTTrackChange);

    CTTrackChange addNewCellDel();

    void unsetCellDel();

    CTCellMergeTrackChange getCellMerge();

    boolean isSetCellMerge();

    void setCellMerge(CTCellMergeTrackChange cTCellMergeTrackChange);

    CTCellMergeTrackChange addNewCellMerge();

    void unsetCellMerge();
}
