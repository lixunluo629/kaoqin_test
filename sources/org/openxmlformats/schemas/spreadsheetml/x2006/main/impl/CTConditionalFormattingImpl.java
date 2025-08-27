package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTConditionalFormattingImpl.class */
public class CTConditionalFormattingImpl extends XmlComplexContentImpl implements CTConditionalFormatting {
    private static final QName CFRULE$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "cfRule");
    private static final QName EXTLST$2 = new QName(XSSFRelation.NS_SPREADSHEETML, "extLst");
    private static final QName PIVOT$4 = new QName("", "pivot");
    private static final QName SQREF$6 = new QName("", "sqref");

    public CTConditionalFormattingImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public List<CTCfRule> getCfRuleList() {
        1CfRuleList r0;
        synchronized (monitor()) {
            check_orphaned();
            r0 = new 1CfRuleList(this);
        }
        return r0;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public CTCfRule[] getCfRuleArray() {
        CTCfRule[] cTCfRuleArr;
        synchronized (monitor()) {
            check_orphaned();
            ArrayList arrayList = new ArrayList();
            get_store().find_all_element_users(CFRULE$0, arrayList);
            cTCfRuleArr = new CTCfRule[arrayList.size()];
            arrayList.toArray(cTCfRuleArr);
        }
        return cTCfRuleArr;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public CTCfRule getCfRuleArray(int i) {
        CTCfRule cTCfRule;
        synchronized (monitor()) {
            check_orphaned();
            cTCfRule = (CTCfRule) get_store().find_element_user(CFRULE$0, i);
            if (cTCfRule == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return cTCfRule;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public int sizeOfCfRuleArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(CFRULE$0);
        }
        return iCount_elements;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public void setCfRuleArray(CTCfRule[] cTCfRuleArr) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cTCfRuleArr, CFRULE$0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public void setCfRuleArray(int i, CTCfRule cTCfRule) {
        synchronized (monitor()) {
            check_orphaned();
            CTCfRule cTCfRule2 = (CTCfRule) get_store().find_element_user(CFRULE$0, i);
            if (cTCfRule2 == null) {
                throw new IndexOutOfBoundsException();
            }
            cTCfRule2.set(cTCfRule);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public CTCfRule insertNewCfRule(int i) {
        CTCfRule cTCfRule;
        synchronized (monitor()) {
            check_orphaned();
            cTCfRule = (CTCfRule) get_store().insert_element_user(CFRULE$0, i);
        }
        return cTCfRule;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public CTCfRule addNewCfRule() {
        CTCfRule cTCfRule;
        synchronized (monitor()) {
            check_orphaned();
            cTCfRule = (CTCfRule) get_store().add_element_user(CFRULE$0);
        }
        return cTCfRule;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public void removeCfRule(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CFRULE$0, i);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$2, 0);
            if (cTExtensionListFind_element_user == null) {
                return null;
            }
            return cTExtensionListFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public void setExtLst(CTExtensionList cTExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$2, 0);
            if (cTExtensionListFind_element_user == null) {
                cTExtensionListFind_element_user = (CTExtensionList) get_store().add_element_user(EXTLST$2);
            }
            cTExtensionListFind_element_user.set(cTExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public CTExtensionList addNewExtLst() {
        CTExtensionList cTExtensionListAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTExtensionListAdd_element_user = get_store().add_element_user(EXTLST$2);
        }
        return cTExtensionListAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public boolean getPivot() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PIVOT$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PIVOT$4);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public XmlBoolean xgetPivot() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PIVOT$4);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(PIVOT$4);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public boolean isSetPivot() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PIVOT$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public void setPivot(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PIVOT$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PIVOT$4);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public void xsetPivot(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PIVOT$4);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(PIVOT$4);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public void unsetPivot() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PIVOT$4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public List getSqref() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SQREF$6);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getListValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public STSqref xgetSqref() {
        STSqref sTSqref;
        synchronized (monitor()) {
            check_orphaned();
            sTSqref = (STSqref) get_store().find_attribute_user(SQREF$6);
        }
        return sTSqref;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public boolean isSetSqref() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SQREF$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public void setSqref(List list) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SQREF$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SQREF$6);
            }
            simpleValue.setListValue(list);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public void xsetSqref(STSqref sTSqref) {
        synchronized (monitor()) {
            check_orphaned();
            STSqref sTSqref2 = (STSqref) get_store().find_attribute_user(SQREF$6);
            if (sTSqref2 == null) {
                sTSqref2 = (STSqref) get_store().add_attribute_user(SQREF$6);
            }
            sTSqref2.set(sTSqref);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
    public void unsetSqref() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SQREF$6);
        }
    }
}
