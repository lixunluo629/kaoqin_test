package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutTarget;
import org.openxmlformats.schemas.drawingml.x2006.chart.STLayoutTarget;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/CTLayoutTargetImpl.class */
public class CTLayoutTargetImpl extends XmlComplexContentImpl implements CTLayoutTarget {
    private static final QName VAL$0 = new QName("", "val");

    public CTLayoutTargetImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutTarget
    public STLayoutTarget.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(VAL$0);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STLayoutTarget.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutTarget
    public STLayoutTarget xgetVal() {
        STLayoutTarget sTLayoutTarget;
        synchronized (monitor()) {
            check_orphaned();
            STLayoutTarget sTLayoutTarget2 = (STLayoutTarget) get_store().find_attribute_user(VAL$0);
            if (sTLayoutTarget2 == null) {
                sTLayoutTarget2 = (STLayoutTarget) get_default_attribute_value(VAL$0);
            }
            sTLayoutTarget = sTLayoutTarget2;
        }
        return sTLayoutTarget;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutTarget
    public boolean isSetVal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VAL$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutTarget
    public void setVal(STLayoutTarget.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutTarget
    public void xsetVal(STLayoutTarget sTLayoutTarget) {
        synchronized (monitor()) {
            check_orphaned();
            STLayoutTarget sTLayoutTarget2 = (STLayoutTarget) get_store().find_attribute_user(VAL$0);
            if (sTLayoutTarget2 == null) {
                sTLayoutTarget2 = (STLayoutTarget) get_store().add_attribute_user(VAL$0);
            }
            sTLayoutTarget2.set(sTLayoutTarget);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutTarget
    public void unsetVal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VAL$0);
        }
    }
}
