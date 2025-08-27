package org.apache.xmlbeans.impl.xb.xsdownload.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdownload/impl/DownloadedSchemaEntryImpl.class */
public class DownloadedSchemaEntryImpl extends XmlComplexContentImpl implements DownloadedSchemaEntry {
    private static final long serialVersionUID = 1;
    private static final QName FILENAME$0 = new QName("http://www.bea.com/2003/01/xmlbean/xsdownload", "filename");
    private static final QName SHA1$2 = new QName("http://www.bea.com/2003/01/xmlbean/xsdownload", "sha1");
    private static final QName SCHEMALOCATION$4 = new QName("http://www.bea.com/2003/01/xmlbean/xsdownload", "schemaLocation");
    private static final QName NAMESPACE$6 = new QName("http://www.bea.com/2003/01/xmlbean/xsdownload", "namespace");

    public DownloadedSchemaEntryImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public String getFilename() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_element_user(FILENAME$0, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public XmlToken xgetFilename() {
        XmlToken target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlToken) get_store().find_element_user(FILENAME$0, 0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public void setFilename(String filename) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_element_user(FILENAME$0, 0);
            if (target == null) {
                target = (SimpleValue) get_store().add_element_user(FILENAME$0);
            }
            target.setStringValue(filename);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public void xsetFilename(XmlToken filename) {
        synchronized (monitor()) {
            check_orphaned();
            XmlToken target = (XmlToken) get_store().find_element_user(FILENAME$0, 0);
            if (target == null) {
                target = (XmlToken) get_store().add_element_user(FILENAME$0);
            }
            target.set(filename);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public String getSha1() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_element_user(SHA1$2, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public XmlToken xgetSha1() {
        XmlToken target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlToken) get_store().find_element_user(SHA1$2, 0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public void setSha1(String sha1) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_element_user(SHA1$2, 0);
            if (target == null) {
                target = (SimpleValue) get_store().add_element_user(SHA1$2);
            }
            target.setStringValue(sha1);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public void xsetSha1(XmlToken sha1) {
        synchronized (monitor()) {
            check_orphaned();
            XmlToken target = (XmlToken) get_store().find_element_user(SHA1$2, 0);
            if (target == null) {
                target = (XmlToken) get_store().add_element_user(SHA1$2);
            }
            target.set(sha1);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public String[] getSchemaLocationArray() {
        String[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(SCHEMALOCATION$4, targetList);
            result = new String[targetList.size()];
            int len = targetList.size();
            for (int i = 0; i < len; i++) {
                result[i] = ((SimpleValue) targetList.get(i)).getStringValue();
            }
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public String getSchemaLocationArray(int i) {
        String stringValue;
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_element_user(SCHEMALOCATION$4, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            stringValue = target.getStringValue();
        }
        return stringValue;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public XmlAnyURI[] xgetSchemaLocationArray() {
        XmlAnyURI[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(SCHEMALOCATION$4, targetList);
            result = new XmlAnyURI[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public XmlAnyURI xgetSchemaLocationArray(int i) {
        XmlAnyURI target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlAnyURI) get_store().find_element_user(SCHEMALOCATION$4, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public int sizeOfSchemaLocationArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(SCHEMALOCATION$4);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public void setSchemaLocationArray(String[] schemaLocationArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(schemaLocationArray, SCHEMALOCATION$4);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public void setSchemaLocationArray(int i, String schemaLocation) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_element_user(SCHEMALOCATION$4, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(schemaLocation);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public void xsetSchemaLocationArray(XmlAnyURI[] schemaLocationArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(schemaLocationArray, SCHEMALOCATION$4);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public void xsetSchemaLocationArray(int i, XmlAnyURI schemaLocation) {
        synchronized (monitor()) {
            check_orphaned();
            XmlAnyURI target = (XmlAnyURI) get_store().find_element_user(SCHEMALOCATION$4, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(schemaLocation);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public void insertSchemaLocation(int i, String schemaLocation) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().insert_element_user(SCHEMALOCATION$4, i);
            target.setStringValue(schemaLocation);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public void addSchemaLocation(String schemaLocation) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().add_element_user(SCHEMALOCATION$4);
            target.setStringValue(schemaLocation);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public XmlAnyURI insertNewSchemaLocation(int i) {
        XmlAnyURI target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlAnyURI) get_store().insert_element_user(SCHEMALOCATION$4, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public XmlAnyURI addNewSchemaLocation() {
        XmlAnyURI target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlAnyURI) get_store().add_element_user(SCHEMALOCATION$4);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public void removeSchemaLocation(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SCHEMALOCATION$4, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public String getNamespace() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_element_user(NAMESPACE$6, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public XmlAnyURI xgetNamespace() {
        XmlAnyURI target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlAnyURI) get_store().find_element_user(NAMESPACE$6, 0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public boolean isSetNamespace() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(NAMESPACE$6) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public void setNamespace(String namespace) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_element_user(NAMESPACE$6, 0);
            if (target == null) {
                target = (SimpleValue) get_store().add_element_user(NAMESPACE$6);
            }
            target.setStringValue(namespace);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public void xsetNamespace(XmlAnyURI namespace) {
        synchronized (monitor()) {
            check_orphaned();
            XmlAnyURI target = (XmlAnyURI) get_store().find_element_user(NAMESPACE$6, 0);
            if (target == null) {
                target = (XmlAnyURI) get_store().add_element_user(NAMESPACE$6);
            }
            target.set(namespace);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry
    public void unsetNamespace() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(NAMESPACE$6, 0);
        }
    }
}
