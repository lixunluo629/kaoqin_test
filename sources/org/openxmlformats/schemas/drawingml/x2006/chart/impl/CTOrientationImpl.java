package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation;
import org.openxmlformats.schemas.drawingml.x2006.chart.STOrientation;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/CTOrientationImpl.class */
public class CTOrientationImpl extends XmlComplexContentImpl implements CTOrientation {
    private static final QName VAL$0 = new QName("", "val");

    public CTOrientationImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation
    public STOrientation.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(VAL$0);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STOrientation.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation
    public STOrientation xgetVal() {
        STOrientation sTOrientation;
        synchronized (monitor()) {
            check_orphaned();
            STOrientation sTOrientation2 = (STOrientation) get_store().find_attribute_user(VAL$0);
            if (sTOrientation2 == null) {
                sTOrientation2 = (STOrientation) get_default_attribute_value(VAL$0);
            }
            sTOrientation = sTOrientation2;
        }
        return sTOrientation;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation
    public boolean isSetVal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VAL$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation
    public void setVal(STOrientation.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation
    public void xsetVal(STOrientation sTOrientation) {
        synchronized (monitor()) {
            check_orphaned();
            STOrientation sTOrientation2 = (STOrientation) get_store().find_attribute_user(VAL$0);
            if (sTOrientation2 == null) {
                sTOrientation2 = (STOrientation) get_store().add_attribute_user(VAL$0);
            }
            sTOrientation2.set(sTOrientation);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTOrientation
    public void unsetVal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VAL$0);
        }
    }
}
