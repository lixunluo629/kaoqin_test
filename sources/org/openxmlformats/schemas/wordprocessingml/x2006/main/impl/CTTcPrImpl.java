package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrChange;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTTcPrImpl.class */
public class CTTcPrImpl extends CTTcPrInnerImpl implements CTTcPr {
    private static final QName TCPRCHANGE$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tcPrChange");

    public CTTcPrImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr
    public CTTcPrChange getTcPrChange() {
        synchronized (monitor()) {
            check_orphaned();
            CTTcPrChange cTTcPrChangeFind_element_user = get_store().find_element_user(TCPRCHANGE$0, 0);
            if (cTTcPrChangeFind_element_user == null) {
                return null;
            }
            return cTTcPrChangeFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr
    public boolean isSetTcPrChange() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(TCPRCHANGE$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr
    public void setTcPrChange(CTTcPrChange cTTcPrChange) {
        synchronized (monitor()) {
            check_orphaned();
            CTTcPrChange cTTcPrChangeFind_element_user = get_store().find_element_user(TCPRCHANGE$0, 0);
            if (cTTcPrChangeFind_element_user == null) {
                cTTcPrChangeFind_element_user = (CTTcPrChange) get_store().add_element_user(TCPRCHANGE$0);
            }
            cTTcPrChangeFind_element_user.set(cTTcPrChange);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr
    public CTTcPrChange addNewTcPrChange() {
        CTTcPrChange cTTcPrChangeAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTTcPrChangeAdd_element_user = get_store().add_element_user(TCPRCHANGE$0);
        }
        return cTTcPrChangeAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr
    public void unsetTcPrChange() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TCPRCHANGE$0, 0);
        }
    }
}
