package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STBorderStyle;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTBorderPrImpl.class */
public class CTBorderPrImpl extends XmlComplexContentImpl implements CTBorderPr {
    private static final QName COLOR$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "color");
    private static final QName STYLE$2 = new QName("", AbstractHtmlElementTag.STYLE_ATTRIBUTE);

    public CTBorderPrImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr
    public CTColor getColor() {
        synchronized (monitor()) {
            check_orphaned();
            CTColor cTColor = (CTColor) get_store().find_element_user(COLOR$0, 0);
            if (cTColor == null) {
                return null;
            }
            return cTColor;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr
    public boolean isSetColor() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(COLOR$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr
    public void setColor(CTColor cTColor) {
        synchronized (monitor()) {
            check_orphaned();
            CTColor cTColor2 = (CTColor) get_store().find_element_user(COLOR$0, 0);
            if (cTColor2 == null) {
                cTColor2 = (CTColor) get_store().add_element_user(COLOR$0);
            }
            cTColor2.set(cTColor);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr
    public CTColor addNewColor() {
        CTColor cTColor;
        synchronized (monitor()) {
            check_orphaned();
            cTColor = (CTColor) get_store().add_element_user(COLOR$0);
        }
        return cTColor;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr
    public void unsetColor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(COLOR$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr
    public STBorderStyle.Enum getStyle() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(STYLE$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(STYLE$2);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STBorderStyle.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr
    public STBorderStyle xgetStyle() {
        STBorderStyle sTBorderStyle;
        synchronized (monitor()) {
            check_orphaned();
            STBorderStyle sTBorderStyle2 = (STBorderStyle) get_store().find_attribute_user(STYLE$2);
            if (sTBorderStyle2 == null) {
                sTBorderStyle2 = (STBorderStyle) get_default_attribute_value(STYLE$2);
            }
            sTBorderStyle = sTBorderStyle2;
        }
        return sTBorderStyle;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr
    public boolean isSetStyle() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(STYLE$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr
    public void setStyle(STBorderStyle.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(STYLE$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(STYLE$2);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr
    public void xsetStyle(STBorderStyle sTBorderStyle) {
        synchronized (monitor()) {
            check_orphaned();
            STBorderStyle sTBorderStyle2 = (STBorderStyle) get_store().find_attribute_user(STYLE$2);
            if (sTBorderStyle2 == null) {
                sTBorderStyle2 = (STBorderStyle) get_store().add_attribute_user(STYLE$2);
            }
            sTBorderStyle2.set(sTBorderStyle);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr
    public void unsetStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(STYLE$2);
        }
    }
}
