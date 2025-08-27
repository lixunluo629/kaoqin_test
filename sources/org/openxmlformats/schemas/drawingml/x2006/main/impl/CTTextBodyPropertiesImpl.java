package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetTextShape;
import org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextNoAutofit;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextShapeAutofit;
import org.openxmlformats.schemas.drawingml.x2006.main.STAngle;
import org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32;
import org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate32;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextColumnCount;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextVertOverflowType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextWrappingType;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTTextBodyPropertiesImpl.class */
public class CTTextBodyPropertiesImpl extends XmlComplexContentImpl implements CTTextBodyProperties {
    private static final QName PRSTTXWARP$0 = new QName(XSSFRelation.NS_DRAWINGML, "prstTxWarp");
    private static final QName NOAUTOFIT$2 = new QName(XSSFRelation.NS_DRAWINGML, "noAutofit");
    private static final QName NORMAUTOFIT$4 = new QName(XSSFRelation.NS_DRAWINGML, "normAutofit");
    private static final QName SPAUTOFIT$6 = new QName(XSSFRelation.NS_DRAWINGML, "spAutoFit");
    private static final QName SCENE3D$8 = new QName(XSSFRelation.NS_DRAWINGML, "scene3d");
    private static final QName SP3D$10 = new QName(XSSFRelation.NS_DRAWINGML, "sp3d");
    private static final QName FLATTX$12 = new QName(XSSFRelation.NS_DRAWINGML, "flatTx");
    private static final QName EXTLST$14 = new QName(XSSFRelation.NS_DRAWINGML, "extLst");
    private static final QName ROT$16 = new QName("", "rot");
    private static final QName SPCFIRSTLASTPARA$18 = new QName("", "spcFirstLastPara");
    private static final QName VERTOVERFLOW$20 = new QName("", "vertOverflow");
    private static final QName HORZOVERFLOW$22 = new QName("", "horzOverflow");
    private static final QName VERT$24 = new QName("", "vert");
    private static final QName WRAP$26 = new QName("", "wrap");
    private static final QName LINS$28 = new QName("", "lIns");
    private static final QName TINS$30 = new QName("", "tIns");
    private static final QName RINS$32 = new QName("", "rIns");
    private static final QName BINS$34 = new QName("", "bIns");
    private static final QName NUMCOL$36 = new QName("", "numCol");
    private static final QName SPCCOL$38 = new QName("", "spcCol");
    private static final QName RTLCOL$40 = new QName("", "rtlCol");
    private static final QName FROMWORDART$42 = new QName("", "fromWordArt");
    private static final QName ANCHOR$44 = new QName("", "anchor");
    private static final QName ANCHORCTR$46 = new QName("", "anchorCtr");
    private static final QName FORCEAA$48 = new QName("", "forceAA");
    private static final QName UPRIGHT$50 = new QName("", "upright");
    private static final QName COMPATLNSPC$52 = new QName("", "compatLnSpc");

    public CTTextBodyPropertiesImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public CTPresetTextShape getPrstTxWarp() {
        synchronized (monitor()) {
            check_orphaned();
            CTPresetTextShape cTPresetTextShapeFind_element_user = get_store().find_element_user(PRSTTXWARP$0, 0);
            if (cTPresetTextShapeFind_element_user == null) {
                return null;
            }
            return cTPresetTextShapeFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetPrstTxWarp() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PRSTTXWARP$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setPrstTxWarp(CTPresetTextShape cTPresetTextShape) {
        synchronized (monitor()) {
            check_orphaned();
            CTPresetTextShape cTPresetTextShapeFind_element_user = get_store().find_element_user(PRSTTXWARP$0, 0);
            if (cTPresetTextShapeFind_element_user == null) {
                cTPresetTextShapeFind_element_user = (CTPresetTextShape) get_store().add_element_user(PRSTTXWARP$0);
            }
            cTPresetTextShapeFind_element_user.set(cTPresetTextShape);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public CTPresetTextShape addNewPrstTxWarp() {
        CTPresetTextShape cTPresetTextShapeAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTPresetTextShapeAdd_element_user = get_store().add_element_user(PRSTTXWARP$0);
        }
        return cTPresetTextShapeAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetPrstTxWarp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PRSTTXWARP$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public CTTextNoAutofit getNoAutofit() {
        synchronized (monitor()) {
            check_orphaned();
            CTTextNoAutofit cTTextNoAutofit = (CTTextNoAutofit) get_store().find_element_user(NOAUTOFIT$2, 0);
            if (cTTextNoAutofit == null) {
                return null;
            }
            return cTTextNoAutofit;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetNoAutofit() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(NOAUTOFIT$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setNoAutofit(CTTextNoAutofit cTTextNoAutofit) {
        synchronized (monitor()) {
            check_orphaned();
            CTTextNoAutofit cTTextNoAutofit2 = (CTTextNoAutofit) get_store().find_element_user(NOAUTOFIT$2, 0);
            if (cTTextNoAutofit2 == null) {
                cTTextNoAutofit2 = (CTTextNoAutofit) get_store().add_element_user(NOAUTOFIT$2);
            }
            cTTextNoAutofit2.set(cTTextNoAutofit);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public CTTextNoAutofit addNewNoAutofit() {
        CTTextNoAutofit cTTextNoAutofit;
        synchronized (monitor()) {
            check_orphaned();
            cTTextNoAutofit = (CTTextNoAutofit) get_store().add_element_user(NOAUTOFIT$2);
        }
        return cTTextNoAutofit;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetNoAutofit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(NOAUTOFIT$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public CTTextNormalAutofit getNormAutofit() {
        synchronized (monitor()) {
            check_orphaned();
            CTTextNormalAutofit cTTextNormalAutofit = (CTTextNormalAutofit) get_store().find_element_user(NORMAUTOFIT$4, 0);
            if (cTTextNormalAutofit == null) {
                return null;
            }
            return cTTextNormalAutofit;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetNormAutofit() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(NORMAUTOFIT$4) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setNormAutofit(CTTextNormalAutofit cTTextNormalAutofit) {
        synchronized (monitor()) {
            check_orphaned();
            CTTextNormalAutofit cTTextNormalAutofit2 = (CTTextNormalAutofit) get_store().find_element_user(NORMAUTOFIT$4, 0);
            if (cTTextNormalAutofit2 == null) {
                cTTextNormalAutofit2 = (CTTextNormalAutofit) get_store().add_element_user(NORMAUTOFIT$4);
            }
            cTTextNormalAutofit2.set(cTTextNormalAutofit);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public CTTextNormalAutofit addNewNormAutofit() {
        CTTextNormalAutofit cTTextNormalAutofit;
        synchronized (monitor()) {
            check_orphaned();
            cTTextNormalAutofit = (CTTextNormalAutofit) get_store().add_element_user(NORMAUTOFIT$4);
        }
        return cTTextNormalAutofit;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetNormAutofit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(NORMAUTOFIT$4, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public CTTextShapeAutofit getSpAutoFit() {
        synchronized (monitor()) {
            check_orphaned();
            CTTextShapeAutofit cTTextShapeAutofit = (CTTextShapeAutofit) get_store().find_element_user(SPAUTOFIT$6, 0);
            if (cTTextShapeAutofit == null) {
                return null;
            }
            return cTTextShapeAutofit;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetSpAutoFit() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SPAUTOFIT$6) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setSpAutoFit(CTTextShapeAutofit cTTextShapeAutofit) {
        synchronized (monitor()) {
            check_orphaned();
            CTTextShapeAutofit cTTextShapeAutofit2 = (CTTextShapeAutofit) get_store().find_element_user(SPAUTOFIT$6, 0);
            if (cTTextShapeAutofit2 == null) {
                cTTextShapeAutofit2 = (CTTextShapeAutofit) get_store().add_element_user(SPAUTOFIT$6);
            }
            cTTextShapeAutofit2.set(cTTextShapeAutofit);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public CTTextShapeAutofit addNewSpAutoFit() {
        CTTextShapeAutofit cTTextShapeAutofit;
        synchronized (monitor()) {
            check_orphaned();
            cTTextShapeAutofit = (CTTextShapeAutofit) get_store().add_element_user(SPAUTOFIT$6);
        }
        return cTTextShapeAutofit;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetSpAutoFit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SPAUTOFIT$6, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public CTScene3D getScene3D() {
        synchronized (monitor()) {
            check_orphaned();
            CTScene3D cTScene3DFind_element_user = get_store().find_element_user(SCENE3D$8, 0);
            if (cTScene3DFind_element_user == null) {
                return null;
            }
            return cTScene3DFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetScene3D() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SCENE3D$8) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setScene3D(CTScene3D cTScene3D) {
        synchronized (monitor()) {
            check_orphaned();
            CTScene3D cTScene3DFind_element_user = get_store().find_element_user(SCENE3D$8, 0);
            if (cTScene3DFind_element_user == null) {
                cTScene3DFind_element_user = (CTScene3D) get_store().add_element_user(SCENE3D$8);
            }
            cTScene3DFind_element_user.set(cTScene3D);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public CTScene3D addNewScene3D() {
        CTScene3D cTScene3DAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTScene3DAdd_element_user = get_store().add_element_user(SCENE3D$8);
        }
        return cTScene3DAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetScene3D() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SCENE3D$8, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public CTShape3D getSp3D() {
        synchronized (monitor()) {
            check_orphaned();
            CTShape3D cTShape3DFind_element_user = get_store().find_element_user(SP3D$10, 0);
            if (cTShape3DFind_element_user == null) {
                return null;
            }
            return cTShape3DFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetSp3D() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SP3D$10) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setSp3D(CTShape3D cTShape3D) {
        synchronized (monitor()) {
            check_orphaned();
            CTShape3D cTShape3DFind_element_user = get_store().find_element_user(SP3D$10, 0);
            if (cTShape3DFind_element_user == null) {
                cTShape3DFind_element_user = (CTShape3D) get_store().add_element_user(SP3D$10);
            }
            cTShape3DFind_element_user.set(cTShape3D);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public CTShape3D addNewSp3D() {
        CTShape3D cTShape3DAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTShape3DAdd_element_user = get_store().add_element_user(SP3D$10);
        }
        return cTShape3DAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetSp3D() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SP3D$10, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public CTFlatText getFlatTx() {
        synchronized (monitor()) {
            check_orphaned();
            CTFlatText cTFlatTextFind_element_user = get_store().find_element_user(FLATTX$12, 0);
            if (cTFlatTextFind_element_user == null) {
                return null;
            }
            return cTFlatTextFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetFlatTx() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(FLATTX$12) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setFlatTx(CTFlatText cTFlatText) {
        synchronized (monitor()) {
            check_orphaned();
            CTFlatText cTFlatTextFind_element_user = get_store().find_element_user(FLATTX$12, 0);
            if (cTFlatTextFind_element_user == null) {
                cTFlatTextFind_element_user = (CTFlatText) get_store().add_element_user(FLATTX$12);
            }
            cTFlatTextFind_element_user.set(cTFlatText);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public CTFlatText addNewFlatTx() {
        CTFlatText cTFlatTextAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTFlatTextAdd_element_user = get_store().add_element_user(FLATTX$12);
        }
        return cTFlatTextAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetFlatTx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(FLATTX$12, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTOfficeArtExtensionList cTOfficeArtExtensionList = (CTOfficeArtExtensionList) get_store().find_element_user(EXTLST$14, 0);
            if (cTOfficeArtExtensionList == null) {
                return null;
            }
            return cTOfficeArtExtensionList;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$14) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTOfficeArtExtensionList cTOfficeArtExtensionList2 = (CTOfficeArtExtensionList) get_store().find_element_user(EXTLST$14, 0);
            if (cTOfficeArtExtensionList2 == null) {
                cTOfficeArtExtensionList2 = (CTOfficeArtExtensionList) get_store().add_element_user(EXTLST$14);
            }
            cTOfficeArtExtensionList2.set(cTOfficeArtExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public CTOfficeArtExtensionList addNewExtLst() {
        CTOfficeArtExtensionList cTOfficeArtExtensionList;
        synchronized (monitor()) {
            check_orphaned();
            cTOfficeArtExtensionList = (CTOfficeArtExtensionList) get_store().add_element_user(EXTLST$14);
        }
        return cTOfficeArtExtensionList;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$14, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public int getRot() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ROT$16);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public STAngle xgetRot() {
        STAngle sTAngle;
        synchronized (monitor()) {
            check_orphaned();
            sTAngle = (STAngle) get_store().find_attribute_user(ROT$16);
        }
        return sTAngle;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetRot() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ROT$16) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setRot(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ROT$16);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ROT$16);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetRot(STAngle sTAngle) {
        synchronized (monitor()) {
            check_orphaned();
            STAngle sTAngle2 = (STAngle) get_store().find_attribute_user(ROT$16);
            if (sTAngle2 == null) {
                sTAngle2 = (STAngle) get_store().add_attribute_user(ROT$16);
            }
            sTAngle2.set(sTAngle);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetRot() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ROT$16);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean getSpcFirstLastPara() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SPCFIRSTLASTPARA$18);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public XmlBoolean xgetSpcFirstLastPara() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(SPCFIRSTLASTPARA$18);
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetSpcFirstLastPara() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SPCFIRSTLASTPARA$18) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setSpcFirstLastPara(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SPCFIRSTLASTPARA$18);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SPCFIRSTLASTPARA$18);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetSpcFirstLastPara(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(SPCFIRSTLASTPARA$18);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(SPCFIRSTLASTPARA$18);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetSpcFirstLastPara() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SPCFIRSTLASTPARA$18);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public STTextVertOverflowType.Enum getVertOverflow() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VERTOVERFLOW$20);
            if (simpleValue == null) {
                return null;
            }
            return (STTextVertOverflowType.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public STTextVertOverflowType xgetVertOverflow() {
        STTextVertOverflowType sTTextVertOverflowType;
        synchronized (monitor()) {
            check_orphaned();
            sTTextVertOverflowType = (STTextVertOverflowType) get_store().find_attribute_user(VERTOVERFLOW$20);
        }
        return sTTextVertOverflowType;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetVertOverflow() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VERTOVERFLOW$20) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setVertOverflow(STTextVertOverflowType.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VERTOVERFLOW$20);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VERTOVERFLOW$20);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetVertOverflow(STTextVertOverflowType sTTextVertOverflowType) {
        synchronized (monitor()) {
            check_orphaned();
            STTextVertOverflowType sTTextVertOverflowType2 = (STTextVertOverflowType) get_store().find_attribute_user(VERTOVERFLOW$20);
            if (sTTextVertOverflowType2 == null) {
                sTTextVertOverflowType2 = (STTextVertOverflowType) get_store().add_attribute_user(VERTOVERFLOW$20);
            }
            sTTextVertOverflowType2.set(sTTextVertOverflowType);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetVertOverflow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VERTOVERFLOW$20);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public STTextHorzOverflowType.Enum getHorzOverflow() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HORZOVERFLOW$22);
            if (simpleValue == null) {
                return null;
            }
            return (STTextHorzOverflowType.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public STTextHorzOverflowType xgetHorzOverflow() {
        STTextHorzOverflowType sTTextHorzOverflowType;
        synchronized (monitor()) {
            check_orphaned();
            sTTextHorzOverflowType = (STTextHorzOverflowType) get_store().find_attribute_user(HORZOVERFLOW$22);
        }
        return sTTextHorzOverflowType;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetHorzOverflow() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(HORZOVERFLOW$22) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setHorzOverflow(STTextHorzOverflowType.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(HORZOVERFLOW$22);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(HORZOVERFLOW$22);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetHorzOverflow(STTextHorzOverflowType sTTextHorzOverflowType) {
        synchronized (monitor()) {
            check_orphaned();
            STTextHorzOverflowType sTTextHorzOverflowType2 = (STTextHorzOverflowType) get_store().find_attribute_user(HORZOVERFLOW$22);
            if (sTTextHorzOverflowType2 == null) {
                sTTextHorzOverflowType2 = (STTextHorzOverflowType) get_store().add_attribute_user(HORZOVERFLOW$22);
            }
            sTTextHorzOverflowType2.set(sTTextHorzOverflowType);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetHorzOverflow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(HORZOVERFLOW$22);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public STTextVerticalType.Enum getVert() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VERT$24);
            if (simpleValue == null) {
                return null;
            }
            return (STTextVerticalType.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public STTextVerticalType xgetVert() {
        STTextVerticalType sTTextVerticalType;
        synchronized (monitor()) {
            check_orphaned();
            sTTextVerticalType = (STTextVerticalType) get_store().find_attribute_user(VERT$24);
        }
        return sTTextVerticalType;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetVert() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VERT$24) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setVert(STTextVerticalType.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VERT$24);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VERT$24);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetVert(STTextVerticalType sTTextVerticalType) {
        synchronized (monitor()) {
            check_orphaned();
            STTextVerticalType sTTextVerticalType2 = (STTextVerticalType) get_store().find_attribute_user(VERT$24);
            if (sTTextVerticalType2 == null) {
                sTTextVerticalType2 = (STTextVerticalType) get_store().add_attribute_user(VERT$24);
            }
            sTTextVerticalType2.set(sTTextVerticalType);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetVert() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VERT$24);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public STTextWrappingType.Enum getWrap() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(WRAP$26);
            if (simpleValue == null) {
                return null;
            }
            return (STTextWrappingType.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public STTextWrappingType xgetWrap() {
        STTextWrappingType sTTextWrappingType;
        synchronized (monitor()) {
            check_orphaned();
            sTTextWrappingType = (STTextWrappingType) get_store().find_attribute_user(WRAP$26);
        }
        return sTTextWrappingType;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetWrap() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(WRAP$26) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setWrap(STTextWrappingType.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(WRAP$26);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(WRAP$26);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetWrap(STTextWrappingType sTTextWrappingType) {
        synchronized (monitor()) {
            check_orphaned();
            STTextWrappingType sTTextWrappingType2 = (STTextWrappingType) get_store().find_attribute_user(WRAP$26);
            if (sTTextWrappingType2 == null) {
                sTTextWrappingType2 = (STTextWrappingType) get_store().add_attribute_user(WRAP$26);
            }
            sTTextWrappingType2.set(sTTextWrappingType);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetWrap() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(WRAP$26);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public int getLIns() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LINS$28);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public STCoordinate32 xgetLIns() {
        STCoordinate32 sTCoordinate32;
        synchronized (monitor()) {
            check_orphaned();
            sTCoordinate32 = (STCoordinate32) get_store().find_attribute_user(LINS$28);
        }
        return sTCoordinate32;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetLIns() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(LINS$28) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setLIns(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LINS$28);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(LINS$28);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetLIns(STCoordinate32 sTCoordinate32) {
        synchronized (monitor()) {
            check_orphaned();
            STCoordinate32 sTCoordinate322 = (STCoordinate32) get_store().find_attribute_user(LINS$28);
            if (sTCoordinate322 == null) {
                sTCoordinate322 = (STCoordinate32) get_store().add_attribute_user(LINS$28);
            }
            sTCoordinate322.set(sTCoordinate32);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetLIns() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(LINS$28);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public int getTIns() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TINS$30);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public STCoordinate32 xgetTIns() {
        STCoordinate32 sTCoordinate32;
        synchronized (monitor()) {
            check_orphaned();
            sTCoordinate32 = (STCoordinate32) get_store().find_attribute_user(TINS$30);
        }
        return sTCoordinate32;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetTIns() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TINS$30) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setTIns(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(TINS$30);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(TINS$30);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetTIns(STCoordinate32 sTCoordinate32) {
        synchronized (monitor()) {
            check_orphaned();
            STCoordinate32 sTCoordinate322 = (STCoordinate32) get_store().find_attribute_user(TINS$30);
            if (sTCoordinate322 == null) {
                sTCoordinate322 = (STCoordinate32) get_store().add_attribute_user(TINS$30);
            }
            sTCoordinate322.set(sTCoordinate32);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetTIns() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TINS$30);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public int getRIns() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(RINS$32);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public STCoordinate32 xgetRIns() {
        STCoordinate32 sTCoordinate32;
        synchronized (monitor()) {
            check_orphaned();
            sTCoordinate32 = (STCoordinate32) get_store().find_attribute_user(RINS$32);
        }
        return sTCoordinate32;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetRIns() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(RINS$32) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setRIns(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(RINS$32);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(RINS$32);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetRIns(STCoordinate32 sTCoordinate32) {
        synchronized (monitor()) {
            check_orphaned();
            STCoordinate32 sTCoordinate322 = (STCoordinate32) get_store().find_attribute_user(RINS$32);
            if (sTCoordinate322 == null) {
                sTCoordinate322 = (STCoordinate32) get_store().add_attribute_user(RINS$32);
            }
            sTCoordinate322.set(sTCoordinate32);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetRIns() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(RINS$32);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public int getBIns() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BINS$34);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public STCoordinate32 xgetBIns() {
        STCoordinate32 sTCoordinate32;
        synchronized (monitor()) {
            check_orphaned();
            sTCoordinate32 = (STCoordinate32) get_store().find_attribute_user(BINS$34);
        }
        return sTCoordinate32;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetBIns() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BINS$34) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setBIns(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(BINS$34);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(BINS$34);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetBIns(STCoordinate32 sTCoordinate32) {
        synchronized (monitor()) {
            check_orphaned();
            STCoordinate32 sTCoordinate322 = (STCoordinate32) get_store().find_attribute_user(BINS$34);
            if (sTCoordinate322 == null) {
                sTCoordinate322 = (STCoordinate32) get_store().add_attribute_user(BINS$34);
            }
            sTCoordinate322.set(sTCoordinate32);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetBIns() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BINS$34);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public int getNumCol() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NUMCOL$36);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public STTextColumnCount xgetNumCol() {
        STTextColumnCount sTTextColumnCountFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTTextColumnCountFind_attribute_user = get_store().find_attribute_user(NUMCOL$36);
        }
        return sTTextColumnCountFind_attribute_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetNumCol() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NUMCOL$36) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setNumCol(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NUMCOL$36);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NUMCOL$36);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetNumCol(STTextColumnCount sTTextColumnCount) {
        synchronized (monitor()) {
            check_orphaned();
            STTextColumnCount sTTextColumnCountFind_attribute_user = get_store().find_attribute_user(NUMCOL$36);
            if (sTTextColumnCountFind_attribute_user == null) {
                sTTextColumnCountFind_attribute_user = (STTextColumnCount) get_store().add_attribute_user(NUMCOL$36);
            }
            sTTextColumnCountFind_attribute_user.set(sTTextColumnCount);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetNumCol() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NUMCOL$36);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public int getSpcCol() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SPCCOL$38);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public STPositiveCoordinate32 xgetSpcCol() {
        STPositiveCoordinate32 sTPositiveCoordinate32;
        synchronized (monitor()) {
            check_orphaned();
            sTPositiveCoordinate32 = (STPositiveCoordinate32) get_store().find_attribute_user(SPCCOL$38);
        }
        return sTPositiveCoordinate32;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetSpcCol() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SPCCOL$38) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setSpcCol(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(SPCCOL$38);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(SPCCOL$38);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetSpcCol(STPositiveCoordinate32 sTPositiveCoordinate32) {
        synchronized (monitor()) {
            check_orphaned();
            STPositiveCoordinate32 sTPositiveCoordinate322 = (STPositiveCoordinate32) get_store().find_attribute_user(SPCCOL$38);
            if (sTPositiveCoordinate322 == null) {
                sTPositiveCoordinate322 = (STPositiveCoordinate32) get_store().add_attribute_user(SPCCOL$38);
            }
            sTPositiveCoordinate322.set(sTPositiveCoordinate32);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetSpcCol() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SPCCOL$38);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean getRtlCol() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(RTLCOL$40);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public XmlBoolean xgetRtlCol() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(RTLCOL$40);
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetRtlCol() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(RTLCOL$40) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setRtlCol(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(RTLCOL$40);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(RTLCOL$40);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetRtlCol(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(RTLCOL$40);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(RTLCOL$40);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetRtlCol() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(RTLCOL$40);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean getFromWordArt() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FROMWORDART$42);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public XmlBoolean xgetFromWordArt() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(FROMWORDART$42);
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetFromWordArt() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FROMWORDART$42) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setFromWordArt(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FROMWORDART$42);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FROMWORDART$42);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetFromWordArt(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(FROMWORDART$42);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(FROMWORDART$42);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetFromWordArt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FROMWORDART$42);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public STTextAnchoringType.Enum getAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ANCHOR$44);
            if (simpleValue == null) {
                return null;
            }
            return (STTextAnchoringType.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public STTextAnchoringType xgetAnchor() {
        STTextAnchoringType sTTextAnchoringType;
        synchronized (monitor()) {
            check_orphaned();
            sTTextAnchoringType = (STTextAnchoringType) get_store().find_attribute_user(ANCHOR$44);
        }
        return sTTextAnchoringType;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetAnchor() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ANCHOR$44) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setAnchor(STTextAnchoringType.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ANCHOR$44);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ANCHOR$44);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetAnchor(STTextAnchoringType sTTextAnchoringType) {
        synchronized (monitor()) {
            check_orphaned();
            STTextAnchoringType sTTextAnchoringType2 = (STTextAnchoringType) get_store().find_attribute_user(ANCHOR$44);
            if (sTTextAnchoringType2 == null) {
                sTTextAnchoringType2 = (STTextAnchoringType) get_store().add_attribute_user(ANCHOR$44);
            }
            sTTextAnchoringType2.set(sTTextAnchoringType);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ANCHOR$44);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean getAnchorCtr() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ANCHORCTR$46);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public XmlBoolean xgetAnchorCtr() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(ANCHORCTR$46);
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetAnchorCtr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ANCHORCTR$46) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setAnchorCtr(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(ANCHORCTR$46);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(ANCHORCTR$46);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetAnchorCtr(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(ANCHORCTR$46);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(ANCHORCTR$46);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetAnchorCtr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ANCHORCTR$46);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean getForceAA() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FORCEAA$48);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public XmlBoolean xgetForceAA() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(FORCEAA$48);
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetForceAA() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FORCEAA$48) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setForceAA(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(FORCEAA$48);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(FORCEAA$48);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetForceAA(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(FORCEAA$48);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(FORCEAA$48);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetForceAA() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FORCEAA$48);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean getUpright() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(UPRIGHT$50);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(UPRIGHT$50);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public XmlBoolean xgetUpright() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(UPRIGHT$50);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(UPRIGHT$50);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetUpright() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(UPRIGHT$50) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setUpright(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(UPRIGHT$50);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(UPRIGHT$50);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetUpright(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(UPRIGHT$50);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(UPRIGHT$50);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetUpright() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(UPRIGHT$50);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean getCompatLnSpc() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COMPATLNSPC$52);
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public XmlBoolean xgetCompatLnSpc() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            xmlBoolean = (XmlBoolean) get_store().find_attribute_user(COMPATLNSPC$52);
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public boolean isSetCompatLnSpc() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(COMPATLNSPC$52) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void setCompatLnSpc(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(COMPATLNSPC$52);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(COMPATLNSPC$52);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void xsetCompatLnSpc(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(COMPATLNSPC$52);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(COMPATLNSPC$52);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
    public void unsetCompatLnSpc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(COMPATLNSPC$52);
        }
    }
}
