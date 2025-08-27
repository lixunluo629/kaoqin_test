package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLogBase;
import org.openxmlformats.schemas.drawingml.x2006.chart.STLogBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/CTLogBaseImpl.class */
public class CTLogBaseImpl extends XmlComplexContentImpl implements CTLogBase {
    private static final QName VAL$0 = new QName("", "val");

    public CTLogBaseImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLogBase
    public double getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                return 0.0d;
            }
            return simpleValue.getDoubleValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLogBase
    public STLogBase xgetVal() {
        STLogBase sTLogBase;
        synchronized (monitor()) {
            check_orphaned();
            sTLogBase = (STLogBase) get_store().find_attribute_user(VAL$0);
        }
        return sTLogBase;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLogBase
    public void setVal(double d) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setDoubleValue(d);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLogBase
    public void xsetVal(STLogBase sTLogBase) {
        synchronized (monitor()) {
            check_orphaned();
            STLogBase sTLogBase2 = (STLogBase) get_store().find_attribute_user(VAL$0);
            if (sTLogBase2 == null) {
                sTLogBase2 = (STLogBase) get_store().add_attribute_user(VAL$0);
            }
            sTLogBase2.set(sTLogBase);
        }
    }
}
