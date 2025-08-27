package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor;
import org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle;
import org.openxmlformats.schemas.drawingml.x2006.main.STPercentage;
import org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate;
import org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle;
import org.openxmlformats.schemas.drawingml.x2006.main.STRectAlignment;
import org.openxmlformats.schemas.drawingml.x2006.main.STRectAlignment$Enum;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTOuterShadowEffectImpl.class */
public class CTOuterShadowEffectImpl extends XmlComplexContentImpl implements CTOuterShadowEffect {
    private static final QName SCRGBCLR$0 = new QName(XSSFRelation.NS_DRAWINGML, "scrgbClr");
    private static final QName SRGBCLR$2 = new QName(XSSFRelation.NS_DRAWINGML, "srgbClr");
    private static final QName HSLCLR$4 = new QName(XSSFRelation.NS_DRAWINGML, "hslClr");
    private static final QName SYSCLR$6 = new QName(XSSFRelation.NS_DRAWINGML, "sysClr");
    private static final QName SCHEMECLR$8 = new QName(XSSFRelation.NS_DRAWINGML, "schemeClr");
    private static final QName PRSTCLR$10 = new QName(XSSFRelation.NS_DRAWINGML, "prstClr");
    private static final QName BLURRAD$12 = new QName("", "blurRad");
    private static final QName DIST$14 = new QName("", "dist");
    private static final QName DIR$16 = new QName("", AbstractHtmlElementTag.DIR_ATTRIBUTE);
    private static final QName SX$18 = new QName("", "sx");
    private static final QName SY$20 = new QName("", "sy");
    private static final QName KX$22 = new QName("", "kx");
    private static final QName KY$24 = new QName("", "ky");
    private static final QName ALGN$26 = new QName("", "algn");
    private static final QName ROTWITHSHAPE$28 = new QName("", "rotWithShape");

    public CTOuterShadowEffectImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
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

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public boolean isSetScrgbClr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SCRGBCLR$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
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

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public CTScRgbColor addNewScrgbClr() {
        CTScRgbColor cTScRgbColor;
        synchronized (monitor()) {
            check_orphaned();
            cTScRgbColor = (CTScRgbColor) get_store().add_element_user(SCRGBCLR$0);
        }
        return cTScRgbColor;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void unsetScrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SCRGBCLR$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
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

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public boolean isSetSrgbClr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SRGBCLR$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
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

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public CTSRgbColor addNewSrgbClr() {
        CTSRgbColor cTSRgbColor;
        synchronized (monitor()) {
            check_orphaned();
            cTSRgbColor = (CTSRgbColor) get_store().add_element_user(SRGBCLR$2);
        }
        return cTSRgbColor;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void unsetSrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SRGBCLR$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
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

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public boolean isSetHslClr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(HSLCLR$4) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
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

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public CTHslColor addNewHslClr() {
        CTHslColor cTHslColor;
        synchronized (monitor()) {
            check_orphaned();
            cTHslColor = (CTHslColor) get_store().add_element_user(HSLCLR$4);
        }
        return cTHslColor;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void unsetHslClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(HSLCLR$4, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
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

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public boolean isSetSysClr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SYSCLR$6) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
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

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public CTSystemColor addNewSysClr() {
        CTSystemColor cTSystemColor;
        synchronized (monitor()) {
            check_orphaned();
            cTSystemColor = (CTSystemColor) get_store().add_element_user(SYSCLR$6);
        }
        return cTSystemColor;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void unsetSysClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SYSCLR$6, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
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

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public boolean isSetSchemeClr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SCHEMECLR$8) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
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

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public CTSchemeColor addNewSchemeClr() {
        CTSchemeColor cTSchemeColor;
        synchronized (monitor()) {
            check_orphaned();
            cTSchemeColor = (CTSchemeColor) get_store().add_element_user(SCHEMECLR$8);
        }
        return cTSchemeColor;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void unsetSchemeClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SCHEMECLR$8, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
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

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public boolean isSetPrstClr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PRSTCLR$10) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
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

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public CTPresetColor addNewPrstClr() {
        CTPresetColor cTPresetColor;
        synchronized (monitor()) {
            check_orphaned();
            cTPresetColor = (CTPresetColor) get_store().add_element_user(PRSTCLR$10);
        }
        return cTPresetColor;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void unsetPrstClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PRSTCLR$10, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public long getBlurRad() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BLURRAD$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(BLURRAD$12);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public STPositiveCoordinate xgetBlurRad() {
        STPositiveCoordinate sTPositiveCoordinate;
        synchronized (monitor()) {
            check_orphaned();
            STPositiveCoordinate sTPositiveCoordinate2 = (STPositiveCoordinate) get_store().find_attribute_user(BLURRAD$12);
            if (sTPositiveCoordinate2 == null) {
                sTPositiveCoordinate2 = (STPositiveCoordinate) get_default_attribute_value(BLURRAD$12);
            }
            sTPositiveCoordinate = sTPositiveCoordinate2;
        }
        return sTPositiveCoordinate;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public boolean isSetBlurRad() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BLURRAD$12) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void setBlurRad(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BLURRAD$12);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BLURRAD$12);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void xsetBlurRad(STPositiveCoordinate sTPositiveCoordinate) {
        synchronized (monitor()) {
            check_orphaned();
            STPositiveCoordinate sTPositiveCoordinate2 = (STPositiveCoordinate) get_store().find_attribute_user(BLURRAD$12);
            if (sTPositiveCoordinate2 == null) {
                sTPositiveCoordinate2 = (STPositiveCoordinate) get_store().add_attribute_user(BLURRAD$12);
            }
            sTPositiveCoordinate2.set(sTPositiveCoordinate);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void unsetBlurRad() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BLURRAD$12);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public long getDist() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DIST$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(DIST$14);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public STPositiveCoordinate xgetDist() {
        STPositiveCoordinate sTPositiveCoordinate;
        synchronized (monitor()) {
            check_orphaned();
            STPositiveCoordinate sTPositiveCoordinate2 = (STPositiveCoordinate) get_store().find_attribute_user(DIST$14);
            if (sTPositiveCoordinate2 == null) {
                sTPositiveCoordinate2 = (STPositiveCoordinate) get_default_attribute_value(DIST$14);
            }
            sTPositiveCoordinate = sTPositiveCoordinate2;
        }
        return sTPositiveCoordinate;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public boolean isSetDist() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DIST$14) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void setDist(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DIST$14);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DIST$14);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void xsetDist(STPositiveCoordinate sTPositiveCoordinate) {
        synchronized (monitor()) {
            check_orphaned();
            STPositiveCoordinate sTPositiveCoordinate2 = (STPositiveCoordinate) get_store().find_attribute_user(DIST$14);
            if (sTPositiveCoordinate2 == null) {
                sTPositiveCoordinate2 = (STPositiveCoordinate) get_store().add_attribute_user(DIST$14);
            }
            sTPositiveCoordinate2.set(sTPositiveCoordinate);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void unsetDist() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DIST$14);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public int getDir() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DIR$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(DIR$16);
            }
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public STPositiveFixedAngle xgetDir() {
        STPositiveFixedAngle sTPositiveFixedAngle;
        synchronized (monitor()) {
            check_orphaned();
            STPositiveFixedAngle sTPositiveFixedAngle2 = (STPositiveFixedAngle) get_store().find_attribute_user(DIR$16);
            if (sTPositiveFixedAngle2 == null) {
                sTPositiveFixedAngle2 = (STPositiveFixedAngle) get_default_attribute_value(DIR$16);
            }
            sTPositiveFixedAngle = sTPositiveFixedAngle2;
        }
        return sTPositiveFixedAngle;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public boolean isSetDir() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DIR$16) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void setDir(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DIR$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DIR$16);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void xsetDir(STPositiveFixedAngle sTPositiveFixedAngle) {
        synchronized (monitor()) {
            check_orphaned();
            STPositiveFixedAngle sTPositiveFixedAngle2 = (STPositiveFixedAngle) get_store().find_attribute_user(DIR$16);
            if (sTPositiveFixedAngle2 == null) {
                sTPositiveFixedAngle2 = (STPositiveFixedAngle) get_store().add_attribute_user(DIR$16);
            }
            sTPositiveFixedAngle2.set(sTPositiveFixedAngle);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void unsetDir() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DIR$16);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public int getSx() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SX$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SX$18);
            }
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public STPercentage xgetSx() {
        STPercentage sTPercentage;
        synchronized (monitor()) {
            check_orphaned();
            STPercentage sTPercentage2 = (STPercentage) get_store().find_attribute_user(SX$18);
            if (sTPercentage2 == null) {
                sTPercentage2 = (STPercentage) get_default_attribute_value(SX$18);
            }
            sTPercentage = sTPercentage2;
        }
        return sTPercentage;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public boolean isSetSx() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SX$18) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void setSx(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SX$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SX$18);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void xsetSx(STPercentage sTPercentage) {
        synchronized (monitor()) {
            check_orphaned();
            STPercentage sTPercentage2 = (STPercentage) get_store().find_attribute_user(SX$18);
            if (sTPercentage2 == null) {
                sTPercentage2 = (STPercentage) get_store().add_attribute_user(SX$18);
            }
            sTPercentage2.set(sTPercentage);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void unsetSx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SX$18);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public int getSy() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SY$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SY$20);
            }
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public STPercentage xgetSy() {
        STPercentage sTPercentage;
        synchronized (monitor()) {
            check_orphaned();
            STPercentage sTPercentage2 = (STPercentage) get_store().find_attribute_user(SY$20);
            if (sTPercentage2 == null) {
                sTPercentage2 = (STPercentage) get_default_attribute_value(SY$20);
            }
            sTPercentage = sTPercentage2;
        }
        return sTPercentage;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public boolean isSetSy() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SY$20) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void setSy(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SY$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SY$20);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void xsetSy(STPercentage sTPercentage) {
        synchronized (monitor()) {
            check_orphaned();
            STPercentage sTPercentage2 = (STPercentage) get_store().find_attribute_user(SY$20);
            if (sTPercentage2 == null) {
                sTPercentage2 = (STPercentage) get_store().add_attribute_user(SY$20);
            }
            sTPercentage2.set(sTPercentage);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void unsetSy() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SY$20);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public int getKx() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(KX$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(KX$22);
            }
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public STFixedAngle xgetKx() {
        STFixedAngle sTFixedAngle;
        synchronized (monitor()) {
            check_orphaned();
            STFixedAngle sTFixedAngleFind_attribute_user = get_store().find_attribute_user(KX$22);
            if (sTFixedAngleFind_attribute_user == null) {
                sTFixedAngleFind_attribute_user = (STFixedAngle) get_default_attribute_value(KX$22);
            }
            sTFixedAngle = sTFixedAngleFind_attribute_user;
        }
        return sTFixedAngle;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public boolean isSetKx() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(KX$22) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void setKx(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(KX$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(KX$22);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void xsetKx(STFixedAngle sTFixedAngle) {
        synchronized (monitor()) {
            check_orphaned();
            STFixedAngle sTFixedAngleFind_attribute_user = get_store().find_attribute_user(KX$22);
            if (sTFixedAngleFind_attribute_user == null) {
                sTFixedAngleFind_attribute_user = (STFixedAngle) get_store().add_attribute_user(KX$22);
            }
            sTFixedAngleFind_attribute_user.set(sTFixedAngle);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void unsetKx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(KX$22);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public int getKy() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(KY$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(KY$24);
            }
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public STFixedAngle xgetKy() {
        STFixedAngle sTFixedAngle;
        synchronized (monitor()) {
            check_orphaned();
            STFixedAngle sTFixedAngleFind_attribute_user = get_store().find_attribute_user(KY$24);
            if (sTFixedAngleFind_attribute_user == null) {
                sTFixedAngleFind_attribute_user = (STFixedAngle) get_default_attribute_value(KY$24);
            }
            sTFixedAngle = sTFixedAngleFind_attribute_user;
        }
        return sTFixedAngle;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public boolean isSetKy() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(KY$24) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void setKy(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(KY$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(KY$24);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void xsetKy(STFixedAngle sTFixedAngle) {
        synchronized (monitor()) {
            check_orphaned();
            STFixedAngle sTFixedAngleFind_attribute_user = get_store().find_attribute_user(KY$24);
            if (sTFixedAngleFind_attribute_user == null) {
                sTFixedAngleFind_attribute_user = (STFixedAngle) get_store().add_attribute_user(KY$24);
            }
            sTFixedAngleFind_attribute_user.set(sTFixedAngle);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void unsetKy() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(KY$24);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public STRectAlignment$Enum getAlgn() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ALGN$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ALGN$26);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STRectAlignment$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public STRectAlignment xgetAlgn() {
        STRectAlignment sTRectAlignment;
        synchronized (monitor()) {
            check_orphaned();
            STRectAlignment sTRectAlignmentFind_attribute_user = get_store().find_attribute_user(ALGN$26);
            if (sTRectAlignmentFind_attribute_user == null) {
                sTRectAlignmentFind_attribute_user = (STRectAlignment) get_default_attribute_value(ALGN$26);
            }
            sTRectAlignment = sTRectAlignmentFind_attribute_user;
        }
        return sTRectAlignment;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public boolean isSetAlgn() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ALGN$26) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void setAlgn(STRectAlignment$Enum sTRectAlignment$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ALGN$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ALGN$26);
            }
            simpleValue.setEnumValue(sTRectAlignment$Enum);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void xsetAlgn(STRectAlignment sTRectAlignment) {
        synchronized (monitor()) {
            check_orphaned();
            STRectAlignment sTRectAlignmentFind_attribute_user = get_store().find_attribute_user(ALGN$26);
            if (sTRectAlignmentFind_attribute_user == null) {
                sTRectAlignmentFind_attribute_user = (STRectAlignment) get_store().add_attribute_user(ALGN$26);
            }
            sTRectAlignmentFind_attribute_user.set(sTRectAlignment);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void unsetAlgn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ALGN$26);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public boolean getRotWithShape() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ROTWITHSHAPE$28);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ROTWITHSHAPE$28);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public XmlBoolean xgetRotWithShape() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ROTWITHSHAPE$28);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(ROTWITHSHAPE$28);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public boolean isSetRotWithShape() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ROTWITHSHAPE$28) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void setRotWithShape(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ROTWITHSHAPE$28);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ROTWITHSHAPE$28);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void xsetRotWithShape(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ROTWITHSHAPE$28);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ROTWITHSHAPE$28);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
    public void unsetRotWithShape() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ROTWITHSHAPE$28);
        }
    }
}
