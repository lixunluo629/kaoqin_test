package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSortState;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDxfId;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STTableType;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STTableType$Enum;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STXstring;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTTableImpl.class */
public class CTTableImpl extends XmlComplexContentImpl implements CTTable {
    private static final QName AUTOFILTER$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "autoFilter");
    private static final QName SORTSTATE$2 = new QName(XSSFRelation.NS_SPREADSHEETML, "sortState");
    private static final QName TABLECOLUMNS$4 = new QName(XSSFRelation.NS_SPREADSHEETML, "tableColumns");
    private static final QName TABLESTYLEINFO$6 = new QName(XSSFRelation.NS_SPREADSHEETML, "tableStyleInfo");
    private static final QName EXTLST$8 = new QName(XSSFRelation.NS_SPREADSHEETML, "extLst");
    private static final QName ID$10 = new QName("", "id");
    private static final QName NAME$12 = new QName("", "name");
    private static final QName DISPLAYNAME$14 = new QName("", "displayName");
    private static final QName COMMENT$16 = new QName("", "comment");
    private static final QName REF$18 = new QName("", "ref");
    private static final QName TABLETYPE$20 = new QName("", "tableType");
    private static final QName HEADERROWCOUNT$22 = new QName("", "headerRowCount");
    private static final QName INSERTROW$24 = new QName("", "insertRow");
    private static final QName INSERTROWSHIFT$26 = new QName("", "insertRowShift");
    private static final QName TOTALSROWCOUNT$28 = new QName("", "totalsRowCount");
    private static final QName TOTALSROWSHOWN$30 = new QName("", "totalsRowShown");
    private static final QName PUBLISHED$32 = new QName("", "published");
    private static final QName HEADERROWDXFID$34 = new QName("", "headerRowDxfId");
    private static final QName DATADXFID$36 = new QName("", "dataDxfId");
    private static final QName TOTALSROWDXFID$38 = new QName("", "totalsRowDxfId");
    private static final QName HEADERROWBORDERDXFID$40 = new QName("", "headerRowBorderDxfId");
    private static final QName TABLEBORDERDXFID$42 = new QName("", "tableBorderDxfId");
    private static final QName TOTALSROWBORDERDXFID$44 = new QName("", "totalsRowBorderDxfId");
    private static final QName HEADERROWCELLSTYLE$46 = new QName("", "headerRowCellStyle");
    private static final QName DATACELLSTYLE$48 = new QName("", "dataCellStyle");
    private static final QName TOTALSROWCELLSTYLE$50 = new QName("", "totalsRowCellStyle");
    private static final QName CONNECTIONID$52 = new QName("", "connectionId");

    public CTTableImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public CTAutoFilter getAutoFilter() {
        synchronized (monitor()) {
            check_orphaned();
            CTAutoFilter cTAutoFilter = (CTAutoFilter) get_store().find_element_user(AUTOFILTER$0, 0);
            if (cTAutoFilter == null) {
                return null;
            }
            return cTAutoFilter;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetAutoFilter() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(AUTOFILTER$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setAutoFilter(CTAutoFilter cTAutoFilter) {
        synchronized (monitor()) {
            check_orphaned();
            CTAutoFilter cTAutoFilter2 = (CTAutoFilter) get_store().find_element_user(AUTOFILTER$0, 0);
            if (cTAutoFilter2 == null) {
                cTAutoFilter2 = (CTAutoFilter) get_store().add_element_user(AUTOFILTER$0);
            }
            cTAutoFilter2.set(cTAutoFilter);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public CTAutoFilter addNewAutoFilter() {
        CTAutoFilter cTAutoFilter;
        synchronized (monitor()) {
            check_orphaned();
            cTAutoFilter = (CTAutoFilter) get_store().add_element_user(AUTOFILTER$0);
        }
        return cTAutoFilter;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetAutoFilter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(AUTOFILTER$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public CTSortState getSortState() {
        synchronized (monitor()) {
            check_orphaned();
            CTSortState cTSortStateFind_element_user = get_store().find_element_user(SORTSTATE$2, 0);
            if (cTSortStateFind_element_user == null) {
                return null;
            }
            return cTSortStateFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetSortState() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SORTSTATE$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setSortState(CTSortState cTSortState) {
        synchronized (monitor()) {
            check_orphaned();
            CTSortState cTSortStateFind_element_user = get_store().find_element_user(SORTSTATE$2, 0);
            if (cTSortStateFind_element_user == null) {
                cTSortStateFind_element_user = (CTSortState) get_store().add_element_user(SORTSTATE$2);
            }
            cTSortStateFind_element_user.set(cTSortState);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public CTSortState addNewSortState() {
        CTSortState cTSortStateAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTSortStateAdd_element_user = get_store().add_element_user(SORTSTATE$2);
        }
        return cTSortStateAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetSortState() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SORTSTATE$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public CTTableColumns getTableColumns() {
        synchronized (monitor()) {
            check_orphaned();
            CTTableColumns cTTableColumns = (CTTableColumns) get_store().find_element_user(TABLECOLUMNS$4, 0);
            if (cTTableColumns == null) {
                return null;
            }
            return cTTableColumns;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setTableColumns(CTTableColumns cTTableColumns) {
        synchronized (monitor()) {
            check_orphaned();
            CTTableColumns cTTableColumns2 = (CTTableColumns) get_store().find_element_user(TABLECOLUMNS$4, 0);
            if (cTTableColumns2 == null) {
                cTTableColumns2 = (CTTableColumns) get_store().add_element_user(TABLECOLUMNS$4);
            }
            cTTableColumns2.set(cTTableColumns);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public CTTableColumns addNewTableColumns() {
        CTTableColumns cTTableColumns;
        synchronized (monitor()) {
            check_orphaned();
            cTTableColumns = (CTTableColumns) get_store().add_element_user(TABLECOLUMNS$4);
        }
        return cTTableColumns;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public CTTableStyleInfo getTableStyleInfo() {
        synchronized (monitor()) {
            check_orphaned();
            CTTableStyleInfo cTTableStyleInfo = (CTTableStyleInfo) get_store().find_element_user(TABLESTYLEINFO$6, 0);
            if (cTTableStyleInfo == null) {
                return null;
            }
            return cTTableStyleInfo;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetTableStyleInfo() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(TABLESTYLEINFO$6) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setTableStyleInfo(CTTableStyleInfo cTTableStyleInfo) {
        synchronized (monitor()) {
            check_orphaned();
            CTTableStyleInfo cTTableStyleInfo2 = (CTTableStyleInfo) get_store().find_element_user(TABLESTYLEINFO$6, 0);
            if (cTTableStyleInfo2 == null) {
                cTTableStyleInfo2 = (CTTableStyleInfo) get_store().add_element_user(TABLESTYLEINFO$6);
            }
            cTTableStyleInfo2.set(cTTableStyleInfo);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public CTTableStyleInfo addNewTableStyleInfo() {
        CTTableStyleInfo cTTableStyleInfo;
        synchronized (monitor()) {
            check_orphaned();
            cTTableStyleInfo = (CTTableStyleInfo) get_store().add_element_user(TABLESTYLEINFO$6);
        }
        return cTTableStyleInfo;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetTableStyleInfo() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TABLESTYLEINFO$6, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$8, 0);
            if (cTExtensionListFind_element_user == null) {
                return null;
            }
            return cTExtensionListFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$8) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setExtLst(CTExtensionList cTExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$8, 0);
            if (cTExtensionListFind_element_user == null) {
                cTExtensionListFind_element_user = (CTExtensionList) get_store().add_element_user(EXTLST$8);
            }
            cTExtensionListFind_element_user.set(cTExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public CTExtensionList addNewExtLst() {
        CTExtensionList cTExtensionListAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTExtensionListAdd_element_user = get_store().add_element_user(EXTLST$8);
        }
        return cTExtensionListAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$8, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public long getId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$10);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public XmlUnsignedInt xgetId() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(ID$10);
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ID$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ID$10);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetId(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(ID$10);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(ID$10);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public String getName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAME$12);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public STXstring xgetName() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(NAME$12);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAME$12) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setName(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAME$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NAME$12);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetName(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(NAME$12);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(NAME$12);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAME$12);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public String getDisplayName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DISPLAYNAME$14);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public STXstring xgetDisplayName() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(DISPLAYNAME$14);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setDisplayName(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DISPLAYNAME$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DISPLAYNAME$14);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetDisplayName(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(DISPLAYNAME$14);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(DISPLAYNAME$14);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public String getComment() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COMMENT$16);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public STXstring xgetComment() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(COMMENT$16);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetComment() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COMMENT$16) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setComment(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COMMENT$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(COMMENT$16);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetComment(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(COMMENT$16);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(COMMENT$16);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetComment() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COMMENT$16);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public String getRef() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(REF$18);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public STRef xgetRef() {
        STRef sTRef;
        synchronized (monitor()) {
            check_orphaned();
            sTRef = (STRef) get_store().find_attribute_user(REF$18);
        }
        return sTRef;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setRef(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(REF$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(REF$18);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetRef(STRef sTRef) {
        synchronized (monitor()) {
            check_orphaned();
            STRef sTRef2 = (STRef) get_store().find_attribute_user(REF$18);
            if (sTRef2 == null) {
                sTRef2 = (STRef) get_store().add_attribute_user(REF$18);
            }
            sTRef2.set(sTRef);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public STTableType$Enum getTableType() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TABLETYPE$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(TABLETYPE$20);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STTableType$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public STTableType xgetTableType() {
        STTableType sTTableType;
        synchronized (monitor()) {
            check_orphaned();
            STTableType sTTableTypeFind_attribute_user = get_store().find_attribute_user(TABLETYPE$20);
            if (sTTableTypeFind_attribute_user == null) {
                sTTableTypeFind_attribute_user = (STTableType) get_default_attribute_value(TABLETYPE$20);
            }
            sTTableType = sTTableTypeFind_attribute_user;
        }
        return sTTableType;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetTableType() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TABLETYPE$20) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setTableType(STTableType$Enum sTTableType$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TABLETYPE$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TABLETYPE$20);
            }
            simpleValue.setEnumValue(sTTableType$Enum);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetTableType(STTableType sTTableType) {
        synchronized (monitor()) {
            check_orphaned();
            STTableType sTTableTypeFind_attribute_user = get_store().find_attribute_user(TABLETYPE$20);
            if (sTTableTypeFind_attribute_user == null) {
                sTTableTypeFind_attribute_user = (STTableType) get_store().add_attribute_user(TABLETYPE$20);
            }
            sTTableTypeFind_attribute_user.set(sTTableType);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetTableType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TABLETYPE$20);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public long getHeaderRowCount() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HEADERROWCOUNT$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(HEADERROWCOUNT$22);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public XmlUnsignedInt xgetHeaderRowCount() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(HEADERROWCOUNT$22);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(HEADERROWCOUNT$22);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetHeaderRowCount() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(HEADERROWCOUNT$22) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setHeaderRowCount(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HEADERROWCOUNT$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(HEADERROWCOUNT$22);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetHeaderRowCount(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(HEADERROWCOUNT$22);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(HEADERROWCOUNT$22);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetHeaderRowCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(HEADERROWCOUNT$22);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean getInsertRow() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INSERTROW$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(INSERTROW$24);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public XmlBoolean xgetInsertRow() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(INSERTROW$24);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(INSERTROW$24);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetInsertRow() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(INSERTROW$24) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setInsertRow(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INSERTROW$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(INSERTROW$24);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetInsertRow(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(INSERTROW$24);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(INSERTROW$24);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetInsertRow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(INSERTROW$24);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean getInsertRowShift() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INSERTROWSHIFT$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(INSERTROWSHIFT$26);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public XmlBoolean xgetInsertRowShift() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(INSERTROWSHIFT$26);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(INSERTROWSHIFT$26);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetInsertRowShift() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(INSERTROWSHIFT$26) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setInsertRowShift(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INSERTROWSHIFT$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(INSERTROWSHIFT$26);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetInsertRowShift(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(INSERTROWSHIFT$26);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(INSERTROWSHIFT$26);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetInsertRowShift() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(INSERTROWSHIFT$26);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public long getTotalsRowCount() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOTALSROWCOUNT$28);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(TOTALSROWCOUNT$28);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public XmlUnsignedInt xgetTotalsRowCount() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(TOTALSROWCOUNT$28);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(TOTALSROWCOUNT$28);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetTotalsRowCount() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TOTALSROWCOUNT$28) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setTotalsRowCount(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOTALSROWCOUNT$28);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TOTALSROWCOUNT$28);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetTotalsRowCount(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(TOTALSROWCOUNT$28);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(TOTALSROWCOUNT$28);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetTotalsRowCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TOTALSROWCOUNT$28);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean getTotalsRowShown() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOTALSROWSHOWN$30);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(TOTALSROWSHOWN$30);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public XmlBoolean xgetTotalsRowShown() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(TOTALSROWSHOWN$30);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(TOTALSROWSHOWN$30);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetTotalsRowShown() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TOTALSROWSHOWN$30) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setTotalsRowShown(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOTALSROWSHOWN$30);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TOTALSROWSHOWN$30);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetTotalsRowShown(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(TOTALSROWSHOWN$30);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(TOTALSROWSHOWN$30);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetTotalsRowShown() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TOTALSROWSHOWN$30);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean getPublished() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PUBLISHED$32);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PUBLISHED$32);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public XmlBoolean xgetPublished() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PUBLISHED$32);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(PUBLISHED$32);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetPublished() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PUBLISHED$32) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setPublished(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PUBLISHED$32);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PUBLISHED$32);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetPublished(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PUBLISHED$32);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(PUBLISHED$32);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetPublished() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PUBLISHED$32);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public long getHeaderRowDxfId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HEADERROWDXFID$34);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public STDxfId xgetHeaderRowDxfId() {
        STDxfId sTDxfId;
        synchronized (monitor()) {
            check_orphaned();
            sTDxfId = (STDxfId) get_store().find_attribute_user(HEADERROWDXFID$34);
        }
        return sTDxfId;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetHeaderRowDxfId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(HEADERROWDXFID$34) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setHeaderRowDxfId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HEADERROWDXFID$34);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(HEADERROWDXFID$34);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetHeaderRowDxfId(STDxfId sTDxfId) {
        synchronized (monitor()) {
            check_orphaned();
            STDxfId sTDxfId2 = (STDxfId) get_store().find_attribute_user(HEADERROWDXFID$34);
            if (sTDxfId2 == null) {
                sTDxfId2 = (STDxfId) get_store().add_attribute_user(HEADERROWDXFID$34);
            }
            sTDxfId2.set(sTDxfId);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetHeaderRowDxfId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(HEADERROWDXFID$34);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public long getDataDxfId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DATADXFID$36);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public STDxfId xgetDataDxfId() {
        STDxfId sTDxfId;
        synchronized (monitor()) {
            check_orphaned();
            sTDxfId = (STDxfId) get_store().find_attribute_user(DATADXFID$36);
        }
        return sTDxfId;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetDataDxfId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DATADXFID$36) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setDataDxfId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DATADXFID$36);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DATADXFID$36);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetDataDxfId(STDxfId sTDxfId) {
        synchronized (monitor()) {
            check_orphaned();
            STDxfId sTDxfId2 = (STDxfId) get_store().find_attribute_user(DATADXFID$36);
            if (sTDxfId2 == null) {
                sTDxfId2 = (STDxfId) get_store().add_attribute_user(DATADXFID$36);
            }
            sTDxfId2.set(sTDxfId);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetDataDxfId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DATADXFID$36);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public long getTotalsRowDxfId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOTALSROWDXFID$38);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public STDxfId xgetTotalsRowDxfId() {
        STDxfId sTDxfId;
        synchronized (monitor()) {
            check_orphaned();
            sTDxfId = (STDxfId) get_store().find_attribute_user(TOTALSROWDXFID$38);
        }
        return sTDxfId;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetTotalsRowDxfId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TOTALSROWDXFID$38) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setTotalsRowDxfId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOTALSROWDXFID$38);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TOTALSROWDXFID$38);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetTotalsRowDxfId(STDxfId sTDxfId) {
        synchronized (monitor()) {
            check_orphaned();
            STDxfId sTDxfId2 = (STDxfId) get_store().find_attribute_user(TOTALSROWDXFID$38);
            if (sTDxfId2 == null) {
                sTDxfId2 = (STDxfId) get_store().add_attribute_user(TOTALSROWDXFID$38);
            }
            sTDxfId2.set(sTDxfId);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetTotalsRowDxfId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TOTALSROWDXFID$38);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public long getHeaderRowBorderDxfId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HEADERROWBORDERDXFID$40);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public STDxfId xgetHeaderRowBorderDxfId() {
        STDxfId sTDxfId;
        synchronized (monitor()) {
            check_orphaned();
            sTDxfId = (STDxfId) get_store().find_attribute_user(HEADERROWBORDERDXFID$40);
        }
        return sTDxfId;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetHeaderRowBorderDxfId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(HEADERROWBORDERDXFID$40) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setHeaderRowBorderDxfId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HEADERROWBORDERDXFID$40);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(HEADERROWBORDERDXFID$40);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetHeaderRowBorderDxfId(STDxfId sTDxfId) {
        synchronized (monitor()) {
            check_orphaned();
            STDxfId sTDxfId2 = (STDxfId) get_store().find_attribute_user(HEADERROWBORDERDXFID$40);
            if (sTDxfId2 == null) {
                sTDxfId2 = (STDxfId) get_store().add_attribute_user(HEADERROWBORDERDXFID$40);
            }
            sTDxfId2.set(sTDxfId);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetHeaderRowBorderDxfId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(HEADERROWBORDERDXFID$40);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public long getTableBorderDxfId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TABLEBORDERDXFID$42);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public STDxfId xgetTableBorderDxfId() {
        STDxfId sTDxfId;
        synchronized (monitor()) {
            check_orphaned();
            sTDxfId = (STDxfId) get_store().find_attribute_user(TABLEBORDERDXFID$42);
        }
        return sTDxfId;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetTableBorderDxfId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TABLEBORDERDXFID$42) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setTableBorderDxfId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TABLEBORDERDXFID$42);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TABLEBORDERDXFID$42);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetTableBorderDxfId(STDxfId sTDxfId) {
        synchronized (monitor()) {
            check_orphaned();
            STDxfId sTDxfId2 = (STDxfId) get_store().find_attribute_user(TABLEBORDERDXFID$42);
            if (sTDxfId2 == null) {
                sTDxfId2 = (STDxfId) get_store().add_attribute_user(TABLEBORDERDXFID$42);
            }
            sTDxfId2.set(sTDxfId);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetTableBorderDxfId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TABLEBORDERDXFID$42);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public long getTotalsRowBorderDxfId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOTALSROWBORDERDXFID$44);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public STDxfId xgetTotalsRowBorderDxfId() {
        STDxfId sTDxfId;
        synchronized (monitor()) {
            check_orphaned();
            sTDxfId = (STDxfId) get_store().find_attribute_user(TOTALSROWBORDERDXFID$44);
        }
        return sTDxfId;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetTotalsRowBorderDxfId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TOTALSROWBORDERDXFID$44) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setTotalsRowBorderDxfId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOTALSROWBORDERDXFID$44);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TOTALSROWBORDERDXFID$44);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetTotalsRowBorderDxfId(STDxfId sTDxfId) {
        synchronized (monitor()) {
            check_orphaned();
            STDxfId sTDxfId2 = (STDxfId) get_store().find_attribute_user(TOTALSROWBORDERDXFID$44);
            if (sTDxfId2 == null) {
                sTDxfId2 = (STDxfId) get_store().add_attribute_user(TOTALSROWBORDERDXFID$44);
            }
            sTDxfId2.set(sTDxfId);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetTotalsRowBorderDxfId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TOTALSROWBORDERDXFID$44);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public String getHeaderRowCellStyle() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HEADERROWCELLSTYLE$46);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public STXstring xgetHeaderRowCellStyle() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(HEADERROWCELLSTYLE$46);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetHeaderRowCellStyle() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(HEADERROWCELLSTYLE$46) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setHeaderRowCellStyle(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HEADERROWCELLSTYLE$46);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(HEADERROWCELLSTYLE$46);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetHeaderRowCellStyle(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(HEADERROWCELLSTYLE$46);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(HEADERROWCELLSTYLE$46);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetHeaderRowCellStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(HEADERROWCELLSTYLE$46);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public String getDataCellStyle() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DATACELLSTYLE$48);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public STXstring xgetDataCellStyle() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(DATACELLSTYLE$48);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetDataCellStyle() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DATACELLSTYLE$48) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setDataCellStyle(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DATACELLSTYLE$48);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DATACELLSTYLE$48);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetDataCellStyle(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(DATACELLSTYLE$48);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(DATACELLSTYLE$48);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetDataCellStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DATACELLSTYLE$48);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public String getTotalsRowCellStyle() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOTALSROWCELLSTYLE$50);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public STXstring xgetTotalsRowCellStyle() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(TOTALSROWCELLSTYLE$50);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetTotalsRowCellStyle() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TOTALSROWCELLSTYLE$50) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setTotalsRowCellStyle(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOTALSROWCELLSTYLE$50);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TOTALSROWCELLSTYLE$50);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetTotalsRowCellStyle(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(TOTALSROWCELLSTYLE$50);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(TOTALSROWCELLSTYLE$50);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetTotalsRowCellStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TOTALSROWCELLSTYLE$50);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public long getConnectionId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CONNECTIONID$52);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public XmlUnsignedInt xgetConnectionId() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(CONNECTIONID$52);
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public boolean isSetConnectionId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(CONNECTIONID$52) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void setConnectionId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CONNECTIONID$52);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(CONNECTIONID$52);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void xsetConnectionId(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(CONNECTIONID$52);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(CONNECTIONID$52);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable
    public void unsetConnectionId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(CONNECTIONID$52);
        }
    }
}
