package org.apache.xmlbeans.impl.xb.xmlconfig.impl;

import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig;
import org.apache.xmlbeans.impl.xb.xmlconfig.Qnametargetlist;
import org.springframework.validation.DataBinder;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/impl/QnameconfigImpl.class */
public class QnameconfigImpl extends XmlComplexContentImpl implements Qnameconfig {
    private static final long serialVersionUID = 1;
    private static final QName NAME$0 = new QName("", "name");
    private static final QName JAVANAME$2 = new QName("", "javaname");
    private static final QName TARGET$4 = new QName("", DataBinder.DEFAULT_OBJECT_NAME);

    public QnameconfigImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig
    public QName getName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$0);
            if (target == null) {
                return null;
            }
            return target.getQNameValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig
    public XmlQName xgetName() {
        XmlQName target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlQName) get_store().find_attribute_user(NAME$0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig
    public boolean isSetName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAME$0) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig
    public void setName(QName name) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$0);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(NAME$0);
            }
            target.setQNameValue(name);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig
    public void xsetName(XmlQName name) {
        synchronized (monitor()) {
            check_orphaned();
            XmlQName target = (XmlQName) get_store().find_attribute_user(NAME$0);
            if (target == null) {
                target = (XmlQName) get_store().add_attribute_user(NAME$0);
            }
            target.set(name);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAME$0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig
    public String getJavaname() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(JAVANAME$2);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig
    public XmlString xgetJavaname() {
        XmlString target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlString) get_store().find_attribute_user(JAVANAME$2);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig
    public boolean isSetJavaname() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(JAVANAME$2) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig
    public void setJavaname(String javaname) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(JAVANAME$2);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(JAVANAME$2);
            }
            target.setStringValue(javaname);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig
    public void xsetJavaname(XmlString javaname) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString target = (XmlString) get_store().find_attribute_user(JAVANAME$2);
            if (target == null) {
                target = (XmlString) get_store().add_attribute_user(JAVANAME$2);
            }
            target.set(javaname);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig
    public void unsetJavaname() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(JAVANAME$2);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig
    public List getTarget() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(TARGET$4);
            if (target == null) {
                target = (SimpleValue) get_default_attribute_value(TARGET$4);
            }
            if (target == null) {
                return null;
            }
            return target.getListValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig
    public Qnametargetlist xgetTarget() {
        Qnametargetlist qnametargetlist;
        synchronized (monitor()) {
            check_orphaned();
            Qnametargetlist target = (Qnametargetlist) get_store().find_attribute_user(TARGET$4);
            if (target == null) {
                target = (Qnametargetlist) get_default_attribute_value(TARGET$4);
            }
            qnametargetlist = target;
        }
        return qnametargetlist;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig
    public boolean isSetTarget() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TARGET$4) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig
    public void setTarget(List targetValue) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(TARGET$4);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(TARGET$4);
            }
            target.setListValue(targetValue);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig
    public void xsetTarget(Qnametargetlist targetValue) {
        synchronized (monitor()) {
            check_orphaned();
            Qnametargetlist target = (Qnametargetlist) get_store().find_attribute_user(TARGET$4);
            if (target == null) {
                target = (Qnametargetlist) get_store().add_attribute_user(TARGET$4);
            }
            target.set(targetValue);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Qnameconfig
    public void unsetTarget() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TARGET$4);
        }
    }
}
