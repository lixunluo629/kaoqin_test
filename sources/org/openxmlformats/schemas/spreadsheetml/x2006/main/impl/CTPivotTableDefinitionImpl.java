package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlUnsignedByte;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartFormats;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColFields;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColHierarchiesUsage;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColItems;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormats;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataFields;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFormats;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLocation;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFields;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFilters;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotHierarchies;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableStyle;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowFields;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowHierarchiesUsage;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowItems;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STXstring;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTPivotTableDefinitionImpl.class */
public class CTPivotTableDefinitionImpl extends XmlComplexContentImpl implements CTPivotTableDefinition {
    private static final QName LOCATION$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "location");
    private static final QName PIVOTFIELDS$2 = new QName(XSSFRelation.NS_SPREADSHEETML, "pivotFields");
    private static final QName ROWFIELDS$4 = new QName(XSSFRelation.NS_SPREADSHEETML, "rowFields");
    private static final QName ROWITEMS$6 = new QName(XSSFRelation.NS_SPREADSHEETML, "rowItems");
    private static final QName COLFIELDS$8 = new QName(XSSFRelation.NS_SPREADSHEETML, "colFields");
    private static final QName COLITEMS$10 = new QName(XSSFRelation.NS_SPREADSHEETML, "colItems");
    private static final QName PAGEFIELDS$12 = new QName(XSSFRelation.NS_SPREADSHEETML, "pageFields");
    private static final QName DATAFIELDS$14 = new QName(XSSFRelation.NS_SPREADSHEETML, "dataFields");
    private static final QName FORMATS$16 = new QName(XSSFRelation.NS_SPREADSHEETML, "formats");
    private static final QName CONDITIONALFORMATS$18 = new QName(XSSFRelation.NS_SPREADSHEETML, "conditionalFormats");
    private static final QName CHARTFORMATS$20 = new QName(XSSFRelation.NS_SPREADSHEETML, "chartFormats");
    private static final QName PIVOTHIERARCHIES$22 = new QName(XSSFRelation.NS_SPREADSHEETML, "pivotHierarchies");
    private static final QName PIVOTTABLESTYLEINFO$24 = new QName(XSSFRelation.NS_SPREADSHEETML, "pivotTableStyleInfo");
    private static final QName FILTERS$26 = new QName(XSSFRelation.NS_SPREADSHEETML, "filters");
    private static final QName ROWHIERARCHIESUSAGE$28 = new QName(XSSFRelation.NS_SPREADSHEETML, "rowHierarchiesUsage");
    private static final QName COLHIERARCHIESUSAGE$30 = new QName(XSSFRelation.NS_SPREADSHEETML, "colHierarchiesUsage");
    private static final QName EXTLST$32 = new QName(XSSFRelation.NS_SPREADSHEETML, "extLst");
    private static final QName NAME$34 = new QName("", "name");
    private static final QName CACHEID$36 = new QName("", "cacheId");
    private static final QName DATAONROWS$38 = new QName("", "dataOnRows");
    private static final QName DATAPOSITION$40 = new QName("", "dataPosition");
    private static final QName AUTOFORMATID$42 = new QName("", "autoFormatId");
    private static final QName APPLYNUMBERFORMATS$44 = new QName("", "applyNumberFormats");
    private static final QName APPLYBORDERFORMATS$46 = new QName("", "applyBorderFormats");
    private static final QName APPLYFONTFORMATS$48 = new QName("", "applyFontFormats");
    private static final QName APPLYPATTERNFORMATS$50 = new QName("", "applyPatternFormats");
    private static final QName APPLYALIGNMENTFORMATS$52 = new QName("", "applyAlignmentFormats");
    private static final QName APPLYWIDTHHEIGHTFORMATS$54 = new QName("", "applyWidthHeightFormats");
    private static final QName DATACAPTION$56 = new QName("", "dataCaption");
    private static final QName GRANDTOTALCAPTION$58 = new QName("", "grandTotalCaption");
    private static final QName ERRORCAPTION$60 = new QName("", "errorCaption");
    private static final QName SHOWERROR$62 = new QName("", "showError");
    private static final QName MISSINGCAPTION$64 = new QName("", "missingCaption");
    private static final QName SHOWMISSING$66 = new QName("", "showMissing");
    private static final QName PAGESTYLE$68 = new QName("", "pageStyle");
    private static final QName PIVOTTABLESTYLE$70 = new QName("", "pivotTableStyle");
    private static final QName VACATEDSTYLE$72 = new QName("", "vacatedStyle");
    private static final QName TAG$74 = new QName("", "tag");
    private static final QName UPDATEDVERSION$76 = new QName("", "updatedVersion");
    private static final QName MINREFRESHABLEVERSION$78 = new QName("", "minRefreshableVersion");
    private static final QName ASTERISKTOTALS$80 = new QName("", "asteriskTotals");
    private static final QName SHOWITEMS$82 = new QName("", "showItems");
    private static final QName EDITDATA$84 = new QName("", "editData");
    private static final QName DISABLEFIELDLIST$86 = new QName("", "disableFieldList");
    private static final QName SHOWCALCMBRS$88 = new QName("", "showCalcMbrs");
    private static final QName VISUALTOTALS$90 = new QName("", "visualTotals");
    private static final QName SHOWMULTIPLELABEL$92 = new QName("", "showMultipleLabel");
    private static final QName SHOWDATADROPDOWN$94 = new QName("", "showDataDropDown");
    private static final QName SHOWDRILL$96 = new QName("", "showDrill");
    private static final QName PRINTDRILL$98 = new QName("", "printDrill");
    private static final QName SHOWMEMBERPROPERTYTIPS$100 = new QName("", "showMemberPropertyTips");
    private static final QName SHOWDATATIPS$102 = new QName("", "showDataTips");
    private static final QName ENABLEWIZARD$104 = new QName("", "enableWizard");
    private static final QName ENABLEDRILL$106 = new QName("", "enableDrill");
    private static final QName ENABLEFIELDPROPERTIES$108 = new QName("", "enableFieldProperties");
    private static final QName PRESERVEFORMATTING$110 = new QName("", "preserveFormatting");
    private static final QName USEAUTOFORMATTING$112 = new QName("", "useAutoFormatting");
    private static final QName PAGEWRAP$114 = new QName("", "pageWrap");
    private static final QName PAGEOVERTHENDOWN$116 = new QName("", "pageOverThenDown");
    private static final QName SUBTOTALHIDDENITEMS$118 = new QName("", "subtotalHiddenItems");
    private static final QName ROWGRANDTOTALS$120 = new QName("", "rowGrandTotals");
    private static final QName COLGRANDTOTALS$122 = new QName("", "colGrandTotals");
    private static final QName FIELDPRINTTITLES$124 = new QName("", "fieldPrintTitles");
    private static final QName ITEMPRINTTITLES$126 = new QName("", "itemPrintTitles");
    private static final QName MERGEITEM$128 = new QName("", "mergeItem");
    private static final QName SHOWDROPZONES$130 = new QName("", "showDropZones");
    private static final QName CREATEDVERSION$132 = new QName("", "createdVersion");
    private static final QName INDENT$134 = new QName("", "indent");
    private static final QName SHOWEMPTYROW$136 = new QName("", "showEmptyRow");
    private static final QName SHOWEMPTYCOL$138 = new QName("", "showEmptyCol");
    private static final QName SHOWHEADERS$140 = new QName("", "showHeaders");
    private static final QName COMPACT$142 = new QName("", "compact");
    private static final QName OUTLINE$144 = new QName("", "outline");
    private static final QName OUTLINEDATA$146 = new QName("", "outlineData");
    private static final QName COMPACTDATA$148 = new QName("", "compactData");
    private static final QName PUBLISHED$150 = new QName("", "published");
    private static final QName GRIDDROPZONES$152 = new QName("", "gridDropZones");
    private static final QName IMMERSIVE$154 = new QName("", "immersive");
    private static final QName MULTIPLEFIELDFILTERS$156 = new QName("", "multipleFieldFilters");
    private static final QName CHARTFORMAT$158 = new QName("", "chartFormat");
    private static final QName ROWHEADERCAPTION$160 = new QName("", "rowHeaderCaption");
    private static final QName COLHEADERCAPTION$162 = new QName("", "colHeaderCaption");
    private static final QName FIELDLISTSORTASCENDING$164 = new QName("", "fieldListSortAscending");
    private static final QName MDXSUBQUERIES$166 = new QName("", "mdxSubqueries");
    private static final QName CUSTOMLISTSORT$168 = new QName("", "customListSort");

    public CTPivotTableDefinitionImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTLocation getLocation() {
        synchronized (monitor()) {
            check_orphaned();
            CTLocation cTLocation = (CTLocation) get_store().find_element_user(LOCATION$0, 0);
            if (cTLocation == null) {
                return null;
            }
            return cTLocation;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setLocation(CTLocation cTLocation) {
        synchronized (monitor()) {
            check_orphaned();
            CTLocation cTLocation2 = (CTLocation) get_store().find_element_user(LOCATION$0, 0);
            if (cTLocation2 == null) {
                cTLocation2 = (CTLocation) get_store().add_element_user(LOCATION$0);
            }
            cTLocation2.set(cTLocation);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTLocation addNewLocation() {
        CTLocation cTLocation;
        synchronized (monitor()) {
            check_orphaned();
            cTLocation = (CTLocation) get_store().add_element_user(LOCATION$0);
        }
        return cTLocation;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTPivotFields getPivotFields() {
        synchronized (monitor()) {
            check_orphaned();
            CTPivotFields cTPivotFields = (CTPivotFields) get_store().find_element_user(PIVOTFIELDS$2, 0);
            if (cTPivotFields == null) {
                return null;
            }
            return cTPivotFields;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetPivotFields() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PIVOTFIELDS$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setPivotFields(CTPivotFields cTPivotFields) {
        synchronized (monitor()) {
            check_orphaned();
            CTPivotFields cTPivotFields2 = (CTPivotFields) get_store().find_element_user(PIVOTFIELDS$2, 0);
            if (cTPivotFields2 == null) {
                cTPivotFields2 = (CTPivotFields) get_store().add_element_user(PIVOTFIELDS$2);
            }
            cTPivotFields2.set(cTPivotFields);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTPivotFields addNewPivotFields() {
        CTPivotFields cTPivotFields;
        synchronized (monitor()) {
            check_orphaned();
            cTPivotFields = (CTPivotFields) get_store().add_element_user(PIVOTFIELDS$2);
        }
        return cTPivotFields;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetPivotFields() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PIVOTFIELDS$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTRowFields getRowFields() {
        synchronized (monitor()) {
            check_orphaned();
            CTRowFields cTRowFields = (CTRowFields) get_store().find_element_user(ROWFIELDS$4, 0);
            if (cTRowFields == null) {
                return null;
            }
            return cTRowFields;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetRowFields() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(ROWFIELDS$4) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setRowFields(CTRowFields cTRowFields) {
        synchronized (monitor()) {
            check_orphaned();
            CTRowFields cTRowFields2 = (CTRowFields) get_store().find_element_user(ROWFIELDS$4, 0);
            if (cTRowFields2 == null) {
                cTRowFields2 = (CTRowFields) get_store().add_element_user(ROWFIELDS$4);
            }
            cTRowFields2.set(cTRowFields);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTRowFields addNewRowFields() {
        CTRowFields cTRowFields;
        synchronized (monitor()) {
            check_orphaned();
            cTRowFields = (CTRowFields) get_store().add_element_user(ROWFIELDS$4);
        }
        return cTRowFields;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetRowFields() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ROWFIELDS$4, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTRowItems getRowItems() {
        synchronized (monitor()) {
            check_orphaned();
            CTRowItems cTRowItemsFind_element_user = get_store().find_element_user(ROWITEMS$6, 0);
            if (cTRowItemsFind_element_user == null) {
                return null;
            }
            return cTRowItemsFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetRowItems() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(ROWITEMS$6) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setRowItems(CTRowItems cTRowItems) {
        synchronized (monitor()) {
            check_orphaned();
            CTRowItems cTRowItemsFind_element_user = get_store().find_element_user(ROWITEMS$6, 0);
            if (cTRowItemsFind_element_user == null) {
                cTRowItemsFind_element_user = (CTRowItems) get_store().add_element_user(ROWITEMS$6);
            }
            cTRowItemsFind_element_user.set(cTRowItems);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTRowItems addNewRowItems() {
        CTRowItems cTRowItemsAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTRowItemsAdd_element_user = get_store().add_element_user(ROWITEMS$6);
        }
        return cTRowItemsAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetRowItems() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ROWITEMS$6, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTColFields getColFields() {
        synchronized (monitor()) {
            check_orphaned();
            CTColFields cTColFields = (CTColFields) get_store().find_element_user(COLFIELDS$8, 0);
            if (cTColFields == null) {
                return null;
            }
            return cTColFields;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetColFields() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(COLFIELDS$8) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setColFields(CTColFields cTColFields) {
        synchronized (monitor()) {
            check_orphaned();
            CTColFields cTColFields2 = (CTColFields) get_store().find_element_user(COLFIELDS$8, 0);
            if (cTColFields2 == null) {
                cTColFields2 = (CTColFields) get_store().add_element_user(COLFIELDS$8);
            }
            cTColFields2.set(cTColFields);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTColFields addNewColFields() {
        CTColFields cTColFields;
        synchronized (monitor()) {
            check_orphaned();
            cTColFields = (CTColFields) get_store().add_element_user(COLFIELDS$8);
        }
        return cTColFields;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetColFields() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(COLFIELDS$8, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTColItems getColItems() {
        synchronized (monitor()) {
            check_orphaned();
            CTColItems cTColItemsFind_element_user = get_store().find_element_user(COLITEMS$10, 0);
            if (cTColItemsFind_element_user == null) {
                return null;
            }
            return cTColItemsFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetColItems() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(COLITEMS$10) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setColItems(CTColItems cTColItems) {
        synchronized (monitor()) {
            check_orphaned();
            CTColItems cTColItemsFind_element_user = get_store().find_element_user(COLITEMS$10, 0);
            if (cTColItemsFind_element_user == null) {
                cTColItemsFind_element_user = (CTColItems) get_store().add_element_user(COLITEMS$10);
            }
            cTColItemsFind_element_user.set(cTColItems);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTColItems addNewColItems() {
        CTColItems cTColItemsAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTColItemsAdd_element_user = get_store().add_element_user(COLITEMS$10);
        }
        return cTColItemsAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetColItems() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(COLITEMS$10, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTPageFields getPageFields() {
        synchronized (monitor()) {
            check_orphaned();
            CTPageFields cTPageFields = (CTPageFields) get_store().find_element_user(PAGEFIELDS$12, 0);
            if (cTPageFields == null) {
                return null;
            }
            return cTPageFields;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetPageFields() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PAGEFIELDS$12) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setPageFields(CTPageFields cTPageFields) {
        synchronized (monitor()) {
            check_orphaned();
            CTPageFields cTPageFields2 = (CTPageFields) get_store().find_element_user(PAGEFIELDS$12, 0);
            if (cTPageFields2 == null) {
                cTPageFields2 = (CTPageFields) get_store().add_element_user(PAGEFIELDS$12);
            }
            cTPageFields2.set(cTPageFields);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTPageFields addNewPageFields() {
        CTPageFields cTPageFields;
        synchronized (monitor()) {
            check_orphaned();
            cTPageFields = (CTPageFields) get_store().add_element_user(PAGEFIELDS$12);
        }
        return cTPageFields;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetPageFields() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PAGEFIELDS$12, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTDataFields getDataFields() {
        synchronized (monitor()) {
            check_orphaned();
            CTDataFields cTDataFields = (CTDataFields) get_store().find_element_user(DATAFIELDS$14, 0);
            if (cTDataFields == null) {
                return null;
            }
            return cTDataFields;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetDataFields() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(DATAFIELDS$14) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setDataFields(CTDataFields cTDataFields) {
        synchronized (monitor()) {
            check_orphaned();
            CTDataFields cTDataFields2 = (CTDataFields) get_store().find_element_user(DATAFIELDS$14, 0);
            if (cTDataFields2 == null) {
                cTDataFields2 = (CTDataFields) get_store().add_element_user(DATAFIELDS$14);
            }
            cTDataFields2.set(cTDataFields);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTDataFields addNewDataFields() {
        CTDataFields cTDataFields;
        synchronized (monitor()) {
            check_orphaned();
            cTDataFields = (CTDataFields) get_store().add_element_user(DATAFIELDS$14);
        }
        return cTDataFields;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetDataFields() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(DATAFIELDS$14, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTFormats getFormats() {
        synchronized (monitor()) {
            check_orphaned();
            CTFormats cTFormatsFind_element_user = get_store().find_element_user(FORMATS$16, 0);
            if (cTFormatsFind_element_user == null) {
                return null;
            }
            return cTFormatsFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetFormats() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(FORMATS$16) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setFormats(CTFormats cTFormats) {
        synchronized (monitor()) {
            check_orphaned();
            CTFormats cTFormatsFind_element_user = get_store().find_element_user(FORMATS$16, 0);
            if (cTFormatsFind_element_user == null) {
                cTFormatsFind_element_user = (CTFormats) get_store().add_element_user(FORMATS$16);
            }
            cTFormatsFind_element_user.set(cTFormats);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTFormats addNewFormats() {
        CTFormats cTFormatsAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTFormatsAdd_element_user = get_store().add_element_user(FORMATS$16);
        }
        return cTFormatsAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(FORMATS$16, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTConditionalFormats getConditionalFormats() {
        synchronized (monitor()) {
            check_orphaned();
            CTConditionalFormats cTConditionalFormatsFind_element_user = get_store().find_element_user(CONDITIONALFORMATS$18, 0);
            if (cTConditionalFormatsFind_element_user == null) {
                return null;
            }
            return cTConditionalFormatsFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetConditionalFormats() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(CONDITIONALFORMATS$18) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setConditionalFormats(CTConditionalFormats cTConditionalFormats) {
        synchronized (monitor()) {
            check_orphaned();
            CTConditionalFormats cTConditionalFormatsFind_element_user = get_store().find_element_user(CONDITIONALFORMATS$18, 0);
            if (cTConditionalFormatsFind_element_user == null) {
                cTConditionalFormatsFind_element_user = (CTConditionalFormats) get_store().add_element_user(CONDITIONALFORMATS$18);
            }
            cTConditionalFormatsFind_element_user.set(cTConditionalFormats);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTConditionalFormats addNewConditionalFormats() {
        CTConditionalFormats cTConditionalFormatsAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTConditionalFormatsAdd_element_user = get_store().add_element_user(CONDITIONALFORMATS$18);
        }
        return cTConditionalFormatsAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetConditionalFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CONDITIONALFORMATS$18, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTChartFormats getChartFormats() {
        synchronized (monitor()) {
            check_orphaned();
            CTChartFormats cTChartFormatsFind_element_user = get_store().find_element_user(CHARTFORMATS$20, 0);
            if (cTChartFormatsFind_element_user == null) {
                return null;
            }
            return cTChartFormatsFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetChartFormats() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(CHARTFORMATS$20) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setChartFormats(CTChartFormats cTChartFormats) {
        synchronized (monitor()) {
            check_orphaned();
            CTChartFormats cTChartFormatsFind_element_user = get_store().find_element_user(CHARTFORMATS$20, 0);
            if (cTChartFormatsFind_element_user == null) {
                cTChartFormatsFind_element_user = (CTChartFormats) get_store().add_element_user(CHARTFORMATS$20);
            }
            cTChartFormatsFind_element_user.set(cTChartFormats);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTChartFormats addNewChartFormats() {
        CTChartFormats cTChartFormatsAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTChartFormatsAdd_element_user = get_store().add_element_user(CHARTFORMATS$20);
        }
        return cTChartFormatsAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetChartFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CHARTFORMATS$20, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTPivotHierarchies getPivotHierarchies() {
        synchronized (monitor()) {
            check_orphaned();
            CTPivotHierarchies cTPivotHierarchiesFind_element_user = get_store().find_element_user(PIVOTHIERARCHIES$22, 0);
            if (cTPivotHierarchiesFind_element_user == null) {
                return null;
            }
            return cTPivotHierarchiesFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetPivotHierarchies() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PIVOTHIERARCHIES$22) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setPivotHierarchies(CTPivotHierarchies cTPivotHierarchies) {
        synchronized (monitor()) {
            check_orphaned();
            CTPivotHierarchies cTPivotHierarchiesFind_element_user = get_store().find_element_user(PIVOTHIERARCHIES$22, 0);
            if (cTPivotHierarchiesFind_element_user == null) {
                cTPivotHierarchiesFind_element_user = (CTPivotHierarchies) get_store().add_element_user(PIVOTHIERARCHIES$22);
            }
            cTPivotHierarchiesFind_element_user.set(cTPivotHierarchies);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTPivotHierarchies addNewPivotHierarchies() {
        CTPivotHierarchies cTPivotHierarchiesAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTPivotHierarchiesAdd_element_user = get_store().add_element_user(PIVOTHIERARCHIES$22);
        }
        return cTPivotHierarchiesAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetPivotHierarchies() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PIVOTHIERARCHIES$22, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTPivotTableStyle getPivotTableStyleInfo() {
        synchronized (monitor()) {
            check_orphaned();
            CTPivotTableStyle cTPivotTableStyle = (CTPivotTableStyle) get_store().find_element_user(PIVOTTABLESTYLEINFO$24, 0);
            if (cTPivotTableStyle == null) {
                return null;
            }
            return cTPivotTableStyle;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetPivotTableStyleInfo() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PIVOTTABLESTYLEINFO$24) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setPivotTableStyleInfo(CTPivotTableStyle cTPivotTableStyle) {
        synchronized (monitor()) {
            check_orphaned();
            CTPivotTableStyle cTPivotTableStyle2 = (CTPivotTableStyle) get_store().find_element_user(PIVOTTABLESTYLEINFO$24, 0);
            if (cTPivotTableStyle2 == null) {
                cTPivotTableStyle2 = (CTPivotTableStyle) get_store().add_element_user(PIVOTTABLESTYLEINFO$24);
            }
            cTPivotTableStyle2.set(cTPivotTableStyle);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTPivotTableStyle addNewPivotTableStyleInfo() {
        CTPivotTableStyle cTPivotTableStyle;
        synchronized (monitor()) {
            check_orphaned();
            cTPivotTableStyle = (CTPivotTableStyle) get_store().add_element_user(PIVOTTABLESTYLEINFO$24);
        }
        return cTPivotTableStyle;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetPivotTableStyleInfo() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PIVOTTABLESTYLEINFO$24, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTPivotFilters getFilters() {
        synchronized (monitor()) {
            check_orphaned();
            CTPivotFilters cTPivotFiltersFind_element_user = get_store().find_element_user(FILTERS$26, 0);
            if (cTPivotFiltersFind_element_user == null) {
                return null;
            }
            return cTPivotFiltersFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetFilters() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(FILTERS$26) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setFilters(CTPivotFilters cTPivotFilters) {
        synchronized (monitor()) {
            check_orphaned();
            CTPivotFilters cTPivotFiltersFind_element_user = get_store().find_element_user(FILTERS$26, 0);
            if (cTPivotFiltersFind_element_user == null) {
                cTPivotFiltersFind_element_user = (CTPivotFilters) get_store().add_element_user(FILTERS$26);
            }
            cTPivotFiltersFind_element_user.set(cTPivotFilters);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTPivotFilters addNewFilters() {
        CTPivotFilters cTPivotFiltersAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTPivotFiltersAdd_element_user = get_store().add_element_user(FILTERS$26);
        }
        return cTPivotFiltersAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetFilters() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(FILTERS$26, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTRowHierarchiesUsage getRowHierarchiesUsage() {
        synchronized (monitor()) {
            check_orphaned();
            CTRowHierarchiesUsage cTRowHierarchiesUsageFind_element_user = get_store().find_element_user(ROWHIERARCHIESUSAGE$28, 0);
            if (cTRowHierarchiesUsageFind_element_user == null) {
                return null;
            }
            return cTRowHierarchiesUsageFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetRowHierarchiesUsage() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(ROWHIERARCHIESUSAGE$28) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setRowHierarchiesUsage(CTRowHierarchiesUsage cTRowHierarchiesUsage) {
        synchronized (monitor()) {
            check_orphaned();
            CTRowHierarchiesUsage cTRowHierarchiesUsageFind_element_user = get_store().find_element_user(ROWHIERARCHIESUSAGE$28, 0);
            if (cTRowHierarchiesUsageFind_element_user == null) {
                cTRowHierarchiesUsageFind_element_user = (CTRowHierarchiesUsage) get_store().add_element_user(ROWHIERARCHIESUSAGE$28);
            }
            cTRowHierarchiesUsageFind_element_user.set(cTRowHierarchiesUsage);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTRowHierarchiesUsage addNewRowHierarchiesUsage() {
        CTRowHierarchiesUsage cTRowHierarchiesUsageAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTRowHierarchiesUsageAdd_element_user = get_store().add_element_user(ROWHIERARCHIESUSAGE$28);
        }
        return cTRowHierarchiesUsageAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetRowHierarchiesUsage() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ROWHIERARCHIESUSAGE$28, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTColHierarchiesUsage getColHierarchiesUsage() {
        synchronized (monitor()) {
            check_orphaned();
            CTColHierarchiesUsage cTColHierarchiesUsageFind_element_user = get_store().find_element_user(COLHIERARCHIESUSAGE$30, 0);
            if (cTColHierarchiesUsageFind_element_user == null) {
                return null;
            }
            return cTColHierarchiesUsageFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetColHierarchiesUsage() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(COLHIERARCHIESUSAGE$30) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setColHierarchiesUsage(CTColHierarchiesUsage cTColHierarchiesUsage) {
        synchronized (monitor()) {
            check_orphaned();
            CTColHierarchiesUsage cTColHierarchiesUsageFind_element_user = get_store().find_element_user(COLHIERARCHIESUSAGE$30, 0);
            if (cTColHierarchiesUsageFind_element_user == null) {
                cTColHierarchiesUsageFind_element_user = (CTColHierarchiesUsage) get_store().add_element_user(COLHIERARCHIESUSAGE$30);
            }
            cTColHierarchiesUsageFind_element_user.set(cTColHierarchiesUsage);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTColHierarchiesUsage addNewColHierarchiesUsage() {
        CTColHierarchiesUsage cTColHierarchiesUsageAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTColHierarchiesUsageAdd_element_user = get_store().add_element_user(COLHIERARCHIESUSAGE$30);
        }
        return cTColHierarchiesUsageAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetColHierarchiesUsage() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(COLHIERARCHIESUSAGE$30, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$32, 0);
            if (cTExtensionListFind_element_user == null) {
                return null;
            }
            return cTExtensionListFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$32) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setExtLst(CTExtensionList cTExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$32, 0);
            if (cTExtensionListFind_element_user == null) {
                cTExtensionListFind_element_user = (CTExtensionList) get_store().add_element_user(EXTLST$32);
            }
            cTExtensionListFind_element_user.set(cTExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public CTExtensionList addNewExtLst() {
        CTExtensionList cTExtensionListAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTExtensionListAdd_element_user = get_store().add_element_user(EXTLST$32);
        }
        return cTExtensionListAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$32, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public String getName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAME$34);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public STXstring xgetName() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(NAME$34);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setName(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAME$34);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NAME$34);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetName(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(NAME$34);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(NAME$34);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public long getCacheId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CACHEID$36);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlUnsignedInt xgetCacheId() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(CACHEID$36);
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setCacheId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CACHEID$36);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(CACHEID$36);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetCacheId(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(CACHEID$36);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(CACHEID$36);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getDataOnRows() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DATAONROWS$38);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(DATAONROWS$38);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetDataOnRows() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DATAONROWS$38);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(DATAONROWS$38);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetDataOnRows() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DATAONROWS$38) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setDataOnRows(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DATAONROWS$38);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DATAONROWS$38);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetDataOnRows(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DATAONROWS$38);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(DATAONROWS$38);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetDataOnRows() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DATAONROWS$38);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public long getDataPosition() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DATAPOSITION$40);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlUnsignedInt xgetDataPosition() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(DATAPOSITION$40);
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetDataPosition() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DATAPOSITION$40) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setDataPosition(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DATAPOSITION$40);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DATAPOSITION$40);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetDataPosition(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(DATAPOSITION$40);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(DATAPOSITION$40);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetDataPosition() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DATAPOSITION$40);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public long getAutoFormatId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AUTOFORMATID$42);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlUnsignedInt xgetAutoFormatId() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(AUTOFORMATID$42);
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetAutoFormatId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(AUTOFORMATID$42) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setAutoFormatId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AUTOFORMATID$42);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(AUTOFORMATID$42);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetAutoFormatId(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(AUTOFORMATID$42);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(AUTOFORMATID$42);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetAutoFormatId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(AUTOFORMATID$42);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getApplyNumberFormats() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(APPLYNUMBERFORMATS$44);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetApplyNumberFormats() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(APPLYNUMBERFORMATS$44);
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetApplyNumberFormats() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(APPLYNUMBERFORMATS$44) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setApplyNumberFormats(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(APPLYNUMBERFORMATS$44);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(APPLYNUMBERFORMATS$44);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetApplyNumberFormats(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(APPLYNUMBERFORMATS$44);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(APPLYNUMBERFORMATS$44);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetApplyNumberFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(APPLYNUMBERFORMATS$44);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getApplyBorderFormats() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(APPLYBORDERFORMATS$46);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetApplyBorderFormats() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(APPLYBORDERFORMATS$46);
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetApplyBorderFormats() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(APPLYBORDERFORMATS$46) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setApplyBorderFormats(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(APPLYBORDERFORMATS$46);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(APPLYBORDERFORMATS$46);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetApplyBorderFormats(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(APPLYBORDERFORMATS$46);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(APPLYBORDERFORMATS$46);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetApplyBorderFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(APPLYBORDERFORMATS$46);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getApplyFontFormats() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(APPLYFONTFORMATS$48);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetApplyFontFormats() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(APPLYFONTFORMATS$48);
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetApplyFontFormats() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(APPLYFONTFORMATS$48) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setApplyFontFormats(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(APPLYFONTFORMATS$48);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(APPLYFONTFORMATS$48);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetApplyFontFormats(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(APPLYFONTFORMATS$48);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(APPLYFONTFORMATS$48);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetApplyFontFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(APPLYFONTFORMATS$48);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getApplyPatternFormats() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(APPLYPATTERNFORMATS$50);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetApplyPatternFormats() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(APPLYPATTERNFORMATS$50);
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetApplyPatternFormats() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(APPLYPATTERNFORMATS$50) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setApplyPatternFormats(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(APPLYPATTERNFORMATS$50);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(APPLYPATTERNFORMATS$50);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetApplyPatternFormats(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(APPLYPATTERNFORMATS$50);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(APPLYPATTERNFORMATS$50);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetApplyPatternFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(APPLYPATTERNFORMATS$50);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getApplyAlignmentFormats() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(APPLYALIGNMENTFORMATS$52);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetApplyAlignmentFormats() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(APPLYALIGNMENTFORMATS$52);
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetApplyAlignmentFormats() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(APPLYALIGNMENTFORMATS$52) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setApplyAlignmentFormats(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(APPLYALIGNMENTFORMATS$52);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(APPLYALIGNMENTFORMATS$52);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetApplyAlignmentFormats(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(APPLYALIGNMENTFORMATS$52);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(APPLYALIGNMENTFORMATS$52);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetApplyAlignmentFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(APPLYALIGNMENTFORMATS$52);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getApplyWidthHeightFormats() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(APPLYWIDTHHEIGHTFORMATS$54);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetApplyWidthHeightFormats() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(APPLYWIDTHHEIGHTFORMATS$54);
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetApplyWidthHeightFormats() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(APPLYWIDTHHEIGHTFORMATS$54) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setApplyWidthHeightFormats(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(APPLYWIDTHHEIGHTFORMATS$54);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(APPLYWIDTHHEIGHTFORMATS$54);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetApplyWidthHeightFormats(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(APPLYWIDTHHEIGHTFORMATS$54);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(APPLYWIDTHHEIGHTFORMATS$54);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetApplyWidthHeightFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(APPLYWIDTHHEIGHTFORMATS$54);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public String getDataCaption() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DATACAPTION$56);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public STXstring xgetDataCaption() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(DATACAPTION$56);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setDataCaption(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DATACAPTION$56);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DATACAPTION$56);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetDataCaption(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(DATACAPTION$56);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(DATACAPTION$56);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public String getGrandTotalCaption() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(GRANDTOTALCAPTION$58);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public STXstring xgetGrandTotalCaption() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(GRANDTOTALCAPTION$58);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetGrandTotalCaption() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(GRANDTOTALCAPTION$58) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setGrandTotalCaption(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(GRANDTOTALCAPTION$58);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(GRANDTOTALCAPTION$58);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetGrandTotalCaption(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(GRANDTOTALCAPTION$58);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(GRANDTOTALCAPTION$58);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetGrandTotalCaption() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(GRANDTOTALCAPTION$58);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public String getErrorCaption() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ERRORCAPTION$60);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public STXstring xgetErrorCaption() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(ERRORCAPTION$60);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetErrorCaption() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ERRORCAPTION$60) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setErrorCaption(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ERRORCAPTION$60);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ERRORCAPTION$60);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetErrorCaption(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(ERRORCAPTION$60);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(ERRORCAPTION$60);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetErrorCaption() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ERRORCAPTION$60);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getShowError() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWERROR$62);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWERROR$62);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetShowError() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWERROR$62);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWERROR$62);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetShowError() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWERROR$62) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setShowError(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWERROR$62);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWERROR$62);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetShowError(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWERROR$62);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWERROR$62);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetShowError() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWERROR$62);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public String getMissingCaption() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MISSINGCAPTION$64);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public STXstring xgetMissingCaption() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(MISSINGCAPTION$64);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetMissingCaption() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MISSINGCAPTION$64) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setMissingCaption(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MISSINGCAPTION$64);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(MISSINGCAPTION$64);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetMissingCaption(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(MISSINGCAPTION$64);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(MISSINGCAPTION$64);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetMissingCaption() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MISSINGCAPTION$64);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getShowMissing() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWMISSING$66);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWMISSING$66);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetShowMissing() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWMISSING$66);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWMISSING$66);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetShowMissing() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWMISSING$66) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setShowMissing(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWMISSING$66);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWMISSING$66);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetShowMissing(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWMISSING$66);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWMISSING$66);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetShowMissing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWMISSING$66);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public String getPageStyle() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PAGESTYLE$68);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public STXstring xgetPageStyle() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(PAGESTYLE$68);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetPageStyle() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PAGESTYLE$68) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setPageStyle(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PAGESTYLE$68);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PAGESTYLE$68);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetPageStyle(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(PAGESTYLE$68);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(PAGESTYLE$68);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetPageStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PAGESTYLE$68);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public String getPivotTableStyle() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PIVOTTABLESTYLE$70);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public STXstring xgetPivotTableStyle() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(PIVOTTABLESTYLE$70);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetPivotTableStyle() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PIVOTTABLESTYLE$70) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setPivotTableStyle(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PIVOTTABLESTYLE$70);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PIVOTTABLESTYLE$70);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetPivotTableStyle(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(PIVOTTABLESTYLE$70);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(PIVOTTABLESTYLE$70);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetPivotTableStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PIVOTTABLESTYLE$70);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public String getVacatedStyle() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VACATEDSTYLE$72);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public STXstring xgetVacatedStyle() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(VACATEDSTYLE$72);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetVacatedStyle() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VACATEDSTYLE$72) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setVacatedStyle(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VACATEDSTYLE$72);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VACATEDSTYLE$72);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetVacatedStyle(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(VACATEDSTYLE$72);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(VACATEDSTYLE$72);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetVacatedStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VACATEDSTYLE$72);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public String getTag() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TAG$74);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public STXstring xgetTag() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(TAG$74);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetTag() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TAG$74) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setTag(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TAG$74);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TAG$74);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetTag(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(TAG$74);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(TAG$74);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetTag() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TAG$74);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public short getUpdatedVersion() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(UPDATEDVERSION$76);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(UPDATEDVERSION$76);
            }
            if (simpleValue == null) {
                return (short) 0;
            }
            return simpleValue.getShortValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlUnsignedByte xgetUpdatedVersion() {
        XmlUnsignedByte xmlUnsignedByte;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedByte xmlUnsignedByte2 = (XmlUnsignedByte) get_store().find_attribute_user(UPDATEDVERSION$76);
            if (xmlUnsignedByte2 == null) {
                xmlUnsignedByte2 = (XmlUnsignedByte) get_default_attribute_value(UPDATEDVERSION$76);
            }
            xmlUnsignedByte = xmlUnsignedByte2;
        }
        return xmlUnsignedByte;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetUpdatedVersion() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(UPDATEDVERSION$76) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setUpdatedVersion(short s) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(UPDATEDVERSION$76);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(UPDATEDVERSION$76);
            }
            simpleValue.setShortValue(s);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetUpdatedVersion(XmlUnsignedByte xmlUnsignedByte) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedByte xmlUnsignedByte2 = (XmlUnsignedByte) get_store().find_attribute_user(UPDATEDVERSION$76);
            if (xmlUnsignedByte2 == null) {
                xmlUnsignedByte2 = (XmlUnsignedByte) get_store().add_attribute_user(UPDATEDVERSION$76);
            }
            xmlUnsignedByte2.set(xmlUnsignedByte);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetUpdatedVersion() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(UPDATEDVERSION$76);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public short getMinRefreshableVersion() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MINREFRESHABLEVERSION$78);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(MINREFRESHABLEVERSION$78);
            }
            if (simpleValue == null) {
                return (short) 0;
            }
            return simpleValue.getShortValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlUnsignedByte xgetMinRefreshableVersion() {
        XmlUnsignedByte xmlUnsignedByte;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedByte xmlUnsignedByte2 = (XmlUnsignedByte) get_store().find_attribute_user(MINREFRESHABLEVERSION$78);
            if (xmlUnsignedByte2 == null) {
                xmlUnsignedByte2 = (XmlUnsignedByte) get_default_attribute_value(MINREFRESHABLEVERSION$78);
            }
            xmlUnsignedByte = xmlUnsignedByte2;
        }
        return xmlUnsignedByte;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetMinRefreshableVersion() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MINREFRESHABLEVERSION$78) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setMinRefreshableVersion(short s) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MINREFRESHABLEVERSION$78);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(MINREFRESHABLEVERSION$78);
            }
            simpleValue.setShortValue(s);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetMinRefreshableVersion(XmlUnsignedByte xmlUnsignedByte) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedByte xmlUnsignedByte2 = (XmlUnsignedByte) get_store().find_attribute_user(MINREFRESHABLEVERSION$78);
            if (xmlUnsignedByte2 == null) {
                xmlUnsignedByte2 = (XmlUnsignedByte) get_store().add_attribute_user(MINREFRESHABLEVERSION$78);
            }
            xmlUnsignedByte2.set(xmlUnsignedByte);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetMinRefreshableVersion() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MINREFRESHABLEVERSION$78);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getAsteriskTotals() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ASTERISKTOTALS$80);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ASTERISKTOTALS$80);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetAsteriskTotals() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ASTERISKTOTALS$80);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(ASTERISKTOTALS$80);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetAsteriskTotals() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ASTERISKTOTALS$80) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setAsteriskTotals(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ASTERISKTOTALS$80);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ASTERISKTOTALS$80);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetAsteriskTotals(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ASTERISKTOTALS$80);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ASTERISKTOTALS$80);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetAsteriskTotals() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ASTERISKTOTALS$80);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getShowItems() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWITEMS$82);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWITEMS$82);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetShowItems() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWITEMS$82);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWITEMS$82);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetShowItems() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWITEMS$82) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setShowItems(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWITEMS$82);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWITEMS$82);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetShowItems(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWITEMS$82);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWITEMS$82);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetShowItems() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWITEMS$82);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getEditData() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(EDITDATA$84);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(EDITDATA$84);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetEditData() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(EDITDATA$84);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(EDITDATA$84);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetEditData() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(EDITDATA$84) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setEditData(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(EDITDATA$84);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(EDITDATA$84);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetEditData(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(EDITDATA$84);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(EDITDATA$84);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetEditData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(EDITDATA$84);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getDisableFieldList() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DISABLEFIELDLIST$86);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(DISABLEFIELDLIST$86);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetDisableFieldList() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DISABLEFIELDLIST$86);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(DISABLEFIELDLIST$86);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetDisableFieldList() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DISABLEFIELDLIST$86) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setDisableFieldList(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DISABLEFIELDLIST$86);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DISABLEFIELDLIST$86);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetDisableFieldList(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DISABLEFIELDLIST$86);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(DISABLEFIELDLIST$86);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetDisableFieldList() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DISABLEFIELDLIST$86);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getShowCalcMbrs() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWCALCMBRS$88);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWCALCMBRS$88);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetShowCalcMbrs() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWCALCMBRS$88);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWCALCMBRS$88);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetShowCalcMbrs() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWCALCMBRS$88) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setShowCalcMbrs(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWCALCMBRS$88);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWCALCMBRS$88);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetShowCalcMbrs(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWCALCMBRS$88);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWCALCMBRS$88);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetShowCalcMbrs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWCALCMBRS$88);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getVisualTotals() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VISUALTOTALS$90);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(VISUALTOTALS$90);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetVisualTotals() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(VISUALTOTALS$90);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(VISUALTOTALS$90);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetVisualTotals() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VISUALTOTALS$90) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setVisualTotals(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VISUALTOTALS$90);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VISUALTOTALS$90);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetVisualTotals(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(VISUALTOTALS$90);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(VISUALTOTALS$90);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetVisualTotals() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VISUALTOTALS$90);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getShowMultipleLabel() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWMULTIPLELABEL$92);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWMULTIPLELABEL$92);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetShowMultipleLabel() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWMULTIPLELABEL$92);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWMULTIPLELABEL$92);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetShowMultipleLabel() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWMULTIPLELABEL$92) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setShowMultipleLabel(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWMULTIPLELABEL$92);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWMULTIPLELABEL$92);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetShowMultipleLabel(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWMULTIPLELABEL$92);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWMULTIPLELABEL$92);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetShowMultipleLabel() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWMULTIPLELABEL$92);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getShowDataDropDown() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWDATADROPDOWN$94);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWDATADROPDOWN$94);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetShowDataDropDown() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWDATADROPDOWN$94);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWDATADROPDOWN$94);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetShowDataDropDown() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWDATADROPDOWN$94) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setShowDataDropDown(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWDATADROPDOWN$94);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWDATADROPDOWN$94);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetShowDataDropDown(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWDATADROPDOWN$94);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWDATADROPDOWN$94);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetShowDataDropDown() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWDATADROPDOWN$94);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getShowDrill() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWDRILL$96);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWDRILL$96);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetShowDrill() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWDRILL$96);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWDRILL$96);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetShowDrill() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWDRILL$96) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setShowDrill(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWDRILL$96);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWDRILL$96);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetShowDrill(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWDRILL$96);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWDRILL$96);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetShowDrill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWDRILL$96);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getPrintDrill() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PRINTDRILL$98);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PRINTDRILL$98);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetPrintDrill() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PRINTDRILL$98);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(PRINTDRILL$98);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetPrintDrill() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PRINTDRILL$98) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setPrintDrill(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PRINTDRILL$98);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PRINTDRILL$98);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetPrintDrill(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PRINTDRILL$98);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(PRINTDRILL$98);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetPrintDrill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PRINTDRILL$98);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getShowMemberPropertyTips() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWMEMBERPROPERTYTIPS$100);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWMEMBERPROPERTYTIPS$100);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetShowMemberPropertyTips() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWMEMBERPROPERTYTIPS$100);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWMEMBERPROPERTYTIPS$100);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetShowMemberPropertyTips() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWMEMBERPROPERTYTIPS$100) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setShowMemberPropertyTips(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWMEMBERPROPERTYTIPS$100);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWMEMBERPROPERTYTIPS$100);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetShowMemberPropertyTips(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWMEMBERPROPERTYTIPS$100);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWMEMBERPROPERTYTIPS$100);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetShowMemberPropertyTips() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWMEMBERPROPERTYTIPS$100);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getShowDataTips() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWDATATIPS$102);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWDATATIPS$102);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetShowDataTips() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWDATATIPS$102);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWDATATIPS$102);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetShowDataTips() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWDATATIPS$102) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setShowDataTips(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWDATATIPS$102);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWDATATIPS$102);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetShowDataTips(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWDATATIPS$102);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWDATATIPS$102);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetShowDataTips() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWDATATIPS$102);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getEnableWizard() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENABLEWIZARD$104);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ENABLEWIZARD$104);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetEnableWizard() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ENABLEWIZARD$104);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(ENABLEWIZARD$104);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetEnableWizard() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ENABLEWIZARD$104) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setEnableWizard(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENABLEWIZARD$104);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ENABLEWIZARD$104);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetEnableWizard(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ENABLEWIZARD$104);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ENABLEWIZARD$104);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetEnableWizard() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ENABLEWIZARD$104);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getEnableDrill() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENABLEDRILL$106);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ENABLEDRILL$106);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetEnableDrill() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ENABLEDRILL$106);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(ENABLEDRILL$106);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetEnableDrill() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ENABLEDRILL$106) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setEnableDrill(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENABLEDRILL$106);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ENABLEDRILL$106);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetEnableDrill(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ENABLEDRILL$106);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ENABLEDRILL$106);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetEnableDrill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ENABLEDRILL$106);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getEnableFieldProperties() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENABLEFIELDPROPERTIES$108);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ENABLEFIELDPROPERTIES$108);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetEnableFieldProperties() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ENABLEFIELDPROPERTIES$108);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(ENABLEFIELDPROPERTIES$108);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetEnableFieldProperties() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ENABLEFIELDPROPERTIES$108) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setEnableFieldProperties(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENABLEFIELDPROPERTIES$108);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ENABLEFIELDPROPERTIES$108);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetEnableFieldProperties(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ENABLEFIELDPROPERTIES$108);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ENABLEFIELDPROPERTIES$108);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetEnableFieldProperties() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ENABLEFIELDPROPERTIES$108);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getPreserveFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PRESERVEFORMATTING$110);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PRESERVEFORMATTING$110);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetPreserveFormatting() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PRESERVEFORMATTING$110);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(PRESERVEFORMATTING$110);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetPreserveFormatting() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PRESERVEFORMATTING$110) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setPreserveFormatting(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PRESERVEFORMATTING$110);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PRESERVEFORMATTING$110);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetPreserveFormatting(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PRESERVEFORMATTING$110);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(PRESERVEFORMATTING$110);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetPreserveFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PRESERVEFORMATTING$110);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getUseAutoFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(USEAUTOFORMATTING$112);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(USEAUTOFORMATTING$112);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetUseAutoFormatting() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(USEAUTOFORMATTING$112);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(USEAUTOFORMATTING$112);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetUseAutoFormatting() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(USEAUTOFORMATTING$112) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setUseAutoFormatting(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(USEAUTOFORMATTING$112);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(USEAUTOFORMATTING$112);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetUseAutoFormatting(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(USEAUTOFORMATTING$112);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(USEAUTOFORMATTING$112);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetUseAutoFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(USEAUTOFORMATTING$112);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public long getPageWrap() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PAGEWRAP$114);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PAGEWRAP$114);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlUnsignedInt xgetPageWrap() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(PAGEWRAP$114);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(PAGEWRAP$114);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetPageWrap() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PAGEWRAP$114) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setPageWrap(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PAGEWRAP$114);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PAGEWRAP$114);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetPageWrap(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(PAGEWRAP$114);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(PAGEWRAP$114);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetPageWrap() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PAGEWRAP$114);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getPageOverThenDown() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PAGEOVERTHENDOWN$116);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PAGEOVERTHENDOWN$116);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetPageOverThenDown() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PAGEOVERTHENDOWN$116);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(PAGEOVERTHENDOWN$116);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetPageOverThenDown() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PAGEOVERTHENDOWN$116) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setPageOverThenDown(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PAGEOVERTHENDOWN$116);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PAGEOVERTHENDOWN$116);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetPageOverThenDown(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PAGEOVERTHENDOWN$116);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(PAGEOVERTHENDOWN$116);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetPageOverThenDown() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PAGEOVERTHENDOWN$116);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getSubtotalHiddenItems() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SUBTOTALHIDDENITEMS$118);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SUBTOTALHIDDENITEMS$118);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetSubtotalHiddenItems() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SUBTOTALHIDDENITEMS$118);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SUBTOTALHIDDENITEMS$118);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetSubtotalHiddenItems() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SUBTOTALHIDDENITEMS$118) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setSubtotalHiddenItems(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SUBTOTALHIDDENITEMS$118);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SUBTOTALHIDDENITEMS$118);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetSubtotalHiddenItems(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SUBTOTALHIDDENITEMS$118);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SUBTOTALHIDDENITEMS$118);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetSubtotalHiddenItems() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SUBTOTALHIDDENITEMS$118);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getRowGrandTotals() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ROWGRANDTOTALS$120);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ROWGRANDTOTALS$120);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetRowGrandTotals() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ROWGRANDTOTALS$120);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(ROWGRANDTOTALS$120);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetRowGrandTotals() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ROWGRANDTOTALS$120) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setRowGrandTotals(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ROWGRANDTOTALS$120);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ROWGRANDTOTALS$120);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetRowGrandTotals(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ROWGRANDTOTALS$120);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ROWGRANDTOTALS$120);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetRowGrandTotals() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ROWGRANDTOTALS$120);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getColGrandTotals() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COLGRANDTOTALS$122);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(COLGRANDTOTALS$122);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetColGrandTotals() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(COLGRANDTOTALS$122);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(COLGRANDTOTALS$122);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetColGrandTotals() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COLGRANDTOTALS$122) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setColGrandTotals(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COLGRANDTOTALS$122);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(COLGRANDTOTALS$122);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetColGrandTotals(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(COLGRANDTOTALS$122);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(COLGRANDTOTALS$122);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetColGrandTotals() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COLGRANDTOTALS$122);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getFieldPrintTitles() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FIELDPRINTTITLES$124);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(FIELDPRINTTITLES$124);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetFieldPrintTitles() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(FIELDPRINTTITLES$124);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(FIELDPRINTTITLES$124);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetFieldPrintTitles() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FIELDPRINTTITLES$124) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setFieldPrintTitles(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FIELDPRINTTITLES$124);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FIELDPRINTTITLES$124);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetFieldPrintTitles(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(FIELDPRINTTITLES$124);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(FIELDPRINTTITLES$124);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetFieldPrintTitles() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FIELDPRINTTITLES$124);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getItemPrintTitles() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ITEMPRINTTITLES$126);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ITEMPRINTTITLES$126);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetItemPrintTitles() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ITEMPRINTTITLES$126);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(ITEMPRINTTITLES$126);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetItemPrintTitles() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ITEMPRINTTITLES$126) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setItemPrintTitles(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ITEMPRINTTITLES$126);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ITEMPRINTTITLES$126);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetItemPrintTitles(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ITEMPRINTTITLES$126);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ITEMPRINTTITLES$126);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetItemPrintTitles() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ITEMPRINTTITLES$126);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getMergeItem() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MERGEITEM$128);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(MERGEITEM$128);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetMergeItem() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(MERGEITEM$128);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(MERGEITEM$128);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetMergeItem() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MERGEITEM$128) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setMergeItem(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MERGEITEM$128);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(MERGEITEM$128);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetMergeItem(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(MERGEITEM$128);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(MERGEITEM$128);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetMergeItem() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MERGEITEM$128);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getShowDropZones() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWDROPZONES$130);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWDROPZONES$130);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetShowDropZones() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWDROPZONES$130);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWDROPZONES$130);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetShowDropZones() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWDROPZONES$130) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setShowDropZones(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWDROPZONES$130);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWDROPZONES$130);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetShowDropZones(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWDROPZONES$130);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWDROPZONES$130);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetShowDropZones() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWDROPZONES$130);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public short getCreatedVersion() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CREATEDVERSION$132);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(CREATEDVERSION$132);
            }
            if (simpleValue == null) {
                return (short) 0;
            }
            return simpleValue.getShortValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlUnsignedByte xgetCreatedVersion() {
        XmlUnsignedByte xmlUnsignedByte;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedByte xmlUnsignedByte2 = (XmlUnsignedByte) get_store().find_attribute_user(CREATEDVERSION$132);
            if (xmlUnsignedByte2 == null) {
                xmlUnsignedByte2 = (XmlUnsignedByte) get_default_attribute_value(CREATEDVERSION$132);
            }
            xmlUnsignedByte = xmlUnsignedByte2;
        }
        return xmlUnsignedByte;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetCreatedVersion() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(CREATEDVERSION$132) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setCreatedVersion(short s) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CREATEDVERSION$132);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(CREATEDVERSION$132);
            }
            simpleValue.setShortValue(s);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetCreatedVersion(XmlUnsignedByte xmlUnsignedByte) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedByte xmlUnsignedByte2 = (XmlUnsignedByte) get_store().find_attribute_user(CREATEDVERSION$132);
            if (xmlUnsignedByte2 == null) {
                xmlUnsignedByte2 = (XmlUnsignedByte) get_store().add_attribute_user(CREATEDVERSION$132);
            }
            xmlUnsignedByte2.set(xmlUnsignedByte);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetCreatedVersion() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(CREATEDVERSION$132);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public long getIndent() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INDENT$134);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(INDENT$134);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlUnsignedInt xgetIndent() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(INDENT$134);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(INDENT$134);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetIndent() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(INDENT$134) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setIndent(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INDENT$134);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(INDENT$134);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetIndent(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(INDENT$134);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(INDENT$134);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetIndent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(INDENT$134);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getShowEmptyRow() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWEMPTYROW$136);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWEMPTYROW$136);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetShowEmptyRow() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWEMPTYROW$136);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWEMPTYROW$136);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetShowEmptyRow() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWEMPTYROW$136) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setShowEmptyRow(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWEMPTYROW$136);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWEMPTYROW$136);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetShowEmptyRow(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWEMPTYROW$136);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWEMPTYROW$136);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetShowEmptyRow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWEMPTYROW$136);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getShowEmptyCol() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWEMPTYCOL$138);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWEMPTYCOL$138);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetShowEmptyCol() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWEMPTYCOL$138);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWEMPTYCOL$138);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetShowEmptyCol() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWEMPTYCOL$138) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setShowEmptyCol(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWEMPTYCOL$138);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWEMPTYCOL$138);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetShowEmptyCol(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWEMPTYCOL$138);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWEMPTYCOL$138);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetShowEmptyCol() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWEMPTYCOL$138);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getShowHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWHEADERS$140);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWHEADERS$140);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetShowHeaders() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWHEADERS$140);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWHEADERS$140);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetShowHeaders() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWHEADERS$140) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setShowHeaders(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWHEADERS$140);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWHEADERS$140);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetShowHeaders(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWHEADERS$140);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWHEADERS$140);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetShowHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWHEADERS$140);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getCompact() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COMPACT$142);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(COMPACT$142);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetCompact() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(COMPACT$142);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(COMPACT$142);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetCompact() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COMPACT$142) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setCompact(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COMPACT$142);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(COMPACT$142);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetCompact(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(COMPACT$142);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(COMPACT$142);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetCompact() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COMPACT$142);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getOutline() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(OUTLINE$144);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(OUTLINE$144);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetOutline() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(OUTLINE$144);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(OUTLINE$144);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetOutline() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(OUTLINE$144) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setOutline(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(OUTLINE$144);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(OUTLINE$144);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetOutline(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(OUTLINE$144);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(OUTLINE$144);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetOutline() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(OUTLINE$144);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getOutlineData() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(OUTLINEDATA$146);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(OUTLINEDATA$146);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetOutlineData() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(OUTLINEDATA$146);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(OUTLINEDATA$146);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetOutlineData() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(OUTLINEDATA$146) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setOutlineData(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(OUTLINEDATA$146);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(OUTLINEDATA$146);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetOutlineData(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(OUTLINEDATA$146);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(OUTLINEDATA$146);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetOutlineData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(OUTLINEDATA$146);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getCompactData() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COMPACTDATA$148);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(COMPACTDATA$148);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetCompactData() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(COMPACTDATA$148);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(COMPACTDATA$148);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetCompactData() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COMPACTDATA$148) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setCompactData(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COMPACTDATA$148);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(COMPACTDATA$148);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetCompactData(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(COMPACTDATA$148);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(COMPACTDATA$148);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetCompactData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COMPACTDATA$148);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getPublished() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PUBLISHED$150);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PUBLISHED$150);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetPublished() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PUBLISHED$150);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(PUBLISHED$150);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetPublished() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PUBLISHED$150) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setPublished(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PUBLISHED$150);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PUBLISHED$150);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetPublished(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PUBLISHED$150);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(PUBLISHED$150);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetPublished() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PUBLISHED$150);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getGridDropZones() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(GRIDDROPZONES$152);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(GRIDDROPZONES$152);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetGridDropZones() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(GRIDDROPZONES$152);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(GRIDDROPZONES$152);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetGridDropZones() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(GRIDDROPZONES$152) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setGridDropZones(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(GRIDDROPZONES$152);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(GRIDDROPZONES$152);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetGridDropZones(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(GRIDDROPZONES$152);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(GRIDDROPZONES$152);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetGridDropZones() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(GRIDDROPZONES$152);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getImmersive() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(IMMERSIVE$154);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(IMMERSIVE$154);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetImmersive() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(IMMERSIVE$154);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(IMMERSIVE$154);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetImmersive() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(IMMERSIVE$154) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setImmersive(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(IMMERSIVE$154);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(IMMERSIVE$154);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetImmersive(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(IMMERSIVE$154);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(IMMERSIVE$154);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetImmersive() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(IMMERSIVE$154);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getMultipleFieldFilters() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MULTIPLEFIELDFILTERS$156);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(MULTIPLEFIELDFILTERS$156);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetMultipleFieldFilters() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(MULTIPLEFIELDFILTERS$156);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(MULTIPLEFIELDFILTERS$156);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetMultipleFieldFilters() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MULTIPLEFIELDFILTERS$156) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setMultipleFieldFilters(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MULTIPLEFIELDFILTERS$156);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(MULTIPLEFIELDFILTERS$156);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetMultipleFieldFilters(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(MULTIPLEFIELDFILTERS$156);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(MULTIPLEFIELDFILTERS$156);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetMultipleFieldFilters() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MULTIPLEFIELDFILTERS$156);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public long getChartFormat() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CHARTFORMAT$158);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(CHARTFORMAT$158);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlUnsignedInt xgetChartFormat() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(CHARTFORMAT$158);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(CHARTFORMAT$158);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetChartFormat() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(CHARTFORMAT$158) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setChartFormat(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CHARTFORMAT$158);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(CHARTFORMAT$158);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetChartFormat(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(CHARTFORMAT$158);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(CHARTFORMAT$158);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetChartFormat() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(CHARTFORMAT$158);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public String getRowHeaderCaption() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ROWHEADERCAPTION$160);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public STXstring xgetRowHeaderCaption() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(ROWHEADERCAPTION$160);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetRowHeaderCaption() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ROWHEADERCAPTION$160) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setRowHeaderCaption(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ROWHEADERCAPTION$160);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ROWHEADERCAPTION$160);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetRowHeaderCaption(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(ROWHEADERCAPTION$160);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(ROWHEADERCAPTION$160);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetRowHeaderCaption() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ROWHEADERCAPTION$160);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public String getColHeaderCaption() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COLHEADERCAPTION$162);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public STXstring xgetColHeaderCaption() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(COLHEADERCAPTION$162);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetColHeaderCaption() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COLHEADERCAPTION$162) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setColHeaderCaption(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COLHEADERCAPTION$162);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(COLHEADERCAPTION$162);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetColHeaderCaption(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(COLHEADERCAPTION$162);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(COLHEADERCAPTION$162);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetColHeaderCaption() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COLHEADERCAPTION$162);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getFieldListSortAscending() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FIELDLISTSORTASCENDING$164);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(FIELDLISTSORTASCENDING$164);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetFieldListSortAscending() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(FIELDLISTSORTASCENDING$164);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(FIELDLISTSORTASCENDING$164);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetFieldListSortAscending() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FIELDLISTSORTASCENDING$164) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setFieldListSortAscending(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FIELDLISTSORTASCENDING$164);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FIELDLISTSORTASCENDING$164);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetFieldListSortAscending(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(FIELDLISTSORTASCENDING$164);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(FIELDLISTSORTASCENDING$164);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetFieldListSortAscending() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FIELDLISTSORTASCENDING$164);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getMdxSubqueries() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MDXSUBQUERIES$166);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(MDXSUBQUERIES$166);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetMdxSubqueries() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(MDXSUBQUERIES$166);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(MDXSUBQUERIES$166);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetMdxSubqueries() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MDXSUBQUERIES$166) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setMdxSubqueries(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MDXSUBQUERIES$166);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(MDXSUBQUERIES$166);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetMdxSubqueries(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(MDXSUBQUERIES$166);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(MDXSUBQUERIES$166);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetMdxSubqueries() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MDXSUBQUERIES$166);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean getCustomListSort() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CUSTOMLISTSORT$168);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(CUSTOMLISTSORT$168);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public XmlBoolean xgetCustomListSort() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(CUSTOMLISTSORT$168);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(CUSTOMLISTSORT$168);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public boolean isSetCustomListSort() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(CUSTOMLISTSORT$168) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void setCustomListSort(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CUSTOMLISTSORT$168);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(CUSTOMLISTSORT$168);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void xsetCustomListSort(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(CUSTOMLISTSORT$168);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(CUSTOMLISTSORT$168);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
    public void unsetCustomListSort() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(CUSTOMLISTSORT$168);
        }
    }
}
