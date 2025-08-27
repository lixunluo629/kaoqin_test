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
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STAxis;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPivotField.class */
public interface CTPivotField extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPivotField.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpivotfieldf961type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPivotField$Factory.class */
    public static final class Factory {
        public static CTPivotField newInstance() {
            return (CTPivotField) POIXMLTypeLoader.newInstance(CTPivotField.type, null);
        }

        public static CTPivotField newInstance(XmlOptions xmlOptions) {
            return (CTPivotField) POIXMLTypeLoader.newInstance(CTPivotField.type, xmlOptions);
        }

        public static CTPivotField parse(String str) throws XmlException {
            return (CTPivotField) POIXMLTypeLoader.parse(str, CTPivotField.type, (XmlOptions) null);
        }

        public static CTPivotField parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPivotField) POIXMLTypeLoader.parse(str, CTPivotField.type, xmlOptions);
        }

        public static CTPivotField parse(File file) throws XmlException, IOException {
            return (CTPivotField) POIXMLTypeLoader.parse(file, CTPivotField.type, (XmlOptions) null);
        }

        public static CTPivotField parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotField) POIXMLTypeLoader.parse(file, CTPivotField.type, xmlOptions);
        }

        public static CTPivotField parse(URL url) throws XmlException, IOException {
            return (CTPivotField) POIXMLTypeLoader.parse(url, CTPivotField.type, (XmlOptions) null);
        }

        public static CTPivotField parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotField) POIXMLTypeLoader.parse(url, CTPivotField.type, xmlOptions);
        }

        public static CTPivotField parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPivotField) POIXMLTypeLoader.parse(inputStream, CTPivotField.type, (XmlOptions) null);
        }

        public static CTPivotField parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotField) POIXMLTypeLoader.parse(inputStream, CTPivotField.type, xmlOptions);
        }

        public static CTPivotField parse(Reader reader) throws XmlException, IOException {
            return (CTPivotField) POIXMLTypeLoader.parse(reader, CTPivotField.type, (XmlOptions) null);
        }

        public static CTPivotField parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotField) POIXMLTypeLoader.parse(reader, CTPivotField.type, xmlOptions);
        }

        public static CTPivotField parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPivotField) POIXMLTypeLoader.parse(xMLStreamReader, CTPivotField.type, (XmlOptions) null);
        }

        public static CTPivotField parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPivotField) POIXMLTypeLoader.parse(xMLStreamReader, CTPivotField.type, xmlOptions);
        }

        public static CTPivotField parse(Node node) throws XmlException {
            return (CTPivotField) POIXMLTypeLoader.parse(node, CTPivotField.type, (XmlOptions) null);
        }

        public static CTPivotField parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPivotField) POIXMLTypeLoader.parse(node, CTPivotField.type, xmlOptions);
        }

        public static CTPivotField parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPivotField) POIXMLTypeLoader.parse(xMLInputStream, CTPivotField.type, (XmlOptions) null);
        }

        public static CTPivotField parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPivotField) POIXMLTypeLoader.parse(xMLInputStream, CTPivotField.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPivotField.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPivotField.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTItems getItems();

    boolean isSetItems();

    void setItems(CTItems cTItems);

    CTItems addNewItems();

    void unsetItems();

    CTAutoSortScope getAutoSortScope();

    boolean isSetAutoSortScope();

    void setAutoSortScope(CTAutoSortScope cTAutoSortScope);

    CTAutoSortScope addNewAutoSortScope();

    void unsetAutoSortScope();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    String getName();

    STXstring xgetName();

    boolean isSetName();

    void setName(String str);

    void xsetName(STXstring sTXstring);

    void unsetName();

    STAxis.Enum getAxis();

    STAxis xgetAxis();

    boolean isSetAxis();

    void setAxis(STAxis.Enum r1);

    void xsetAxis(STAxis sTAxis);

    void unsetAxis();

    boolean getDataField();

    XmlBoolean xgetDataField();

    boolean isSetDataField();

    void setDataField(boolean z);

    void xsetDataField(XmlBoolean xmlBoolean);

    void unsetDataField();

    String getSubtotalCaption();

    STXstring xgetSubtotalCaption();

    boolean isSetSubtotalCaption();

    void setSubtotalCaption(String str);

    void xsetSubtotalCaption(STXstring sTXstring);

    void unsetSubtotalCaption();

    boolean getShowDropDowns();

    XmlBoolean xgetShowDropDowns();

    boolean isSetShowDropDowns();

    void setShowDropDowns(boolean z);

    void xsetShowDropDowns(XmlBoolean xmlBoolean);

    void unsetShowDropDowns();

    boolean getHiddenLevel();

    XmlBoolean xgetHiddenLevel();

    boolean isSetHiddenLevel();

    void setHiddenLevel(boolean z);

    void xsetHiddenLevel(XmlBoolean xmlBoolean);

    void unsetHiddenLevel();

    String getUniqueMemberProperty();

    STXstring xgetUniqueMemberProperty();

    boolean isSetUniqueMemberProperty();

    void setUniqueMemberProperty(String str);

    void xsetUniqueMemberProperty(STXstring sTXstring);

    void unsetUniqueMemberProperty();

    boolean getCompact();

    XmlBoolean xgetCompact();

    boolean isSetCompact();

    void setCompact(boolean z);

    void xsetCompact(XmlBoolean xmlBoolean);

    void unsetCompact();

    boolean getAllDrilled();

    XmlBoolean xgetAllDrilled();

    boolean isSetAllDrilled();

    void setAllDrilled(boolean z);

    void xsetAllDrilled(XmlBoolean xmlBoolean);

    void unsetAllDrilled();

    long getNumFmtId();

    STNumFmtId xgetNumFmtId();

    boolean isSetNumFmtId();

    void setNumFmtId(long j);

    void xsetNumFmtId(STNumFmtId sTNumFmtId);

    void unsetNumFmtId();

    boolean getOutline();

    XmlBoolean xgetOutline();

    boolean isSetOutline();

    void setOutline(boolean z);

    void xsetOutline(XmlBoolean xmlBoolean);

    void unsetOutline();

    boolean getSubtotalTop();

    XmlBoolean xgetSubtotalTop();

    boolean isSetSubtotalTop();

    void setSubtotalTop(boolean z);

    void xsetSubtotalTop(XmlBoolean xmlBoolean);

    void unsetSubtotalTop();

    boolean getDragToRow();

    XmlBoolean xgetDragToRow();

    boolean isSetDragToRow();

    void setDragToRow(boolean z);

    void xsetDragToRow(XmlBoolean xmlBoolean);

    void unsetDragToRow();

    boolean getDragToCol();

    XmlBoolean xgetDragToCol();

    boolean isSetDragToCol();

    void setDragToCol(boolean z);

    void xsetDragToCol(XmlBoolean xmlBoolean);

    void unsetDragToCol();

    boolean getMultipleItemSelectionAllowed();

    XmlBoolean xgetMultipleItemSelectionAllowed();

    boolean isSetMultipleItemSelectionAllowed();

    void setMultipleItemSelectionAllowed(boolean z);

    void xsetMultipleItemSelectionAllowed(XmlBoolean xmlBoolean);

    void unsetMultipleItemSelectionAllowed();

    boolean getDragToPage();

    XmlBoolean xgetDragToPage();

    boolean isSetDragToPage();

    void setDragToPage(boolean z);

    void xsetDragToPage(XmlBoolean xmlBoolean);

    void unsetDragToPage();

    boolean getDragToData();

    XmlBoolean xgetDragToData();

    boolean isSetDragToData();

    void setDragToData(boolean z);

    void xsetDragToData(XmlBoolean xmlBoolean);

    void unsetDragToData();

    boolean getDragOff();

    XmlBoolean xgetDragOff();

    boolean isSetDragOff();

    void setDragOff(boolean z);

    void xsetDragOff(XmlBoolean xmlBoolean);

    void unsetDragOff();

    boolean getShowAll();

    XmlBoolean xgetShowAll();

    boolean isSetShowAll();

    void setShowAll(boolean z);

    void xsetShowAll(XmlBoolean xmlBoolean);

    void unsetShowAll();

    boolean getInsertBlankRow();

    XmlBoolean xgetInsertBlankRow();

    boolean isSetInsertBlankRow();

    void setInsertBlankRow(boolean z);

    void xsetInsertBlankRow(XmlBoolean xmlBoolean);

    void unsetInsertBlankRow();

    boolean getServerField();

    XmlBoolean xgetServerField();

    boolean isSetServerField();

    void setServerField(boolean z);

    void xsetServerField(XmlBoolean xmlBoolean);

    void unsetServerField();

    boolean getInsertPageBreak();

    XmlBoolean xgetInsertPageBreak();

    boolean isSetInsertPageBreak();

    void setInsertPageBreak(boolean z);

    void xsetInsertPageBreak(XmlBoolean xmlBoolean);

    void unsetInsertPageBreak();

    boolean getAutoShow();

    XmlBoolean xgetAutoShow();

    boolean isSetAutoShow();

    void setAutoShow(boolean z);

    void xsetAutoShow(XmlBoolean xmlBoolean);

    void unsetAutoShow();

    boolean getTopAutoShow();

    XmlBoolean xgetTopAutoShow();

    boolean isSetTopAutoShow();

    void setTopAutoShow(boolean z);

    void xsetTopAutoShow(XmlBoolean xmlBoolean);

    void unsetTopAutoShow();

    boolean getHideNewItems();

    XmlBoolean xgetHideNewItems();

    boolean isSetHideNewItems();

    void setHideNewItems(boolean z);

    void xsetHideNewItems(XmlBoolean xmlBoolean);

    void unsetHideNewItems();

    boolean getMeasureFilter();

    XmlBoolean xgetMeasureFilter();

    boolean isSetMeasureFilter();

    void setMeasureFilter(boolean z);

    void xsetMeasureFilter(XmlBoolean xmlBoolean);

    void unsetMeasureFilter();

    boolean getIncludeNewItemsInFilter();

    XmlBoolean xgetIncludeNewItemsInFilter();

    boolean isSetIncludeNewItemsInFilter();

    void setIncludeNewItemsInFilter(boolean z);

    void xsetIncludeNewItemsInFilter(XmlBoolean xmlBoolean);

    void unsetIncludeNewItemsInFilter();

    long getItemPageCount();

    XmlUnsignedInt xgetItemPageCount();

    boolean isSetItemPageCount();

    void setItemPageCount(long j);

    void xsetItemPageCount(XmlUnsignedInt xmlUnsignedInt);

    void unsetItemPageCount();

    STFieldSortType$Enum getSortType();

    STFieldSortType xgetSortType();

    boolean isSetSortType();

    void setSortType(STFieldSortType$Enum sTFieldSortType$Enum);

    void xsetSortType(STFieldSortType sTFieldSortType);

    void unsetSortType();

    boolean getDataSourceSort();

    XmlBoolean xgetDataSourceSort();

    boolean isSetDataSourceSort();

    void setDataSourceSort(boolean z);

    void xsetDataSourceSort(XmlBoolean xmlBoolean);

    void unsetDataSourceSort();

    boolean getNonAutoSortDefault();

    XmlBoolean xgetNonAutoSortDefault();

    boolean isSetNonAutoSortDefault();

    void setNonAutoSortDefault(boolean z);

    void xsetNonAutoSortDefault(XmlBoolean xmlBoolean);

    void unsetNonAutoSortDefault();

    long getRankBy();

    XmlUnsignedInt xgetRankBy();

    boolean isSetRankBy();

    void setRankBy(long j);

    void xsetRankBy(XmlUnsignedInt xmlUnsignedInt);

    void unsetRankBy();

    boolean getDefaultSubtotal();

    XmlBoolean xgetDefaultSubtotal();

    boolean isSetDefaultSubtotal();

    void setDefaultSubtotal(boolean z);

    void xsetDefaultSubtotal(XmlBoolean xmlBoolean);

    void unsetDefaultSubtotal();

    boolean getSumSubtotal();

    XmlBoolean xgetSumSubtotal();

    boolean isSetSumSubtotal();

    void setSumSubtotal(boolean z);

    void xsetSumSubtotal(XmlBoolean xmlBoolean);

    void unsetSumSubtotal();

    boolean getCountASubtotal();

    XmlBoolean xgetCountASubtotal();

    boolean isSetCountASubtotal();

    void setCountASubtotal(boolean z);

    void xsetCountASubtotal(XmlBoolean xmlBoolean);

    void unsetCountASubtotal();

    boolean getAvgSubtotal();

    XmlBoolean xgetAvgSubtotal();

    boolean isSetAvgSubtotal();

    void setAvgSubtotal(boolean z);

    void xsetAvgSubtotal(XmlBoolean xmlBoolean);

    void unsetAvgSubtotal();

    boolean getMaxSubtotal();

    XmlBoolean xgetMaxSubtotal();

    boolean isSetMaxSubtotal();

    void setMaxSubtotal(boolean z);

    void xsetMaxSubtotal(XmlBoolean xmlBoolean);

    void unsetMaxSubtotal();

    boolean getMinSubtotal();

    XmlBoolean xgetMinSubtotal();

    boolean isSetMinSubtotal();

    void setMinSubtotal(boolean z);

    void xsetMinSubtotal(XmlBoolean xmlBoolean);

    void unsetMinSubtotal();

    boolean getProductSubtotal();

    XmlBoolean xgetProductSubtotal();

    boolean isSetProductSubtotal();

    void setProductSubtotal(boolean z);

    void xsetProductSubtotal(XmlBoolean xmlBoolean);

    void unsetProductSubtotal();

    boolean getCountSubtotal();

    XmlBoolean xgetCountSubtotal();

    boolean isSetCountSubtotal();

    void setCountSubtotal(boolean z);

    void xsetCountSubtotal(XmlBoolean xmlBoolean);

    void unsetCountSubtotal();

    boolean getStdDevSubtotal();

    XmlBoolean xgetStdDevSubtotal();

    boolean isSetStdDevSubtotal();

    void setStdDevSubtotal(boolean z);

    void xsetStdDevSubtotal(XmlBoolean xmlBoolean);

    void unsetStdDevSubtotal();

    boolean getStdDevPSubtotal();

    XmlBoolean xgetStdDevPSubtotal();

    boolean isSetStdDevPSubtotal();

    void setStdDevPSubtotal(boolean z);

    void xsetStdDevPSubtotal(XmlBoolean xmlBoolean);

    void unsetStdDevPSubtotal();

    boolean getVarSubtotal();

    XmlBoolean xgetVarSubtotal();

    boolean isSetVarSubtotal();

    void setVarSubtotal(boolean z);

    void xsetVarSubtotal(XmlBoolean xmlBoolean);

    void unsetVarSubtotal();

    boolean getVarPSubtotal();

    XmlBoolean xgetVarPSubtotal();

    boolean isSetVarPSubtotal();

    void setVarPSubtotal(boolean z);

    void xsetVarPSubtotal(XmlBoolean xmlBoolean);

    void unsetVarPSubtotal();

    boolean getShowPropCell();

    XmlBoolean xgetShowPropCell();

    boolean isSetShowPropCell();

    void setShowPropCell(boolean z);

    void xsetShowPropCell(XmlBoolean xmlBoolean);

    void unsetShowPropCell();

    boolean getShowPropTip();

    XmlBoolean xgetShowPropTip();

    boolean isSetShowPropTip();

    void setShowPropTip(boolean z);

    void xsetShowPropTip(XmlBoolean xmlBoolean);

    void unsetShowPropTip();

    boolean getShowPropAsCaption();

    XmlBoolean xgetShowPropAsCaption();

    boolean isSetShowPropAsCaption();

    void setShowPropAsCaption(boolean z);

    void xsetShowPropAsCaption(XmlBoolean xmlBoolean);

    void unsetShowPropAsCaption();

    boolean getDefaultAttributeDrillState();

    XmlBoolean xgetDefaultAttributeDrillState();

    boolean isSetDefaultAttributeDrillState();

    void setDefaultAttributeDrillState(boolean z);

    void xsetDefaultAttributeDrillState(XmlBoolean xmlBoolean);

    void unsetDefaultAttributeDrillState();
}
