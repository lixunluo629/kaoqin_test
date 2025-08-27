package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAxPos;
import org.openxmlformats.schemas.drawingml.x2006.chart.STAxPos;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/CTAxPosImpl.class */
public class CTAxPosImpl extends XmlComplexContentImpl implements CTAxPos {
    private static final QName VAL$0 = new QName("", "val");

    public CTAxPosImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTAxPos
    public STAxPos.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                return null;
            }
            return (STAxPos.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTAxPos
    public STAxPos xgetVal() {
        STAxPos sTAxPos;
        synchronized (monitor()) {
            check_orphaned();
            sTAxPos = (STAxPos) get_store().find_attribute_user(VAL$0);
        }
        return sTAxPos;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTAxPos
    public void setVal(STAxPos.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTAxPos
    public void xsetVal(STAxPos sTAxPos) {
        synchronized (monitor()) {
            check_orphaned();
            STAxPos sTAxPos2 = (STAxPos) get_store().find_attribute_user(VAL$0);
            if (sTAxPos2 == null) {
                sTAxPos2 = (STAxPos) get_store().add_attribute_user(VAL$0);
            }
            sTAxPos2.set(sTAxPos);
        }
    }
}
