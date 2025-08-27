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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTStylesheet.class */
public interface CTStylesheet extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTStylesheet.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctstylesheet4257type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTStylesheet$Factory.class */
    public static final class Factory {
        public static CTStylesheet newInstance() {
            return (CTStylesheet) POIXMLTypeLoader.newInstance(CTStylesheet.type, null);
        }

        public static CTStylesheet newInstance(XmlOptions xmlOptions) {
            return (CTStylesheet) POIXMLTypeLoader.newInstance(CTStylesheet.type, xmlOptions);
        }

        public static CTStylesheet parse(String str) throws XmlException {
            return (CTStylesheet) POIXMLTypeLoader.parse(str, CTStylesheet.type, (XmlOptions) null);
        }

        public static CTStylesheet parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTStylesheet) POIXMLTypeLoader.parse(str, CTStylesheet.type, xmlOptions);
        }

        public static CTStylesheet parse(File file) throws XmlException, IOException {
            return (CTStylesheet) POIXMLTypeLoader.parse(file, CTStylesheet.type, (XmlOptions) null);
        }

        public static CTStylesheet parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStylesheet) POIXMLTypeLoader.parse(file, CTStylesheet.type, xmlOptions);
        }

        public static CTStylesheet parse(URL url) throws XmlException, IOException {
            return (CTStylesheet) POIXMLTypeLoader.parse(url, CTStylesheet.type, (XmlOptions) null);
        }

        public static CTStylesheet parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStylesheet) POIXMLTypeLoader.parse(url, CTStylesheet.type, xmlOptions);
        }

        public static CTStylesheet parse(InputStream inputStream) throws XmlException, IOException {
            return (CTStylesheet) POIXMLTypeLoader.parse(inputStream, CTStylesheet.type, (XmlOptions) null);
        }

        public static CTStylesheet parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStylesheet) POIXMLTypeLoader.parse(inputStream, CTStylesheet.type, xmlOptions);
        }

        public static CTStylesheet parse(Reader reader) throws XmlException, IOException {
            return (CTStylesheet) POIXMLTypeLoader.parse(reader, CTStylesheet.type, (XmlOptions) null);
        }

        public static CTStylesheet parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTStylesheet) POIXMLTypeLoader.parse(reader, CTStylesheet.type, xmlOptions);
        }

        public static CTStylesheet parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTStylesheet) POIXMLTypeLoader.parse(xMLStreamReader, CTStylesheet.type, (XmlOptions) null);
        }

        public static CTStylesheet parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTStylesheet) POIXMLTypeLoader.parse(xMLStreamReader, CTStylesheet.type, xmlOptions);
        }

        public static CTStylesheet parse(Node node) throws XmlException {
            return (CTStylesheet) POIXMLTypeLoader.parse(node, CTStylesheet.type, (XmlOptions) null);
        }

        public static CTStylesheet parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTStylesheet) POIXMLTypeLoader.parse(node, CTStylesheet.type, xmlOptions);
        }

        public static CTStylesheet parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTStylesheet) POIXMLTypeLoader.parse(xMLInputStream, CTStylesheet.type, (XmlOptions) null);
        }

        public static CTStylesheet parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTStylesheet) POIXMLTypeLoader.parse(xMLInputStream, CTStylesheet.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTStylesheet.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTStylesheet.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTNumFmts getNumFmts();

    boolean isSetNumFmts();

    void setNumFmts(CTNumFmts cTNumFmts);

    CTNumFmts addNewNumFmts();

    void unsetNumFmts();

    CTFonts getFonts();

    boolean isSetFonts();

    void setFonts(CTFonts cTFonts);

    CTFonts addNewFonts();

    void unsetFonts();

    CTFills getFills();

    boolean isSetFills();

    void setFills(CTFills cTFills);

    CTFills addNewFills();

    void unsetFills();

    CTBorders getBorders();

    boolean isSetBorders();

    void setBorders(CTBorders cTBorders);

    CTBorders addNewBorders();

    void unsetBorders();

    CTCellStyleXfs getCellStyleXfs();

    boolean isSetCellStyleXfs();

    void setCellStyleXfs(CTCellStyleXfs cTCellStyleXfs);

    CTCellStyleXfs addNewCellStyleXfs();

    void unsetCellStyleXfs();

    CTCellXfs getCellXfs();

    boolean isSetCellXfs();

    void setCellXfs(CTCellXfs cTCellXfs);

    CTCellXfs addNewCellXfs();

    void unsetCellXfs();

    CTCellStyles getCellStyles();

    boolean isSetCellStyles();

    void setCellStyles(CTCellStyles cTCellStyles);

    CTCellStyles addNewCellStyles();

    void unsetCellStyles();

    CTDxfs getDxfs();

    boolean isSetDxfs();

    void setDxfs(CTDxfs cTDxfs);

    CTDxfs addNewDxfs();

    void unsetDxfs();

    CTTableStyles getTableStyles();

    boolean isSetTableStyles();

    void setTableStyles(CTTableStyles cTTableStyles);

    CTTableStyles addNewTableStyles();

    void unsetTableStyles();

    CTColors getColors();

    boolean isSetColors();

    void setColors(CTColors cTColors);

    CTColors addNewColors();

    void unsetColors();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
