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
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSheetProtection.class */
public interface CTSheetProtection extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSheetProtection.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctsheetprotection22f7type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSheetProtection$Factory.class */
    public static final class Factory {
        public static CTSheetProtection newInstance() {
            return (CTSheetProtection) POIXMLTypeLoader.newInstance(CTSheetProtection.type, null);
        }

        public static CTSheetProtection newInstance(XmlOptions xmlOptions) {
            return (CTSheetProtection) POIXMLTypeLoader.newInstance(CTSheetProtection.type, xmlOptions);
        }

        public static CTSheetProtection parse(String str) throws XmlException {
            return (CTSheetProtection) POIXMLTypeLoader.parse(str, CTSheetProtection.type, (XmlOptions) null);
        }

        public static CTSheetProtection parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSheetProtection) POIXMLTypeLoader.parse(str, CTSheetProtection.type, xmlOptions);
        }

        public static CTSheetProtection parse(File file) throws XmlException, IOException {
            return (CTSheetProtection) POIXMLTypeLoader.parse(file, CTSheetProtection.type, (XmlOptions) null);
        }

        public static CTSheetProtection parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetProtection) POIXMLTypeLoader.parse(file, CTSheetProtection.type, xmlOptions);
        }

        public static CTSheetProtection parse(URL url) throws XmlException, IOException {
            return (CTSheetProtection) POIXMLTypeLoader.parse(url, CTSheetProtection.type, (XmlOptions) null);
        }

        public static CTSheetProtection parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetProtection) POIXMLTypeLoader.parse(url, CTSheetProtection.type, xmlOptions);
        }

        public static CTSheetProtection parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSheetProtection) POIXMLTypeLoader.parse(inputStream, CTSheetProtection.type, (XmlOptions) null);
        }

        public static CTSheetProtection parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetProtection) POIXMLTypeLoader.parse(inputStream, CTSheetProtection.type, xmlOptions);
        }

        public static CTSheetProtection parse(Reader reader) throws XmlException, IOException {
            return (CTSheetProtection) POIXMLTypeLoader.parse(reader, CTSheetProtection.type, (XmlOptions) null);
        }

        public static CTSheetProtection parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetProtection) POIXMLTypeLoader.parse(reader, CTSheetProtection.type, xmlOptions);
        }

        public static CTSheetProtection parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSheetProtection) POIXMLTypeLoader.parse(xMLStreamReader, CTSheetProtection.type, (XmlOptions) null);
        }

        public static CTSheetProtection parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSheetProtection) POIXMLTypeLoader.parse(xMLStreamReader, CTSheetProtection.type, xmlOptions);
        }

        public static CTSheetProtection parse(Node node) throws XmlException {
            return (CTSheetProtection) POIXMLTypeLoader.parse(node, CTSheetProtection.type, (XmlOptions) null);
        }

        public static CTSheetProtection parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSheetProtection) POIXMLTypeLoader.parse(node, CTSheetProtection.type, xmlOptions);
        }

        public static CTSheetProtection parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSheetProtection) POIXMLTypeLoader.parse(xMLInputStream, CTSheetProtection.type, (XmlOptions) null);
        }

        public static CTSheetProtection parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSheetProtection) POIXMLTypeLoader.parse(xMLInputStream, CTSheetProtection.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSheetProtection.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSheetProtection.type, xmlOptions);
        }

        private Factory() {
        }
    }

    byte[] getPassword();

    STUnsignedShortHex xgetPassword();

    boolean isSetPassword();

    void setPassword(byte[] bArr);

    void xsetPassword(STUnsignedShortHex sTUnsignedShortHex);

    void unsetPassword();

    boolean getSheet();

    XmlBoolean xgetSheet();

    boolean isSetSheet();

    void setSheet(boolean z);

    void xsetSheet(XmlBoolean xmlBoolean);

    void unsetSheet();

    boolean getObjects();

    XmlBoolean xgetObjects();

    boolean isSetObjects();

    void setObjects(boolean z);

    void xsetObjects(XmlBoolean xmlBoolean);

    void unsetObjects();

    boolean getScenarios();

    XmlBoolean xgetScenarios();

    boolean isSetScenarios();

    void setScenarios(boolean z);

    void xsetScenarios(XmlBoolean xmlBoolean);

    void unsetScenarios();

    boolean getFormatCells();

    XmlBoolean xgetFormatCells();

    boolean isSetFormatCells();

    void setFormatCells(boolean z);

    void xsetFormatCells(XmlBoolean xmlBoolean);

    void unsetFormatCells();

    boolean getFormatColumns();

    XmlBoolean xgetFormatColumns();

    boolean isSetFormatColumns();

    void setFormatColumns(boolean z);

    void xsetFormatColumns(XmlBoolean xmlBoolean);

    void unsetFormatColumns();

    boolean getFormatRows();

    XmlBoolean xgetFormatRows();

    boolean isSetFormatRows();

    void setFormatRows(boolean z);

    void xsetFormatRows(XmlBoolean xmlBoolean);

    void unsetFormatRows();

    boolean getInsertColumns();

    XmlBoolean xgetInsertColumns();

    boolean isSetInsertColumns();

    void setInsertColumns(boolean z);

    void xsetInsertColumns(XmlBoolean xmlBoolean);

    void unsetInsertColumns();

    boolean getInsertRows();

    XmlBoolean xgetInsertRows();

    boolean isSetInsertRows();

    void setInsertRows(boolean z);

    void xsetInsertRows(XmlBoolean xmlBoolean);

    void unsetInsertRows();

    boolean getInsertHyperlinks();

    XmlBoolean xgetInsertHyperlinks();

    boolean isSetInsertHyperlinks();

    void setInsertHyperlinks(boolean z);

    void xsetInsertHyperlinks(XmlBoolean xmlBoolean);

    void unsetInsertHyperlinks();

    boolean getDeleteColumns();

    XmlBoolean xgetDeleteColumns();

    boolean isSetDeleteColumns();

    void setDeleteColumns(boolean z);

    void xsetDeleteColumns(XmlBoolean xmlBoolean);

    void unsetDeleteColumns();

    boolean getDeleteRows();

    XmlBoolean xgetDeleteRows();

    boolean isSetDeleteRows();

    void setDeleteRows(boolean z);

    void xsetDeleteRows(XmlBoolean xmlBoolean);

    void unsetDeleteRows();

    boolean getSelectLockedCells();

    XmlBoolean xgetSelectLockedCells();

    boolean isSetSelectLockedCells();

    void setSelectLockedCells(boolean z);

    void xsetSelectLockedCells(XmlBoolean xmlBoolean);

    void unsetSelectLockedCells();

    boolean getSort();

    XmlBoolean xgetSort();

    boolean isSetSort();

    void setSort(boolean z);

    void xsetSort(XmlBoolean xmlBoolean);

    void unsetSort();

    boolean getAutoFilter();

    XmlBoolean xgetAutoFilter();

    boolean isSetAutoFilter();

    void setAutoFilter(boolean z);

    void xsetAutoFilter(XmlBoolean xmlBoolean);

    void unsetAutoFilter();

    boolean getPivotTables();

    XmlBoolean xgetPivotTables();

    boolean isSetPivotTables();

    void setPivotTables(boolean z);

    void xsetPivotTables(XmlBoolean xmlBoolean);

    void unsetPivotTables();

    boolean getSelectUnlockedCells();

    XmlBoolean xgetSelectUnlockedCells();

    boolean isSetSelectUnlockedCells();

    void setSelectUnlockedCells(boolean z);

    void xsetSelectUnlockedCells(XmlBoolean xmlBoolean);

    void unsetSelectUnlockedCells();
}
