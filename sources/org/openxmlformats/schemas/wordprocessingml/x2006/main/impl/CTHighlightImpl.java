package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTHighlightImpl.class */
public class CTHighlightImpl extends XmlComplexContentImpl implements CTHighlight {
    private static final QName VAL$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "val");

    public CTHighlightImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight
    public STHighlightColor.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                return null;
            }
            return (STHighlightColor.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight
    public STHighlightColor xgetVal() {
        STHighlightColor sTHighlightColor;
        synchronized (monitor()) {
            check_orphaned();
            sTHighlightColor = (STHighlightColor) get_store().find_attribute_user(VAL$0);
        }
        return sTHighlightColor;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight
    public void setVal(STHighlightColor.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight
    public void xsetVal(STHighlightColor sTHighlightColor) {
        synchronized (monitor()) {
            check_orphaned();
            STHighlightColor sTHighlightColor2 = (STHighlightColor) get_store().find_attribute_user(VAL$0);
            if (sTHighlightColor2 == null) {
                sTHighlightColor2 = (STHighlightColor) get_store().add_attribute_user(VAL$0);
            }
            sTHighlightColor2.set(sTHighlightColor);
        }
    }
}
