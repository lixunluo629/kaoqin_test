package org.apache.xmlbeans.impl.xb.xmlconfig.impl;

import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xmlconfig.NamespaceList;
import org.apache.xmlbeans.impl.xb.xmlconfig.NamespacePrefixList;
import org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/impl/NsconfigImpl.class */
public class NsconfigImpl extends XmlComplexContentImpl implements Nsconfig {
    private static final long serialVersionUID = 1;
    private static final QName PACKAGE$0 = new QName("http://xml.apache.org/xmlbeans/2004/02/xbean/config", "package");
    private static final QName PREFIX$2 = new QName("http://xml.apache.org/xmlbeans/2004/02/xbean/config", "prefix");
    private static final QName SUFFIX$4 = new QName("http://xml.apache.org/xmlbeans/2004/02/xbean/config", "suffix");
    private static final QName URI$6 = new QName("", "uri");
    private static final QName URIPREFIX$8 = new QName("", "uriprefix");

    public NsconfigImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public String getPackage() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_element_user(PACKAGE$0, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public XmlString xgetPackage() {
        XmlString target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlString) get_store().find_element_user(PACKAGE$0, 0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public boolean isSetPackage() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PACKAGE$0) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public void setPackage(String xpackage) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_element_user(PACKAGE$0, 0);
            if (target == null) {
                target = (SimpleValue) get_store().add_element_user(PACKAGE$0);
            }
            target.setStringValue(xpackage);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public void xsetPackage(XmlString xpackage) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString target = (XmlString) get_store().find_element_user(PACKAGE$0, 0);
            if (target == null) {
                target = (XmlString) get_store().add_element_user(PACKAGE$0);
            }
            target.set(xpackage);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public void unsetPackage() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PACKAGE$0, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public String getPrefix() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_element_user(PREFIX$2, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public XmlString xgetPrefix() {
        XmlString target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlString) get_store().find_element_user(PREFIX$2, 0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public boolean isSetPrefix() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PREFIX$2) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public void setPrefix(String prefix) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_element_user(PREFIX$2, 0);
            if (target == null) {
                target = (SimpleValue) get_store().add_element_user(PREFIX$2);
            }
            target.setStringValue(prefix);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public void xsetPrefix(XmlString prefix) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString target = (XmlString) get_store().find_element_user(PREFIX$2, 0);
            if (target == null) {
                target = (XmlString) get_store().add_element_user(PREFIX$2);
            }
            target.set(prefix);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public void unsetPrefix() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PREFIX$2, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public String getSuffix() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_element_user(SUFFIX$4, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public XmlString xgetSuffix() {
        XmlString target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlString) get_store().find_element_user(SUFFIX$4, 0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public boolean isSetSuffix() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SUFFIX$4) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public void setSuffix(String suffix) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_element_user(SUFFIX$4, 0);
            if (target == null) {
                target = (SimpleValue) get_store().add_element_user(SUFFIX$4);
            }
            target.setStringValue(suffix);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public void xsetSuffix(XmlString suffix) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString target = (XmlString) get_store().find_element_user(SUFFIX$4, 0);
            if (target == null) {
                target = (XmlString) get_store().add_element_user(SUFFIX$4);
            }
            target.set(suffix);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public void unsetSuffix() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SUFFIX$4, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public Object getUri() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(URI$6);
            if (target == null) {
                return null;
            }
            return target.getObjectValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public NamespaceList xgetUri() {
        NamespaceList target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NamespaceList) get_store().find_attribute_user(URI$6);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public boolean isSetUri() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(URI$6) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public void setUri(Object uri) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(URI$6);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(URI$6);
            }
            target.setObjectValue(uri);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public void xsetUri(NamespaceList uri) {
        synchronized (monitor()) {
            check_orphaned();
            NamespaceList target = (NamespaceList) get_store().find_attribute_user(URI$6);
            if (target == null) {
                target = (NamespaceList) get_store().add_attribute_user(URI$6);
            }
            target.set(uri);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public void unsetUri() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(URI$6);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public List getUriprefix() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(URIPREFIX$8);
            if (target == null) {
                return null;
            }
            return target.getListValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public NamespacePrefixList xgetUriprefix() {
        NamespacePrefixList target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NamespacePrefixList) get_store().find_attribute_user(URIPREFIX$8);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public boolean isSetUriprefix() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(URIPREFIX$8) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public void setUriprefix(List uriprefix) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(URIPREFIX$8);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(URIPREFIX$8);
            }
            target.setListValue(uriprefix);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public void xsetUriprefix(NamespacePrefixList uriprefix) {
        synchronized (monitor()) {
            check_orphaned();
            NamespacePrefixList target = (NamespacePrefixList) get_store().find_attribute_user(URIPREFIX$8);
            if (target == null) {
                target = (NamespacePrefixList) get_store().add_attribute_user(URIPREFIX$8);
            }
            target.set(uriprefix);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Nsconfig
    public void unsetUriprefix() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(URIPREFIX$8);
        }
    }
}
