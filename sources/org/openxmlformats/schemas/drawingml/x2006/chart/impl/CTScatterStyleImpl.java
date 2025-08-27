package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterStyle;
import org.openxmlformats.schemas.drawingml.x2006.chart.STScatterStyle;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/CTScatterStyleImpl.class */
public class CTScatterStyleImpl extends XmlComplexContentImpl implements CTScatterStyle {
    private static final QName VAL$0 = new QName("", "val");

    public CTScatterStyleImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterStyle
    public STScatterStyle.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(VAL$0);
            }
            if (simpleValue == null) {
                return null;
            }
            return (STScatterStyle.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterStyle
    public STScatterStyle xgetVal() {
        STScatterStyle sTScatterStyle;
        synchronized (monitor()) {
            check_orphaned();
            STScatterStyle sTScatterStyle2 = (STScatterStyle) get_store().find_attribute_user(VAL$0);
            if (sTScatterStyle2 == null) {
                sTScatterStyle2 = (STScatterStyle) get_default_attribute_value(VAL$0);
            }
            sTScatterStyle = sTScatterStyle2;
        }
        return sTScatterStyle;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterStyle
    public boolean isSetVal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VAL$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterStyle
    public void setVal(STScatterStyle.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterStyle
    public void xsetVal(STScatterStyle sTScatterStyle) {
        synchronized (monitor()) {
            check_orphaned();
            STScatterStyle sTScatterStyle2 = (STScatterStyle) get_store().find_attribute_user(VAL$0);
            if (sTScatterStyle2 == null) {
                sTScatterStyle2 = (STScatterStyle) get_store().add_attribute_user(VAL$0);
            }
            sTScatterStyle2.set(sTScatterStyle);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterStyle
    public void unsetVal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VAL$0);
        }
    }
}
