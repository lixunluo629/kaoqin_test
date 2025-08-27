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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTable.class */
public interface CTTable extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTable.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttable5f3ftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/CTTable$Factory.class */
    public static final class Factory {
        public static CTTable newInstance() {
            return (CTTable) POIXMLTypeLoader.newInstance(CTTable.type, null);
        }

        public static CTTable newInstance(XmlOptions xmlOptions) {
            return (CTTable) POIXMLTypeLoader.newInstance(CTTable.type, xmlOptions);
        }

        public static CTTable parse(String str) throws XmlException {
            return (CTTable) POIXMLTypeLoader.parse(str, CTTable.type, (XmlOptions) null);
        }

        public static CTTable parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTable) POIXMLTypeLoader.parse(str, CTTable.type, xmlOptions);
        }

        public static CTTable parse(File file) throws XmlException, IOException {
            return (CTTable) POIXMLTypeLoader.parse(file, CTTable.type, (XmlOptions) null);
        }

        public static CTTable parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTable) POIXMLTypeLoader.parse(file, CTTable.type, xmlOptions);
        }

        public static CTTable parse(URL url) throws XmlException, IOException {
            return (CTTable) POIXMLTypeLoader.parse(url, CTTable.type, (XmlOptions) null);
        }

        public static CTTable parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTable) POIXMLTypeLoader.parse(url, CTTable.type, xmlOptions);
        }

        public static CTTable parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTable) POIXMLTypeLoader.parse(inputStream, CTTable.type, (XmlOptions) null);
        }

        public static CTTable parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTable) POIXMLTypeLoader.parse(inputStream, CTTable.type, xmlOptions);
        }

        public static CTTable parse(Reader reader) throws XmlException, IOException {
            return (CTTable) POIXMLTypeLoader.parse(reader, CTTable.type, (XmlOptions) null);
        }

        public static CTTable parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTable) POIXMLTypeLoader.parse(reader, CTTable.type, xmlOptions);
        }

        public static CTTable parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTable) POIXMLTypeLoader.parse(xMLStreamReader, CTTable.type, (XmlOptions) null);
        }

        public static CTTable parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTable) POIXMLTypeLoader.parse(xMLStreamReader, CTTable.type, xmlOptions);
        }

        public static CTTable parse(Node node) throws XmlException {
            return (CTTable) POIXMLTypeLoader.parse(node, CTTable.type, (XmlOptions) null);
        }

        public static CTTable parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTable) POIXMLTypeLoader.parse(node, CTTable.type, xmlOptions);
        }

        public static CTTable parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTable) POIXMLTypeLoader.parse(xMLInputStream, CTTable.type, (XmlOptions) null);
        }

        public static CTTable parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTable) POIXMLTypeLoader.parse(xMLInputStream, CTTable.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTable.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTable.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTTableProperties getTblPr();

    boolean isSetTblPr();

    void setTblPr(CTTableProperties cTTableProperties);

    CTTableProperties addNewTblPr();

    void unsetTblPr();

    CTTableGrid getTblGrid();

    void setTblGrid(CTTableGrid cTTableGrid);

    CTTableGrid addNewTblGrid();

    List<CTTableRow> getTrList();

    CTTableRow[] getTrArray();

    CTTableRow getTrArray(int i);

    int sizeOfTrArray();

    void setTrArray(CTTableRow[] cTTableRowArr);

    void setTrArray(int i, CTTableRow cTTableRow);

    CTTableRow insertNewTr(int i);

    CTTableRow addNewTr();

    void removeTr(int i);
}
