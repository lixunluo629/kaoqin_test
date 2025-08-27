package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle;
import org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTShapeStyleImpl.class */
public class CTShapeStyleImpl extends XmlComplexContentImpl implements CTShapeStyle {
    private static final QName LNREF$0 = new QName(XSSFRelation.NS_DRAWINGML, "lnRef");
    private static final QName FILLREF$2 = new QName(XSSFRelation.NS_DRAWINGML, "fillRef");
    private static final QName EFFECTREF$4 = new QName(XSSFRelation.NS_DRAWINGML, "effectRef");
    private static final QName FONTREF$6 = new QName(XSSFRelation.NS_DRAWINGML, "fontRef");

    public CTShapeStyleImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle
    public CTStyleMatrixReference getLnRef() {
        synchronized (monitor()) {
            check_orphaned();
            CTStyleMatrixReference cTStyleMatrixReference = (CTStyleMatrixReference) get_store().find_element_user(LNREF$0, 0);
            if (cTStyleMatrixReference == null) {
                return null;
            }
            return cTStyleMatrixReference;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle
    public void setLnRef(CTStyleMatrixReference cTStyleMatrixReference) {
        synchronized (monitor()) {
            check_orphaned();
            CTStyleMatrixReference cTStyleMatrixReference2 = (CTStyleMatrixReference) get_store().find_element_user(LNREF$0, 0);
            if (cTStyleMatrixReference2 == null) {
                cTStyleMatrixReference2 = (CTStyleMatrixReference) get_store().add_element_user(LNREF$0);
            }
            cTStyleMatrixReference2.set(cTStyleMatrixReference);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle
    public CTStyleMatrixReference addNewLnRef() {
        CTStyleMatrixReference cTStyleMatrixReference;
        synchronized (monitor()) {
            check_orphaned();
            cTStyleMatrixReference = (CTStyleMatrixReference) get_store().add_element_user(LNREF$0);
        }
        return cTStyleMatrixReference;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle
    public CTStyleMatrixReference getFillRef() {
        synchronized (monitor()) {
            check_orphaned();
            CTStyleMatrixReference cTStyleMatrixReference = (CTStyleMatrixReference) get_store().find_element_user(FILLREF$2, 0);
            if (cTStyleMatrixReference == null) {
                return null;
            }
            return cTStyleMatrixReference;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle
    public void setFillRef(CTStyleMatrixReference cTStyleMatrixReference) {
        synchronized (monitor()) {
            check_orphaned();
            CTStyleMatrixReference cTStyleMatrixReference2 = (CTStyleMatrixReference) get_store().find_element_user(FILLREF$2, 0);
            if (cTStyleMatrixReference2 == null) {
                cTStyleMatrixReference2 = (CTStyleMatrixReference) get_store().add_element_user(FILLREF$2);
            }
            cTStyleMatrixReference2.set(cTStyleMatrixReference);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle
    public CTStyleMatrixReference addNewFillRef() {
        CTStyleMatrixReference cTStyleMatrixReference;
        synchronized (monitor()) {
            check_orphaned();
            cTStyleMatrixReference = (CTStyleMatrixReference) get_store().add_element_user(FILLREF$2);
        }
        return cTStyleMatrixReference;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle
    public CTStyleMatrixReference getEffectRef() {
        synchronized (monitor()) {
            check_orphaned();
            CTStyleMatrixReference cTStyleMatrixReference = (CTStyleMatrixReference) get_store().find_element_user(EFFECTREF$4, 0);
            if (cTStyleMatrixReference == null) {
                return null;
            }
            return cTStyleMatrixReference;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle
    public void setEffectRef(CTStyleMatrixReference cTStyleMatrixReference) {
        synchronized (monitor()) {
            check_orphaned();
            CTStyleMatrixReference cTStyleMatrixReference2 = (CTStyleMatrixReference) get_store().find_element_user(EFFECTREF$4, 0);
            if (cTStyleMatrixReference2 == null) {
                cTStyleMatrixReference2 = (CTStyleMatrixReference) get_store().add_element_user(EFFECTREF$4);
            }
            cTStyleMatrixReference2.set(cTStyleMatrixReference);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle
    public CTStyleMatrixReference addNewEffectRef() {
        CTStyleMatrixReference cTStyleMatrixReference;
        synchronized (monitor()) {
            check_orphaned();
            cTStyleMatrixReference = (CTStyleMatrixReference) get_store().add_element_user(EFFECTREF$4);
        }
        return cTStyleMatrixReference;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle
    public CTFontReference getFontRef() {
        synchronized (monitor()) {
            check_orphaned();
            CTFontReference cTFontReference = (CTFontReference) get_store().find_element_user(FONTREF$6, 0);
            if (cTFontReference == null) {
                return null;
            }
            return cTFontReference;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle
    public void setFontRef(CTFontReference cTFontReference) {
        synchronized (monitor()) {
            check_orphaned();
            CTFontReference cTFontReference2 = (CTFontReference) get_store().find_element_user(FONTREF$6, 0);
            if (cTFontReference2 == null) {
                cTFontReference2 = (CTFontReference) get_store().add_element_user(FONTREF$6);
            }
            cTFontReference2.set(cTFontReference);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle
    public CTFontReference addNewFontRef() {
        CTFontReference cTFontReference;
        synchronized (monitor()) {
            check_orphaned();
            cTFontReference = (CTFontReference) get_store().add_element_user(FONTREF$6);
        }
        return cTFontReference;
    }
}
