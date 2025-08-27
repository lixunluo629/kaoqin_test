package org.apache.xmlbeans.impl.xb.ltgfmt.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.ltgfmt.Code;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/ltgfmt/impl/CodeImpl.class */
public class CodeImpl extends XmlComplexContentImpl implements Code {
    private static final long serialVersionUID = 1;
    private static final QName ID$0 = new QName("", "ID");

    public CodeImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.Code
    public String getID() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(ID$0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.Code
    public XmlToken xgetID() {
        XmlToken target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlToken) get_store().find_attribute_user(ID$0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.Code
    public boolean isSetID() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ID$0) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.Code
    public void setID(String id) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(ID$0);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(ID$0);
            }
            target.setStringValue(id);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.Code
    public void xsetID(XmlToken id) {
        synchronized (monitor()) {
            check_orphaned();
            XmlToken target = (XmlToken) get_store().find_attribute_user(ID$0);
            if (target == null) {
                target = (XmlToken) get_store().add_attribute_user(ID$0);
            }
            target.set(id);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.ltgfmt.Code
    public void unsetID() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ID$0);
        }
    }
}
