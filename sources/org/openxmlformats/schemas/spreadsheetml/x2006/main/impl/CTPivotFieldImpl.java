package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoSortScope;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTItems;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STAxis;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STFieldSortType;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STFieldSortType$Enum;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STNumFmtId;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STXstring;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTPivotFieldImpl.class */
public class CTPivotFieldImpl extends XmlComplexContentImpl implements CTPivotField {
    private static final QName ITEMS$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "items");
    private static final QName AUTOSORTSCOPE$2 = new QName(XSSFRelation.NS_SPREADSHEETML, "autoSortScope");
    private static final QName EXTLST$4 = new QName(XSSFRelation.NS_SPREADSHEETML, "extLst");
    private static final QName NAME$6 = new QName("", "name");
    private static final QName AXIS$8 = new QName("", "axis");
    private static final QName DATAFIELD$10 = new QName("", "dataField");
    private static final QName SUBTOTALCAPTION$12 = new QName("", "subtotalCaption");
    private static final QName SHOWDROPDOWNS$14 = new QName("", "showDropDowns");
    private static final QName HIDDENLEVEL$16 = new QName("", "hiddenLevel");
    private static final QName UNIQUEMEMBERPROPERTY$18 = new QName("", "uniqueMemberProperty");
    private static final QName COMPACT$20 = new QName("", "compact");
    private static final QName ALLDRILLED$22 = new QName("", "allDrilled");
    private static final QName NUMFMTID$24 = new QName("", "numFmtId");
    private static final QName OUTLINE$26 = new QName("", "outline");
    private static final QName SUBTOTALTOP$28 = new QName("", "subtotalTop");
    private static final QName DRAGTOROW$30 = new QName("", "dragToRow");
    private static final QName DRAGTOCOL$32 = new QName("", "dragToCol");
    private static final QName MULTIPLEITEMSELECTIONALLOWED$34 = new QName("", "multipleItemSelectionAllowed");
    private static final QName DRAGTOPAGE$36 = new QName("", "dragToPage");
    private static final QName DRAGTODATA$38 = new QName("", "dragToData");
    private static final QName DRAGOFF$40 = new QName("", "dragOff");
    private static final QName SHOWALL$42 = new QName("", "showAll");
    private static final QName INSERTBLANKROW$44 = new QName("", "insertBlankRow");
    private static final QName SERVERFIELD$46 = new QName("", "serverField");
    private static final QName INSERTPAGEBREAK$48 = new QName("", "insertPageBreak");
    private static final QName AUTOSHOW$50 = new QName("", "autoShow");
    private static final QName TOPAUTOSHOW$52 = new QName("", "topAutoShow");
    private static final QName HIDENEWITEMS$54 = new QName("", "hideNewItems");
    private static final QName MEASUREFILTER$56 = new QName("", "measureFilter");
    private static final QName INCLUDENEWITEMSINFILTER$58 = new QName("", "includeNewItemsInFilter");
    private static final QName ITEMPAGECOUNT$60 = new QName("", "itemPageCount");
    private static final QName SORTTYPE$62 = new QName("", "sortType");
    private static final QName DATASOURCESORT$64 = new QName("", "dataSourceSort");
    private static final QName NONAUTOSORTDEFAULT$66 = new QName("", "nonAutoSortDefault");
    private static final QName RANKBY$68 = new QName("", "rankBy");
    private static final QName DEFAULTSUBTOTAL$70 = new QName("", "defaultSubtotal");
    private static final QName SUMSUBTOTAL$72 = new QName("", "sumSubtotal");
    private static final QName COUNTASUBTOTAL$74 = new QName("", "countASubtotal");
    private static final QName AVGSUBTOTAL$76 = new QName("", "avgSubtotal");
    private static final QName MAXSUBTOTAL$78 = new QName("", "maxSubtotal");
    private static final QName MINSUBTOTAL$80 = new QName("", "minSubtotal");
    private static final QName PRODUCTSUBTOTAL$82 = new QName("", "productSubtotal");
    private static final QName COUNTSUBTOTAL$84 = new QName("", "countSubtotal");
    private static final QName STDDEVSUBTOTAL$86 = new QName("", "stdDevSubtotal");
    private static final QName STDDEVPSUBTOTAL$88 = new QName("", "stdDevPSubtotal");
    private static final QName VARSUBTOTAL$90 = new QName("", "varSubtotal");
    private static final QName VARPSUBTOTAL$92 = new QName("", "varPSubtotal");
    private static final QName SHOWPROPCELL$94 = new QName("", "showPropCell");
    private static final QName SHOWPROPTIP$96 = new QName("", "showPropTip");
    private static final QName SHOWPROPASCAPTION$98 = new QName("", "showPropAsCaption");
    private static final QName DEFAULTATTRIBUTEDRILLSTATE$100 = new QName("", "defaultAttributeDrillState");

    public CTPivotFieldImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public CTItems getItems() {
        synchronized (monitor()) {
            check_orphaned();
            CTItems cTItems = (CTItems) get_store().find_element_user(ITEMS$0, 0);
            if (cTItems == null) {
                return null;
            }
            return cTItems;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetItems() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(ITEMS$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setItems(CTItems cTItems) {
        synchronized (monitor()) {
            check_orphaned();
            CTItems cTItems2 = (CTItems) get_store().find_element_user(ITEMS$0, 0);
            if (cTItems2 == null) {
                cTItems2 = (CTItems) get_store().add_element_user(ITEMS$0);
            }
            cTItems2.set(cTItems);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public CTItems addNewItems() {
        CTItems cTItems;
        synchronized (monitor()) {
            check_orphaned();
            cTItems = (CTItems) get_store().add_element_user(ITEMS$0);
        }
        return cTItems;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetItems() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ITEMS$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public CTAutoSortScope getAutoSortScope() {
        synchronized (monitor()) {
            check_orphaned();
            CTAutoSortScope cTAutoSortScopeFind_element_user = get_store().find_element_user(AUTOSORTSCOPE$2, 0);
            if (cTAutoSortScopeFind_element_user == null) {
                return null;
            }
            return cTAutoSortScopeFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetAutoSortScope() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(AUTOSORTSCOPE$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setAutoSortScope(CTAutoSortScope cTAutoSortScope) {
        synchronized (monitor()) {
            check_orphaned();
            CTAutoSortScope cTAutoSortScopeFind_element_user = get_store().find_element_user(AUTOSORTSCOPE$2, 0);
            if (cTAutoSortScopeFind_element_user == null) {
                cTAutoSortScopeFind_element_user = (CTAutoSortScope) get_store().add_element_user(AUTOSORTSCOPE$2);
            }
            cTAutoSortScopeFind_element_user.set(cTAutoSortScope);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public CTAutoSortScope addNewAutoSortScope() {
        CTAutoSortScope cTAutoSortScopeAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTAutoSortScopeAdd_element_user = get_store().add_element_user(AUTOSORTSCOPE$2);
        }
        return cTAutoSortScopeAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetAutoSortScope() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(AUTOSORTSCOPE$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$4, 0);
            if (cTExtensionListFind_element_user == null) {
                return null;
            }
            return cTExtensionListFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$4) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setExtLst(CTExtensionList cTExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$4, 0);
            if (cTExtensionListFind_element_user == null) {
                cTExtensionListFind_element_user = (CTExtensionList) get_store().add_element_user(EXTLST$4);
            }
            cTExtensionListFind_element_user.set(cTExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public CTExtensionList addNewExtLst() {
        CTExtensionList cTExtensionListAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTExtensionListAdd_element_user = get_store().add_element_user(EXTLST$4);
        }
        return cTExtensionListAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$4, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public String getName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAME$6);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public STXstring xgetName() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(NAME$6);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAME$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setName(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAME$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NAME$6);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetName(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(NAME$6);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(NAME$6);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAME$6);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public STAxis.Enum getAxis() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AXIS$8);
            if (simpleValue == null) {
                return null;
            }
            return (STAxis.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public STAxis xgetAxis() {
        STAxis sTAxis;
        synchronized (monitor()) {
            check_orphaned();
            sTAxis = (STAxis) get_store().find_attribute_user(AXIS$8);
        }
        return sTAxis;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetAxis() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(AXIS$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setAxis(STAxis.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AXIS$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(AXIS$8);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetAxis(STAxis sTAxis) {
        synchronized (monitor()) {
            check_orphaned();
            STAxis sTAxis2 = (STAxis) get_store().find_attribute_user(AXIS$8);
            if (sTAxis2 == null) {
                sTAxis2 = (STAxis) get_store().add_attribute_user(AXIS$8);
            }
            sTAxis2.set(sTAxis);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetAxis() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(AXIS$8);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getDataField() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DATAFIELD$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(DATAFIELD$10);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetDataField() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DATAFIELD$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(DATAFIELD$10);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetDataField() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DATAFIELD$10) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setDataField(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DATAFIELD$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DATAFIELD$10);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetDataField(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DATAFIELD$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(DATAFIELD$10);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetDataField() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DATAFIELD$10);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public String getSubtotalCaption() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SUBTOTALCAPTION$12);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public STXstring xgetSubtotalCaption() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(SUBTOTALCAPTION$12);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetSubtotalCaption() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SUBTOTALCAPTION$12) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setSubtotalCaption(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SUBTOTALCAPTION$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SUBTOTALCAPTION$12);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetSubtotalCaption(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(SUBTOTALCAPTION$12);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(SUBTOTALCAPTION$12);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetSubtotalCaption() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SUBTOTALCAPTION$12);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getShowDropDowns() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWDROPDOWNS$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWDROPDOWNS$14);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetShowDropDowns() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWDROPDOWNS$14);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWDROPDOWNS$14);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetShowDropDowns() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWDROPDOWNS$14) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setShowDropDowns(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWDROPDOWNS$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWDROPDOWNS$14);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetShowDropDowns(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWDROPDOWNS$14);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWDROPDOWNS$14);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetShowDropDowns() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWDROPDOWNS$14);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getHiddenLevel() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HIDDENLEVEL$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(HIDDENLEVEL$16);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetHiddenLevel() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(HIDDENLEVEL$16);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(HIDDENLEVEL$16);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetHiddenLevel() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(HIDDENLEVEL$16) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setHiddenLevel(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HIDDENLEVEL$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(HIDDENLEVEL$16);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetHiddenLevel(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(HIDDENLEVEL$16);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(HIDDENLEVEL$16);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetHiddenLevel() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(HIDDENLEVEL$16);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public String getUniqueMemberProperty() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(UNIQUEMEMBERPROPERTY$18);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public STXstring xgetUniqueMemberProperty() {
        STXstring sTXstring;
        synchronized (monitor()) {
            check_orphaned();
            sTXstring = (STXstring) get_store().find_attribute_user(UNIQUEMEMBERPROPERTY$18);
        }
        return sTXstring;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetUniqueMemberProperty() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(UNIQUEMEMBERPROPERTY$18) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setUniqueMemberProperty(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(UNIQUEMEMBERPROPERTY$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(UNIQUEMEMBERPROPERTY$18);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetUniqueMemberProperty(STXstring sTXstring) {
        synchronized (monitor()) {
            check_orphaned();
            STXstring sTXstring2 = (STXstring) get_store().find_attribute_user(UNIQUEMEMBERPROPERTY$18);
            if (sTXstring2 == null) {
                sTXstring2 = (STXstring) get_store().add_attribute_user(UNIQUEMEMBERPROPERTY$18);
            }
            sTXstring2.set(sTXstring);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetUniqueMemberProperty() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(UNIQUEMEMBERPROPERTY$18);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getCompact() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COMPACT$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(COMPACT$20);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetCompact() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(COMPACT$20);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(COMPACT$20);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetCompact() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COMPACT$20) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setCompact(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COMPACT$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(COMPACT$20);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetCompact(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(COMPACT$20);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(COMPACT$20);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetCompact() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COMPACT$20);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getAllDrilled() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ALLDRILLED$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ALLDRILLED$22);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetAllDrilled() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ALLDRILLED$22);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(ALLDRILLED$22);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetAllDrilled() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ALLDRILLED$22) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setAllDrilled(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ALLDRILLED$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ALLDRILLED$22);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetAllDrilled(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ALLDRILLED$22);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ALLDRILLED$22);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetAllDrilled() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ALLDRILLED$22);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public long getNumFmtId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NUMFMTID$24);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public STNumFmtId xgetNumFmtId() {
        STNumFmtId sTNumFmtId;
        synchronized (monitor()) {
            check_orphaned();
            sTNumFmtId = (STNumFmtId) get_store().find_attribute_user(NUMFMTID$24);
        }
        return sTNumFmtId;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetNumFmtId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NUMFMTID$24) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setNumFmtId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NUMFMTID$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NUMFMTID$24);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetNumFmtId(STNumFmtId sTNumFmtId) {
        synchronized (monitor()) {
            check_orphaned();
            STNumFmtId sTNumFmtId2 = (STNumFmtId) get_store().find_attribute_user(NUMFMTID$24);
            if (sTNumFmtId2 == null) {
                sTNumFmtId2 = (STNumFmtId) get_store().add_attribute_user(NUMFMTID$24);
            }
            sTNumFmtId2.set(sTNumFmtId);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetNumFmtId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NUMFMTID$24);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getOutline() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(OUTLINE$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(OUTLINE$26);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetOutline() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(OUTLINE$26);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(OUTLINE$26);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetOutline() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(OUTLINE$26) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setOutline(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(OUTLINE$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(OUTLINE$26);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetOutline(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(OUTLINE$26);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(OUTLINE$26);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetOutline() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(OUTLINE$26);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getSubtotalTop() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SUBTOTALTOP$28);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SUBTOTALTOP$28);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetSubtotalTop() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SUBTOTALTOP$28);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SUBTOTALTOP$28);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetSubtotalTop() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SUBTOTALTOP$28) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setSubtotalTop(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SUBTOTALTOP$28);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SUBTOTALTOP$28);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetSubtotalTop(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SUBTOTALTOP$28);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SUBTOTALTOP$28);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetSubtotalTop() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SUBTOTALTOP$28);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getDragToRow() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DRAGTOROW$30);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(DRAGTOROW$30);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetDragToRow() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DRAGTOROW$30);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(DRAGTOROW$30);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetDragToRow() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DRAGTOROW$30) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setDragToRow(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DRAGTOROW$30);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DRAGTOROW$30);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetDragToRow(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DRAGTOROW$30);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(DRAGTOROW$30);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetDragToRow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DRAGTOROW$30);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getDragToCol() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DRAGTOCOL$32);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(DRAGTOCOL$32);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetDragToCol() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DRAGTOCOL$32);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(DRAGTOCOL$32);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetDragToCol() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DRAGTOCOL$32) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setDragToCol(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DRAGTOCOL$32);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DRAGTOCOL$32);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetDragToCol(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DRAGTOCOL$32);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(DRAGTOCOL$32);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetDragToCol() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DRAGTOCOL$32);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getMultipleItemSelectionAllowed() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MULTIPLEITEMSELECTIONALLOWED$34);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(MULTIPLEITEMSELECTIONALLOWED$34);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetMultipleItemSelectionAllowed() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(MULTIPLEITEMSELECTIONALLOWED$34);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(MULTIPLEITEMSELECTIONALLOWED$34);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetMultipleItemSelectionAllowed() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MULTIPLEITEMSELECTIONALLOWED$34) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setMultipleItemSelectionAllowed(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MULTIPLEITEMSELECTIONALLOWED$34);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(MULTIPLEITEMSELECTIONALLOWED$34);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetMultipleItemSelectionAllowed(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(MULTIPLEITEMSELECTIONALLOWED$34);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(MULTIPLEITEMSELECTIONALLOWED$34);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetMultipleItemSelectionAllowed() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MULTIPLEITEMSELECTIONALLOWED$34);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getDragToPage() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DRAGTOPAGE$36);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(DRAGTOPAGE$36);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetDragToPage() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DRAGTOPAGE$36);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(DRAGTOPAGE$36);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetDragToPage() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DRAGTOPAGE$36) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setDragToPage(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DRAGTOPAGE$36);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DRAGTOPAGE$36);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetDragToPage(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DRAGTOPAGE$36);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(DRAGTOPAGE$36);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetDragToPage() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DRAGTOPAGE$36);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getDragToData() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DRAGTODATA$38);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(DRAGTODATA$38);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetDragToData() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DRAGTODATA$38);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(DRAGTODATA$38);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetDragToData() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DRAGTODATA$38) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setDragToData(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DRAGTODATA$38);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DRAGTODATA$38);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetDragToData(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DRAGTODATA$38);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(DRAGTODATA$38);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetDragToData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DRAGTODATA$38);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getDragOff() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DRAGOFF$40);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(DRAGOFF$40);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetDragOff() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DRAGOFF$40);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(DRAGOFF$40);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetDragOff() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DRAGOFF$40) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setDragOff(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DRAGOFF$40);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DRAGOFF$40);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetDragOff(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DRAGOFF$40);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(DRAGOFF$40);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetDragOff() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DRAGOFF$40);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getShowAll() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWALL$42);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWALL$42);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetShowAll() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWALL$42);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWALL$42);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetShowAll() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWALL$42) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setShowAll(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWALL$42);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWALL$42);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetShowAll(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWALL$42);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWALL$42);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetShowAll() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWALL$42);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getInsertBlankRow() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INSERTBLANKROW$44);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(INSERTBLANKROW$44);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetInsertBlankRow() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(INSERTBLANKROW$44);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(INSERTBLANKROW$44);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetInsertBlankRow() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(INSERTBLANKROW$44) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setInsertBlankRow(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INSERTBLANKROW$44);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(INSERTBLANKROW$44);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetInsertBlankRow(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(INSERTBLANKROW$44);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(INSERTBLANKROW$44);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetInsertBlankRow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(INSERTBLANKROW$44);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getServerField() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SERVERFIELD$46);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SERVERFIELD$46);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetServerField() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SERVERFIELD$46);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SERVERFIELD$46);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetServerField() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SERVERFIELD$46) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setServerField(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SERVERFIELD$46);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SERVERFIELD$46);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetServerField(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SERVERFIELD$46);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SERVERFIELD$46);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetServerField() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SERVERFIELD$46);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getInsertPageBreak() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INSERTPAGEBREAK$48);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(INSERTPAGEBREAK$48);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetInsertPageBreak() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(INSERTPAGEBREAK$48);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(INSERTPAGEBREAK$48);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetInsertPageBreak() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(INSERTPAGEBREAK$48) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setInsertPageBreak(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INSERTPAGEBREAK$48);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(INSERTPAGEBREAK$48);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetInsertPageBreak(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(INSERTPAGEBREAK$48);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(INSERTPAGEBREAK$48);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetInsertPageBreak() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(INSERTPAGEBREAK$48);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getAutoShow() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AUTOSHOW$50);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(AUTOSHOW$50);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetAutoShow() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(AUTOSHOW$50);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(AUTOSHOW$50);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetAutoShow() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(AUTOSHOW$50) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setAutoShow(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AUTOSHOW$50);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(AUTOSHOW$50);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetAutoShow(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(AUTOSHOW$50);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(AUTOSHOW$50);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetAutoShow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(AUTOSHOW$50);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getTopAutoShow() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOPAUTOSHOW$52);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(TOPAUTOSHOW$52);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetTopAutoShow() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(TOPAUTOSHOW$52);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(TOPAUTOSHOW$52);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetTopAutoShow() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TOPAUTOSHOW$52) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setTopAutoShow(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TOPAUTOSHOW$52);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TOPAUTOSHOW$52);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetTopAutoShow(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(TOPAUTOSHOW$52);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(TOPAUTOSHOW$52);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetTopAutoShow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TOPAUTOSHOW$52);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getHideNewItems() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HIDENEWITEMS$54);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(HIDENEWITEMS$54);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetHideNewItems() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(HIDENEWITEMS$54);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(HIDENEWITEMS$54);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetHideNewItems() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(HIDENEWITEMS$54) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setHideNewItems(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HIDENEWITEMS$54);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(HIDENEWITEMS$54);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetHideNewItems(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(HIDENEWITEMS$54);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(HIDENEWITEMS$54);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetHideNewItems() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(HIDENEWITEMS$54);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getMeasureFilter() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MEASUREFILTER$56);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(MEASUREFILTER$56);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetMeasureFilter() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(MEASUREFILTER$56);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(MEASUREFILTER$56);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetMeasureFilter() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MEASUREFILTER$56) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setMeasureFilter(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MEASUREFILTER$56);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(MEASUREFILTER$56);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetMeasureFilter(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(MEASUREFILTER$56);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(MEASUREFILTER$56);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetMeasureFilter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MEASUREFILTER$56);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getIncludeNewItemsInFilter() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INCLUDENEWITEMSINFILTER$58);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(INCLUDENEWITEMSINFILTER$58);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetIncludeNewItemsInFilter() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(INCLUDENEWITEMSINFILTER$58);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(INCLUDENEWITEMSINFILTER$58);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetIncludeNewItemsInFilter() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(INCLUDENEWITEMSINFILTER$58) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setIncludeNewItemsInFilter(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(INCLUDENEWITEMSINFILTER$58);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(INCLUDENEWITEMSINFILTER$58);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetIncludeNewItemsInFilter(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(INCLUDENEWITEMSINFILTER$58);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(INCLUDENEWITEMSINFILTER$58);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetIncludeNewItemsInFilter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(INCLUDENEWITEMSINFILTER$58);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public long getItemPageCount() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ITEMPAGECOUNT$60);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ITEMPAGECOUNT$60);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlUnsignedInt xgetItemPageCount() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(ITEMPAGECOUNT$60);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(ITEMPAGECOUNT$60);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetItemPageCount() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ITEMPAGECOUNT$60) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setItemPageCount(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ITEMPAGECOUNT$60);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ITEMPAGECOUNT$60);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetItemPageCount(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(ITEMPAGECOUNT$60);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(ITEMPAGECOUNT$60);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetItemPageCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ITEMPAGECOUNT$60);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public STFieldSortType$Enum getSortType() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SORTTYPE$62);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SORTTYPE$62);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STFieldSortType$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public STFieldSortType xgetSortType() {
        STFieldSortType sTFieldSortType;
        synchronized (monitor()) {
            check_orphaned();
            STFieldSortType sTFieldSortTypeFind_attribute_user = get_store().find_attribute_user(SORTTYPE$62);
            if (sTFieldSortTypeFind_attribute_user == null) {
                sTFieldSortTypeFind_attribute_user = (STFieldSortType) get_default_attribute_value(SORTTYPE$62);
            }
            sTFieldSortType = sTFieldSortTypeFind_attribute_user;
        }
        return sTFieldSortType;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetSortType() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SORTTYPE$62) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setSortType(STFieldSortType$Enum sTFieldSortType$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SORTTYPE$62);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SORTTYPE$62);
            }
            simpleValue.setEnumValue(sTFieldSortType$Enum);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetSortType(STFieldSortType sTFieldSortType) {
        synchronized (monitor()) {
            check_orphaned();
            STFieldSortType sTFieldSortTypeFind_attribute_user = get_store().find_attribute_user(SORTTYPE$62);
            if (sTFieldSortTypeFind_attribute_user == null) {
                sTFieldSortTypeFind_attribute_user = (STFieldSortType) get_store().add_attribute_user(SORTTYPE$62);
            }
            sTFieldSortTypeFind_attribute_user.set(sTFieldSortType);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetSortType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SORTTYPE$62);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getDataSourceSort() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DATASOURCESORT$64);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetDataSourceSort() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(DATASOURCESORT$64);
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetDataSourceSort() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DATASOURCESORT$64) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setDataSourceSort(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DATASOURCESORT$64);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DATASOURCESORT$64);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetDataSourceSort(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DATASOURCESORT$64);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(DATASOURCESORT$64);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetDataSourceSort() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DATASOURCESORT$64);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getNonAutoSortDefault() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NONAUTOSORTDEFAULT$66);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(NONAUTOSORTDEFAULT$66);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetNonAutoSortDefault() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(NONAUTOSORTDEFAULT$66);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(NONAUTOSORTDEFAULT$66);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetNonAutoSortDefault() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NONAUTOSORTDEFAULT$66) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setNonAutoSortDefault(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NONAUTOSORTDEFAULT$66);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NONAUTOSORTDEFAULT$66);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetNonAutoSortDefault(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(NONAUTOSORTDEFAULT$66);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(NONAUTOSORTDEFAULT$66);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetNonAutoSortDefault() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NONAUTOSORTDEFAULT$66);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public long getRankBy() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(RANKBY$68);
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlUnsignedInt xgetRankBy() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            xmlUnsignedInt = (XmlUnsignedInt) get_store().find_attribute_user(RANKBY$68);
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetRankBy() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(RANKBY$68) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setRankBy(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(RANKBY$68);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(RANKBY$68);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetRankBy(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(RANKBY$68);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(RANKBY$68);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetRankBy() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(RANKBY$68);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getDefaultSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DEFAULTSUBTOTAL$70);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(DEFAULTSUBTOTAL$70);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetDefaultSubtotal() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DEFAULTSUBTOTAL$70);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(DEFAULTSUBTOTAL$70);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetDefaultSubtotal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DEFAULTSUBTOTAL$70) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setDefaultSubtotal(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DEFAULTSUBTOTAL$70);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DEFAULTSUBTOTAL$70);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetDefaultSubtotal(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DEFAULTSUBTOTAL$70);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(DEFAULTSUBTOTAL$70);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetDefaultSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DEFAULTSUBTOTAL$70);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getSumSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SUMSUBTOTAL$72);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SUMSUBTOTAL$72);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetSumSubtotal() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SUMSUBTOTAL$72);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SUMSUBTOTAL$72);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetSumSubtotal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SUMSUBTOTAL$72) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setSumSubtotal(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SUMSUBTOTAL$72);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SUMSUBTOTAL$72);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetSumSubtotal(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SUMSUBTOTAL$72);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SUMSUBTOTAL$72);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetSumSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SUMSUBTOTAL$72);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getCountASubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COUNTASUBTOTAL$74);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(COUNTASUBTOTAL$74);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetCountASubtotal() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(COUNTASUBTOTAL$74);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(COUNTASUBTOTAL$74);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetCountASubtotal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COUNTASUBTOTAL$74) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setCountASubtotal(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COUNTASUBTOTAL$74);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(COUNTASUBTOTAL$74);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetCountASubtotal(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(COUNTASUBTOTAL$74);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(COUNTASUBTOTAL$74);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetCountASubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COUNTASUBTOTAL$74);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getAvgSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AVGSUBTOTAL$76);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(AVGSUBTOTAL$76);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetAvgSubtotal() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(AVGSUBTOTAL$76);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(AVGSUBTOTAL$76);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetAvgSubtotal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(AVGSUBTOTAL$76) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setAvgSubtotal(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(AVGSUBTOTAL$76);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(AVGSUBTOTAL$76);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetAvgSubtotal(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(AVGSUBTOTAL$76);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(AVGSUBTOTAL$76);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetAvgSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(AVGSUBTOTAL$76);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getMaxSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MAXSUBTOTAL$78);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(MAXSUBTOTAL$78);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetMaxSubtotal() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(MAXSUBTOTAL$78);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(MAXSUBTOTAL$78);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetMaxSubtotal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MAXSUBTOTAL$78) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setMaxSubtotal(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MAXSUBTOTAL$78);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(MAXSUBTOTAL$78);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetMaxSubtotal(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(MAXSUBTOTAL$78);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(MAXSUBTOTAL$78);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetMaxSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MAXSUBTOTAL$78);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getMinSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MINSUBTOTAL$80);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(MINSUBTOTAL$80);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetMinSubtotal() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(MINSUBTOTAL$80);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(MINSUBTOTAL$80);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetMinSubtotal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MINSUBTOTAL$80) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setMinSubtotal(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(MINSUBTOTAL$80);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(MINSUBTOTAL$80);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetMinSubtotal(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(MINSUBTOTAL$80);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(MINSUBTOTAL$80);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetMinSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MINSUBTOTAL$80);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getProductSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PRODUCTSUBTOTAL$82);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PRODUCTSUBTOTAL$82);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetProductSubtotal() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PRODUCTSUBTOTAL$82);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(PRODUCTSUBTOTAL$82);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetProductSubtotal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PRODUCTSUBTOTAL$82) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setProductSubtotal(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PRODUCTSUBTOTAL$82);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PRODUCTSUBTOTAL$82);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetProductSubtotal(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PRODUCTSUBTOTAL$82);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(PRODUCTSUBTOTAL$82);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetProductSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PRODUCTSUBTOTAL$82);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getCountSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COUNTSUBTOTAL$84);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(COUNTSUBTOTAL$84);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetCountSubtotal() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(COUNTSUBTOTAL$84);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(COUNTSUBTOTAL$84);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetCountSubtotal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COUNTSUBTOTAL$84) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setCountSubtotal(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COUNTSUBTOTAL$84);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(COUNTSUBTOTAL$84);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetCountSubtotal(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(COUNTSUBTOTAL$84);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(COUNTSUBTOTAL$84);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetCountSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COUNTSUBTOTAL$84);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getStdDevSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(STDDEVSUBTOTAL$86);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(STDDEVSUBTOTAL$86);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetStdDevSubtotal() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(STDDEVSUBTOTAL$86);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(STDDEVSUBTOTAL$86);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetStdDevSubtotal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(STDDEVSUBTOTAL$86) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setStdDevSubtotal(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(STDDEVSUBTOTAL$86);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(STDDEVSUBTOTAL$86);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetStdDevSubtotal(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(STDDEVSUBTOTAL$86);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(STDDEVSUBTOTAL$86);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetStdDevSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(STDDEVSUBTOTAL$86);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getStdDevPSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(STDDEVPSUBTOTAL$88);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(STDDEVPSUBTOTAL$88);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetStdDevPSubtotal() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(STDDEVPSUBTOTAL$88);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(STDDEVPSUBTOTAL$88);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetStdDevPSubtotal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(STDDEVPSUBTOTAL$88) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setStdDevPSubtotal(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(STDDEVPSUBTOTAL$88);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(STDDEVPSUBTOTAL$88);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetStdDevPSubtotal(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(STDDEVPSUBTOTAL$88);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(STDDEVPSUBTOTAL$88);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetStdDevPSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(STDDEVPSUBTOTAL$88);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getVarSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VARSUBTOTAL$90);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(VARSUBTOTAL$90);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetVarSubtotal() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(VARSUBTOTAL$90);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(VARSUBTOTAL$90);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetVarSubtotal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VARSUBTOTAL$90) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setVarSubtotal(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VARSUBTOTAL$90);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VARSUBTOTAL$90);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetVarSubtotal(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(VARSUBTOTAL$90);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(VARSUBTOTAL$90);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetVarSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VARSUBTOTAL$90);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getVarPSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VARPSUBTOTAL$92);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(VARPSUBTOTAL$92);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetVarPSubtotal() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(VARPSUBTOTAL$92);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(VARPSUBTOTAL$92);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetVarPSubtotal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VARPSUBTOTAL$92) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setVarPSubtotal(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VARPSUBTOTAL$92);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VARPSUBTOTAL$92);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetVarPSubtotal(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(VARPSUBTOTAL$92);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(VARPSUBTOTAL$92);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetVarPSubtotal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VARPSUBTOTAL$92);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getShowPropCell() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWPROPCELL$94);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWPROPCELL$94);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetShowPropCell() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWPROPCELL$94);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWPROPCELL$94);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetShowPropCell() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWPROPCELL$94) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setShowPropCell(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWPROPCELL$94);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWPROPCELL$94);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetShowPropCell(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWPROPCELL$94);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWPROPCELL$94);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetShowPropCell() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWPROPCELL$94);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getShowPropTip() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWPROPTIP$96);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWPROPTIP$96);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetShowPropTip() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWPROPTIP$96);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWPROPTIP$96);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetShowPropTip() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWPROPTIP$96) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setShowPropTip(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWPROPTIP$96);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWPROPTIP$96);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetShowPropTip(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWPROPTIP$96);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWPROPTIP$96);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetShowPropTip() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWPROPTIP$96);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getShowPropAsCaption() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWPROPASCAPTION$98);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SHOWPROPASCAPTION$98);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetShowPropAsCaption() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWPROPASCAPTION$98);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SHOWPROPASCAPTION$98);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetShowPropAsCaption() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SHOWPROPASCAPTION$98) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setShowPropAsCaption(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SHOWPROPASCAPTION$98);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SHOWPROPASCAPTION$98);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetShowPropAsCaption(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SHOWPROPASCAPTION$98);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SHOWPROPASCAPTION$98);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetShowPropAsCaption() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SHOWPROPASCAPTION$98);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean getDefaultAttributeDrillState() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DEFAULTATTRIBUTEDRILLSTATE$100);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(DEFAULTATTRIBUTEDRILLSTATE$100);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public XmlBoolean xgetDefaultAttributeDrillState() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DEFAULTATTRIBUTEDRILLSTATE$100);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(DEFAULTATTRIBUTEDRILLSTATE$100);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public boolean isSetDefaultAttributeDrillState() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DEFAULTATTRIBUTEDRILLSTATE$100) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void setDefaultAttributeDrillState(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DEFAULTATTRIBUTEDRILLSTATE$100);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DEFAULTATTRIBUTEDRILLSTATE$100);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void xsetDefaultAttributeDrillState(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DEFAULTATTRIBUTEDRILLSTATE$100);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(DEFAULTATTRIBUTEDRILLSTATE$100);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField
    public void unsetDefaultAttributeDrillState() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DEFAULTATTRIBUTEDRILLSTATE$100);
        }
    }
}
