package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTNumFmtImpl.class */
public class CTNumFmtImpl extends XmlComplexContentImpl implements CTNumFmt {
    private static final QName VAL$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "val");

    public CTNumFmtImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt
    public STNumberFormat.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                return null;
            }
            return (STNumberFormat.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt
    public STNumberFormat xgetVal() {
        STNumberFormat sTNumberFormat;
        synchronized (monitor()) {
            check_orphaned();
            sTNumberFormat = (STNumberFormat) get_store().find_attribute_user(VAL$0);
        }
        return sTNumberFormat;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt
    public void setVal(STNumberFormat.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt
    public void xsetVal(STNumberFormat sTNumberFormat) {
        synchronized (monitor()) {
            check_orphaned();
            STNumberFormat sTNumberFormat2 = (STNumberFormat) get_store().find_attribute_user(VAL$0);
            if (sTNumberFormat2 == null) {
                sTNumberFormat2 = (STNumberFormat) get_store().add_attribute_user(VAL$0);
            }
            sTNumberFormat2.set(sTNumberFormat);
        }
    }
}
