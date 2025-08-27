package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLegendPos;
import org.openxmlformats.schemas.drawingml.x2006.chart.STLegendPos;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/CTLegendPosImpl.class */
public class CTLegendPosImpl extends XmlComplexContentImpl implements CTLegendPos {
    private static final QName VAL$0 = new QName("", "val");

    public CTLegendPosImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLegendPos
    public STLegendPos.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(VAL$0);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STLegendPos.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLegendPos
    public STLegendPos xgetVal() {
        STLegendPos sTLegendPos;
        synchronized (monitor()) {
            check_orphaned();
            STLegendPos sTLegendPos2 = (STLegendPos) get_store().find_attribute_user(VAL$0);
            if (sTLegendPos2 == null) {
                sTLegendPos2 = (STLegendPos) get_default_attribute_value(VAL$0);
            }
            sTLegendPos = sTLegendPos2;
        }
        return sTLegendPos;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLegendPos
    public boolean isSetVal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VAL$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLegendPos
    public void setVal(STLegendPos.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLegendPos
    public void xsetVal(STLegendPos sTLegendPos) {
        synchronized (monitor()) {
            check_orphaned();
            STLegendPos sTLegendPos2 = (STLegendPos) get_store().find_attribute_user(VAL$0);
            if (sTLegendPos2 == null) {
                sTLegendPos2 = (STLegendPos) get_store().add_attribute_user(VAL$0);
            }
            sTLegendPos2.set(sTLegendPos);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLegendPos
    public void unsetVal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VAL$0);
        }
    }
}
