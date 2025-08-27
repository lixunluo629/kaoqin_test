package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLang;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTLangImpl.class */
public class CTLangImpl extends XmlComplexContentImpl implements CTLang {
    private static final QName VAL$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "val");

    public CTLangImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang
    public Object getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getObjectValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang
    public STLang xgetVal() {
        STLang sTLang;
        synchronized (monitor()) {
            check_orphaned();
            sTLang = (STLang) get_store().find_attribute_user(VAL$0);
        }
        return sTLang;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang
    public void setVal(Object obj) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setObjectValue(obj);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang
    public void xsetVal(STLang sTLang) {
        synchronized (monitor()) {
            check_orphaned();
            STLang sTLang2 = (STLang) get_store().find_attribute_user(VAL$0);
            if (sTLang2 == null) {
                sTLang2 = (STLang) get_store().add_attribute_user(VAL$0);
            }
            sTLang2.set(sTLang);
        }
    }
}
