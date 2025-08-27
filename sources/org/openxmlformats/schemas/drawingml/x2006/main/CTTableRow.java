package org.openxmlformats.schemas.drawingml.x2006.main;

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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTableRow.class */
public interface CTTableRow extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTableRow.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttablerow4ac7type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTableRow$Factory.class */
    public static final class Factory {
        public static CTTableRow newInstance() {
            return (CTTableRow) POIXMLTypeLoader.newInstance(CTTableRow.type, null);
        }

        public static CTTableRow newInstance(XmlOptions xmlOptions) {
            return (CTTableRow) POIXMLTypeLoader.newInstance(CTTableRow.type, xmlOptions);
        }

        public static CTTableRow parse(String str) throws XmlException {
            return (CTTableRow) POIXMLTypeLoader.parse(str, CTTableRow.type, (XmlOptions) null);
        }

        public static CTTableRow parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTableRow) POIXMLTypeLoader.parse(str, CTTableRow.type, xmlOptions);
        }

        public static CTTableRow parse(File file) throws XmlException, IOException {
            return (CTTableRow) POIXMLTypeLoader.parse(file, CTTableRow.type, (XmlOptions) null);
        }

        public static CTTableRow parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableRow) POIXMLTypeLoader.parse(file, CTTableRow.type, xmlOptions);
        }

        public static CTTableRow parse(URL url) throws XmlException, IOException {
            return (CTTableRow) POIXMLTypeLoader.parse(url, CTTableRow.type, (XmlOptions) null);
        }

        public static CTTableRow parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableRow) POIXMLTypeLoader.parse(url, CTTableRow.type, xmlOptions);
        }

        public static CTTableRow parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTableRow) POIXMLTypeLoader.parse(inputStream, CTTableRow.type, (XmlOptions) null);
        }

        public static CTTableRow parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableRow) POIXMLTypeLoader.parse(inputStream, CTTableRow.type, xmlOptions);
        }

        public static CTTableRow parse(Reader reader) throws XmlException, IOException {
            return (CTTableRow) POIXMLTypeLoader.parse(reader, CTTableRow.type, (XmlOptions) null);
        }

        public static CTTableRow parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableRow) POIXMLTypeLoader.parse(reader, CTTableRow.type, xmlOptions);
        }

        public static CTTableRow parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTableRow) POIXMLTypeLoader.parse(xMLStreamReader, CTTableRow.type, (XmlOptions) null);
        }

        public static CTTableRow parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTableRow) POIXMLTypeLoader.parse(xMLStreamReader, CTTableRow.type, xmlOptions);
        }

        public static CTTableRow parse(Node node) throws XmlException {
            return (CTTableRow) POIXMLTypeLoader.parse(node, CTTableRow.type, (XmlOptions) null);
        }

        public static CTTableRow parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTableRow) POIXMLTypeLoader.parse(node, CTTableRow.type, xmlOptions);
        }

        public static CTTableRow parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTableRow) POIXMLTypeLoader.parse(xMLInputStream, CTTableRow.type, (XmlOptions) null);
        }

        public static CTTableRow parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTableRow) POIXMLTypeLoader.parse(xMLInputStream, CTTableRow.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTableRow.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTableRow.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<CTTableCell> getTcList();

    CTTableCell[] getTcArray();

    CTTableCell getTcArray(int i);

    int sizeOfTcArray();

    void setTcArray(CTTableCell[] cTTableCellArr);

    void setTcArray(int i, CTTableCell cTTableCell);

    CTTableCell insertNewTc(int i);

    CTTableCell addNewTc();

    void removeTc(int i);

    CTOfficeArtExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList);

    CTOfficeArtExtensionList addNewExtLst();

    void unsetExtLst();

    long getH();

    STCoordinate xgetH();

    void setH(long j);

    void xsetH(STCoordinate sTCoordinate);
}
