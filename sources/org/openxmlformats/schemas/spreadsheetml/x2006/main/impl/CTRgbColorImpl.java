package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedIntHex;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTRgbColorImpl.class */
public class CTRgbColorImpl extends XmlComplexContentImpl implements CTRgbColor {
    private static final QName RGB$0 = new QName("", "rgb");

    public CTRgbColorImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor
    public byte[] getRgb() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(RGB$0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getByteArrayValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor
    public STUnsignedIntHex xgetRgb() {
        STUnsignedIntHex sTUnsignedIntHex;
        synchronized (monitor()) {
            check_orphaned();
            sTUnsignedIntHex = (STUnsignedIntHex) get_store().find_attribute_user(RGB$0);
        }
        return sTUnsignedIntHex;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor
    public boolean isSetRgb() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(RGB$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor
    public void setRgb(byte[] bArr) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(RGB$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(RGB$0);
            }
            simpleValue.setByteArrayValue(bArr);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor
    public void xsetRgb(STUnsignedIntHex sTUnsignedIntHex) {
        synchronized (monitor()) {
            check_orphaned();
            STUnsignedIntHex sTUnsignedIntHex2 = (STUnsignedIntHex) get_store().find_attribute_user(RGB$0);
            if (sTUnsignedIntHex2 == null) {
                sTUnsignedIntHex2 = (STUnsignedIntHex) get_store().add_attribute_user(RGB$0);
            }
            sTUnsignedIntHex2.set(sTUnsignedIntHex);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor
    public void unsetRgb() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(RGB$0);
        }
    }
}
