package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.impl.xb.xsdschema.Facet;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/FacetImpl.class */
public class FacetImpl extends AnnotatedImpl implements Facet {
    private static final long serialVersionUID = 1;
    private static final QName VALUE$0 = new QName("", "value");
    private static final QName FIXED$2 = new QName("", "fixed");

    public FacetImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Facet
    public XmlAnySimpleType getValue() {
        synchronized (monitor()) {
            check_orphaned();
            XmlAnySimpleType target = (XmlAnySimpleType) get_store().find_attribute_user(VALUE$0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Facet
    public void setValue(XmlAnySimpleType value) {
        synchronized (monitor()) {
            check_orphaned();
            XmlAnySimpleType target = (XmlAnySimpleType) get_store().find_attribute_user(VALUE$0);
            if (target == null) {
                target = (XmlAnySimpleType) get_store().add_attribute_user(VALUE$0);
            }
            target.set(value);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Facet
    public XmlAnySimpleType addNewValue() {
        XmlAnySimpleType target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlAnySimpleType) get_store().add_attribute_user(VALUE$0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Facet
    public boolean getFixed() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FIXED$2);
            if (target == null) {
                target = (SimpleValue) get_default_attribute_value(FIXED$2);
            }
            if (target == null) {
                return false;
            }
            return target.getBooleanValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Facet
    public XmlBoolean xgetFixed() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean target = (XmlBoolean) get_store().find_attribute_user(FIXED$2);
            if (target == null) {
                target = (XmlBoolean) get_default_attribute_value(FIXED$2);
            }
            xmlBoolean = target;
        }
        return xmlBoolean;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Facet
    public boolean isSetFixed() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FIXED$2) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Facet
    public void setFixed(boolean fixed) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FIXED$2);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(FIXED$2);
            }
            target.setBooleanValue(fixed);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Facet
    public void xsetFixed(XmlBoolean fixed) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean target = (XmlBoolean) get_store().find_attribute_user(FIXED$2);
            if (target == null) {
                target = (XmlBoolean) get_store().add_attribute_user(FIXED$2);
            }
            target.set(fixed);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Facet
    public void unsetFixed() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FIXED$2);
        }
    }
}
