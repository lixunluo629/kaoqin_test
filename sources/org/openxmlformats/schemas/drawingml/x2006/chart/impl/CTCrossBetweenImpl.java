package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTCrossBetween;
import org.openxmlformats.schemas.drawingml.x2006.chart.STCrossBetween;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/CTCrossBetweenImpl.class */
public class CTCrossBetweenImpl extends XmlComplexContentImpl implements CTCrossBetween {
    private static final QName VAL$0 = new QName("", "val");

    public CTCrossBetweenImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTCrossBetween
    public STCrossBetween.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                return null;
            }
            return (STCrossBetween.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTCrossBetween
    public STCrossBetween xgetVal() {
        STCrossBetween sTCrossBetween;
        synchronized (monitor()) {
            check_orphaned();
            sTCrossBetween = (STCrossBetween) get_store().find_attribute_user(VAL$0);
        }
        return sTCrossBetween;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTCrossBetween
    public void setVal(STCrossBetween.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTCrossBetween
    public void xsetVal(STCrossBetween sTCrossBetween) {
        synchronized (monitor()) {
            check_orphaned();
            STCrossBetween sTCrossBetween2 = (STCrossBetween) get_store().find_attribute_user(VAL$0);
            if (sTCrossBetween2 == null) {
                sTCrossBetween2 = (STCrossBetween) get_store().add_attribute_user(VAL$0);
            }
            sTCrossBetween2.set(sTCrossBetween);
        }
    }
}
