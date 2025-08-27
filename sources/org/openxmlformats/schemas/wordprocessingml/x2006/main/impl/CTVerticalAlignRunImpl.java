package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalAlignRun;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTVerticalAlignRunImpl.class */
public class CTVerticalAlignRunImpl extends XmlComplexContentImpl implements CTVerticalAlignRun {
    private static final QName VAL$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "val");

    public CTVerticalAlignRunImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun
    public STVerticalAlignRun.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                return null;
            }
            return (STVerticalAlignRun.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun
    public STVerticalAlignRun xgetVal() {
        STVerticalAlignRun sTVerticalAlignRun;
        synchronized (monitor()) {
            check_orphaned();
            sTVerticalAlignRun = (STVerticalAlignRun) get_store().find_attribute_user(VAL$0);
        }
        return sTVerticalAlignRun;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun
    public void setVal(STVerticalAlignRun.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun
    public void xsetVal(STVerticalAlignRun sTVerticalAlignRun) {
        synchronized (monitor()) {
            check_orphaned();
            STVerticalAlignRun sTVerticalAlignRun2 = (STVerticalAlignRun) get_store().find_attribute_user(VAL$0);
            if (sTVerticalAlignRun2 == null) {
                sTVerticalAlignRun2 = (STVerticalAlignRun) get_store().add_attribute_user(VAL$0);
            }
            sTVerticalAlignRun2.set(sTVerticalAlignRun);
        }
    }
}
