package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTIgnoredErrorImpl.class */
public class CTIgnoredErrorImpl extends XmlComplexContentImpl implements CTIgnoredError {
    private static final QName SQREF$0 = new QName("", "sqref");
    private static final QName EVALERROR$2 = new QName("", "evalError");
    private static final QName TWODIGITTEXTYEAR$4 = new QName("", "twoDigitTextYear");
    private static final QName NUMBERSTOREDASTEXT$6 = new QName("", "numberStoredAsText");
    private static final QName FORMULA$8 = new QName("", "formula");
    private static final QName FORMULARANGE$10 = new QName("", "formulaRange");
    private static final QName UNLOCKEDFORMULA$12 = new QName("", "unlockedFormula");
    private static final QName EMPTYCELLREFERENCE$14 = new QName("", "emptyCellReference");
    private static final QName LISTDATAVALIDATION$16 = new QName("", "listDataValidation");
    private static final QName CALCULATEDCOLUMN$18 = new QName("", "calculatedColumn");

    public CTIgnoredErrorImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public List getSqref() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SQREF$0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getListValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public STSqref xgetSqref() {
        STSqref sTSqref;
        synchronized (monitor()) {
            check_orphaned();
            sTSqref = (STSqref) get_store().find_attribute_user(SQREF$0);
        }
        return sTSqref;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void setSqref(List list) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SQREF$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SQREF$0);
            }
            simpleValue.setListValue(list);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void xsetSqref(STSqref sTSqref) {
        synchronized (monitor()) {
            check_orphaned();
            STSqref sTSqref2 = (STSqref) get_store().find_attribute_user(SQREF$0);
            if (sTSqref2 == null) {
                sTSqref2 = (STSqref) get_store().add_attribute_user(SQREF$0);
            }
            sTSqref2.set(sTSqref);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public boolean getEvalError() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(EVALERROR$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(EVALERROR$2);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public XmlBoolean xgetEvalError() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(EVALERROR$2);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(EVALERROR$2);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public boolean isSetEvalError() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(EVALERROR$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void setEvalError(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(EVALERROR$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(EVALERROR$2);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void xsetEvalError(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(EVALERROR$2);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(EVALERROR$2);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void unsetEvalError() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(EVALERROR$2);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public boolean getTwoDigitTextYear() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TWODIGITTEXTYEAR$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(TWODIGITTEXTYEAR$4);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public XmlBoolean xgetTwoDigitTextYear() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(TWODIGITTEXTYEAR$4);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(TWODIGITTEXTYEAR$4);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public boolean isSetTwoDigitTextYear() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TWODIGITTEXTYEAR$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void setTwoDigitTextYear(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TWODIGITTEXTYEAR$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TWODIGITTEXTYEAR$4);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void xsetTwoDigitTextYear(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(TWODIGITTEXTYEAR$4);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(TWODIGITTEXTYEAR$4);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void unsetTwoDigitTextYear() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TWODIGITTEXTYEAR$4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public boolean getNumberStoredAsText() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NUMBERSTOREDASTEXT$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(NUMBERSTOREDASTEXT$6);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public XmlBoolean xgetNumberStoredAsText() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(NUMBERSTOREDASTEXT$6);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(NUMBERSTOREDASTEXT$6);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public boolean isSetNumberStoredAsText() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NUMBERSTOREDASTEXT$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void setNumberStoredAsText(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NUMBERSTOREDASTEXT$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NUMBERSTOREDASTEXT$6);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void xsetNumberStoredAsText(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(NUMBERSTOREDASTEXT$6);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(NUMBERSTOREDASTEXT$6);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void unsetNumberStoredAsText() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NUMBERSTOREDASTEXT$6);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public boolean getFormula() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FORMULA$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(FORMULA$8);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public XmlBoolean xgetFormula() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(FORMULA$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(FORMULA$8);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public boolean isSetFormula() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FORMULA$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void setFormula(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FORMULA$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FORMULA$8);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void xsetFormula(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(FORMULA$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(FORMULA$8);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void unsetFormula() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FORMULA$8);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public boolean getFormulaRange() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FORMULARANGE$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(FORMULARANGE$10);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public XmlBoolean xgetFormulaRange() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(FORMULARANGE$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(FORMULARANGE$10);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public boolean isSetFormulaRange() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FORMULARANGE$10) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void setFormulaRange(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FORMULARANGE$10);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FORMULARANGE$10);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void xsetFormulaRange(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(FORMULARANGE$10);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(FORMULARANGE$10);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void unsetFormulaRange() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FORMULARANGE$10);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public boolean getUnlockedFormula() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(UNLOCKEDFORMULA$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(UNLOCKEDFORMULA$12);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public XmlBoolean xgetUnlockedFormula() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(UNLOCKEDFORMULA$12);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(UNLOCKEDFORMULA$12);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public boolean isSetUnlockedFormula() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(UNLOCKEDFORMULA$12) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void setUnlockedFormula(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(UNLOCKEDFORMULA$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(UNLOCKEDFORMULA$12);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void xsetUnlockedFormula(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(UNLOCKEDFORMULA$12);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(UNLOCKEDFORMULA$12);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void unsetUnlockedFormula() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(UNLOCKEDFORMULA$12);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public boolean getEmptyCellReference() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(EMPTYCELLREFERENCE$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(EMPTYCELLREFERENCE$14);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public XmlBoolean xgetEmptyCellReference() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(EMPTYCELLREFERENCE$14);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(EMPTYCELLREFERENCE$14);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public boolean isSetEmptyCellReference() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(EMPTYCELLREFERENCE$14) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void setEmptyCellReference(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(EMPTYCELLREFERENCE$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(EMPTYCELLREFERENCE$14);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void xsetEmptyCellReference(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(EMPTYCELLREFERENCE$14);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(EMPTYCELLREFERENCE$14);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void unsetEmptyCellReference() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(EMPTYCELLREFERENCE$14);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public boolean getListDataValidation() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LISTDATAVALIDATION$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(LISTDATAVALIDATION$16);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public XmlBoolean xgetListDataValidation() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(LISTDATAVALIDATION$16);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(LISTDATAVALIDATION$16);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public boolean isSetListDataValidation() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(LISTDATAVALIDATION$16) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void setListDataValidation(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LISTDATAVALIDATION$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(LISTDATAVALIDATION$16);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void xsetListDataValidation(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(LISTDATAVALIDATION$16);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(LISTDATAVALIDATION$16);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void unsetListDataValidation() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(LISTDATAVALIDATION$16);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public boolean getCalculatedColumn() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CALCULATEDCOLUMN$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(CALCULATEDCOLUMN$18);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public XmlBoolean xgetCalculatedColumn() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(CALCULATEDCOLUMN$18);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(CALCULATEDCOLUMN$18);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public boolean isSetCalculatedColumn() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(CALCULATEDCOLUMN$18) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void setCalculatedColumn(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CALCULATEDCOLUMN$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(CALCULATEDCOLUMN$18);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void xsetCalculatedColumn(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(CALCULATEDCOLUMN$18);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(CALCULATEDCOLUMN$18);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError
    public void unsetCalculatedColumn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(CALCULATEDCOLUMN$18);
        }
    }
}
