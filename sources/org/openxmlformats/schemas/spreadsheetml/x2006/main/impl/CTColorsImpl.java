package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndexedColors;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMRUColors;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTColorsImpl.class */
public class CTColorsImpl extends XmlComplexContentImpl implements CTColors {
    private static final QName INDEXEDCOLORS$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "indexedColors");
    private static final QName MRUCOLORS$2 = new QName(XSSFRelation.NS_SPREADSHEETML, "mruColors");

    public CTColorsImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors
    public CTIndexedColors getIndexedColors() {
        synchronized (monitor()) {
            check_orphaned();
            CTIndexedColors cTIndexedColors = (CTIndexedColors) get_store().find_element_user(INDEXEDCOLORS$0, 0);
            if (cTIndexedColors == null) {
                return null;
            }
            return cTIndexedColors;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors
    public boolean isSetIndexedColors() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(INDEXEDCOLORS$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors
    public void setIndexedColors(CTIndexedColors cTIndexedColors) {
        synchronized (monitor()) {
            check_orphaned();
            CTIndexedColors cTIndexedColors2 = (CTIndexedColors) get_store().find_element_user(INDEXEDCOLORS$0, 0);
            if (cTIndexedColors2 == null) {
                cTIndexedColors2 = (CTIndexedColors) get_store().add_element_user(INDEXEDCOLORS$0);
            }
            cTIndexedColors2.set(cTIndexedColors);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors
    public CTIndexedColors addNewIndexedColors() {
        CTIndexedColors cTIndexedColors;
        synchronized (monitor()) {
            check_orphaned();
            cTIndexedColors = (CTIndexedColors) get_store().add_element_user(INDEXEDCOLORS$0);
        }
        return cTIndexedColors;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors
    public void unsetIndexedColors() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(INDEXEDCOLORS$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors
    public CTMRUColors getMruColors() {
        synchronized (monitor()) {
            check_orphaned();
            CTMRUColors cTMRUColorsFind_element_user = get_store().find_element_user(MRUCOLORS$2, 0);
            if (cTMRUColorsFind_element_user == null) {
                return null;
            }
            return cTMRUColorsFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors
    public boolean isSetMruColors() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(MRUCOLORS$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors
    public void setMruColors(CTMRUColors cTMRUColors) {
        synchronized (monitor()) {
            check_orphaned();
            CTMRUColors cTMRUColorsFind_element_user = get_store().find_element_user(MRUCOLORS$2, 0);
            if (cTMRUColorsFind_element_user == null) {
                cTMRUColorsFind_element_user = (CTMRUColors) get_store().add_element_user(MRUCOLORS$2);
            }
            cTMRUColorsFind_element_user.set(cTMRUColors);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors
    public CTMRUColors addNewMruColors() {
        CTMRUColors cTMRUColorsAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTMRUColorsAdd_element_user = get_store().add_element_user(MRUCOLORS$2);
        }
        return cTMRUColorsAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors
    public void unsetMruColors() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(MRUCOLORS$2, 0);
        }
    }
}
