package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer;
import org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineFillFollowText;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineFillGroupWrapper;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineLineFollowText;
import org.openxmlformats.schemas.drawingml.x2006.main.STPercentage;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextCapsType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextFontSize;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextLanguageID;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextNonNegativePoint;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextPoint;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextStrikeType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextUnderlineType;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTTextCharacterPropertiesImpl.class */
public class CTTextCharacterPropertiesImpl extends XmlComplexContentImpl implements CTTextCharacterProperties {
    private static final QName LN$0 = new QName(XSSFRelation.NS_DRAWINGML, "ln");
    private static final QName NOFILL$2 = new QName(XSSFRelation.NS_DRAWINGML, "noFill");
    private static final QName SOLIDFILL$4 = new QName(XSSFRelation.NS_DRAWINGML, "solidFill");
    private static final QName GRADFILL$6 = new QName(XSSFRelation.NS_DRAWINGML, "gradFill");
    private static final QName BLIPFILL$8 = new QName(XSSFRelation.NS_DRAWINGML, "blipFill");
    private static final QName PATTFILL$10 = new QName(XSSFRelation.NS_DRAWINGML, "pattFill");
    private static final QName GRPFILL$12 = new QName(XSSFRelation.NS_DRAWINGML, "grpFill");
    private static final QName EFFECTLST$14 = new QName(XSSFRelation.NS_DRAWINGML, "effectLst");
    private static final QName EFFECTDAG$16 = new QName(XSSFRelation.NS_DRAWINGML, "effectDag");
    private static final QName HIGHLIGHT$18 = new QName(XSSFRelation.NS_DRAWINGML, "highlight");
    private static final QName ULNTX$20 = new QName(XSSFRelation.NS_DRAWINGML, "uLnTx");
    private static final QName ULN$22 = new QName(XSSFRelation.NS_DRAWINGML, "uLn");
    private static final QName UFILLTX$24 = new QName(XSSFRelation.NS_DRAWINGML, "uFillTx");
    private static final QName UFILL$26 = new QName(XSSFRelation.NS_DRAWINGML, "uFill");
    private static final QName LATIN$28 = new QName(XSSFRelation.NS_DRAWINGML, "latin");
    private static final QName EA$30 = new QName(XSSFRelation.NS_DRAWINGML, "ea");
    private static final QName CS$32 = new QName(XSSFRelation.NS_DRAWINGML, "cs");
    private static final QName SYM$34 = new QName(XSSFRelation.NS_DRAWINGML, "sym");
    private static final QName HLINKCLICK$36 = new QName(XSSFRelation.NS_DRAWINGML, "hlinkClick");
    private static final QName HLINKMOUSEOVER$38 = new QName(XSSFRelation.NS_DRAWINGML, "hlinkMouseOver");
    private static final QName EXTLST$40 = new QName(XSSFRelation.NS_DRAWINGML, "extLst");
    private static final QName KUMIMOJI$42 = new QName("", "kumimoji");
    private static final QName LANG$44 = new QName("", AbstractHtmlElementTag.LANG_ATTRIBUTE);
    private static final QName ALTLANG$46 = new QName("", "altLang");
    private static final QName SZ$48 = new QName("", "sz");
    private static final QName B$50 = new QName("", "b");
    private static final QName I$52 = new QName("", "i");
    private static final QName U$54 = new QName("", "u");
    private static final QName STRIKE$56 = new QName("", "strike");
    private static final QName KERN$58 = new QName("", "kern");
    private static final QName CAP$60 = new QName("", "cap");
    private static final QName SPC$62 = new QName("", "spc");
    private static final QName NORMALIZEH$64 = new QName("", "normalizeH");
    private static final QName BASELINE$66 = new QName("", "baseline");
    private static final QName NOPROOF$68 = new QName("", "noProof");
    private static final QName DIRTY$70 = new QName("", "dirty");
    private static final QName ERR$72 = new QName("", "err");
    private static final QName SMTCLEAN$74 = new QName("", "smtClean");
    private static final QName SMTID$76 = new QName("", "smtId");
    private static final QName BMK$78 = new QName("", "bmk");

    public CTTextCharacterPropertiesImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTLineProperties getLn() {
        synchronized (monitor()) {
            check_orphaned();
            CTLineProperties cTLineProperties = (CTLineProperties) get_store().find_element_user(LN$0, 0);
            if (cTLineProperties == null) {
                return null;
            }
            return cTLineProperties;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetLn() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(LN$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setLn(CTLineProperties cTLineProperties) {
        synchronized (monitor()) {
            check_orphaned();
            CTLineProperties cTLineProperties2 = (CTLineProperties) get_store().find_element_user(LN$0, 0);
            if (cTLineProperties2 == null) {
                cTLineProperties2 = (CTLineProperties) get_store().add_element_user(LN$0);
            }
            cTLineProperties2.set(cTLineProperties);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTLineProperties addNewLn() {
        CTLineProperties cTLineProperties;
        synchronized (monitor()) {
            check_orphaned();
            cTLineProperties = (CTLineProperties) get_store().add_element_user(LN$0);
        }
        return cTLineProperties;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetLn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(LN$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTNoFillProperties getNoFill() {
        synchronized (monitor()) {
            check_orphaned();
            CTNoFillProperties cTNoFillProperties = (CTNoFillProperties) get_store().find_element_user(NOFILL$2, 0);
            if (cTNoFillProperties == null) {
                return null;
            }
            return cTNoFillProperties;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetNoFill() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(NOFILL$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setNoFill(CTNoFillProperties cTNoFillProperties) {
        synchronized (monitor()) {
            check_orphaned();
            CTNoFillProperties cTNoFillProperties2 = (CTNoFillProperties) get_store().find_element_user(NOFILL$2, 0);
            if (cTNoFillProperties2 == null) {
                cTNoFillProperties2 = (CTNoFillProperties) get_store().add_element_user(NOFILL$2);
            }
            cTNoFillProperties2.set(cTNoFillProperties);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTNoFillProperties addNewNoFill() {
        CTNoFillProperties cTNoFillProperties;
        synchronized (monitor()) {
            check_orphaned();
            cTNoFillProperties = (CTNoFillProperties) get_store().add_element_user(NOFILL$2);
        }
        return cTNoFillProperties;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetNoFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(NOFILL$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTSolidColorFillProperties getSolidFill() {
        synchronized (monitor()) {
            check_orphaned();
            CTSolidColorFillProperties cTSolidColorFillProperties = (CTSolidColorFillProperties) get_store().find_element_user(SOLIDFILL$4, 0);
            if (cTSolidColorFillProperties == null) {
                return null;
            }
            return cTSolidColorFillProperties;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetSolidFill() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SOLIDFILL$4) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setSolidFill(CTSolidColorFillProperties cTSolidColorFillProperties) {
        synchronized (monitor()) {
            check_orphaned();
            CTSolidColorFillProperties cTSolidColorFillProperties2 = (CTSolidColorFillProperties) get_store().find_element_user(SOLIDFILL$4, 0);
            if (cTSolidColorFillProperties2 == null) {
                cTSolidColorFillProperties2 = (CTSolidColorFillProperties) get_store().add_element_user(SOLIDFILL$4);
            }
            cTSolidColorFillProperties2.set(cTSolidColorFillProperties);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTSolidColorFillProperties addNewSolidFill() {
        CTSolidColorFillProperties cTSolidColorFillProperties;
        synchronized (monitor()) {
            check_orphaned();
            cTSolidColorFillProperties = (CTSolidColorFillProperties) get_store().add_element_user(SOLIDFILL$4);
        }
        return cTSolidColorFillProperties;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetSolidFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SOLIDFILL$4, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTGradientFillProperties getGradFill() {
        synchronized (monitor()) {
            check_orphaned();
            CTGradientFillProperties cTGradientFillProperties = (CTGradientFillProperties) get_store().find_element_user(GRADFILL$6, 0);
            if (cTGradientFillProperties == null) {
                return null;
            }
            return cTGradientFillProperties;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetGradFill() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(GRADFILL$6) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setGradFill(CTGradientFillProperties cTGradientFillProperties) {
        synchronized (monitor()) {
            check_orphaned();
            CTGradientFillProperties cTGradientFillProperties2 = (CTGradientFillProperties) get_store().find_element_user(GRADFILL$6, 0);
            if (cTGradientFillProperties2 == null) {
                cTGradientFillProperties2 = (CTGradientFillProperties) get_store().add_element_user(GRADFILL$6);
            }
            cTGradientFillProperties2.set(cTGradientFillProperties);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTGradientFillProperties addNewGradFill() {
        CTGradientFillProperties cTGradientFillProperties;
        synchronized (monitor()) {
            check_orphaned();
            cTGradientFillProperties = (CTGradientFillProperties) get_store().add_element_user(GRADFILL$6);
        }
        return cTGradientFillProperties;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetGradFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(GRADFILL$6, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTBlipFillProperties getBlipFill() {
        synchronized (monitor()) {
            check_orphaned();
            CTBlipFillProperties cTBlipFillProperties = (CTBlipFillProperties) get_store().find_element_user(BLIPFILL$8, 0);
            if (cTBlipFillProperties == null) {
                return null;
            }
            return cTBlipFillProperties;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetBlipFill() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(BLIPFILL$8) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setBlipFill(CTBlipFillProperties cTBlipFillProperties) {
        synchronized (monitor()) {
            check_orphaned();
            CTBlipFillProperties cTBlipFillProperties2 = (CTBlipFillProperties) get_store().find_element_user(BLIPFILL$8, 0);
            if (cTBlipFillProperties2 == null) {
                cTBlipFillProperties2 = (CTBlipFillProperties) get_store().add_element_user(BLIPFILL$8);
            }
            cTBlipFillProperties2.set(cTBlipFillProperties);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTBlipFillProperties addNewBlipFill() {
        CTBlipFillProperties cTBlipFillProperties;
        synchronized (monitor()) {
            check_orphaned();
            cTBlipFillProperties = (CTBlipFillProperties) get_store().add_element_user(BLIPFILL$8);
        }
        return cTBlipFillProperties;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetBlipFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(BLIPFILL$8, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTPatternFillProperties getPattFill() {
        synchronized (monitor()) {
            check_orphaned();
            CTPatternFillProperties cTPatternFillProperties = (CTPatternFillProperties) get_store().find_element_user(PATTFILL$10, 0);
            if (cTPatternFillProperties == null) {
                return null;
            }
            return cTPatternFillProperties;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetPattFill() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PATTFILL$10) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setPattFill(CTPatternFillProperties cTPatternFillProperties) {
        synchronized (monitor()) {
            check_orphaned();
            CTPatternFillProperties cTPatternFillProperties2 = (CTPatternFillProperties) get_store().find_element_user(PATTFILL$10, 0);
            if (cTPatternFillProperties2 == null) {
                cTPatternFillProperties2 = (CTPatternFillProperties) get_store().add_element_user(PATTFILL$10);
            }
            cTPatternFillProperties2.set(cTPatternFillProperties);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTPatternFillProperties addNewPattFill() {
        CTPatternFillProperties cTPatternFillProperties;
        synchronized (monitor()) {
            check_orphaned();
            cTPatternFillProperties = (CTPatternFillProperties) get_store().add_element_user(PATTFILL$10);
        }
        return cTPatternFillProperties;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetPattFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PATTFILL$10, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTGroupFillProperties getGrpFill() {
        synchronized (monitor()) {
            check_orphaned();
            CTGroupFillProperties cTGroupFillProperties = (CTGroupFillProperties) get_store().find_element_user(GRPFILL$12, 0);
            if (cTGroupFillProperties == null) {
                return null;
            }
            return cTGroupFillProperties;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetGrpFill() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(GRPFILL$12) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setGrpFill(CTGroupFillProperties cTGroupFillProperties) {
        synchronized (monitor()) {
            check_orphaned();
            CTGroupFillProperties cTGroupFillProperties2 = (CTGroupFillProperties) get_store().find_element_user(GRPFILL$12, 0);
            if (cTGroupFillProperties2 == null) {
                cTGroupFillProperties2 = (CTGroupFillProperties) get_store().add_element_user(GRPFILL$12);
            }
            cTGroupFillProperties2.set(cTGroupFillProperties);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTGroupFillProperties addNewGrpFill() {
        CTGroupFillProperties cTGroupFillProperties;
        synchronized (monitor()) {
            check_orphaned();
            cTGroupFillProperties = (CTGroupFillProperties) get_store().add_element_user(GRPFILL$12);
        }
        return cTGroupFillProperties;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetGrpFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(GRPFILL$12, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTEffectList getEffectLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTEffectList cTEffectList = (CTEffectList) get_store().find_element_user(EFFECTLST$14, 0);
            if (cTEffectList == null) {
                return null;
            }
            return cTEffectList;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetEffectLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EFFECTLST$14) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setEffectLst(CTEffectList cTEffectList) {
        synchronized (monitor()) {
            check_orphaned();
            CTEffectList cTEffectList2 = (CTEffectList) get_store().find_element_user(EFFECTLST$14, 0);
            if (cTEffectList2 == null) {
                cTEffectList2 = (CTEffectList) get_store().add_element_user(EFFECTLST$14);
            }
            cTEffectList2.set(cTEffectList);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTEffectList addNewEffectLst() {
        CTEffectList cTEffectList;
        synchronized (monitor()) {
            check_orphaned();
            cTEffectList = (CTEffectList) get_store().add_element_user(EFFECTLST$14);
        }
        return cTEffectList;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetEffectLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EFFECTLST$14, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTEffectContainer getEffectDag() {
        synchronized (monitor()) {
            check_orphaned();
            CTEffectContainer cTEffectContainerFind_element_user = get_store().find_element_user(EFFECTDAG$16, 0);
            if (cTEffectContainerFind_element_user == null) {
                return null;
            }
            return cTEffectContainerFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetEffectDag() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EFFECTDAG$16) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setEffectDag(CTEffectContainer cTEffectContainer) {
        synchronized (monitor()) {
            check_orphaned();
            CTEffectContainer cTEffectContainerFind_element_user = get_store().find_element_user(EFFECTDAG$16, 0);
            if (cTEffectContainerFind_element_user == null) {
                cTEffectContainerFind_element_user = (CTEffectContainer) get_store().add_element_user(EFFECTDAG$16);
            }
            cTEffectContainerFind_element_user.set(cTEffectContainer);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTEffectContainer addNewEffectDag() {
        CTEffectContainer cTEffectContainerAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTEffectContainerAdd_element_user = get_store().add_element_user(EFFECTDAG$16);
        }
        return cTEffectContainerAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetEffectDag() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EFFECTDAG$16, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTColor getHighlight() {
        synchronized (monitor()) {
            check_orphaned();
            CTColor cTColor = (CTColor) get_store().find_element_user(HIGHLIGHT$18, 0);
            if (cTColor == null) {
                return null;
            }
            return cTColor;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetHighlight() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(HIGHLIGHT$18) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setHighlight(CTColor cTColor) {
        synchronized (monitor()) {
            check_orphaned();
            CTColor cTColor2 = (CTColor) get_store().find_element_user(HIGHLIGHT$18, 0);
            if (cTColor2 == null) {
                cTColor2 = (CTColor) get_store().add_element_user(HIGHLIGHT$18);
            }
            cTColor2.set(cTColor);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTColor addNewHighlight() {
        CTColor cTColor;
        synchronized (monitor()) {
            check_orphaned();
            cTColor = (CTColor) get_store().add_element_user(HIGHLIGHT$18);
        }
        return cTColor;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetHighlight() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(HIGHLIGHT$18, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTTextUnderlineLineFollowText getULnTx() {
        synchronized (monitor()) {
            check_orphaned();
            CTTextUnderlineLineFollowText cTTextUnderlineLineFollowTextFind_element_user = get_store().find_element_user(ULNTX$20, 0);
            if (cTTextUnderlineLineFollowTextFind_element_user == null) {
                return null;
            }
            return cTTextUnderlineLineFollowTextFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetULnTx() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(ULNTX$20) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setULnTx(CTTextUnderlineLineFollowText cTTextUnderlineLineFollowText) {
        synchronized (monitor()) {
            check_orphaned();
            CTTextUnderlineLineFollowText cTTextUnderlineLineFollowTextFind_element_user = get_store().find_element_user(ULNTX$20, 0);
            if (cTTextUnderlineLineFollowTextFind_element_user == null) {
                cTTextUnderlineLineFollowTextFind_element_user = (CTTextUnderlineLineFollowText) get_store().add_element_user(ULNTX$20);
            }
            cTTextUnderlineLineFollowTextFind_element_user.set(cTTextUnderlineLineFollowText);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTTextUnderlineLineFollowText addNewULnTx() {
        CTTextUnderlineLineFollowText cTTextUnderlineLineFollowTextAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTTextUnderlineLineFollowTextAdd_element_user = get_store().add_element_user(ULNTX$20);
        }
        return cTTextUnderlineLineFollowTextAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetULnTx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ULNTX$20, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTLineProperties getULn() {
        synchronized (monitor()) {
            check_orphaned();
            CTLineProperties cTLineProperties = (CTLineProperties) get_store().find_element_user(ULN$22, 0);
            if (cTLineProperties == null) {
                return null;
            }
            return cTLineProperties;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetULn() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(ULN$22) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setULn(CTLineProperties cTLineProperties) {
        synchronized (monitor()) {
            check_orphaned();
            CTLineProperties cTLineProperties2 = (CTLineProperties) get_store().find_element_user(ULN$22, 0);
            if (cTLineProperties2 == null) {
                cTLineProperties2 = (CTLineProperties) get_store().add_element_user(ULN$22);
            }
            cTLineProperties2.set(cTLineProperties);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTLineProperties addNewULn() {
        CTLineProperties cTLineProperties;
        synchronized (monitor()) {
            check_orphaned();
            cTLineProperties = (CTLineProperties) get_store().add_element_user(ULN$22);
        }
        return cTLineProperties;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetULn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ULN$22, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTTextUnderlineFillFollowText getUFillTx() {
        synchronized (monitor()) {
            check_orphaned();
            CTTextUnderlineFillFollowText cTTextUnderlineFillFollowTextFind_element_user = get_store().find_element_user(UFILLTX$24, 0);
            if (cTTextUnderlineFillFollowTextFind_element_user == null) {
                return null;
            }
            return cTTextUnderlineFillFollowTextFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetUFillTx() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(UFILLTX$24) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setUFillTx(CTTextUnderlineFillFollowText cTTextUnderlineFillFollowText) {
        synchronized (monitor()) {
            check_orphaned();
            CTTextUnderlineFillFollowText cTTextUnderlineFillFollowTextFind_element_user = get_store().find_element_user(UFILLTX$24, 0);
            if (cTTextUnderlineFillFollowTextFind_element_user == null) {
                cTTextUnderlineFillFollowTextFind_element_user = (CTTextUnderlineFillFollowText) get_store().add_element_user(UFILLTX$24);
            }
            cTTextUnderlineFillFollowTextFind_element_user.set(cTTextUnderlineFillFollowText);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTTextUnderlineFillFollowText addNewUFillTx() {
        CTTextUnderlineFillFollowText cTTextUnderlineFillFollowTextAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTTextUnderlineFillFollowTextAdd_element_user = get_store().add_element_user(UFILLTX$24);
        }
        return cTTextUnderlineFillFollowTextAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetUFillTx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(UFILLTX$24, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTTextUnderlineFillGroupWrapper getUFill() {
        synchronized (monitor()) {
            check_orphaned();
            CTTextUnderlineFillGroupWrapper cTTextUnderlineFillGroupWrapperFind_element_user = get_store().find_element_user(UFILL$26, 0);
            if (cTTextUnderlineFillGroupWrapperFind_element_user == null) {
                return null;
            }
            return cTTextUnderlineFillGroupWrapperFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetUFill() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(UFILL$26) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setUFill(CTTextUnderlineFillGroupWrapper cTTextUnderlineFillGroupWrapper) {
        synchronized (monitor()) {
            check_orphaned();
            CTTextUnderlineFillGroupWrapper cTTextUnderlineFillGroupWrapperFind_element_user = get_store().find_element_user(UFILL$26, 0);
            if (cTTextUnderlineFillGroupWrapperFind_element_user == null) {
                cTTextUnderlineFillGroupWrapperFind_element_user = (CTTextUnderlineFillGroupWrapper) get_store().add_element_user(UFILL$26);
            }
            cTTextUnderlineFillGroupWrapperFind_element_user.set(cTTextUnderlineFillGroupWrapper);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTTextUnderlineFillGroupWrapper addNewUFill() {
        CTTextUnderlineFillGroupWrapper cTTextUnderlineFillGroupWrapperAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTTextUnderlineFillGroupWrapperAdd_element_user = get_store().add_element_user(UFILL$26);
        }
        return cTTextUnderlineFillGroupWrapperAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetUFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(UFILL$26, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTTextFont getLatin() {
        synchronized (monitor()) {
            check_orphaned();
            CTTextFont cTTextFont = (CTTextFont) get_store().find_element_user(LATIN$28, 0);
            if (cTTextFont == null) {
                return null;
            }
            return cTTextFont;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetLatin() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(LATIN$28) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setLatin(CTTextFont cTTextFont) {
        synchronized (monitor()) {
            check_orphaned();
            CTTextFont cTTextFont2 = (CTTextFont) get_store().find_element_user(LATIN$28, 0);
            if (cTTextFont2 == null) {
                cTTextFont2 = (CTTextFont) get_store().add_element_user(LATIN$28);
            }
            cTTextFont2.set(cTTextFont);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTTextFont addNewLatin() {
        CTTextFont cTTextFont;
        synchronized (monitor()) {
            check_orphaned();
            cTTextFont = (CTTextFont) get_store().add_element_user(LATIN$28);
        }
        return cTTextFont;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetLatin() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(LATIN$28, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTTextFont getEa() {
        synchronized (monitor()) {
            check_orphaned();
            CTTextFont cTTextFont = (CTTextFont) get_store().find_element_user(EA$30, 0);
            if (cTTextFont == null) {
                return null;
            }
            return cTTextFont;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetEa() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EA$30) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setEa(CTTextFont cTTextFont) {
        synchronized (monitor()) {
            check_orphaned();
            CTTextFont cTTextFont2 = (CTTextFont) get_store().find_element_user(EA$30, 0);
            if (cTTextFont2 == null) {
                cTTextFont2 = (CTTextFont) get_store().add_element_user(EA$30);
            }
            cTTextFont2.set(cTTextFont);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTTextFont addNewEa() {
        CTTextFont cTTextFont;
        synchronized (monitor()) {
            check_orphaned();
            cTTextFont = (CTTextFont) get_store().add_element_user(EA$30);
        }
        return cTTextFont;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetEa() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EA$30, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTTextFont getCs() {
        synchronized (monitor()) {
            check_orphaned();
            CTTextFont cTTextFont = (CTTextFont) get_store().find_element_user(CS$32, 0);
            if (cTTextFont == null) {
                return null;
            }
            return cTTextFont;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetCs() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(CS$32) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setCs(CTTextFont cTTextFont) {
        synchronized (monitor()) {
            check_orphaned();
            CTTextFont cTTextFont2 = (CTTextFont) get_store().find_element_user(CS$32, 0);
            if (cTTextFont2 == null) {
                cTTextFont2 = (CTTextFont) get_store().add_element_user(CS$32);
            }
            cTTextFont2.set(cTTextFont);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTTextFont addNewCs() {
        CTTextFont cTTextFont;
        synchronized (monitor()) {
            check_orphaned();
            cTTextFont = (CTTextFont) get_store().add_element_user(CS$32);
        }
        return cTTextFont;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetCs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CS$32, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTTextFont getSym() {
        synchronized (monitor()) {
            check_orphaned();
            CTTextFont cTTextFont = (CTTextFont) get_store().find_element_user(SYM$34, 0);
            if (cTTextFont == null) {
                return null;
            }
            return cTTextFont;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetSym() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SYM$34) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setSym(CTTextFont cTTextFont) {
        synchronized (monitor()) {
            check_orphaned();
            CTTextFont cTTextFont2 = (CTTextFont) get_store().find_element_user(SYM$34, 0);
            if (cTTextFont2 == null) {
                cTTextFont2 = (CTTextFont) get_store().add_element_user(SYM$34);
            }
            cTTextFont2.set(cTTextFont);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTTextFont addNewSym() {
        CTTextFont cTTextFont;
        synchronized (monitor()) {
            check_orphaned();
            cTTextFont = (CTTextFont) get_store().add_element_user(SYM$34);
        }
        return cTTextFont;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetSym() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SYM$34, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTHyperlink getHlinkClick() {
        synchronized (monitor()) {
            check_orphaned();
            CTHyperlink cTHyperlink = (CTHyperlink) get_store().find_element_user(HLINKCLICK$36, 0);
            if (cTHyperlink == null) {
                return null;
            }
            return cTHyperlink;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetHlinkClick() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(HLINKCLICK$36) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setHlinkClick(CTHyperlink cTHyperlink) {
        synchronized (monitor()) {
            check_orphaned();
            CTHyperlink cTHyperlink2 = (CTHyperlink) get_store().find_element_user(HLINKCLICK$36, 0);
            if (cTHyperlink2 == null) {
                cTHyperlink2 = (CTHyperlink) get_store().add_element_user(HLINKCLICK$36);
            }
            cTHyperlink2.set(cTHyperlink);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTHyperlink addNewHlinkClick() {
        CTHyperlink cTHyperlink;
        synchronized (monitor()) {
            check_orphaned();
            cTHyperlink = (CTHyperlink) get_store().add_element_user(HLINKCLICK$36);
        }
        return cTHyperlink;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetHlinkClick() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(HLINKCLICK$36, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTHyperlink getHlinkMouseOver() {
        synchronized (monitor()) {
            check_orphaned();
            CTHyperlink cTHyperlink = (CTHyperlink) get_store().find_element_user(HLINKMOUSEOVER$38, 0);
            if (cTHyperlink == null) {
                return null;
            }
            return cTHyperlink;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetHlinkMouseOver() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(HLINKMOUSEOVER$38) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setHlinkMouseOver(CTHyperlink cTHyperlink) {
        synchronized (monitor()) {
            check_orphaned();
            CTHyperlink cTHyperlink2 = (CTHyperlink) get_store().find_element_user(HLINKMOUSEOVER$38, 0);
            if (cTHyperlink2 == null) {
                cTHyperlink2 = (CTHyperlink) get_store().add_element_user(HLINKMOUSEOVER$38);
            }
            cTHyperlink2.set(cTHyperlink);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTHyperlink addNewHlinkMouseOver() {
        CTHyperlink cTHyperlink;
        synchronized (monitor()) {
            check_orphaned();
            cTHyperlink = (CTHyperlink) get_store().add_element_user(HLINKMOUSEOVER$38);
        }
        return cTHyperlink;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetHlinkMouseOver() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(HLINKMOUSEOVER$38, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTOfficeArtExtensionList cTOfficeArtExtensionList = (CTOfficeArtExtensionList) get_store().find_element_user(EXTLST$40, 0);
            if (cTOfficeArtExtensionList == null) {
                return null;
            }
            return cTOfficeArtExtensionList;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$40) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTOfficeArtExtensionList cTOfficeArtExtensionList2 = (CTOfficeArtExtensionList) get_store().find_element_user(EXTLST$40, 0);
            if (cTOfficeArtExtensionList2 == null) {
                cTOfficeArtExtensionList2 = (CTOfficeArtExtensionList) get_store().add_element_user(EXTLST$40);
            }
            cTOfficeArtExtensionList2.set(cTOfficeArtExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public CTOfficeArtExtensionList addNewExtLst() {
        CTOfficeArtExtensionList cTOfficeArtExtensionList;
        synchronized (monitor()) {
            check_orphaned();
            cTOfficeArtExtensionList = (CTOfficeArtExtensionList) get_store().add_element_user(EXTLST$40);
        }
        return cTOfficeArtExtensionList;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$40, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean getKumimoji() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(KUMIMOJI$42);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public XmlBoolean xgetKumimoji() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(KUMIMOJI$42);
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetKumimoji() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(KUMIMOJI$42) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setKumimoji(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(KUMIMOJI$42);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(KUMIMOJI$42);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetKumimoji(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(KUMIMOJI$42);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(KUMIMOJI$42);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetKumimoji() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(KUMIMOJI$42);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public String getLang() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LANG$44);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public STTextLanguageID xgetLang() {
        STTextLanguageID sTTextLanguageID;
        synchronized (monitor()) {
            check_orphaned();
            sTTextLanguageID = (STTextLanguageID) get_store().find_attribute_user(LANG$44);
        }
        return sTTextLanguageID;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetLang() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(LANG$44) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setLang(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LANG$44);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(LANG$44);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetLang(STTextLanguageID sTTextLanguageID) {
        synchronized (monitor()) {
            check_orphaned();
            STTextLanguageID sTTextLanguageID2 = (STTextLanguageID) get_store().find_attribute_user(LANG$44);
            if (sTTextLanguageID2 == null) {
                sTTextLanguageID2 = (STTextLanguageID) get_store().add_attribute_user(LANG$44);
            }
            sTTextLanguageID2.set(sTTextLanguageID);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetLang() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(LANG$44);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public String getAltLang() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ALTLANG$46);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public STTextLanguageID xgetAltLang() {
        STTextLanguageID sTTextLanguageID;
        synchronized (monitor()) {
            check_orphaned();
            sTTextLanguageID = (STTextLanguageID) get_store().find_attribute_user(ALTLANG$46);
        }
        return sTTextLanguageID;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetAltLang() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ALTLANG$46) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setAltLang(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ALTLANG$46);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ALTLANG$46);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetAltLang(STTextLanguageID sTTextLanguageID) {
        synchronized (monitor()) {
            check_orphaned();
            STTextLanguageID sTTextLanguageID2 = (STTextLanguageID) get_store().find_attribute_user(ALTLANG$46);
            if (sTTextLanguageID2 == null) {
                sTTextLanguageID2 = (STTextLanguageID) get_store().add_attribute_user(ALTLANG$46);
            }
            sTTextLanguageID2.set(sTTextLanguageID);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetAltLang() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ALTLANG$46);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public int getSz() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SZ$48);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public STTextFontSize xgetSz() {
        STTextFontSize sTTextFontSize;
        synchronized (monitor()) {
            check_orphaned();
            sTTextFontSize = (STTextFontSize) get_store().find_attribute_user(SZ$48);
        }
        return sTTextFontSize;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetSz() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SZ$48) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setSz(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SZ$48);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SZ$48);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetSz(STTextFontSize sTTextFontSize) {
        synchronized (monitor()) {
            check_orphaned();
            STTextFontSize sTTextFontSize2 = (STTextFontSize) get_store().find_attribute_user(SZ$48);
            if (sTTextFontSize2 == null) {
                sTTextFontSize2 = (STTextFontSize) get_store().add_attribute_user(SZ$48);
            }
            sTTextFontSize2.set(sTTextFontSize);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetSz() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SZ$48);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean getB() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(B$50);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public XmlBoolean xgetB() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(B$50);
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetB() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(B$50) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setB(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(B$50);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(B$50);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetB(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(B$50);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(B$50);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetB() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(B$50);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean getI() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(I$52);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public XmlBoolean xgetI() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(I$52);
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetI() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(I$52) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setI(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(I$52);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(I$52);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetI(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(I$52);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(I$52);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetI() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(I$52);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public STTextUnderlineType.Enum getU() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(U$54);
            if (simpleValue == null) {
                return null;
            }
            return (STTextUnderlineType.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public STTextUnderlineType xgetU() {
        STTextUnderlineType sTTextUnderlineType;
        synchronized (monitor()) {
            check_orphaned();
            sTTextUnderlineType = (STTextUnderlineType) get_store().find_attribute_user(U$54);
        }
        return sTTextUnderlineType;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetU() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(U$54) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setU(STTextUnderlineType.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(U$54);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(U$54);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetU(STTextUnderlineType sTTextUnderlineType) {
        synchronized (monitor()) {
            check_orphaned();
            STTextUnderlineType sTTextUnderlineType2 = (STTextUnderlineType) get_store().find_attribute_user(U$54);
            if (sTTextUnderlineType2 == null) {
                sTTextUnderlineType2 = (STTextUnderlineType) get_store().add_attribute_user(U$54);
            }
            sTTextUnderlineType2.set(sTTextUnderlineType);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetU() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(U$54);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public STTextStrikeType.Enum getStrike() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(STRIKE$56);
            if (simpleValue == null) {
                return null;
            }
            return (STTextStrikeType.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public STTextStrikeType xgetStrike() {
        STTextStrikeType sTTextStrikeType;
        synchronized (monitor()) {
            check_orphaned();
            sTTextStrikeType = (STTextStrikeType) get_store().find_attribute_user(STRIKE$56);
        }
        return sTTextStrikeType;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetStrike() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(STRIKE$56) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setStrike(STTextStrikeType.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(STRIKE$56);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(STRIKE$56);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetStrike(STTextStrikeType sTTextStrikeType) {
        synchronized (monitor()) {
            check_orphaned();
            STTextStrikeType sTTextStrikeType2 = (STTextStrikeType) get_store().find_attribute_user(STRIKE$56);
            if (sTTextStrikeType2 == null) {
                sTTextStrikeType2 = (STTextStrikeType) get_store().add_attribute_user(STRIKE$56);
            }
            sTTextStrikeType2.set(sTTextStrikeType);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetStrike() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(STRIKE$56);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public int getKern() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(KERN$58);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public STTextNonNegativePoint xgetKern() {
        STTextNonNegativePoint sTTextNonNegativePointFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTTextNonNegativePointFind_attribute_user = get_store().find_attribute_user(KERN$58);
        }
        return sTTextNonNegativePointFind_attribute_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetKern() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(KERN$58) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setKern(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(KERN$58);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(KERN$58);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetKern(STTextNonNegativePoint sTTextNonNegativePoint) {
        synchronized (monitor()) {
            check_orphaned();
            STTextNonNegativePoint sTTextNonNegativePointFind_attribute_user = get_store().find_attribute_user(KERN$58);
            if (sTTextNonNegativePointFind_attribute_user == null) {
                sTTextNonNegativePointFind_attribute_user = (STTextNonNegativePoint) get_store().add_attribute_user(KERN$58);
            }
            sTTextNonNegativePointFind_attribute_user.set(sTTextNonNegativePoint);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetKern() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(KERN$58);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public STTextCapsType.Enum getCap() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CAP$60);
            if (simpleValue == null) {
                return null;
            }
            return (STTextCapsType.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public STTextCapsType xgetCap() {
        STTextCapsType sTTextCapsType;
        synchronized (monitor()) {
            check_orphaned();
            sTTextCapsType = (STTextCapsType) get_store().find_attribute_user(CAP$60);
        }
        return sTTextCapsType;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetCap() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(CAP$60) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setCap(STTextCapsType.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(CAP$60);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(CAP$60);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetCap(STTextCapsType sTTextCapsType) {
        synchronized (monitor()) {
            check_orphaned();
            STTextCapsType sTTextCapsType2 = (STTextCapsType) get_store().find_attribute_user(CAP$60);
            if (sTTextCapsType2 == null) {
                sTTextCapsType2 = (STTextCapsType) get_store().add_attribute_user(CAP$60);
            }
            sTTextCapsType2.set(sTTextCapsType);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetCap() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(CAP$60);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public int getSpc() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SPC$62);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public STTextPoint xgetSpc() {
        STTextPoint sTTextPoint;
        synchronized (monitor()) {
            check_orphaned();
            sTTextPoint = (STTextPoint) get_store().find_attribute_user(SPC$62);
        }
        return sTTextPoint;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetSpc() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SPC$62) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setSpc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SPC$62);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SPC$62);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetSpc(STTextPoint sTTextPoint) {
        synchronized (monitor()) {
            check_orphaned();
            STTextPoint sTTextPoint2 = (STTextPoint) get_store().find_attribute_user(SPC$62);
            if (sTTextPoint2 == null) {
                sTTextPoint2 = (STTextPoint) get_store().add_attribute_user(SPC$62);
            }
            sTTextPoint2.set(sTTextPoint);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetSpc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SPC$62);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean getNormalizeH() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NORMALIZEH$64);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public XmlBoolean xgetNormalizeH() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(NORMALIZEH$64);
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetNormalizeH() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NORMALIZEH$64) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setNormalizeH(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NORMALIZEH$64);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NORMALIZEH$64);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetNormalizeH(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(NORMALIZEH$64);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(NORMALIZEH$64);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetNormalizeH() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NORMALIZEH$64);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public int getBaseline() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BASELINE$66);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public STPercentage xgetBaseline() {
        STPercentage sTPercentage;
        synchronized (monitor()) {
            check_orphaned();
            sTPercentage = (STPercentage) get_store().find_attribute_user(BASELINE$66);
        }
        return sTPercentage;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetBaseline() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BASELINE$66) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setBaseline(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BASELINE$66);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BASELINE$66);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetBaseline(STPercentage sTPercentage) {
        synchronized (monitor()) {
            check_orphaned();
            STPercentage sTPercentage2 = (STPercentage) get_store().find_attribute_user(BASELINE$66);
            if (sTPercentage2 == null) {
                sTPercentage2 = (STPercentage) get_store().add_attribute_user(BASELINE$66);
            }
            sTPercentage2.set(sTPercentage);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetBaseline() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BASELINE$66);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean getNoProof() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NOPROOF$68);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public XmlBoolean xgetNoProof() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(NOPROOF$68);
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetNoProof() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NOPROOF$68) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setNoProof(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NOPROOF$68);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NOPROOF$68);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetNoProof(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(NOPROOF$68);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(NOPROOF$68);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetNoProof() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NOPROOF$68);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean getDirty() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DIRTY$70);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(DIRTY$70);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public XmlBoolean xgetDirty() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DIRTY$70);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(DIRTY$70);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetDirty() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DIRTY$70) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setDirty(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(DIRTY$70);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(DIRTY$70);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetDirty(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(DIRTY$70);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(DIRTY$70);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetDirty() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DIRTY$70);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean getErr() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ERR$72);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(ERR$72);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public XmlBoolean xgetErr() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ERR$72);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(ERR$72);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetErr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ERR$72) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setErr(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ERR$72);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ERR$72);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetErr(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ERR$72);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ERR$72);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetErr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ERR$72);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean getSmtClean() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SMTCLEAN$74);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SMTCLEAN$74);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public XmlBoolean xgetSmtClean() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SMTCLEAN$74);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(SMTCLEAN$74);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetSmtClean() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SMTCLEAN$74) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setSmtClean(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SMTCLEAN$74);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SMTCLEAN$74);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetSmtClean(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SMTCLEAN$74);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SMTCLEAN$74);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetSmtClean() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SMTCLEAN$74);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public long getSmtId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SMTID$76);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(SMTID$76);
            }
            if (simpleValue == null) {
                return 0L;
            }
            return simpleValue.getLongValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public XmlUnsignedInt xgetSmtId() {
        XmlUnsignedInt xmlUnsignedInt;
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(SMTID$76);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_default_attribute_value(SMTID$76);
            }
            xmlUnsignedInt = xmlUnsignedInt2;
        }
        return xmlUnsignedInt;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetSmtId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SMTID$76) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setSmtId(long j) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SMTID$76);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SMTID$76);
            }
            simpleValue.setLongValue(j);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetSmtId(XmlUnsignedInt xmlUnsignedInt) {
        synchronized (monitor()) {
            check_orphaned();
            XmlUnsignedInt xmlUnsignedInt2 = (XmlUnsignedInt) get_store().find_attribute_user(SMTID$76);
            if (xmlUnsignedInt2 == null) {
                xmlUnsignedInt2 = (XmlUnsignedInt) get_store().add_attribute_user(SMTID$76);
            }
            xmlUnsignedInt2.set(xmlUnsignedInt);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetSmtId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SMTID$76);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public String getBmk() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BMK$78);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public XmlString xgetBmk() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            xmlString = (XmlString) get_store().find_attribute_user(BMK$78);
        }
        return xmlString;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public boolean isSetBmk() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BMK$78) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void setBmk(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BMK$78);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BMK$78);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void xsetBmk(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(BMK$78);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(BMK$78);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
    public void unsetBmk() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BMK$78);
        }
    }
}
