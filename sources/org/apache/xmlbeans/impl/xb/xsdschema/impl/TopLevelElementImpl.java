package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlNCName;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelElement;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/TopLevelElementImpl.class */
public class TopLevelElementImpl extends ElementImpl implements TopLevelElement {
    private static final long serialVersionUID = 1;
    private static final QName NAME$0 = new QName("", "name");

    public TopLevelElementImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.impl.ElementImpl, org.apache.xmlbeans.impl.xb.xsdschema.Element
    public String getName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.impl.ElementImpl, org.apache.xmlbeans.impl.xb.xsdschema.Element
    public XmlNCName xgetName() {
        XmlNCName target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlNCName) get_store().find_attribute_user(NAME$0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.impl.ElementImpl, org.apache.xmlbeans.impl.xb.xsdschema.Element
    public boolean isSetName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAME$0) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.impl.ElementImpl, org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setName(String name) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$0);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(NAME$0);
            }
            target.setStringValue(name);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.impl.ElementImpl, org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void xsetName(XmlNCName name) {
        synchronized (monitor()) {
            check_orphaned();
            XmlNCName target = (XmlNCName) get_store().find_attribute_user(NAME$0);
            if (target == null) {
                target = (XmlNCName) get_store().add_attribute_user(NAME$0);
            }
            target.set(name);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.impl.ElementImpl, org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAME$0);
        }
    }
}
