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
import org.apache.xmlbeans.XmlUnsignedByte;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPivotTableDefinition.class */
public interface CTPivotTableDefinition extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPivotTableDefinition.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpivottabledefinitionb188type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPivotTableDefinition$Factory.class */
    public static final class Factory {
        public static CTPivotTableDefinition newInstance() {
            return (CTPivotTableDefinition) POIXMLTypeLoader.newInstance(CTPivotTableDefinition.type, null);
        }

        public static CTPivotTableDefinition newInstance(XmlOptions xmlOptions) {
            return (CTPivotTableDefinition) POIXMLTypeLoader.newInstance(CTPivotTableDefinition.type, xmlOptions);
        }

        public static CTPivotTableDefinition parse(String str) throws XmlException {
            return (CTPivotTableDefinition) POIXMLTypeLoader.parse(str, CTPivotTableDefinition.type, (XmlOptions) null);
        }

        public static CTPivotTableDefinition parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPivotTableDefinition) POIXMLTypeLoader.parse(str, CTPivotTableDefinition.type, xmlOptions);
        }

        public static CTPivotTableDefinition parse(File file) throws XmlException, IOException {
            return (CTPivotTableDefinition) POIXMLTypeLoader.parse(file, CTPivotTableDefinition.type, (XmlOptions) null);
        }

        public static CTPivotTableDefinition parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotTableDefinition) POIXMLTypeLoader.parse(file, CTPivotTableDefinition.type, xmlOptions);
        }

        public static CTPivotTableDefinition parse(URL url) throws XmlException, IOException {
            return (CTPivotTableDefinition) POIXMLTypeLoader.parse(url, CTPivotTableDefinition.type, (XmlOptions) null);
        }

        public static CTPivotTableDefinition parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotTableDefinition) POIXMLTypeLoader.parse(url, CTPivotTableDefinition.type, xmlOptions);
        }

        public static CTPivotTableDefinition parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPivotTableDefinition) POIXMLTypeLoader.parse(inputStream, CTPivotTableDefinition.type, (XmlOptions) null);
        }

        public static CTPivotTableDefinition parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotTableDefinition) POIXMLTypeLoader.parse(inputStream, CTPivotTableDefinition.type, xmlOptions);
        }

        public static CTPivotTableDefinition parse(Reader reader) throws XmlException, IOException {
            return (CTPivotTableDefinition) POIXMLTypeLoader.parse(reader, CTPivotTableDefinition.type, (XmlOptions) null);
        }

        public static CTPivotTableDefinition parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPivotTableDefinition) POIXMLTypeLoader.parse(reader, CTPivotTableDefinition.type, xmlOptions);
        }

        public static CTPivotTableDefinition parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPivotTableDefinition) POIXMLTypeLoader.parse(xMLStreamReader, CTPivotTableDefinition.type, (XmlOptions) null);
        }

        public static CTPivotTableDefinition parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPivotTableDefinition) POIXMLTypeLoader.parse(xMLStreamReader, CTPivotTableDefinition.type, xmlOptions);
        }

        public static CTPivotTableDefinition parse(Node node) throws XmlException {
            return (CTPivotTableDefinition) POIXMLTypeLoader.parse(node, CTPivotTableDefinition.type, (XmlOptions) null);
        }

        public static CTPivotTableDefinition parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPivotTableDefinition) POIXMLTypeLoader.parse(node, CTPivotTableDefinition.type, xmlOptions);
        }

        public static CTPivotTableDefinition parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPivotTableDefinition) POIXMLTypeLoader.parse(xMLInputStream, CTPivotTableDefinition.type, (XmlOptions) null);
        }

        public static CTPivotTableDefinition parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPivotTableDefinition) POIXMLTypeLoader.parse(xMLInputStream, CTPivotTableDefinition.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPivotTableDefinition.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPivotTableDefinition.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTLocation getLocation();

    void setLocation(CTLocation cTLocation);

    CTLocation addNewLocation();

    CTPivotFields getPivotFields();

    boolean isSetPivotFields();

    void setPivotFields(CTPivotFields cTPivotFields);

    CTPivotFields addNewPivotFields();

    void unsetPivotFields();

    CTRowFields getRowFields();

    boolean isSetRowFields();

    void setRowFields(CTRowFields cTRowFields);

    CTRowFields addNewRowFields();

    void unsetRowFields();

    CTRowItems getRowItems();

    boolean isSetRowItems();

    void setRowItems(CTRowItems cTRowItems);

    CTRowItems addNewRowItems();

    void unsetRowItems();

    CTColFields getColFields();

    boolean isSetColFields();

    void setColFields(CTColFields cTColFields);

    CTColFields addNewColFields();

    void unsetColFields();

    CTColItems getColItems();

    boolean isSetColItems();

    void setColItems(CTColItems cTColItems);

    CTColItems addNewColItems();

    void unsetColItems();

    CTPageFields getPageFields();

    boolean isSetPageFields();

    void setPageFields(CTPageFields cTPageFields);

    CTPageFields addNewPageFields();

    void unsetPageFields();

    CTDataFields getDataFields();

    boolean isSetDataFields();

    void setDataFields(CTDataFields cTDataFields);

    CTDataFields addNewDataFields();

    void unsetDataFields();

    CTFormats getFormats();

    boolean isSetFormats();

    void setFormats(CTFormats cTFormats);

    CTFormats addNewFormats();

    void unsetFormats();

    CTConditionalFormats getConditionalFormats();

    boolean isSetConditionalFormats();

    void setConditionalFormats(CTConditionalFormats cTConditionalFormats);

    CTConditionalFormats addNewConditionalFormats();

    void unsetConditionalFormats();

    CTChartFormats getChartFormats();

    boolean isSetChartFormats();

    void setChartFormats(CTChartFormats cTChartFormats);

    CTChartFormats addNewChartFormats();

    void unsetChartFormats();

    CTPivotHierarchies getPivotHierarchies();

    boolean isSetPivotHierarchies();

    void setPivotHierarchies(CTPivotHierarchies cTPivotHierarchies);

    CTPivotHierarchies addNewPivotHierarchies();

    void unsetPivotHierarchies();

    CTPivotTableStyle getPivotTableStyleInfo();

    boolean isSetPivotTableStyleInfo();

    void setPivotTableStyleInfo(CTPivotTableStyle cTPivotTableStyle);

    CTPivotTableStyle addNewPivotTableStyleInfo();

    void unsetPivotTableStyleInfo();

    CTPivotFilters getFilters();

    boolean isSetFilters();

    void setFilters(CTPivotFilters cTPivotFilters);

    CTPivotFilters addNewFilters();

    void unsetFilters();

    CTRowHierarchiesUsage getRowHierarchiesUsage();

    boolean isSetRowHierarchiesUsage();

    void setRowHierarchiesUsage(CTRowHierarchiesUsage cTRowHierarchiesUsage);

    CTRowHierarchiesUsage addNewRowHierarchiesUsage();

    void unsetRowHierarchiesUsage();

    CTColHierarchiesUsage getColHierarchiesUsage();

    boolean isSetColHierarchiesUsage();

    void setColHierarchiesUsage(CTColHierarchiesUsage cTColHierarchiesUsage);

    CTColHierarchiesUsage addNewColHierarchiesUsage();

    void unsetColHierarchiesUsage();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    String getName();

    STXstring xgetName();

    void setName(String str);

    void xsetName(STXstring sTXstring);

    long getCacheId();

    XmlUnsignedInt xgetCacheId();

    void setCacheId(long j);

    void xsetCacheId(XmlUnsignedInt xmlUnsignedInt);

    boolean getDataOnRows();

    XmlBoolean xgetDataOnRows();

    boolean isSetDataOnRows();

    void setDataOnRows(boolean z);

    void xsetDataOnRows(XmlBoolean xmlBoolean);

    void unsetDataOnRows();

    long getDataPosition();

    XmlUnsignedInt xgetDataPosition();

    boolean isSetDataPosition();

    void setDataPosition(long j);

    void xsetDataPosition(XmlUnsignedInt xmlUnsignedInt);

    void unsetDataPosition();

    long getAutoFormatId();

    XmlUnsignedInt xgetAutoFormatId();

    boolean isSetAutoFormatId();

    void setAutoFormatId(long j);

    void xsetAutoFormatId(XmlUnsignedInt xmlUnsignedInt);

    void unsetAutoFormatId();

    boolean getApplyNumberFormats();

    XmlBoolean xgetApplyNumberFormats();

    boolean isSetApplyNumberFormats();

    void setApplyNumberFormats(boolean z);

    void xsetApplyNumberFormats(XmlBoolean xmlBoolean);

    void unsetApplyNumberFormats();

    boolean getApplyBorderFormats();

    XmlBoolean xgetApplyBorderFormats();

    boolean isSetApplyBorderFormats();

    void setApplyBorderFormats(boolean z);

    void xsetApplyBorderFormats(XmlBoolean xmlBoolean);

    void unsetApplyBorderFormats();

    boolean getApplyFontFormats();

    XmlBoolean xgetApplyFontFormats();

    boolean isSetApplyFontFormats();

    void setApplyFontFormats(boolean z);

    void xsetApplyFontFormats(XmlBoolean xmlBoolean);

    void unsetApplyFontFormats();

    boolean getApplyPatternFormats();

    XmlBoolean xgetApplyPatternFormats();

    boolean isSetApplyPatternFormats();

    void setApplyPatternFormats(boolean z);

    void xsetApplyPatternFormats(XmlBoolean xmlBoolean);

    void unsetApplyPatternFormats();

    boolean getApplyAlignmentFormats();

    XmlBoolean xgetApplyAlignmentFormats();

    boolean isSetApplyAlignmentFormats();

    void setApplyAlignmentFormats(boolean z);

    void xsetApplyAlignmentFormats(XmlBoolean xmlBoolean);

    void unsetApplyAlignmentFormats();

    boolean getApplyWidthHeightFormats();

    XmlBoolean xgetApplyWidthHeightFormats();

    boolean isSetApplyWidthHeightFormats();

    void setApplyWidthHeightFormats(boolean z);

    void xsetApplyWidthHeightFormats(XmlBoolean xmlBoolean);

    void unsetApplyWidthHeightFormats();

    String getDataCaption();

    STXstring xgetDataCaption();

    void setDataCaption(String str);

    void xsetDataCaption(STXstring sTXstring);

    String getGrandTotalCaption();

    STXstring xgetGrandTotalCaption();

    boolean isSetGrandTotalCaption();

    void setGrandTotalCaption(String str);

    void xsetGrandTotalCaption(STXstring sTXstring);

    void unsetGrandTotalCaption();

    String getErrorCaption();

    STXstring xgetErrorCaption();

    boolean isSetErrorCaption();

    void setErrorCaption(String str);

    void xsetErrorCaption(STXstring sTXstring);

    void unsetErrorCaption();

    boolean getShowError();

    XmlBoolean xgetShowError();

    boolean isSetShowError();

    void setShowError(boolean z);

    void xsetShowError(XmlBoolean xmlBoolean);

    void unsetShowError();

    String getMissingCaption();

    STXstring xgetMissingCaption();

    boolean isSetMissingCaption();

    void setMissingCaption(String str);

    void xsetMissingCaption(STXstring sTXstring);

    void unsetMissingCaption();

    boolean getShowMissing();

    XmlBoolean xgetShowMissing();

    boolean isSetShowMissing();

    void setShowMissing(boolean z);

    void xsetShowMissing(XmlBoolean xmlBoolean);

    void unsetShowMissing();

    String getPageStyle();

    STXstring xgetPageStyle();

    boolean isSetPageStyle();

    void setPageStyle(String str);

    void xsetPageStyle(STXstring sTXstring);

    void unsetPageStyle();

    String getPivotTableStyle();

    STXstring xgetPivotTableStyle();

    boolean isSetPivotTableStyle();

    void setPivotTableStyle(String str);

    void xsetPivotTableStyle(STXstring sTXstring);

    void unsetPivotTableStyle();

    String getVacatedStyle();

    STXstring xgetVacatedStyle();

    boolean isSetVacatedStyle();

    void setVacatedStyle(String str);

    void xsetVacatedStyle(STXstring sTXstring);

    void unsetVacatedStyle();

    String getTag();

    STXstring xgetTag();

    boolean isSetTag();

    void setTag(String str);

    void xsetTag(STXstring sTXstring);

    void unsetTag();

    short getUpdatedVersion();

    XmlUnsignedByte xgetUpdatedVersion();

    boolean isSetUpdatedVersion();

    void setUpdatedVersion(short s);

    void xsetUpdatedVersion(XmlUnsignedByte xmlUnsignedByte);

    void unsetUpdatedVersion();

    short getMinRefreshableVersion();

    XmlUnsignedByte xgetMinRefreshableVersion();

    boolean isSetMinRefreshableVersion();

    void setMinRefreshableVersion(short s);

    void xsetMinRefreshableVersion(XmlUnsignedByte xmlUnsignedByte);

    void unsetMinRefreshableVersion();

    boolean getAsteriskTotals();

    XmlBoolean xgetAsteriskTotals();

    boolean isSetAsteriskTotals();

    void setAsteriskTotals(boolean z);

    void xsetAsteriskTotals(XmlBoolean xmlBoolean);

    void unsetAsteriskTotals();

    boolean getShowItems();

    XmlBoolean xgetShowItems();

    boolean isSetShowItems();

    void setShowItems(boolean z);

    void xsetShowItems(XmlBoolean xmlBoolean);

    void unsetShowItems();

    boolean getEditData();

    XmlBoolean xgetEditData();

    boolean isSetEditData();

    void setEditData(boolean z);

    void xsetEditData(XmlBoolean xmlBoolean);

    void unsetEditData();

    boolean getDisableFieldList();

    XmlBoolean xgetDisableFieldList();

    boolean isSetDisableFieldList();

    void setDisableFieldList(boolean z);

    void xsetDisableFieldList(XmlBoolean xmlBoolean);

    void unsetDisableFieldList();

    boolean getShowCalcMbrs();

    XmlBoolean xgetShowCalcMbrs();

    boolean isSetShowCalcMbrs();

    void setShowCalcMbrs(boolean z);

    void xsetShowCalcMbrs(XmlBoolean xmlBoolean);

    void unsetShowCalcMbrs();

    boolean getVisualTotals();

    XmlBoolean xgetVisualTotals();

    boolean isSetVisualTotals();

    void setVisualTotals(boolean z);

    void xsetVisualTotals(XmlBoolean xmlBoolean);

    void unsetVisualTotals();

    boolean getShowMultipleLabel();

    XmlBoolean xgetShowMultipleLabel();

    boolean isSetShowMultipleLabel();

    void setShowMultipleLabel(boolean z);

    void xsetShowMultipleLabel(XmlBoolean xmlBoolean);

    void unsetShowMultipleLabel();

    boolean getShowDataDropDown();

    XmlBoolean xgetShowDataDropDown();

    boolean isSetShowDataDropDown();

    void setShowDataDropDown(boolean z);

    void xsetShowDataDropDown(XmlBoolean xmlBoolean);

    void unsetShowDataDropDown();

    boolean getShowDrill();

    XmlBoolean xgetShowDrill();

    boolean isSetShowDrill();

    void setShowDrill(boolean z);

    void xsetShowDrill(XmlBoolean xmlBoolean);

    void unsetShowDrill();

    boolean getPrintDrill();

    XmlBoolean xgetPrintDrill();

    boolean isSetPrintDrill();

    void setPrintDrill(boolean z);

    void xsetPrintDrill(XmlBoolean xmlBoolean);

    void unsetPrintDrill();

    boolean getShowMemberPropertyTips();

    XmlBoolean xgetShowMemberPropertyTips();

    boolean isSetShowMemberPropertyTips();

    void setShowMemberPropertyTips(boolean z);

    void xsetShowMemberPropertyTips(XmlBoolean xmlBoolean);

    void unsetShowMemberPropertyTips();

    boolean getShowDataTips();

    XmlBoolean xgetShowDataTips();

    boolean isSetShowDataTips();

    void setShowDataTips(boolean z);

    void xsetShowDataTips(XmlBoolean xmlBoolean);

    void unsetShowDataTips();

    boolean getEnableWizard();

    XmlBoolean xgetEnableWizard();

    boolean isSetEnableWizard();

    void setEnableWizard(boolean z);

    void xsetEnableWizard(XmlBoolean xmlBoolean);

    void unsetEnableWizard();

    boolean getEnableDrill();

    XmlBoolean xgetEnableDrill();

    boolean isSetEnableDrill();

    void setEnableDrill(boolean z);

    void xsetEnableDrill(XmlBoolean xmlBoolean);

    void unsetEnableDrill();

    boolean getEnableFieldProperties();

    XmlBoolean xgetEnableFieldProperties();

    boolean isSetEnableFieldProperties();

    void setEnableFieldProperties(boolean z);

    void xsetEnableFieldProperties(XmlBoolean xmlBoolean);

    void unsetEnableFieldProperties();

    boolean getPreserveFormatting();

    XmlBoolean xgetPreserveFormatting();

    boolean isSetPreserveFormatting();

    void setPreserveFormatting(boolean z);

    void xsetPreserveFormatting(XmlBoolean xmlBoolean);

    void unsetPreserveFormatting();

    boolean getUseAutoFormatting();

    XmlBoolean xgetUseAutoFormatting();

    boolean isSetUseAutoFormatting();

    void setUseAutoFormatting(boolean z);

    void xsetUseAutoFormatting(XmlBoolean xmlBoolean);

    void unsetUseAutoFormatting();

    long getPageWrap();

    XmlUnsignedInt xgetPageWrap();

    boolean isSetPageWrap();

    void setPageWrap(long j);

    void xsetPageWrap(XmlUnsignedInt xmlUnsignedInt);

    void unsetPageWrap();

    boolean getPageOverThenDown();

    XmlBoolean xgetPageOverThenDown();

    boolean isSetPageOverThenDown();

    void setPageOverThenDown(boolean z);

    void xsetPageOverThenDown(XmlBoolean xmlBoolean);

    void unsetPageOverThenDown();

    boolean getSubtotalHiddenItems();

    XmlBoolean xgetSubtotalHiddenItems();

    boolean isSetSubtotalHiddenItems();

    void setSubtotalHiddenItems(boolean z);

    void xsetSubtotalHiddenItems(XmlBoolean xmlBoolean);

    void unsetSubtotalHiddenItems();

    boolean getRowGrandTotals();

    XmlBoolean xgetRowGrandTotals();

    boolean isSetRowGrandTotals();

    void setRowGrandTotals(boolean z);

    void xsetRowGrandTotals(XmlBoolean xmlBoolean);

    void unsetRowGrandTotals();

    boolean getColGrandTotals();

    XmlBoolean xgetColGrandTotals();

    boolean isSetColGrandTotals();

    void setColGrandTotals(boolean z);

    void xsetColGrandTotals(XmlBoolean xmlBoolean);

    void unsetColGrandTotals();

    boolean getFieldPrintTitles();

    XmlBoolean xgetFieldPrintTitles();

    boolean isSetFieldPrintTitles();

    void setFieldPrintTitles(boolean z);

    void xsetFieldPrintTitles(XmlBoolean xmlBoolean);

    void unsetFieldPrintTitles();

    boolean getItemPrintTitles();

    XmlBoolean xgetItemPrintTitles();

    boolean isSetItemPrintTitles();

    void setItemPrintTitles(boolean z);

    void xsetItemPrintTitles(XmlBoolean xmlBoolean);

    void unsetItemPrintTitles();

    boolean getMergeItem();

    XmlBoolean xgetMergeItem();

    boolean isSetMergeItem();

    void setMergeItem(boolean z);

    void xsetMergeItem(XmlBoolean xmlBoolean);

    void unsetMergeItem();

    boolean getShowDropZones();

    XmlBoolean xgetShowDropZones();

    boolean isSetShowDropZones();

    void setShowDropZones(boolean z);

    void xsetShowDropZones(XmlBoolean xmlBoolean);

    void unsetShowDropZones();

    short getCreatedVersion();

    XmlUnsignedByte xgetCreatedVersion();

    boolean isSetCreatedVersion();

    void setCreatedVersion(short s);

    void xsetCreatedVersion(XmlUnsignedByte xmlUnsignedByte);

    void unsetCreatedVersion();

    long getIndent();

    XmlUnsignedInt xgetIndent();

    boolean isSetIndent();

    void setIndent(long j);

    void xsetIndent(XmlUnsignedInt xmlUnsignedInt);

    void unsetIndent();

    boolean getShowEmptyRow();

    XmlBoolean xgetShowEmptyRow();

    boolean isSetShowEmptyRow();

    void setShowEmptyRow(boolean z);

    void xsetShowEmptyRow(XmlBoolean xmlBoolean);

    void unsetShowEmptyRow();

    boolean getShowEmptyCol();

    XmlBoolean xgetShowEmptyCol();

    boolean isSetShowEmptyCol();

    void setShowEmptyCol(boolean z);

    void xsetShowEmptyCol(XmlBoolean xmlBoolean);

    void unsetShowEmptyCol();

    boolean getShowHeaders();

    XmlBoolean xgetShowHeaders();

    boolean isSetShowHeaders();

    void setShowHeaders(boolean z);

    void xsetShowHeaders(XmlBoolean xmlBoolean);

    void unsetShowHeaders();

    boolean getCompact();

    XmlBoolean xgetCompact();

    boolean isSetCompact();

    void setCompact(boolean z);

    void xsetCompact(XmlBoolean xmlBoolean);

    void unsetCompact();

    boolean getOutline();

    XmlBoolean xgetOutline();

    boolean isSetOutline();

    void setOutline(boolean z);

    void xsetOutline(XmlBoolean xmlBoolean);

    void unsetOutline();

    boolean getOutlineData();

    XmlBoolean xgetOutlineData();

    boolean isSetOutlineData();

    void setOutlineData(boolean z);

    void xsetOutlineData(XmlBoolean xmlBoolean);

    void unsetOutlineData();

    boolean getCompactData();

    XmlBoolean xgetCompactData();

    boolean isSetCompactData();

    void setCompactData(boolean z);

    void xsetCompactData(XmlBoolean xmlBoolean);

    void unsetCompactData();

    boolean getPublished();

    XmlBoolean xgetPublished();

    boolean isSetPublished();

    void setPublished(boolean z);

    void xsetPublished(XmlBoolean xmlBoolean);

    void unsetPublished();

    boolean getGridDropZones();

    XmlBoolean xgetGridDropZones();

    boolean isSetGridDropZones();

    void setGridDropZones(boolean z);

    void xsetGridDropZones(XmlBoolean xmlBoolean);

    void unsetGridDropZones();

    boolean getImmersive();

    XmlBoolean xgetImmersive();

    boolean isSetImmersive();

    void setImmersive(boolean z);

    void xsetImmersive(XmlBoolean xmlBoolean);

    void unsetImmersive();

    boolean getMultipleFieldFilters();

    XmlBoolean xgetMultipleFieldFilters();

    boolean isSetMultipleFieldFilters();

    void setMultipleFieldFilters(boolean z);

    void xsetMultipleFieldFilters(XmlBoolean xmlBoolean);

    void unsetMultipleFieldFilters();

    long getChartFormat();

    XmlUnsignedInt xgetChartFormat();

    boolean isSetChartFormat();

    void setChartFormat(long j);

    void xsetChartFormat(XmlUnsignedInt xmlUnsignedInt);

    void unsetChartFormat();

    String getRowHeaderCaption();

    STXstring xgetRowHeaderCaption();

    boolean isSetRowHeaderCaption();

    void setRowHeaderCaption(String str);

    void xsetRowHeaderCaption(STXstring sTXstring);

    void unsetRowHeaderCaption();

    String getColHeaderCaption();

    STXstring xgetColHeaderCaption();

    boolean isSetColHeaderCaption();

    void setColHeaderCaption(String str);

    void xsetColHeaderCaption(STXstring sTXstring);

    void unsetColHeaderCaption();

    boolean getFieldListSortAscending();

    XmlBoolean xgetFieldListSortAscending();

    boolean isSetFieldListSortAscending();

    void setFieldListSortAscending(boolean z);

    void xsetFieldListSortAscending(XmlBoolean xmlBoolean);

    void unsetFieldListSortAscending();

    boolean getMdxSubqueries();

    XmlBoolean xgetMdxSubqueries();

    boolean isSetMdxSubqueries();

    void setMdxSubqueries(boolean z);

    void xsetMdxSubqueries(XmlBoolean xmlBoolean);

    void unsetMdxSubqueries();

    boolean getCustomListSort();

    XmlBoolean xgetCustomListSort();

    boolean isSetCustomListSort();

    void setCustomListSort(boolean z);

    void xsetCustomListSort(XmlBoolean xmlBoolean);

    void unsetCustomListSort();
}
