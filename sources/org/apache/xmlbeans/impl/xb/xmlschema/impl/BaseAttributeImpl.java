package org.apache.xmlbeans.impl.xb.xmlschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xmlschema.BaseAttribute;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlschema/impl/BaseAttributeImpl.class */
public class BaseAttributeImpl extends XmlComplexContentImpl implements BaseAttribute {
    private static final long serialVersionUID = 1;
    private static final QName BASE$0 = new QName("http://www.w3.org/XML/1998/namespace", "base");

    public BaseAttributeImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlschema.BaseAttribute
    public String getBase() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(BASE$0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlschema.BaseAttribute
    public XmlAnyURI xgetBase() {
        XmlAnyURI target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlAnyURI) get_store().find_attribute_user(BASE$0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlschema.BaseAttribute
    public boolean isSetBase() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BASE$0) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlschema.BaseAttribute
    public void setBase(String base) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(BASE$0);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(BASE$0);
            }
            target.setStringValue(base);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlschema.BaseAttribute
    public void xsetBase(XmlAnyURI base) {
        synchronized (monitor()) {
            check_orphaned();
            XmlAnyURI target = (XmlAnyURI) get_store().find_attribute_user(BASE$0);
            if (target == null) {
                target = (XmlAnyURI) get_store().add_attribute_user(BASE$0);
            }
            target.set(base);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xmlschema.BaseAttribute
    public void unsetBase() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BASE$0);
        }
    }
}
