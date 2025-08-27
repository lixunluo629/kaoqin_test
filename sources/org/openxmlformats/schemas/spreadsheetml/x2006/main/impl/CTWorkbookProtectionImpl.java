package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/CTWorkbookProtectionImpl.class */
public class CTWorkbookProtectionImpl extends XmlComplexContentImpl implements CTWorkbookProtection {
    private static final QName WORKBOOKPASSWORD$0 = new QName("", "workbookPassword");
    private static final QName REVISIONSPASSWORD$2 = new QName("", "revisionsPassword");
    private static final QName LOCKSTRUCTURE$4 = new QName("", "lockStructure");
    private static final QName LOCKWINDOWS$6 = new QName("", "lockWindows");
    private static final QName LOCKREVISION$8 = new QName("", "lockRevision");

    public CTWorkbookProtectionImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public byte[] getWorkbookPassword() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(WORKBOOKPASSWORD$0);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getByteArrayValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public STUnsignedShortHex xgetWorkbookPassword() {
        STUnsignedShortHex sTUnsignedShortHex;
        synchronized (monitor()) {
            check_orphaned();
            sTUnsignedShortHex = (STUnsignedShortHex) get_store().find_attribute_user(WORKBOOKPASSWORD$0);
        }
        return sTUnsignedShortHex;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public boolean isSetWorkbookPassword() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(WORKBOOKPASSWORD$0) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public void setWorkbookPassword(byte[] bArr) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(WORKBOOKPASSWORD$0);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(WORKBOOKPASSWORD$0);
            }
            simpleValue.setByteArrayValue(bArr);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public void xsetWorkbookPassword(STUnsignedShortHex sTUnsignedShortHex) {
        synchronized (monitor()) {
            check_orphaned();
            STUnsignedShortHex sTUnsignedShortHex2 = (STUnsignedShortHex) get_store().find_attribute_user(WORKBOOKPASSWORD$0);
            if (sTUnsignedShortHex2 == null) {
                sTUnsignedShortHex2 = (STUnsignedShortHex) get_store().add_attribute_user(WORKBOOKPASSWORD$0);
            }
            sTUnsignedShortHex2.set(sTUnsignedShortHex);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public void unsetWorkbookPassword() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(WORKBOOKPASSWORD$0);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public byte[] getRevisionsPassword() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(REVISIONSPASSWORD$2);
            if (simpleValue == null) {
                return null;
            }
            return simpleValue.getByteArrayValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public STUnsignedShortHex xgetRevisionsPassword() {
        STUnsignedShortHex sTUnsignedShortHex;
        synchronized (monitor()) {
            check_orphaned();
            sTUnsignedShortHex = (STUnsignedShortHex) get_store().find_attribute_user(REVISIONSPASSWORD$2);
        }
        return sTUnsignedShortHex;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public boolean isSetRevisionsPassword() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(REVISIONSPASSWORD$2) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public void setRevisionsPassword(byte[] bArr) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(REVISIONSPASSWORD$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(REVISIONSPASSWORD$2);
            }
            simpleValue.setByteArrayValue(bArr);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public void xsetRevisionsPassword(STUnsignedShortHex sTUnsignedShortHex) {
        synchronized (monitor()) {
            check_orphaned();
            STUnsignedShortHex sTUnsignedShortHex2 = (STUnsignedShortHex) get_store().find_attribute_user(REVISIONSPASSWORD$2);
            if (sTUnsignedShortHex2 == null) {
                sTUnsignedShortHex2 = (STUnsignedShortHex) get_store().add_attribute_user(REVISIONSPASSWORD$2);
            }
            sTUnsignedShortHex2.set(sTUnsignedShortHex);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public void unsetRevisionsPassword() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(REVISIONSPASSWORD$2);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public boolean getLockStructure() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LOCKSTRUCTURE$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(LOCKSTRUCTURE$4);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public XmlBoolean xgetLockStructure() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(LOCKSTRUCTURE$4);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(LOCKSTRUCTURE$4);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public boolean isSetLockStructure() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(LOCKSTRUCTURE$4) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public void setLockStructure(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LOCKSTRUCTURE$4);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(LOCKSTRUCTURE$4);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public void xsetLockStructure(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(LOCKSTRUCTURE$4);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(LOCKSTRUCTURE$4);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public void unsetLockStructure() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(LOCKSTRUCTURE$4);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public boolean getLockWindows() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LOCKWINDOWS$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(LOCKWINDOWS$6);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public XmlBoolean xgetLockWindows() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(LOCKWINDOWS$6);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(LOCKWINDOWS$6);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public boolean isSetLockWindows() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(LOCKWINDOWS$6) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public void setLockWindows(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LOCKWINDOWS$6);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(LOCKWINDOWS$6);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public void xsetLockWindows(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(LOCKWINDOWS$6);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(LOCKWINDOWS$6);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public void unsetLockWindows() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(LOCKWINDOWS$6);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public boolean getLockRevision() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LOCKREVISION$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_default_attribute_value(LOCKREVISION$8);
            }
            if (simpleValue == null) {
                return false;
            }
            return simpleValue.getBooleanValue();
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public XmlBoolean xgetLockRevision() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(LOCKREVISION$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_default_attribute_value(LOCKREVISION$8);
            }
            xmlBoolean = xmlBoolean2;
        }
        return xmlBoolean;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public boolean isSetLockRevision() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(LOCKREVISION$8) != null;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public void setLockRevision(boolean z) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(LOCKREVISION$8);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(LOCKREVISION$8);
            }
            simpleValue.setBooleanValue(z);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public void xsetLockRevision(XmlBoolean xmlBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean xmlBoolean2 = (XmlBoolean) get_store().find_attribute_user(LOCKREVISION$8);
            if (xmlBoolean2 == null) {
                xmlBoolean2 = (XmlBoolean) get_store().add_attribute_user(LOCKREVISION$8);
            }
            xmlBoolean2.set(xmlBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
    public void unsetLockRevision() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(LOCKREVISION$8);
        }
    }
}
