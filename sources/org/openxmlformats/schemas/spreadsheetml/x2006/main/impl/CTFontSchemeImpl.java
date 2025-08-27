package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STFontScheme;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTFontSchemeImpl.class */
public class CTFontSchemeImpl extends XmlComplexContentImpl implements CTFontScheme {
    private static final QName VAL$0 = new QName("", "val");

    public CTFontSchemeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme
    public STFontScheme.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                return null;
            }
            return (STFontScheme.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme
    public STFontScheme xgetVal() {
        STFontScheme sTFontScheme;
        synchronized (monitor()) {
            check_orphaned();
            sTFontScheme = (STFontScheme) get_store().find_attribute_user(VAL$0);
        }
        return sTFontScheme;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme
    public void setVal(STFontScheme.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme
    public void xsetVal(STFontScheme sTFontScheme) {
        synchronized (monitor()) {
            check_orphaned();
            STFontScheme sTFontScheme2 = (STFontScheme) get_store().find_attribute_user(VAL$0);
            if (sTFontScheme2 == null) {
                sTFontScheme2 = (STFontScheme) get_store().add_attribute_user(VAL$0);
            }
            sTFontScheme2.set(sTFontScheme);
        }
    }
}
