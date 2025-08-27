package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTCrosses;
import org.openxmlformats.schemas.drawingml.x2006.chart.STCrosses;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/CTCrossesImpl.class */
public class CTCrossesImpl extends XmlComplexContentImpl implements CTCrosses {
    private static final QName VAL$0 = new QName("", "val");

    public CTCrossesImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTCrosses
    public STCrosses.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                return null;
            }
            return (STCrosses.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTCrosses
    public STCrosses xgetVal() {
        STCrosses sTCrosses;
        synchronized (monitor()) {
            check_orphaned();
            sTCrosses = (STCrosses) get_store().find_attribute_user(VAL$0);
        }
        return sTCrosses;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTCrosses
    public void setVal(STCrosses.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTCrosses
    public void xsetVal(STCrosses sTCrosses) {
        synchronized (monitor()) {
            check_orphaned();
            STCrosses sTCrosses2 = (STCrosses) get_store().find_attribute_user(VAL$0);
            if (sTCrosses2 == null) {
                sTCrosses2 = (STCrosses) get_store().add_attribute_user(VAL$0);
            }
            sTCrosses2.set(sTCrosses);
        }
    }
}
