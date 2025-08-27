package org.apache.xmlbeans.impl.xb.xmlconfig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/impl/UsertypeconfigImpl.class */
public class UsertypeconfigImpl extends XmlComplexContentImpl implements Usertypeconfig {
    private static final long serialVersionUID = 1;
    private static final QName STATICHANDLER$0 = new QName("http://xml.apache.org/xmlbeans/2004/02/xbean/config", "staticHandler");
    private static final QName NAME$2 = new QName("", "name");
    private static final QName JAVANAME$4 = new QName("", "javaname");

    public UsertypeconfigImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig
    public String getStaticHandler() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_element_user(STATICHANDLER$0, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig
    public XmlString xgetStaticHandler() {
        XmlString target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlString) get_store().find_element_user(STATICHANDLER$0, 0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig
    public void setStaticHandler(String staticHandler) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_element_user(STATICHANDLER$0, 0);
            if (target == null) {
                target = (SimpleValue) get_store().add_element_user(STATICHANDLER$0);
            }
            target.setStringValue(staticHandler);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig
    public void xsetStaticHandler(XmlString staticHandler) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString target = (XmlString) get_store().find_element_user(STATICHANDLER$0, 0);
            if (target == null) {
                target = (XmlString) get_store().add_element_user(STATICHANDLER$0);
            }
            target.set(staticHandler);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig
    public QName getName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$2);
            if (target == null) {
                return null;
            }
            return target.getQNameValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig
    public XmlQName xgetName() {
        XmlQName target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlQName) get_store().find_attribute_user(NAME$2);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig
    public boolean isSetName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAME$2) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig
    public void setName(QName name) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$2);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(NAME$2);
            }
            target.setQNameValue(name);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig
    public void xsetName(XmlQName name) {
        synchronized (monitor()) {
            check_orphaned();
            XmlQName target = (XmlQName) get_store().find_attribute_user(NAME$2);
            if (target == null) {
                target = (XmlQName) get_store().add_attribute_user(NAME$2);
            }
            target.set(name);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAME$2);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig
    public String getJavaname() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(JAVANAME$4);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig
    public XmlString xgetJavaname() {
        XmlString target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlString) get_store().find_attribute_user(JAVANAME$4);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig
    public boolean isSetJavaname() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(JAVANAME$4) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig
    public void setJavaname(String javaname) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(JAVANAME$4);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(JAVANAME$4);
            }
            target.setStringValue(javaname);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig
    public void xsetJavaname(XmlString javaname) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString target = (XmlString) get_store().find_attribute_user(JAVANAME$4);
            if (target == null) {
                target = (XmlString) get_store().add_attribute_user(JAVANAME$4);
            }
            target.set(javaname);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlconfig.Usertypeconfig
    public void unsetJavaname() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(JAVANAME$4);
        }
    }
}
