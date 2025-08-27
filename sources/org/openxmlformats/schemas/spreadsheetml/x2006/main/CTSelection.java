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
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSelection.class */
public interface CTSelection extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSelection.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctselectionca2btype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSelection$Factory.class */
    public static final class Factory {
        public static CTSelection newInstance() {
            return (CTSelection) POIXMLTypeLoader.newInstance(CTSelection.type, null);
        }

        public static CTSelection newInstance(XmlOptions xmlOptions) {
            return (CTSelection) POIXMLTypeLoader.newInstance(CTSelection.type, xmlOptions);
        }

        public static CTSelection parse(String str) throws XmlException {
            return (CTSelection) POIXMLTypeLoader.parse(str, CTSelection.type, (XmlOptions) null);
        }

        public static CTSelection parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSelection) POIXMLTypeLoader.parse(str, CTSelection.type, xmlOptions);
        }

        public static CTSelection parse(File file) throws XmlException, IOException {
            return (CTSelection) POIXMLTypeLoader.parse(file, CTSelection.type, (XmlOptions) null);
        }

        public static CTSelection parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSelection) POIXMLTypeLoader.parse(file, CTSelection.type, xmlOptions);
        }

        public static CTSelection parse(URL url) throws XmlException, IOException {
            return (CTSelection) POIXMLTypeLoader.parse(url, CTSelection.type, (XmlOptions) null);
        }

        public static CTSelection parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSelection) POIXMLTypeLoader.parse(url, CTSelection.type, xmlOptions);
        }

        public static CTSelection parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSelection) POIXMLTypeLoader.parse(inputStream, CTSelection.type, (XmlOptions) null);
        }

        public static CTSelection parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSelection) POIXMLTypeLoader.parse(inputStream, CTSelection.type, xmlOptions);
        }

        public static CTSelection parse(Reader reader) throws XmlException, IOException {
            return (CTSelection) POIXMLTypeLoader.parse(reader, CTSelection.type, (XmlOptions) null);
        }

        public static CTSelection parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSelection) POIXMLTypeLoader.parse(reader, CTSelection.type, xmlOptions);
        }

        public static CTSelection parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSelection) POIXMLTypeLoader.parse(xMLStreamReader, CTSelection.type, (XmlOptions) null);
        }

        public static CTSelection parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSelection) POIXMLTypeLoader.parse(xMLStreamReader, CTSelection.type, xmlOptions);
        }

        public static CTSelection parse(Node node) throws XmlException {
            return (CTSelection) POIXMLTypeLoader.parse(node, CTSelection.type, (XmlOptions) null);
        }

        public static CTSelection parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSelection) POIXMLTypeLoader.parse(node, CTSelection.type, xmlOptions);
        }

        public static CTSelection parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSelection) POIXMLTypeLoader.parse(xMLInputStream, CTSelection.type, (XmlOptions) null);
        }

        public static CTSelection parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSelection) POIXMLTypeLoader.parse(xMLInputStream, CTSelection.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSelection.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSelection.type, xmlOptions);
        }

        private Factory() {
        }
    }

    STPane.Enum getPane();

    STPane xgetPane();

    boolean isSetPane();

    void setPane(STPane.Enum r1);

    void xsetPane(STPane sTPane);

    void unsetPane();

    String getActiveCell();

    STCellRef xgetActiveCell();

    boolean isSetActiveCell();

    void setActiveCell(String str);

    void xsetActiveCell(STCellRef sTCellRef);

    void unsetActiveCell();

    long getActiveCellId();

    XmlUnsignedInt xgetActiveCellId();

    boolean isSetActiveCellId();

    void setActiveCellId(long j);

    void xsetActiveCellId(XmlUnsignedInt xmlUnsignedInt);

    void unsetActiveCellId();

    List getSqref();

    STSqref xgetSqref();

    boolean isSetSqref();

    void setSqref(List list);

    void xsetSqref(STSqref sTSqref);

    void unsetSqref();
}
