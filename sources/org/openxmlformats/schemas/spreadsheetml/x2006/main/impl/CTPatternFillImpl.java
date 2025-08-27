package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STPatternType;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTPatternFillImpl.class */
public class CTPatternFillImpl extends XmlComplexContentImpl implements CTPatternFill {
    private static final QName FGCOLOR$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "fgColor");
    private static final QName BGCOLOR$2 = new QName(XSSFRelation.NS_SPREADSHEETML, "bgColor");
    private static final QName PATTERNTYPE$4 = new QName("", "patternType");

    public CTPatternFillImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill
    public CTColor getFgColor() {
        synchronized (monitor()) {
            check_orphaned();
            CTColor cTColor = (CTColor) get_store().find_element_user(FGCOLOR$0, 0);
            if (cTColor == null) {
                return null;
            }
            return cTColor;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill
    public boolean isSetFgColor() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(FGCOLOR$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill
    public void setFgColor(CTColor cTColor) {
        synchronized (monitor()) {
            check_orphaned();
            CTColor cTColor2 = (CTColor) get_store().find_element_user(FGCOLOR$0, 0);
            if (cTColor2 == null) {
                cTColor2 = (CTColor) get_store().add_element_user(FGCOLOR$0);
            }
            cTColor2.set(cTColor);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill
    public CTColor addNewFgColor() {
        CTColor cTColor;
        synchronized (monitor()) {
            check_orphaned();
            cTColor = (CTColor) get_store().add_element_user(FGCOLOR$0);
        }
        return cTColor;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill
    public void unsetFgColor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(FGCOLOR$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill
    public CTColor getBgColor() {
        synchronized (monitor()) {
            check_orphaned();
            CTColor cTColor = (CTColor) get_store().find_element_user(BGCOLOR$2, 0);
            if (cTColor == null) {
                return null;
            }
            return cTColor;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill
    public boolean isSetBgColor() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(BGCOLOR$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill
    public void setBgColor(CTColor cTColor) {
        synchronized (monitor()) {
            check_orphaned();
            CTColor cTColor2 = (CTColor) get_store().find_element_user(BGCOLOR$2, 0);
            if (cTColor2 == null) {
                cTColor2 = (CTColor) get_store().add_element_user(BGCOLOR$2);
            }
            cTColor2.set(cTColor);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill
    public CTColor addNewBgColor() {
        CTColor cTColor;
        synchronized (monitor()) {
            check_orphaned();
            cTColor = (CTColor) get_store().add_element_user(BGCOLOR$2);
        }
        return cTColor;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill
    public void unsetBgColor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(BGCOLOR$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill
    public STPatternType.Enum getPatternType() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PATTERNTYPE$4);
            if (simpleValue == null) {
                return null;
            }
            return (STPatternType.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill
    public STPatternType xgetPatternType() {
        STPatternType sTPatternType;
        synchronized (monitor()) {
            check_orphaned();
            sTPatternType = (STPatternType) get_store().find_attribute_user(PATTERNTYPE$4);
        }
        return sTPatternType;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill
    public boolean isSetPatternType() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(PATTERNTYPE$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill
    public void setPatternType(STPatternType.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PATTERNTYPE$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PATTERNTYPE$4);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill
    public void xsetPatternType(STPatternType sTPatternType) {
        synchronized (monitor()) {
            check_orphaned();
            STPatternType sTPatternType2 = (STPatternType) get_store().find_attribute_user(PATTERNTYPE$4);
            if (sTPatternType2 == null) {
                sTPatternType2 = (STPatternType) get_store().add_attribute_user(PATTERNTYPE$4);
            }
            sTPatternType2.set(sTPatternType);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill
    public void unsetPatternType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PATTERNTYPE$4);
        }
    }
}
