package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTTickLblPos;
import org.openxmlformats.schemas.drawingml.x2006.chart.STTickLblPos;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/CTTickLblPosImpl.class */
public class CTTickLblPosImpl extends XmlComplexContentImpl implements CTTickLblPos {
    private static final QName VAL$0 = new QName("", "val");

    public CTTickLblPosImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTTickLblPos
    public STTickLblPos.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(VAL$0);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STTickLblPos.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTTickLblPos
    public STTickLblPos xgetVal() {
        STTickLblPos sTTickLblPos;
        synchronized (monitor()) {
            check_orphaned();
            STTickLblPos sTTickLblPos2 = (STTickLblPos) get_store().find_attribute_user(VAL$0);
            if (sTTickLblPos2 == null) {
                sTTickLblPos2 = (STTickLblPos) get_default_attribute_value(VAL$0);
            }
            sTTickLblPos = sTTickLblPos2;
        }
        return sTTickLblPos;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTTickLblPos
    public boolean isSetVal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VAL$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTTickLblPos
    public void setVal(STTickLblPos.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTTickLblPos
    public void xsetVal(STTickLblPos sTTickLblPos) {
        synchronized (monitor()) {
            check_orphaned();
            STTickLblPos sTTickLblPos2 = (STTickLblPos) get_store().find_attribute_user(VAL$0);
            if (sTTickLblPos2 == null) {
                sTTickLblPos2 = (STTickLblPos) get_store().add_attribute_user(VAL$0);
            }
            sTTickLblPos2.set(sTTickLblPos);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTTickLblPos
    public void unsetVal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VAL$0);
        }
    }
}
