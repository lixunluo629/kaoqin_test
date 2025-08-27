package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnderlineValues;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTUnderlinePropertyImpl.class */
public class CTUnderlinePropertyImpl extends XmlComplexContentImpl implements CTUnderlineProperty {
    private static final QName VAL$0 = new QName("", "val");

    public CTUnderlinePropertyImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty
    public STUnderlineValues.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(VAL$0);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STUnderlineValues.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty
    public STUnderlineValues xgetVal() {
        STUnderlineValues sTUnderlineValues;
        synchronized (monitor()) {
            check_orphaned();
            STUnderlineValues sTUnderlineValues2 = (STUnderlineValues) get_store().find_attribute_user(VAL$0);
            if (sTUnderlineValues2 == null) {
                sTUnderlineValues2 = (STUnderlineValues) get_default_attribute_value(VAL$0);
            }
            sTUnderlineValues = sTUnderlineValues2;
        }
        return sTUnderlineValues;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty
    public boolean isSetVal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VAL$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty
    public void setVal(STUnderlineValues.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty
    public void xsetVal(STUnderlineValues sTUnderlineValues) {
        synchronized (monitor()) {
            check_orphaned();
            STUnderlineValues sTUnderlineValues2 = (STUnderlineValues) get_store().find_attribute_user(VAL$0);
            if (sTUnderlineValues2 == null) {
                sTUnderlineValues2 = (STUnderlineValues) get_store().add_attribute_user(VAL$0);
            }
            sTUnderlineValues2.set(sTUnderlineValues);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty
    public void unsetVal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VAL$0);
        }
    }
}
