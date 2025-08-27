package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark;
import org.openxmlformats.schemas.drawingml.x2006.chart.STTickMark;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/CTTickMarkImpl.class */
public class CTTickMarkImpl extends XmlComplexContentImpl implements CTTickMark {
    private static final QName VAL$0 = new QName("", "val");

    public CTTickMarkImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark
    public STTickMark.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(VAL$0);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STTickMark.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark
    public STTickMark xgetVal() {
        STTickMark sTTickMark;
        synchronized (monitor()) {
            check_orphaned();
            STTickMark sTTickMark2 = (STTickMark) get_store().find_attribute_user(VAL$0);
            if (sTTickMark2 == null) {
                sTTickMark2 = (STTickMark) get_default_attribute_value(VAL$0);
            }
            sTTickMark = sTTickMark2;
        }
        return sTTickMark;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark
    public boolean isSetVal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VAL$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark
    public void setVal(STTickMark.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark
    public void xsetVal(STTickMark sTTickMark) {
        synchronized (monitor()) {
            check_orphaned();
            STTickMark sTTickMark2 = (STTickMark) get_store().find_attribute_user(VAL$0);
            if (sTTickMark2 == null) {
                sTTickMark2 = (STTickMark) get_store().add_attribute_user(VAL$0);
            }
            sTTickMark2.set(sTTickMark);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark
    public void unsetVal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VAL$0);
        }
    }
}
