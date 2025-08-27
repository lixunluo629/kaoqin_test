package org.apache.xmlbeans.impl.xb.ltgfmt.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.ltgfmt.Code;
import org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/ltgfmt/impl/FileDescImpl.class */
public class FileDescImpl extends XmlComplexContentImpl implements FileDesc {
    private static final long serialVersionUID = 1;
    private static final QName CODE$0 = new QName("http://www.bea.com/2003/05/xmlbean/ltgfmt", "code");
    private static final QName TSDIR$2 = new QName("", "tsDir");
    private static final QName FOLDER$4 = new QName("", "folder");
    private static final QName FILENAME$6 = new QName("", "fileName");
    private static final QName ROLE$8 = new QName("", "role");
    private static final QName VALIDITY$10 = new QName("", "validity");

    public FileDescImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public Code getCode() {
        synchronized (monitor()) {
            check_orphaned();
            Code target = (Code) get_store().find_element_user(CODE$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public boolean isSetCode() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(CODE$0) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public void setCode(Code code) {
        generatedSetterHelperImpl(code, CODE$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public Code addNewCode() {
        Code target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Code) get_store().add_element_user(CODE$0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public void unsetCode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CODE$0, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public String getTsDir() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(TSDIR$2);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public XmlToken xgetTsDir() {
        XmlToken target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlToken) get_store().find_attribute_user(TSDIR$2);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public boolean isSetTsDir() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TSDIR$2) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public void setTsDir(String tsDir) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(TSDIR$2);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(TSDIR$2);
            }
            target.setStringValue(tsDir);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public void xsetTsDir(XmlToken tsDir) {
        synchronized (monitor()) {
            check_orphaned();
            XmlToken target = (XmlToken) get_store().find_attribute_user(TSDIR$2);
            if (target == null) {
                target = (XmlToken) get_store().add_attribute_user(TSDIR$2);
            }
            target.set(tsDir);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public void unsetTsDir() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TSDIR$2);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public String getFolder() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FOLDER$4);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public XmlToken xgetFolder() {
        XmlToken target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlToken) get_store().find_attribute_user(FOLDER$4);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public boolean isSetFolder() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FOLDER$4) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public void setFolder(String folder) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FOLDER$4);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(FOLDER$4);
            }
            target.setStringValue(folder);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public void xsetFolder(XmlToken folder) {
        synchronized (monitor()) {
            check_orphaned();
            XmlToken target = (XmlToken) get_store().find_attribute_user(FOLDER$4);
            if (target == null) {
                target = (XmlToken) get_store().add_attribute_user(FOLDER$4);
            }
            target.set(folder);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public void unsetFolder() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FOLDER$4);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public String getFileName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FILENAME$6);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public XmlToken xgetFileName() {
        XmlToken target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlToken) get_store().find_attribute_user(FILENAME$6);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public boolean isSetFileName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FILENAME$6) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public void setFileName(String fileName) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FILENAME$6);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(FILENAME$6);
            }
            target.setStringValue(fileName);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public void xsetFileName(XmlToken fileName) {
        synchronized (monitor()) {
            check_orphaned();
            XmlToken target = (XmlToken) get_store().find_attribute_user(FILENAME$6);
            if (target == null) {
                target = (XmlToken) get_store().add_attribute_user(FILENAME$6);
            }
            target.set(fileName);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public void unsetFileName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FILENAME$6);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public FileDesc.Role.Enum getRole() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(ROLE$8);
            if (target == null) {
                return null;
            }
            return (FileDesc.Role.Enum) target.getEnumValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public FileDesc.Role xgetRole() {
        FileDesc.Role target;
        synchronized (monitor()) {
            check_orphaned();
            target = (FileDesc.Role) get_store().find_attribute_user(ROLE$8);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public boolean isSetRole() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ROLE$8) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public void setRole(FileDesc.Role.Enum role) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(ROLE$8);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(ROLE$8);
            }
            target.setEnumValue(role);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public void xsetRole(FileDesc.Role role) {
        synchronized (monitor()) {
            check_orphaned();
            FileDesc.Role target = (FileDesc.Role) get_store().find_attribute_user(ROLE$8);
            if (target == null) {
                target = (FileDesc.Role) get_store().add_attribute_user(ROLE$8);
            }
            target.set(role);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public void unsetRole() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ROLE$8);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public boolean getValidity() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(VALIDITY$10);
            if (target == null) {
                return false;
            }
            return target.getBooleanValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public XmlBoolean xgetValidity() {
        XmlBoolean target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlBoolean) get_store().find_attribute_user(VALIDITY$10);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public boolean isSetValidity() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(VALIDITY$10) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public void setValidity(boolean validity) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(VALIDITY$10);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(VALIDITY$10);
            }
            target.setBooleanValue(validity);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public void xsetValidity(XmlBoolean validity) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean target = (XmlBoolean) get_store().find_attribute_user(VALIDITY$10);
            if (target == null) {
                target = (XmlBoolean) get_store().add_attribute_user(VALIDITY$10);
            }
            target.set(validity);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc
    public void unsetValidity() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(VALIDITY$10);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/ltgfmt/impl/FileDescImpl$RoleImpl.class */
    public static class RoleImpl extends JavaStringEnumerationHolderEx implements FileDesc.Role {
        private static final long serialVersionUID = 1;

        public RoleImpl(SchemaType sType) {
            super(sType, false);
        }

        protected RoleImpl(SchemaType sType, boolean b) {
            super(sType, b);
        }
    }
}
