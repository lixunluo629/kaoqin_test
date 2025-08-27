package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOutlinePr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetUpPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTSheetPrImpl.class */
public class CTSheetPrImpl extends XmlComplexContentImpl implements CTSheetPr {
    private static final QName TABCOLOR$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "tabColor");
    private static final QName OUTLINEPR$2 = new QName(XSSFRelation.NS_SPREADSHEETML, "outlinePr");
    private static final QName PAGESETUPPR$4 = new QName(XSSFRelation.NS_SPREADSHEETML, "pageSetUpPr");
    private static final QName SYNCHORIZONTAL$6 = new QName("", "syncHorizontal");
    private static final QName SYNCVERTICAL$8 = new QName("", "syncVertical");
    private static final QName SYNCREF$10 = new QName("", "syncRef");
    private static final QName TRANSITIONEVALUATION$12 = new QName("", "transitionEvaluation");
    private static final QName TRANSITIONENTRY$14 = new QName("", "transitionEntry");
    private static final QName PUBLISHED$16 = new QName("", "published");
    private static final QName CODENAME$18 = new QName("", "codeName");
    private static final QName FILTERMODE$20 = new QName("", "filterMode");
    private static final QName ENABLEFORMATCONDITIONSCALCULATION$22 = new QName("", "enableFormatConditionsCalculation");

    public CTSheetPrImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public CTColor getTabColor() {
        synchronized (monitor()) {
            check_orphaned();
            CTColor cTColor = (CTColor) get_store().find_element_user(TABCOLOR$0, 0);
            if (cTColor == null) {
                return null;
            }
            return cTColor;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean isSetTabColor() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(TABCOLOR$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void setTabColor(CTColor cTColor) {
        synchronized (monitor()) {
            check_orphaned();
            CTColor cTColor2 = (CTColor) get_store().find_element_user(TABCOLOR$0, 0);
            if (cTColor2 == null) {
                cTColor2 = (CTColor) get_store().add_element_user(TABCOLOR$0);
            }
            cTColor2.set(cTColor);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public CTColor addNewTabColor() {
        CTColor cTColor;
        synchronized (monitor()) {
            check_orphaned();
            cTColor = (CTColor) get_store().add_element_user(TABCOLOR$0);
        }
        return cTColor;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void unsetTabColor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TABCOLOR$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public CTOutlinePr getOutlinePr() {
        synchronized (monitor()) {
            check_orphaned();
            CTOutlinePr cTOutlinePr = (CTOutlinePr) get_store().find_element_user(OUTLINEPR$2, 0);
            if (cTOutlinePr == null) {
                return null;
            }
            return cTOutlinePr;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean isSetOutlinePr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(OUTLINEPR$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void setOutlinePr(CTOutlinePr cTOutlinePr) {
        synchronized (monitor()) {
            check_orphaned();
            CTOutlinePr cTOutlinePr2 = (CTOutlinePr) get_store().find_element_user(OUTLINEPR$2, 0);
            if (cTOutlinePr2 == null) {
                cTOutlinePr2 = (CTOutlinePr) get_store().add_element_user(OUTLINEPR$2);
            }
            cTOutlinePr2.set(cTOutlinePr);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public CTOutlinePr addNewOutlinePr() {
        CTOutlinePr cTOutlinePr;
        synchronized (monitor()) {
            check_orphaned();
            cTOutlinePr = (CTOutlinePr) get_store().add_element_user(OUTLINEPR$2);
        }
        return cTOutlinePr;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void unsetOutlinePr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(OUTLINEPR$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public CTPageSetUpPr getPageSetUpPr() {
        synchronized (monitor()) {
            check_orphaned();
            CTPageSetUpPr cTPageSetUpPr = (CTPageSetUpPr) get_store().find_element_user(PAGESETUPPR$4, 0);
            if (cTPageSetUpPr == null) {
                return null;
            }
            return cTPageSetUpPr;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean isSetPageSetUpPr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PAGESETUPPR$4) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void setPageSetUpPr(CTPageSetUpPr cTPageSetUpPr) {
        synchronized (monitor()) {
            check_orphaned();
            CTPageSetUpPr cTPageSetUpPr2 = (CTPageSetUpPr) get_store().find_element_user(PAGESETUPPR$4, 0);
            if (cTPageSetUpPr2 == null) {
                cTPageSetUpPr2 = (CTPageSetUpPr) get_store().add_element_user(PAGESETUPPR$4);
            }
            cTPageSetUpPr2.set(cTPageSetUpPr);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public CTPageSetUpPr addNewPageSetUpPr() {
        CTPageSetUpPr cTPageSetUpPr;
        synchronized (monitor()) {
            check_orphaned();
            cTPageSetUpPr = (CTPageSetUpPr) get_store().add_element_user(PAGESETUPPR$4);
        }
        return cTPageSetUpPr;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void unsetPageSetUpPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PAGESETUPPR$4, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean getSyncHorizontal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SYNCHORIZONTAL$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SYNCHORIZONTAL$6);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public XmlBoolean xgetSyncHorizontal() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SYNCHORIZONTAL$6);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SYNCHORIZONTAL$6);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean isSetSyncHorizontal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SYNCHORIZONTAL$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void setSyncHorizontal(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SYNCHORIZONTAL$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SYNCHORIZONTAL$6);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void xsetSyncHorizontal(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SYNCHORIZONTAL$6);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SYNCHORIZONTAL$6);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void unsetSyncHorizontal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SYNCHORIZONTAL$6);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean getSyncVertical() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SYNCVERTICAL$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SYNCVERTICAL$8);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public XmlBoolean xgetSyncVertical() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SYNCVERTICAL$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SYNCVERTICAL$8);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean isSetSyncVertical() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SYNCVERTICAL$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void setSyncVertical(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SYNCVERTICAL$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SYNCVERTICAL$8);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void xsetSyncVertical(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SYNCVERTICAL$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SYNCVERTICAL$8);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void unsetSyncVertical() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SYNCVERTICAL$8);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public String getSyncRef() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SYNCREF$10);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public STRef xgetSyncRef() {
        STRef sTRef;
        synchronized (monitor()) {
            check_orphaned();
            sTRef = (STRef) get_store().find_attribute_user(SYNCREF$10);
        }
        return sTRef;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean isSetSyncRef() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SYNCREF$10) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void setSyncRef(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SYNCREF$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SYNCREF$10);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void xsetSyncRef(STRef sTRef) {
        synchronized (monitor()) {
            check_orphaned();
            STRef sTRef2 = (STRef) get_store().find_attribute_user(SYNCREF$10);
            if (sTRef2 == null) {
                sTRef2 = (STRef) get_store().add_attribute_user(SYNCREF$10);
            }
            sTRef2.set(sTRef);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void unsetSyncRef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SYNCREF$10);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean getTransitionEvaluation() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TRANSITIONEVALUATION$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(TRANSITIONEVALUATION$12);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public XmlBoolean xgetTransitionEvaluation() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(TRANSITIONEVALUATION$12);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(TRANSITIONEVALUATION$12);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean isSetTransitionEvaluation() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TRANSITIONEVALUATION$12) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void setTransitionEvaluation(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TRANSITIONEVALUATION$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TRANSITIONEVALUATION$12);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void xsetTransitionEvaluation(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(TRANSITIONEVALUATION$12);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(TRANSITIONEVALUATION$12);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void unsetTransitionEvaluation() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TRANSITIONEVALUATION$12);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean getTransitionEntry() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TRANSITIONENTRY$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(TRANSITIONENTRY$14);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public XmlBoolean xgetTransitionEntry() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(TRANSITIONENTRY$14);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(TRANSITIONENTRY$14);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean isSetTransitionEntry() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TRANSITIONENTRY$14) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void setTransitionEntry(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TRANSITIONENTRY$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TRANSITIONENTRY$14);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void xsetTransitionEntry(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(TRANSITIONENTRY$14);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(TRANSITIONENTRY$14);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void unsetTransitionEntry() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TRANSITIONENTRY$14);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean getPublished() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PUBLISHED$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(PUBLISHED$16);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public XmlBoolean xgetPublished() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PUBLISHED$16);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(PUBLISHED$16);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean isSetPublished() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PUBLISHED$16) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void setPublished(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PUBLISHED$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PUBLISHED$16);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void xsetPublished(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(PUBLISHED$16);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(PUBLISHED$16);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void unsetPublished() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PUBLISHED$16);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public String getCodeName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CODENAME$18);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public XmlString xgetCodeName() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(CODENAME$18);
        }
        return xmlString;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean isSetCodeName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(CODENAME$18) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void setCodeName(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CODENAME$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(CODENAME$18);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void xsetCodeName(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(CODENAME$18);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(CODENAME$18);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void unsetCodeName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(CODENAME$18);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean getFilterMode() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FILTERMODE$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(FILTERMODE$20);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public XmlBoolean xgetFilterMode() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(FILTERMODE$20);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(FILTERMODE$20);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean isSetFilterMode() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FILTERMODE$20) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void setFilterMode(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FILTERMODE$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FILTERMODE$20);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void xsetFilterMode(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(FILTERMODE$20);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(FILTERMODE$20);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void unsetFilterMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FILTERMODE$20);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean getEnableFormatConditionsCalculation() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENABLEFORMATCONDITIONSCALCULATION$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ENABLEFORMATCONDITIONSCALCULATION$22);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public XmlBoolean xgetEnableFormatConditionsCalculation() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ENABLEFORMATCONDITIONSCALCULATION$22);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(ENABLEFORMATCONDITIONSCALCULATION$22);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public boolean isSetEnableFormatConditionsCalculation() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ENABLEFORMATCONDITIONSCALCULATION$22) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void setEnableFormatConditionsCalculation(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ENABLEFORMATCONDITIONSCALCULATION$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ENABLEFORMATCONDITIONSCALCULATION$22);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void xsetEnableFormatConditionsCalculation(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ENABLEFORMATCONDITIONSCALCULATION$22);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ENABLEFORMATCONDITIONSCALCULATION$22);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
    public void unsetEnableFormatConditionsCalculation() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ENABLEFORMATCONDITIONSCALCULATION$22);
        }
    }
}
