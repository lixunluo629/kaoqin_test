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
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTTable.class */
public interface CTTable extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTable.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttable736dtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTTable$Factory.class */
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

    CTAutoFilter getAutoFilter();

    boolean isSetAutoFilter();

    void setAutoFilter(CTAutoFilter cTAutoFilter);

    CTAutoFilter addNewAutoFilter();

    void unsetAutoFilter();

    CTSortState getSortState();

    boolean isSetSortState();

    void setSortState(CTSortState cTSortState);

    CTSortState addNewSortState();

    void unsetSortState();

    CTTableColumns getTableColumns();

    void setTableColumns(CTTableColumns cTTableColumns);

    CTTableColumns addNewTableColumns();

    CTTableStyleInfo getTableStyleInfo();

    boolean isSetTableStyleInfo();

    void setTableStyleInfo(CTTableStyleInfo cTTableStyleInfo);

    CTTableStyleInfo addNewTableStyleInfo();

    void unsetTableStyleInfo();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    long getId();

    XmlUnsignedInt xgetId();

    void setId(long j);

    void xsetId(XmlUnsignedInt xmlUnsignedInt);

    String getName();

    STXstring xgetName();

    boolean isSetName();

    void setName(String str);

    void xsetName(STXstring sTXstring);

    void unsetName();

    String getDisplayName();

    STXstring xgetDisplayName();

    void setDisplayName(String str);

    void xsetDisplayName(STXstring sTXstring);

    String getComment();

    STXstring xgetComment();

    boolean isSetComment();

    void setComment(String str);

    void xsetComment(STXstring sTXstring);

    void unsetComment();

    String getRef();

    STRef xgetRef();

    void setRef(String str);

    void xsetRef(STRef sTRef);

    STTableType$Enum getTableType();

    STTableType xgetTableType();

    boolean isSetTableType();

    void setTableType(STTableType$Enum sTTableType$Enum);

    void xsetTableType(STTableType sTTableType);

    void unsetTableType();

    long getHeaderRowCount();

    XmlUnsignedInt xgetHeaderRowCount();

    boolean isSetHeaderRowCount();

    void setHeaderRowCount(long j);

    void xsetHeaderRowCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetHeaderRowCount();

    boolean getInsertRow();

    XmlBoolean xgetInsertRow();

    boolean isSetInsertRow();

    void setInsertRow(boolean z);

    void xsetInsertRow(XmlBoolean xmlBoolean);

    void unsetInsertRow();

    boolean getInsertRowShift();

    XmlBoolean xgetInsertRowShift();

    boolean isSetInsertRowShift();

    void setInsertRowShift(boolean z);

    void xsetInsertRowShift(XmlBoolean xmlBoolean);

    void unsetInsertRowShift();

    long getTotalsRowCount();

    XmlUnsignedInt xgetTotalsRowCount();

    boolean isSetTotalsRowCount();

    void setTotalsRowCount(long j);

    void xsetTotalsRowCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetTotalsRowCount();

    boolean getTotalsRowShown();

    XmlBoolean xgetTotalsRowShown();

    boolean isSetTotalsRowShown();

    void setTotalsRowShown(boolean z);

    void xsetTotalsRowShown(XmlBoolean xmlBoolean);

    void unsetTotalsRowShown();

    boolean getPublished();

    XmlBoolean xgetPublished();

    boolean isSetPublished();

    void setPublished(boolean z);

    void xsetPublished(XmlBoolean xmlBoolean);

    void unsetPublished();

    long getHeaderRowDxfId();

    STDxfId xgetHeaderRowDxfId();

    boolean isSetHeaderRowDxfId();

    void setHeaderRowDxfId(long j);

    void xsetHeaderRowDxfId(STDxfId sTDxfId);

    void unsetHeaderRowDxfId();

    long getDataDxfId();

    STDxfId xgetDataDxfId();

    boolean isSetDataDxfId();

    void setDataDxfId(long j);

    void xsetDataDxfId(STDxfId sTDxfId);

    void unsetDataDxfId();

    long getTotalsRowDxfId();

    STDxfId xgetTotalsRowDxfId();

    boolean isSetTotalsRowDxfId();

    void setTotalsRowDxfId(long j);

    void xsetTotalsRowDxfId(STDxfId sTDxfId);

    void unsetTotalsRowDxfId();

    long getHeaderRowBorderDxfId();

    STDxfId xgetHeaderRowBorderDxfId();

    boolean isSetHeaderRowBorderDxfId();

    void setHeaderRowBorderDxfId(long j);

    void xsetHeaderRowBorderDxfId(STDxfId sTDxfId);

    void unsetHeaderRowBorderDxfId();

    long getTableBorderDxfId();

    STDxfId xgetTableBorderDxfId();

    boolean isSetTableBorderDxfId();

    void setTableBorderDxfId(long j);

    void xsetTableBorderDxfId(STDxfId sTDxfId);

    void unsetTableBorderDxfId();

    long getTotalsRowBorderDxfId();

    STDxfId xgetTotalsRowBorderDxfId();

    boolean isSetTotalsRowBorderDxfId();

    void setTotalsRowBorderDxfId(long j);

    void xsetTotalsRowBorderDxfId(STDxfId sTDxfId);

    void unsetTotalsRowBorderDxfId();

    String getHeaderRowCellStyle();

    STXstring xgetHeaderRowCellStyle();

    boolean isSetHeaderRowCellStyle();

    void setHeaderRowCellStyle(String str);

    void xsetHeaderRowCellStyle(STXstring sTXstring);

    void unsetHeaderRowCellStyle();

    String getDataCellStyle();

    STXstring xgetDataCellStyle();

    boolean isSetDataCellStyle();

    void setDataCellStyle(String str);

    void xsetDataCellStyle(STXstring sTXstring);

    void unsetDataCellStyle();

    String getTotalsRowCellStyle();

    STXstring xgetTotalsRowCellStyle();

    boolean isSetTotalsRowCellStyle();

    void setTotalsRowCellStyle(String str);

    void xsetTotalsRowCellStyle(STXstring sTXstring);

    void unsetTotalsRowCellStyle();

    long getConnectionId();

    XmlUnsignedInt xgetConnectionId();

    boolean isSetConnectionId();

    void setConnectionId(long j);

    void xsetConnectionId(XmlUnsignedInt xmlUnsignedInt);

    void unsetConnectionId();
}
