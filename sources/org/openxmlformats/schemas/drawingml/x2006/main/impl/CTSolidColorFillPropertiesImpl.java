package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTSolidColorFillPropertiesImpl.class */
public class CTSolidColorFillPropertiesImpl extends XmlComplexContentImpl implements CTSolidColorFillProperties {
    private static final QName SCRGBCLR$0 = new QName(XSSFRelation.NS_DRAWINGML, "scrgbClr");
    private static final QName SRGBCLR$2 = new QName(XSSFRelation.NS_DRAWINGML, "srgbClr");
    private static final QName HSLCLR$4 = new QName(XSSFRelation.NS_DRAWINGML, "hslClr");
    private static final QName SYSCLR$6 = new QName(XSSFRelation.NS_DRAWINGML, "sysClr");
    private static final QName SCHEMECLR$8 = new QName(XSSFRelation.NS_DRAWINGML, "schemeClr");
    private static final QName PRSTCLR$10 = new QName(XSSFRelation.NS_DRAWINGML, "prstClr");

    public CTSolidColorFillPropertiesImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public CTScRgbColor getScrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            CTScRgbColor cTScRgbColor = (CTScRgbColor) get_store().find_element_user(SCRGBCLR$0, 0);
            if (cTScRgbColor == null) {
                return null;
            }
            return cTScRgbColor;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public boolean isSetScrgbClr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SCRGBCLR$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public void setScrgbClr(CTScRgbColor cTScRgbColor) {
        synchronized (monitor()) {
            check_orphaned();
            CTScRgbColor cTScRgbColor2 = (CTScRgbColor) get_store().find_element_user(SCRGBCLR$0, 0);
            if (cTScRgbColor2 == null) {
                cTScRgbColor2 = (CTScRgbColor) get_store().add_element_user(SCRGBCLR$0);
            }
            cTScRgbColor2.set(cTScRgbColor);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public CTScRgbColor addNewScrgbClr() {
        CTScRgbColor cTScRgbColor;
        synchronized (monitor()) {
            check_orphaned();
            cTScRgbColor = (CTScRgbColor) get_store().add_element_user(SCRGBCLR$0);
        }
        return cTScRgbColor;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public void unsetScrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SCRGBCLR$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public CTSRgbColor getSrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            CTSRgbColor cTSRgbColor = (CTSRgbColor) get_store().find_element_user(SRGBCLR$2, 0);
            if (cTSRgbColor == null) {
                return null;
            }
            return cTSRgbColor;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public boolean isSetSrgbClr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SRGBCLR$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public void setSrgbClr(CTSRgbColor cTSRgbColor) {
        synchronized (monitor()) {
            check_orphaned();
            CTSRgbColor cTSRgbColor2 = (CTSRgbColor) get_store().find_element_user(SRGBCLR$2, 0);
            if (cTSRgbColor2 == null) {
                cTSRgbColor2 = (CTSRgbColor) get_store().add_element_user(SRGBCLR$2);
            }
            cTSRgbColor2.set(cTSRgbColor);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public CTSRgbColor addNewSrgbClr() {
        CTSRgbColor cTSRgbColor;
        synchronized (monitor()) {
            check_orphaned();
            cTSRgbColor = (CTSRgbColor) get_store().add_element_user(SRGBCLR$2);
        }
        return cTSRgbColor;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public void unsetSrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SRGBCLR$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public CTHslColor getHslClr() {
        synchronized (monitor()) {
            check_orphaned();
            CTHslColor cTHslColor = (CTHslColor) get_store().find_element_user(HSLCLR$4, 0);
            if (cTHslColor == null) {
                return null;
            }
            return cTHslColor;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public boolean isSetHslClr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(HSLCLR$4) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public void setHslClr(CTHslColor cTHslColor) {
        synchronized (monitor()) {
            check_orphaned();
            CTHslColor cTHslColor2 = (CTHslColor) get_store().find_element_user(HSLCLR$4, 0);
            if (cTHslColor2 == null) {
                cTHslColor2 = (CTHslColor) get_store().add_element_user(HSLCLR$4);
            }
            cTHslColor2.set(cTHslColor);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public CTHslColor addNewHslClr() {
        CTHslColor cTHslColor;
        synchronized (monitor()) {
            check_orphaned();
            cTHslColor = (CTHslColor) get_store().add_element_user(HSLCLR$4);
        }
        return cTHslColor;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public void unsetHslClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(HSLCLR$4, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public CTSystemColor getSysClr() {
        synchronized (monitor()) {
            check_orphaned();
            CTSystemColor cTSystemColor = (CTSystemColor) get_store().find_element_user(SYSCLR$6, 0);
            if (cTSystemColor == null) {
                return null;
            }
            return cTSystemColor;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public boolean isSetSysClr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SYSCLR$6) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public void setSysClr(CTSystemColor cTSystemColor) {
        synchronized (monitor()) {
            check_orphaned();
            CTSystemColor cTSystemColor2 = (CTSystemColor) get_store().find_element_user(SYSCLR$6, 0);
            if (cTSystemColor2 == null) {
                cTSystemColor2 = (CTSystemColor) get_store().add_element_user(SYSCLR$6);
            }
            cTSystemColor2.set(cTSystemColor);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public CTSystemColor addNewSysClr() {
        CTSystemColor cTSystemColor;
        synchronized (monitor()) {
            check_orphaned();
            cTSystemColor = (CTSystemColor) get_store().add_element_user(SYSCLR$6);
        }
        return cTSystemColor;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public void unsetSysClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SYSCLR$6, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public CTSchemeColor getSchemeClr() {
        synchronized (monitor()) {
            check_orphaned();
            CTSchemeColor cTSchemeColor = (CTSchemeColor) get_store().find_element_user(SCHEMECLR$8, 0);
            if (cTSchemeColor == null) {
                return null;
            }
            return cTSchemeColor;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public boolean isSetSchemeClr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SCHEMECLR$8) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public void setSchemeClr(CTSchemeColor cTSchemeColor) {
        synchronized (monitor()) {
            check_orphaned();
            CTSchemeColor cTSchemeColor2 = (CTSchemeColor) get_store().find_element_user(SCHEMECLR$8, 0);
            if (cTSchemeColor2 == null) {
                cTSchemeColor2 = (CTSchemeColor) get_store().add_element_user(SCHEMECLR$8);
            }
            cTSchemeColor2.set(cTSchemeColor);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public CTSchemeColor addNewSchemeClr() {
        CTSchemeColor cTSchemeColor;
        synchronized (monitor()) {
            check_orphaned();
            cTSchemeColor = (CTSchemeColor) get_store().add_element_user(SCHEMECLR$8);
        }
        return cTSchemeColor;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public void unsetSchemeClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SCHEMECLR$8, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public CTPresetColor getPrstClr() {
        synchronized (monitor()) {
            check_orphaned();
            CTPresetColor cTPresetColor = (CTPresetColor) get_store().find_element_user(PRSTCLR$10, 0);
            if (cTPresetColor == null) {
                return null;
            }
            return cTPresetColor;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public boolean isSetPrstClr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PRSTCLR$10) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public void setPrstClr(CTPresetColor cTPresetColor) {
        synchronized (monitor()) {
            check_orphaned();
            CTPresetColor cTPresetColor2 = (CTPresetColor) get_store().find_element_user(PRSTCLR$10, 0);
            if (cTPresetColor2 == null) {
                cTPresetColor2 = (CTPresetColor) get_store().add_element_user(PRSTCLR$10);
            }
            cTPresetColor2.set(cTPresetColor);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public CTPresetColor addNewPrstClr() {
        CTPresetColor cTPresetColor;
        synchronized (monitor()) {
            check_orphaned();
            cTPresetColor = (CTPresetColor) get_store().add_element_user(PRSTCLR$10);
        }
        return cTPresetColor;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties
    public void unsetPrstClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PRSTCLR$10, 0);
        }
    }
}
