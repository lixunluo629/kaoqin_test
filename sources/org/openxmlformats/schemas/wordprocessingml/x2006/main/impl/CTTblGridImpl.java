package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridChange;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTTblGridImpl.class */
public class CTTblGridImpl extends CTTblGridBaseImpl implements CTTblGrid {
    private static final QName TBLGRIDCHANGE$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblGridChange");

    public CTTblGridImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid
    public CTTblGridChange getTblGridChange() {
        synchronized (monitor()) {
            check_orphaned();
            CTTblGridChange cTTblGridChangeFind_element_user = get_store().find_element_user(TBLGRIDCHANGE$0, 0);
            if (cTTblGridChangeFind_element_user == null) {
                return null;
            }
            return cTTblGridChangeFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid
    public boolean isSetTblGridChange() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(TBLGRIDCHANGE$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid
    public void setTblGridChange(CTTblGridChange cTTblGridChange) {
        synchronized (monitor()) {
            check_orphaned();
            CTTblGridChange cTTblGridChangeFind_element_user = get_store().find_element_user(TBLGRIDCHANGE$0, 0);
            if (cTTblGridChangeFind_element_user == null) {
                cTTblGridChangeFind_element_user = (CTTblGridChange) get_store().add_element_user(TBLGRIDCHANGE$0);
            }
            cTTblGridChangeFind_element_user.set(cTTblGridChange);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid
    public CTTblGridChange addNewTblGridChange() {
        CTTblGridChange cTTblGridChangeAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTTblGridChangeAdd_element_user = get_store().add_element_user(TBLGRIDCHANGE$0);
        }
        return cTTblGridChangeAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid
    public void unsetTblGridChange() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TBLGRIDCHANGE$0, 0);
        }
    }
}
