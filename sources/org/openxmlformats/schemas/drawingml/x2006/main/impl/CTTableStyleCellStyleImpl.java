package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTableCellBorderStyle;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTTableStyleCellStyleImpl.class */
public class CTTableStyleCellStyleImpl extends XmlComplexContentImpl implements CTTableStyleCellStyle {
    private static final QName TCBDR$0 = new QName(XSSFRelation.NS_DRAWINGML, "tcBdr");
    private static final QName FILL$2 = new QName(XSSFRelation.NS_DRAWINGML, "fill");
    private static final QName FILLREF$4 = new QName(XSSFRelation.NS_DRAWINGML, "fillRef");
    private static final QName CELL3D$6 = new QName(XSSFRelation.NS_DRAWINGML, "cell3D");

    public CTTableStyleCellStyleImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public CTTableCellBorderStyle getTcBdr() {
        synchronized (monitor()) {
            check_orphaned();
            CTTableCellBorderStyle cTTableCellBorderStyleFind_element_user = get_store().find_element_user(TCBDR$0, 0);
            if (cTTableCellBorderStyleFind_element_user == null) {
                return null;
            }
            return cTTableCellBorderStyleFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public boolean isSetTcBdr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(TCBDR$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public void setTcBdr(CTTableCellBorderStyle cTTableCellBorderStyle) {
        synchronized (monitor()) {
            check_orphaned();
            CTTableCellBorderStyle cTTableCellBorderStyleFind_element_user = get_store().find_element_user(TCBDR$0, 0);
            if (cTTableCellBorderStyleFind_element_user == null) {
                cTTableCellBorderStyleFind_element_user = (CTTableCellBorderStyle) get_store().add_element_user(TCBDR$0);
            }
            cTTableCellBorderStyleFind_element_user.set(cTTableCellBorderStyle);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public CTTableCellBorderStyle addNewTcBdr() {
        CTTableCellBorderStyle cTTableCellBorderStyleAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTTableCellBorderStyleAdd_element_user = get_store().add_element_user(TCBDR$0);
        }
        return cTTableCellBorderStyleAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public void unsetTcBdr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TCBDR$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public CTFillProperties getFill() {
        synchronized (monitor()) {
            check_orphaned();
            CTFillProperties cTFillProperties = (CTFillProperties) get_store().find_element_user(FILL$2, 0);
            if (cTFillProperties == null) {
                return null;
            }
            return cTFillProperties;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public boolean isSetFill() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(FILL$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public void setFill(CTFillProperties cTFillProperties) {
        synchronized (monitor()) {
            check_orphaned();
            CTFillProperties cTFillProperties2 = (CTFillProperties) get_store().find_element_user(FILL$2, 0);
            if (cTFillProperties2 == null) {
                cTFillProperties2 = (CTFillProperties) get_store().add_element_user(FILL$2);
            }
            cTFillProperties2.set(cTFillProperties);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public CTFillProperties addNewFill() {
        CTFillProperties cTFillProperties;
        synchronized (monitor()) {
            check_orphaned();
            cTFillProperties = (CTFillProperties) get_store().add_element_user(FILL$2);
        }
        return cTFillProperties;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public void unsetFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(FILL$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public CTStyleMatrixReference getFillRef() {
        synchronized (monitor()) {
            check_orphaned();
            CTStyleMatrixReference cTStyleMatrixReference = (CTStyleMatrixReference) get_store().find_element_user(FILLREF$4, 0);
            if (cTStyleMatrixReference == null) {
                return null;
            }
            return cTStyleMatrixReference;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public boolean isSetFillRef() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(FILLREF$4) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public void setFillRef(CTStyleMatrixReference cTStyleMatrixReference) {
        synchronized (monitor()) {
            check_orphaned();
            CTStyleMatrixReference cTStyleMatrixReference2 = (CTStyleMatrixReference) get_store().find_element_user(FILLREF$4, 0);
            if (cTStyleMatrixReference2 == null) {
                cTStyleMatrixReference2 = (CTStyleMatrixReference) get_store().add_element_user(FILLREF$4);
            }
            cTStyleMatrixReference2.set(cTStyleMatrixReference);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public CTStyleMatrixReference addNewFillRef() {
        CTStyleMatrixReference cTStyleMatrixReference;
        synchronized (monitor()) {
            check_orphaned();
            cTStyleMatrixReference = (CTStyleMatrixReference) get_store().add_element_user(FILLREF$4);
        }
        return cTStyleMatrixReference;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public void unsetFillRef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(FILLREF$4, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public CTCell3D getCell3D() {
        synchronized (monitor()) {
            check_orphaned();
            CTCell3D cTCell3DFind_element_user = get_store().find_element_user(CELL3D$6, 0);
            if (cTCell3DFind_element_user == null) {
                return null;
            }
            return cTCell3DFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public boolean isSetCell3D() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(CELL3D$6) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public void setCell3D(CTCell3D cTCell3D) {
        synchronized (monitor()) {
            check_orphaned();
            CTCell3D cTCell3DFind_element_user = get_store().find_element_user(CELL3D$6, 0);
            if (cTCell3DFind_element_user == null) {
                cTCell3DFind_element_user = (CTCell3D) get_store().add_element_user(CELL3D$6);
            }
            cTCell3DFind_element_user.set(cTCell3D);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public CTCell3D addNewCell3D() {
        CTCell3D cTCell3DAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTCell3DAdd_element_user = get_store().add_element_user(CELL3D$6);
        }
        return cTCell3DAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
    public void unsetCell3D() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CELL3D$6, 0);
        }
    }
}
