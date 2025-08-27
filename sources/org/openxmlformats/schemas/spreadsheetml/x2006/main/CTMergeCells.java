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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTMergeCells.class */
public interface CTMergeCells extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTMergeCells.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctmergecells1242type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTMergeCells$Factory.class */
    public static final class Factory {
        public static CTMergeCells newInstance() {
            return (CTMergeCells) POIXMLTypeLoader.newInstance(CTMergeCells.type, null);
        }

        public static CTMergeCells newInstance(XmlOptions xmlOptions) {
            return (CTMergeCells) POIXMLTypeLoader.newInstance(CTMergeCells.type, xmlOptions);
        }

        public static CTMergeCells parse(String str) throws XmlException {
            return (CTMergeCells) POIXMLTypeLoader.parse(str, CTMergeCells.type, (XmlOptions) null);
        }

        public static CTMergeCells parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTMergeCells) POIXMLTypeLoader.parse(str, CTMergeCells.type, xmlOptions);
        }

        public static CTMergeCells parse(File file) throws XmlException, IOException {
            return (CTMergeCells) POIXMLTypeLoader.parse(file, CTMergeCells.type, (XmlOptions) null);
        }

        public static CTMergeCells parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMergeCells) POIXMLTypeLoader.parse(file, CTMergeCells.type, xmlOptions);
        }

        public static CTMergeCells parse(URL url) throws XmlException, IOException {
            return (CTMergeCells) POIXMLTypeLoader.parse(url, CTMergeCells.type, (XmlOptions) null);
        }

        public static CTMergeCells parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMergeCells) POIXMLTypeLoader.parse(url, CTMergeCells.type, xmlOptions);
        }

        public static CTMergeCells parse(InputStream inputStream) throws XmlException, IOException {
            return (CTMergeCells) POIXMLTypeLoader.parse(inputStream, CTMergeCells.type, (XmlOptions) null);
        }

        public static CTMergeCells parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMergeCells) POIXMLTypeLoader.parse(inputStream, CTMergeCells.type, xmlOptions);
        }

        public static CTMergeCells parse(Reader reader) throws XmlException, IOException {
            return (CTMergeCells) POIXMLTypeLoader.parse(reader, CTMergeCells.type, (XmlOptions) null);
        }

        public static CTMergeCells parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTMergeCells) POIXMLTypeLoader.parse(reader, CTMergeCells.type, xmlOptions);
        }

        public static CTMergeCells parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTMergeCells) POIXMLTypeLoader.parse(xMLStreamReader, CTMergeCells.type, (XmlOptions) null);
        }

        public static CTMergeCells parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTMergeCells) POIXMLTypeLoader.parse(xMLStreamReader, CTMergeCells.type, xmlOptions);
        }

        public static CTMergeCells parse(Node node) throws XmlException {
            return (CTMergeCells) POIXMLTypeLoader.parse(node, CTMergeCells.type, (XmlOptions) null);
        }

        public static CTMergeCells parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTMergeCells) POIXMLTypeLoader.parse(node, CTMergeCells.type, xmlOptions);
        }

        public static CTMergeCells parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTMergeCells) POIXMLTypeLoader.parse(xMLInputStream, CTMergeCells.type, (XmlOptions) null);
        }

        public static CTMergeCells parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTMergeCells) POIXMLTypeLoader.parse(xMLInputStream, CTMergeCells.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTMergeCells.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTMergeCells.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTMergeCell> getMergeCellList();

    CTMergeCell[] getMergeCellArray();

    CTMergeCell getMergeCellArray(int i);

    int sizeOfMergeCellArray();

    void setMergeCellArray(CTMergeCell[] cTMergeCellArr);

    void setMergeCellArray(int i, CTMergeCell cTMergeCell);

    CTMergeCell insertNewMergeCell(int i);

    CTMergeCell addNewMergeCell();

    void removeMergeCell(int i);

    long getCount();

    XmlUnsignedInt xgetCount();

    boolean isSetCount();

    void setCount(long j);

    void xsetCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetCount();
}
