package org.openxmlformats.schemas.xpackage.x2006.digitalSignature.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime;
import org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STFormat;
import org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STValue;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/xpackage/x2006/digitalSignature/impl/CTSignatureTimeImpl.class */
public class CTSignatureTimeImpl extends XmlComplexContentImpl implements CTSignatureTime {
    private static final QName FORMAT$0 = new QName("http://schemas.openxmlformats.org/package/2006/digital-signature", "Format");
    private static final QName VALUE$2 = new QName("http://schemas.openxmlformats.org/package/2006/digital-signature", "Value");

    public CTSignatureTimeImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime
    public String getFormat() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(FORMAT$0, 0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime
    public STFormat xgetFormat() {
        STFormat sTFormat;
        synchronized (monitor()) {
            check_orphaned();
            sTFormat = (STFormat) get_store().find_element_user(FORMAT$0, 0);
        }
        return sTFormat;
    }

    @Override // org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime
    public void setFormat(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(FORMAT$0, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(FORMAT$0);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime
    public void xsetFormat(STFormat sTFormat) {
        synchronized (monitor()) {
            check_orphaned();
            STFormat sTFormat2 = (STFormat) get_store().find_element_user(FORMAT$0, 0);
            if (sTFormat2 == null) {
                sTFormat2 = (STFormat) get_store().add_element_user(FORMAT$0);
            }
            sTFormat2.set(sTFormat);
        }
    }

    @Override // org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime
    public String getValue() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(VALUE$2, 0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getStringValue();
        }
    }

    @Override // org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime
    public STValue xgetValue() {
        STValue sTValue;
        synchronized (monitor()) {
            check_orphaned();
            sTValue = (STValue) get_store().find_element_user(VALUE$2, 0);
        }
        return sTValue;
    }

    @Override // org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime
    public void setValue(String str) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_element_user(VALUE$2, 0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_element_user(VALUE$2);
            }
            simpleValue.setStringValue(str);
        }
    }

    @Override // org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime
    public void xsetValue(STValue sTValue) {
        synchronized (monitor()) {
            check_orphaned();
            STValue sTValue2 = (STValue) get_store().find_element_user(VALUE$2, 0);
            if (sTValue2 == null) {
                sTValue2 = (STValue) get_store().add_element_user(VALUE$2);
            }
            sTValue2.set(sTValue);
        }
    }
}
