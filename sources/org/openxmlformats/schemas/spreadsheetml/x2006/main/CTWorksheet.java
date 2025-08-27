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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTWorksheet.class */
public interface CTWorksheet extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTWorksheet.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctworksheet530dtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTWorksheet$Factory.class */
    public static final class Factory {
        public static CTWorksheet newInstance() {
            return (CTWorksheet) POIXMLTypeLoader.newInstance(CTWorksheet.type, null);
        }

        public static CTWorksheet newInstance(XmlOptions xmlOptions) {
            return (CTWorksheet) POIXMLTypeLoader.newInstance(CTWorksheet.type, xmlOptions);
        }

        public static CTWorksheet parse(String str) throws XmlException {
            return (CTWorksheet) POIXMLTypeLoader.parse(str, CTWorksheet.type, (XmlOptions) null);
        }

        public static CTWorksheet parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTWorksheet) POIXMLTypeLoader.parse(str, CTWorksheet.type, xmlOptions);
        }

        public static CTWorksheet parse(File file) throws XmlException, IOException {
            return (CTWorksheet) POIXMLTypeLoader.parse(file, CTWorksheet.type, (XmlOptions) null);
        }

        public static CTWorksheet parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorksheet) POIXMLTypeLoader.parse(file, CTWorksheet.type, xmlOptions);
        }

        public static CTWorksheet parse(URL url) throws XmlException, IOException {
            return (CTWorksheet) POIXMLTypeLoader.parse(url, CTWorksheet.type, (XmlOptions) null);
        }

        public static CTWorksheet parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorksheet) POIXMLTypeLoader.parse(url, CTWorksheet.type, xmlOptions);
        }

        public static CTWorksheet parse(InputStream inputStream) throws XmlException, IOException {
            return (CTWorksheet) POIXMLTypeLoader.parse(inputStream, CTWorksheet.type, (XmlOptions) null);
        }

        public static CTWorksheet parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorksheet) POIXMLTypeLoader.parse(inputStream, CTWorksheet.type, xmlOptions);
        }

        public static CTWorksheet parse(Reader reader) throws XmlException, IOException {
            return (CTWorksheet) POIXMLTypeLoader.parse(reader, CTWorksheet.type, (XmlOptions) null);
        }

        public static CTWorksheet parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTWorksheet) POIXMLTypeLoader.parse(reader, CTWorksheet.type, xmlOptions);
        }

        public static CTWorksheet parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTWorksheet) POIXMLTypeLoader.parse(xMLStreamReader, CTWorksheet.type, (XmlOptions) null);
        }

        public static CTWorksheet parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTWorksheet) POIXMLTypeLoader.parse(xMLStreamReader, CTWorksheet.type, xmlOptions);
        }

        public static CTWorksheet parse(Node node) throws XmlException {
            return (CTWorksheet) POIXMLTypeLoader.parse(node, CTWorksheet.type, (XmlOptions) null);
        }

        public static CTWorksheet parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTWorksheet) POIXMLTypeLoader.parse(node, CTWorksheet.type, xmlOptions);
        }

        public static CTWorksheet parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTWorksheet) POIXMLTypeLoader.parse(xMLInputStream, CTWorksheet.type, (XmlOptions) null);
        }

        public static CTWorksheet parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTWorksheet) POIXMLTypeLoader.parse(xMLInputStream, CTWorksheet.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTWorksheet.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTWorksheet.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTSheetPr getSheetPr();

    boolean isSetSheetPr();

    void setSheetPr(CTSheetPr cTSheetPr);

    CTSheetPr addNewSheetPr();

    void unsetSheetPr();

    CTSheetDimension getDimension();

    boolean isSetDimension();

    void setDimension(CTSheetDimension cTSheetDimension);

    CTSheetDimension addNewDimension();

    void unsetDimension();

    CTSheetViews getSheetViews();

    boolean isSetSheetViews();

    void setSheetViews(CTSheetViews cTSheetViews);

    CTSheetViews addNewSheetViews();

    void unsetSheetViews();

    CTSheetFormatPr getSheetFormatPr();

    boolean isSetSheetFormatPr();

    void setSheetFormatPr(CTSheetFormatPr cTSheetFormatPr);

    CTSheetFormatPr addNewSheetFormatPr();

    void unsetSheetFormatPr();

    List<CTCols> getColsList();

    CTCols[] getColsArray();

    CTCols getColsArray(int i);

    int sizeOfColsArray();

    void setColsArray(CTCols[] cTColsArr);

    void setColsArray(int i, CTCols cTCols);

    CTCols insertNewCols(int i);

    CTCols addNewCols();

    void removeCols(int i);

    CTSheetData getSheetData();

    void setSheetData(CTSheetData cTSheetData);

    CTSheetData addNewSheetData();

    CTSheetCalcPr getSheetCalcPr();

    boolean isSetSheetCalcPr();

    void setSheetCalcPr(CTSheetCalcPr cTSheetCalcPr);

    CTSheetCalcPr addNewSheetCalcPr();

    void unsetSheetCalcPr();

    CTSheetProtection getSheetProtection();

    boolean isSetSheetProtection();

    void setSheetProtection(CTSheetProtection cTSheetProtection);

    CTSheetProtection addNewSheetProtection();

    void unsetSheetProtection();

    CTProtectedRanges getProtectedRanges();

    boolean isSetProtectedRanges();

    void setProtectedRanges(CTProtectedRanges cTProtectedRanges);

    CTProtectedRanges addNewProtectedRanges();

    void unsetProtectedRanges();

    CTScenarios getScenarios();

    boolean isSetScenarios();

    void setScenarios(CTScenarios cTScenarios);

    CTScenarios addNewScenarios();

    void unsetScenarios();

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

    CTDataConsolidate getDataConsolidate();

    boolean isSetDataConsolidate();

    void setDataConsolidate(CTDataConsolidate cTDataConsolidate);

    CTDataConsolidate addNewDataConsolidate();

    void unsetDataConsolidate();

    CTCustomSheetViews getCustomSheetViews();

    boolean isSetCustomSheetViews();

    void setCustomSheetViews(CTCustomSheetViews cTCustomSheetViews);

    CTCustomSheetViews addNewCustomSheetViews();

    void unsetCustomSheetViews();

    CTMergeCells getMergeCells();

    boolean isSetMergeCells();

    void setMergeCells(CTMergeCells cTMergeCells);

    CTMergeCells addNewMergeCells();

    void unsetMergeCells();

    CTPhoneticPr getPhoneticPr();

    boolean isSetPhoneticPr();

    void setPhoneticPr(CTPhoneticPr cTPhoneticPr);

    CTPhoneticPr addNewPhoneticPr();

    void unsetPhoneticPr();

    List<CTConditionalFormatting> getConditionalFormattingList();

    CTConditionalFormatting[] getConditionalFormattingArray();

    CTConditionalFormatting getConditionalFormattingArray(int i);

    int sizeOfConditionalFormattingArray();

    void setConditionalFormattingArray(CTConditionalFormatting[] cTConditionalFormattingArr);

    void setConditionalFormattingArray(int i, CTConditionalFormatting cTConditionalFormatting);

    CTConditionalFormatting insertNewConditionalFormatting(int i);

    CTConditionalFormatting addNewConditionalFormatting();

    void removeConditionalFormatting(int i);

    CTDataValidations getDataValidations();

    boolean isSetDataValidations();

    void setDataValidations(CTDataValidations cTDataValidations);

    CTDataValidations addNewDataValidations();

    void unsetDataValidations();

    CTHyperlinks getHyperlinks();

    boolean isSetHyperlinks();

    void setHyperlinks(CTHyperlinks cTHyperlinks);

    CTHyperlinks addNewHyperlinks();

    void unsetHyperlinks();

    CTPrintOptions getPrintOptions();

    boolean isSetPrintOptions();

    void setPrintOptions(CTPrintOptions cTPrintOptions);

    CTPrintOptions addNewPrintOptions();

    void unsetPrintOptions();

    CTPageMargins getPageMargins();

    boolean isSetPageMargins();

    void setPageMargins(CTPageMargins cTPageMargins);

    CTPageMargins addNewPageMargins();

    void unsetPageMargins();

    CTPageSetup getPageSetup();

    boolean isSetPageSetup();

    void setPageSetup(CTPageSetup cTPageSetup);

    CTPageSetup addNewPageSetup();

    void unsetPageSetup();

    CTHeaderFooter getHeaderFooter();

    boolean isSetHeaderFooter();

    void setHeaderFooter(CTHeaderFooter cTHeaderFooter);

    CTHeaderFooter addNewHeaderFooter();

    void unsetHeaderFooter();

    CTPageBreak getRowBreaks();

    boolean isSetRowBreaks();

    void setRowBreaks(CTPageBreak cTPageBreak);

    CTPageBreak addNewRowBreaks();

    void unsetRowBreaks();

    CTPageBreak getColBreaks();

    boolean isSetColBreaks();

    void setColBreaks(CTPageBreak cTPageBreak);

    CTPageBreak addNewColBreaks();

    void unsetColBreaks();

    CTCustomProperties getCustomProperties();

    boolean isSetCustomProperties();

    void setCustomProperties(CTCustomProperties cTCustomProperties);

    CTCustomProperties addNewCustomProperties();

    void unsetCustomProperties();

    CTCellWatches getCellWatches();

    boolean isSetCellWatches();

    void setCellWatches(CTCellWatches cTCellWatches);

    CTCellWatches addNewCellWatches();

    void unsetCellWatches();

    CTIgnoredErrors getIgnoredErrors();

    boolean isSetIgnoredErrors();

    void setIgnoredErrors(CTIgnoredErrors cTIgnoredErrors);

    CTIgnoredErrors addNewIgnoredErrors();

    void unsetIgnoredErrors();

    CTSmartTags getSmartTags();

    boolean isSetSmartTags();

    void setSmartTags(CTSmartTags cTSmartTags);

    CTSmartTags addNewSmartTags();

    void unsetSmartTags();

    CTDrawing getDrawing();

    boolean isSetDrawing();

    void setDrawing(CTDrawing cTDrawing);

    CTDrawing addNewDrawing();

    void unsetDrawing();

    CTLegacyDrawing getLegacyDrawing();

    boolean isSetLegacyDrawing();

    void setLegacyDrawing(CTLegacyDrawing cTLegacyDrawing);

    CTLegacyDrawing addNewLegacyDrawing();

    void unsetLegacyDrawing();

    CTLegacyDrawing getLegacyDrawingHF();

    boolean isSetLegacyDrawingHF();

    void setLegacyDrawingHF(CTLegacyDrawing cTLegacyDrawing);

    CTLegacyDrawing addNewLegacyDrawingHF();

    void unsetLegacyDrawingHF();

    CTSheetBackgroundPicture getPicture();

    boolean isSetPicture();

    void setPicture(CTSheetBackgroundPicture cTSheetBackgroundPicture);

    CTSheetBackgroundPicture addNewPicture();

    void unsetPicture();

    CTOleObjects getOleObjects();

    boolean isSetOleObjects();

    void setOleObjects(CTOleObjects cTOleObjects);

    CTOleObjects addNewOleObjects();

    void unsetOleObjects();

    CTControls getControls();

    boolean isSetControls();

    void setControls(CTControls cTControls);

    CTControls addNewControls();

    void unsetControls();

    CTWebPublishItems getWebPublishItems();

    boolean isSetWebPublishItems();

    void setWebPublishItems(CTWebPublishItems cTWebPublishItems);

    CTWebPublishItems addNewWebPublishItems();

    void unsetWebPublishItems();

    CTTableParts getTableParts();

    boolean isSetTableParts();

    void setTableParts(CTTableParts cTTableParts);

    CTTableParts addNewTableParts();

    void unsetTableParts();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();
}
