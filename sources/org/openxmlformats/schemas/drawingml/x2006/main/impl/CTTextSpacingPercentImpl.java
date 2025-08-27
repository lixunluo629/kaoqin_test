package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPercent;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextSpacingPercent;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTTextSpacingPercentImpl.class */
public class CTTextSpacingPercentImpl extends XmlComplexContentImpl implements CTTextSpacingPercent {
    private static final QName VAL$0 = new QName("", "val");

    public CTTextSpacingPercentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPercent
    public int getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                return 0;
            }
            return simpleValue.getIntValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPercent
    public STTextSpacingPercent xgetVal() {
        STTextSpacingPercent sTTextSpacingPercent;
        synchronized (monitor()) {
            check_orphaned();
            sTTextSpacingPercent = (STTextSpacingPercent) get_store().find_attribute_user(VAL$0);
        }
        return sTTextSpacingPercent;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPercent
    public void setVal(int i) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setIntValue(i);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPercent
    public void xsetVal(STTextSpacingPercent sTTextSpacingPercent) {
        synchronized (monitor()) {
            check_orphaned();
            STTextSpacingPercent sTTextSpacingPercent2 = (STTextSpacingPercent) get_store().find_attribute_user(VAL$0);
            if (sTTextSpacingPercent2 == null) {
                sTTextSpacingPercent2 = (STTextSpacingPercent) get_store().add_attribute_user(VAL$0);
            }
            sTTextSpacingPercent2.set(sTTextSpacingPercent);
        }
    }
}
