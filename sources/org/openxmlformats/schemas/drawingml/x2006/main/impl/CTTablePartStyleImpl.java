package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleTextStyle;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTTablePartStyleImpl.class */
public class CTTablePartStyleImpl extends XmlComplexContentImpl implements CTTablePartStyle {
    private static final QName TCTXSTYLE$0 = new QName(XSSFRelation.NS_DRAWINGML, "tcTxStyle");
    private static final QName TCSTYLE$2 = new QName(XSSFRelation.NS_DRAWINGML, "tcStyle");

    public CTTablePartStyleImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle
    public CTTableStyleTextStyle getTcTxStyle() {
        synchronized (monitor()) {
            check_orphaned();
            CTTableStyleTextStyle cTTableStyleTextStyleFind_element_user = get_store().find_element_user(TCTXSTYLE$0, 0);
            if (cTTableStyleTextStyleFind_element_user == null) {
                return null;
            }
            return cTTableStyleTextStyleFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle
    public boolean isSetTcTxStyle() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(TCTXSTYLE$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle
    public void setTcTxStyle(CTTableStyleTextStyle cTTableStyleTextStyle) {
        synchronized (monitor()) {
            check_orphaned();
            CTTableStyleTextStyle cTTableStyleTextStyleFind_element_user = get_store().find_element_user(TCTXSTYLE$0, 0);
            if (cTTableStyleTextStyleFind_element_user == null) {
                cTTableStyleTextStyleFind_element_user = (CTTableStyleTextStyle) get_store().add_element_user(TCTXSTYLE$0);
            }
            cTTableStyleTextStyleFind_element_user.set(cTTableStyleTextStyle);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle
    public CTTableStyleTextStyle addNewTcTxStyle() {
        CTTableStyleTextStyle cTTableStyleTextStyleAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTTableStyleTextStyleAdd_element_user = get_store().add_element_user(TCTXSTYLE$0);
        }
        return cTTableStyleTextStyleAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle
    public void unsetTcTxStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TCTXSTYLE$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle
    public CTTableStyleCellStyle getTcStyle() {
        synchronized (monitor()) {
            check_orphaned();
            CTTableStyleCellStyle cTTableStyleCellStyle = (CTTableStyleCellStyle) get_store().find_element_user(TCSTYLE$2, 0);
            if (cTTableStyleCellStyle == null) {
                return null;
            }
            return cTTableStyleCellStyle;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle
    public boolean isSetTcStyle() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(TCSTYLE$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle
    public void setTcStyle(CTTableStyleCellStyle cTTableStyleCellStyle) {
        synchronized (monitor()) {
            check_orphaned();
            CTTableStyleCellStyle cTTableStyleCellStyle2 = (CTTableStyleCellStyle) get_store().find_element_user(TCSTYLE$2, 0);
            if (cTTableStyleCellStyle2 == null) {
                cTTableStyleCellStyle2 = (CTTableStyleCellStyle) get_store().add_element_user(TCSTYLE$2);
            }
            cTTableStyleCellStyle2.set(cTTableStyleCellStyle);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle
    public CTTableStyleCellStyle addNewTcStyle() {
        CTTableStyleCellStyle cTTableStyleCellStyle;
        synchronized (monitor()) {
            check_orphaned();
            cTTableStyleCellStyle = (CTTableStyleCellStyle) get_store().add_element_user(TCSTYLE$2);
        }
        return cTTableStyleCellStyle;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle
    public void unsetTcStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TCSTYLE$2, 0);
        }
    }
}
