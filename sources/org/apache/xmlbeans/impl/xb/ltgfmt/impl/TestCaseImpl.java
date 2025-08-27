package org.apache.xmlbeans.impl.xb.ltgfmt.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.ltgfmt.FileDesc;
import org.apache.xmlbeans.impl.xb.ltgfmt.TestCase;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/ltgfmt/impl/TestCaseImpl.class */
public class TestCaseImpl extends XmlComplexContentImpl implements TestCase {
    private static final long serialVersionUID = 1;
    private static final QName DESCRIPTION$0 = new QName("http://www.bea.com/2003/05/xmlbean/ltgfmt", "description");
    private static final QName FILES$2 = new QName("http://www.bea.com/2003/05/xmlbean/ltgfmt", "files");
    private static final QName ID$4 = new QName("", "id");
    private static final QName ORIGIN$6 = new QName("", "origin");
    private static final QName MODIFIED$8 = new QName("", "modified");

    public TestCaseImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public String getDescription() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_element_user(DESCRIPTION$0, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public XmlString xgetDescription() {
        XmlString target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlString) get_store().find_element_user(DESCRIPTION$0, 0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public boolean isSetDescription() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(DESCRIPTION$0) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public void setDescription(String description) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_element_user(DESCRIPTION$0, 0);
            if (target == null) {
                target = (SimpleValue) get_store().add_element_user(DESCRIPTION$0);
            }
            target.setStringValue(description);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public void xsetDescription(XmlString description) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString target = (XmlString) get_store().find_element_user(DESCRIPTION$0, 0);
            if (target == null) {
                target = (XmlString) get_store().add_element_user(DESCRIPTION$0);
            }
            target.set(description);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public void unsetDescription() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(DESCRIPTION$0, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public TestCase.Files getFiles() {
        synchronized (monitor()) {
            check_orphaned();
            TestCase.Files target = (TestCase.Files) get_store().find_element_user(FILES$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public void setFiles(TestCase.Files files) {
        generatedSetterHelperImpl(files, FILES$2, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public TestCase.Files addNewFiles() {
        TestCase.Files target;
        synchronized (monitor()) {
            check_orphaned();
            target = (TestCase.Files) get_store().add_element_user(FILES$2);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public String getId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(ID$4);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public XmlID xgetId() {
        XmlID target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlID) get_store().find_attribute_user(ID$4);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public boolean isSetId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ID$4) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public void setId(String id) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(ID$4);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(ID$4);
            }
            target.setStringValue(id);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public void xsetId(XmlID id) {
        synchronized (monitor()) {
            check_orphaned();
            XmlID target = (XmlID) get_store().find_attribute_user(ID$4);
            if (target == null) {
                target = (XmlID) get_store().add_attribute_user(ID$4);
            }
            target.set(id);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ID$4);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public String getOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(ORIGIN$6);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public XmlToken xgetOrigin() {
        XmlToken target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlToken) get_store().find_attribute_user(ORIGIN$6);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public boolean isSetOrigin() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ORIGIN$6) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public void setOrigin(String origin) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(ORIGIN$6);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(ORIGIN$6);
            }
            target.setStringValue(origin);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public void xsetOrigin(XmlToken origin) {
        synchronized (monitor()) {
            check_orphaned();
            XmlToken target = (XmlToken) get_store().find_attribute_user(ORIGIN$6);
            if (target == null) {
                target = (XmlToken) get_store().add_attribute_user(ORIGIN$6);
            }
            target.set(origin);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public void unsetOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ORIGIN$6);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public boolean getModified() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(MODIFIED$8);
            if (target == null) {
                return false;
            }
            return target.getBooleanValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public XmlBoolean xgetModified() {
        XmlBoolean target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlBoolean) get_store().find_attribute_user(MODIFIED$8);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public boolean isSetModified() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MODIFIED$8) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public void setModified(boolean modified) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(MODIFIED$8);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(MODIFIED$8);
            }
            target.setBooleanValue(modified);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public void xsetModified(XmlBoolean modified) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean target = (XmlBoolean) get_store().find_attribute_user(MODIFIED$8);
            if (target == null) {
                target = (XmlBoolean) get_store().add_attribute_user(MODIFIED$8);
            }
            target.set(modified);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase
    public void unsetModified() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MODIFIED$8);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/ltgfmt/impl/TestCaseImpl$FilesImpl.class */
    public static class FilesImpl extends XmlComplexContentImpl implements TestCase.Files {
        private static final long serialVersionUID = 1;
        private static final QName FILE$0 = new QName("http://www.bea.com/2003/05/xmlbean/ltgfmt", "file");

        public FilesImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase.Files
        public FileDesc[] getFileArray() {
            FileDesc[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(FILE$0, targetList);
                result = new FileDesc[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase.Files
        public FileDesc getFileArray(int i) {
            FileDesc target;
            synchronized (monitor()) {
                check_orphaned();
                target = (FileDesc) get_store().find_element_user(FILE$0, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase.Files
        public int sizeOfFileArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(FILE$0);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase.Files
        public void setFileArray(FileDesc[] fileArray) {
            check_orphaned();
            arraySetterHelper(fileArray, FILE$0);
        }

        @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase.Files
        public void setFileArray(int i, FileDesc file) {
            generatedSetterHelperImpl(file, FILE$0, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase.Files
        public FileDesc insertNewFile(int i) {
            FileDesc target;
            synchronized (monitor()) {
                check_orphaned();
                target = (FileDesc) get_store().insert_element_user(FILE$0, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase.Files
        public FileDesc addNewFile() {
            FileDesc target;
            synchronized (monitor()) {
                check_orphaned();
                target = (FileDesc) get_store().add_element_user(FILE$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.ltgfmt.TestCase.Files
        public void removeFile(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(FILE$0, i);
            }
        }
    }
}
