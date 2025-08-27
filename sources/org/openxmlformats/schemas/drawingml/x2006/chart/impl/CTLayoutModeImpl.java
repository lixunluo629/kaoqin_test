package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode;
import org.openxmlformats.schemas.drawingml.x2006.chart.STLayoutMode;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/CTLayoutModeImpl.class */
public class CTLayoutModeImpl extends XmlComplexContentImpl implements CTLayoutMode {
    private static final QName VAL$0 = new QName("", "val");

    public CTLayoutModeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode
    public STLayoutMode.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(VAL$0);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STLayoutMode.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode
    public STLayoutMode xgetVal() {
        STLayoutMode sTLayoutMode;
        synchronized (monitor()) {
            check_orphaned();
            STLayoutMode sTLayoutMode2 = (STLayoutMode) get_store().find_attribute_user(VAL$0);
            if (sTLayoutMode2 == null) {
                sTLayoutMode2 = (STLayoutMode) get_default_attribute_value(VAL$0);
            }
            sTLayoutMode = sTLayoutMode2;
        }
        return sTLayoutMode;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode
    public boolean isSetVal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VAL$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode
    public void setVal(STLayoutMode.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode
    public void xsetVal(STLayoutMode sTLayoutMode) {
        synchronized (monitor()) {
            check_orphaned();
            STLayoutMode sTLayoutMode2 = (STLayoutMode) get_store().find_attribute_user(VAL$0);
            if (sTLayoutMode2 == null) {
                sTLayoutMode2 = (STLayoutMode) get_store().add_attribute_user(VAL$0);
            }
            sTLayoutMode2.set(sTLayoutMode);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode
    public void unsetVal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VAL$0);
        }
    }
}
