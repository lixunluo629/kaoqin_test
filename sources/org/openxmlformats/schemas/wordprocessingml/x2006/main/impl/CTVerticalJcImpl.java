package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTVerticalJcImpl.class */
public class CTVerticalJcImpl extends XmlComplexContentImpl implements CTVerticalJc {
    private static final QName VAL$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "val");

    public CTVerticalJcImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc
    public STVerticalJc.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                return null;
            }
            return (STVerticalJc.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc
    public STVerticalJc xgetVal() {
        STVerticalJc sTVerticalJc;
        synchronized (monitor()) {
            check_orphaned();
            sTVerticalJc = (STVerticalJc) get_store().find_attribute_user(VAL$0);
        }
        return sTVerticalJc;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc
    public void setVal(STVerticalJc.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc
    public void xsetVal(STVerticalJc sTVerticalJc) {
        synchronized (monitor()) {
            check_orphaned();
            STVerticalJc sTVerticalJc2 = (STVerticalJc) get_store().find_attribute_user(VAL$0);
            if (sTVerticalJc2 == null) {
                sTVerticalJc2 = (STVerticalJc) get_store().add_attribute_user(VAL$0);
            }
            sTVerticalJc2.set(sTVerticalJc);
        }
    }
}
