package org.apache.xmlbeans.impl.xb.substwsdl.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.substwsdl.TImport;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/substwsdl/impl/TImportImpl.class */
public class TImportImpl extends XmlComplexContentImpl implements TImport {
    private static final long serialVersionUID = 1;
    private static final QName NAMESPACE$0 = new QName("", "namespace");
    private static final QName LOCATION$2 = new QName("", "location");

    public TImportImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.substwsdl.TImport
    public String getNamespace() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAMESPACE$0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.substwsdl.TImport
    public XmlAnyURI xgetNamespace() {
        XmlAnyURI target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlAnyURI) get_store().find_attribute_user(NAMESPACE$0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.substwsdl.TImport
    public void setNamespace(String namespace) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAMESPACE$0);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(NAMESPACE$0);
            }
            target.setStringValue(namespace);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.substwsdl.TImport
    public void xsetNamespace(XmlAnyURI namespace) {
        synchronized (monitor()) {
            check_orphaned();
            XmlAnyURI target = (XmlAnyURI) get_store().find_attribute_user(NAMESPACE$0);
            if (target == null) {
                target = (XmlAnyURI) get_store().add_attribute_user(NAMESPACE$0);
            }
            target.set(namespace);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.substwsdl.TImport
    public String getLocation() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(LOCATION$2);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.substwsdl.TImport
    public XmlAnyURI xgetLocation() {
        XmlAnyURI target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlAnyURI) get_store().find_attribute_user(LOCATION$2);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.substwsdl.TImport
    public void setLocation(String location) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(LOCATION$2);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(LOCATION$2);
            }
            target.setStringValue(location);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.substwsdl.TImport
    public void xsetLocation(XmlAnyURI location) {
        synchronized (monitor()) {
            check_orphaned();
            XmlAnyURI target = (XmlAnyURI) get_store().find_attribute_user(LOCATION$2);
            if (target == null) {
                target = (XmlAnyURI) get_store().add_attribute_user(LOCATION$2);
            }
            target.set(location);
        }
    }
}
