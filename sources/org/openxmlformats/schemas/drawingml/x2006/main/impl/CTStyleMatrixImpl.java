package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFillStyleList;
import org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleList;
import org.openxmlformats.schemas.drawingml.x2006.main.CTFillStyleList;
import org.openxmlformats.schemas.drawingml.x2006.main.CTLineStyleList;
import org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTStyleMatrixImpl.class */
public class CTStyleMatrixImpl extends XmlComplexContentImpl implements CTStyleMatrix {
    private static final QName FILLSTYLELST$0 = new QName(XSSFRelation.NS_DRAWINGML, "fillStyleLst");
    private static final QName LNSTYLELST$2 = new QName(XSSFRelation.NS_DRAWINGML, "lnStyleLst");
    private static final QName EFFECTSTYLELST$4 = new QName(XSSFRelation.NS_DRAWINGML, "effectStyleLst");
    private static final QName BGFILLSTYLELST$6 = new QName(XSSFRelation.NS_DRAWINGML, "bgFillStyleLst");
    private static final QName NAME$8 = new QName("", "name");

    public CTStyleMatrixImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
    public CTFillStyleList getFillStyleLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTFillStyleList cTFillStyleList = (CTFillStyleList) get_store().find_element_user(FILLSTYLELST$0, 0);
            if (cTFillStyleList == null) {
                return null;
            }
            return cTFillStyleList;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
    public void setFillStyleLst(CTFillStyleList cTFillStyleList) {
        synchronized (monitor()) {
            check_orphaned();
            CTFillStyleList cTFillStyleList2 = (CTFillStyleList) get_store().find_element_user(FILLSTYLELST$0, 0);
            if (cTFillStyleList2 == null) {
                cTFillStyleList2 = (CTFillStyleList) get_store().add_element_user(FILLSTYLELST$0);
            }
            cTFillStyleList2.set(cTFillStyleList);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
    public CTFillStyleList addNewFillStyleLst() {
        CTFillStyleList cTFillStyleList;
        synchronized (monitor()) {
            check_orphaned();
            cTFillStyleList = (CTFillStyleList) get_store().add_element_user(FILLSTYLELST$0);
        }
        return cTFillStyleList;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
    public CTLineStyleList getLnStyleLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTLineStyleList cTLineStyleList = (CTLineStyleList) get_store().find_element_user(LNSTYLELST$2, 0);
            if (cTLineStyleList == null) {
                return null;
            }
            return cTLineStyleList;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
    public void setLnStyleLst(CTLineStyleList cTLineStyleList) {
        synchronized (monitor()) {
            check_orphaned();
            CTLineStyleList cTLineStyleList2 = (CTLineStyleList) get_store().find_element_user(LNSTYLELST$2, 0);
            if (cTLineStyleList2 == null) {
                cTLineStyleList2 = (CTLineStyleList) get_store().add_element_user(LNSTYLELST$2);
            }
            cTLineStyleList2.set(cTLineStyleList);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
    public CTLineStyleList addNewLnStyleLst() {
        CTLineStyleList cTLineStyleList;
        synchronized (monitor()) {
            check_orphaned();
            cTLineStyleList = (CTLineStyleList) get_store().add_element_user(LNSTYLELST$2);
        }
        return cTLineStyleList;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
    public CTEffectStyleList getEffectStyleLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTEffectStyleList cTEffectStyleList = (CTEffectStyleList) get_store().find_element_user(EFFECTSTYLELST$4, 0);
            if (cTEffectStyleList == null) {
                return null;
            }
            return cTEffectStyleList;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
    public void setEffectStyleLst(CTEffectStyleList cTEffectStyleList) {
        synchronized (monitor()) {
            check_orphaned();
            CTEffectStyleList cTEffectStyleList2 = (CTEffectStyleList) get_store().find_element_user(EFFECTSTYLELST$4, 0);
            if (cTEffectStyleList2 == null) {
                cTEffectStyleList2 = (CTEffectStyleList) get_store().add_element_user(EFFECTSTYLELST$4);
            }
            cTEffectStyleList2.set(cTEffectStyleList);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
    public CTEffectStyleList addNewEffectStyleLst() {
        CTEffectStyleList cTEffectStyleList;
        synchronized (monitor()) {
            check_orphaned();
            cTEffectStyleList = (CTEffectStyleList) get_store().add_element_user(EFFECTSTYLELST$4);
        }
        return cTEffectStyleList;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
    public CTBackgroundFillStyleList getBgFillStyleLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTBackgroundFillStyleList cTBackgroundFillStyleList = (CTBackgroundFillStyleList) get_store().find_element_user(BGFILLSTYLELST$6, 0);
            if (cTBackgroundFillStyleList == null) {
                return null;
            }
            return cTBackgroundFillStyleList;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
    public void setBgFillStyleLst(CTBackgroundFillStyleList cTBackgroundFillStyleList) {
        synchronized (monitor()) {
            check_orphaned();
            CTBackgroundFillStyleList cTBackgroundFillStyleList2 = (CTBackgroundFillStyleList) get_store().find_element_user(BGFILLSTYLELST$6, 0);
            if (cTBackgroundFillStyleList2 == null) {
                cTBackgroundFillStyleList2 = (CTBackgroundFillStyleList) get_store().add_element_user(BGFILLSTYLELST$6);
            }
            cTBackgroundFillStyleList2.set(cTBackgroundFillStyleList);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
    public CTBackgroundFillStyleList addNewBgFillStyleLst() {
        CTBackgroundFillStyleList cTBackgroundFillStyleList;
        synchronized (monitor()) {
            check_orphaned();
            cTBackgroundFillStyleList = (CTBackgroundFillStyleList) get_store().add_element_user(BGFILLSTYLELST$6);
        }
        return cTBackgroundFillStyleList;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
    public String getName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAME$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(NAME$8);
            }
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
    public XmlString xgetName() {
        XmlString xmlString;
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(NAME$8);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_default_attribute_value(NAME$8);
            }
            xmlString = xmlString2;
        }
        return xmlString;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
    public boolean isSetName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAME$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
    public void setName(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(NAME$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(NAME$8);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
    public void xsetName(XmlString xmlString) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString xmlString2 = (XmlString) get_store().find_attribute_user(NAME$8);
            if (xmlString2 == null) {
                xmlString2 = (XmlString) get_store().add_attribute_user(NAME$8);
            }
            xmlString2.set(xmlString);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAME$8);
        }
    }
}
