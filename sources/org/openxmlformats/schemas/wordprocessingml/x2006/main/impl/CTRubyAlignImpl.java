package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyAlign;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STRubyAlign;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STRubyAlign$Enum;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/CTRubyAlignImpl.class */
public class CTRubyAlignImpl extends XmlComplexContentImpl implements CTRubyAlign {
    private static final QName VAL$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "val");

    public CTRubyAlignImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyAlign
    public STRubyAlign$Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                return null;
            }
            return (STRubyAlign$Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyAlign
    public STRubyAlign xgetVal() {
        STRubyAlign sTRubyAlignFind_attribute_user;
        synchronized (monitor()) {
            check_orphaned();
            sTRubyAlignFind_attribute_user = get_store().find_attribute_user(VAL$0);
        }
        return sTRubyAlignFind_attribute_user;
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyAlign
    public void setVal(STRubyAlign$Enum sTRubyAlign$Enum) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(VAL$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(VAL$0);
            }
            simpleValue.setEnumValue(sTRubyAlign$Enum);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyAlign
    public void xsetVal(STRubyAlign sTRubyAlign) {
        synchronized (monitor()) {
            check_orphaned();
            STRubyAlign sTRubyAlignFind_attribute_user = get_store().find_attribute_user(VAL$0);
            if (sTRubyAlignFind_attribute_user == null) {
                sTRubyAlignFind_attribute_user = (STRubyAlign) get_store().add_attribute_user(VAL$0);
            }
            sTRubyAlignFind_attribute_user.set(sTRubyAlign);
        }
    }
}
