package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlNCName;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.apache.xmlbeans.impl.xb.xsdschema.FormChoice;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalSimpleType;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/AttributeImpl.class */
public class AttributeImpl extends AnnotatedImpl implements Attribute {
    private static final long serialVersionUID = 1;
    private static final QName SIMPLETYPE$0 = new QName("http://www.w3.org/2001/XMLSchema", "simpleType");
    private static final QName NAME$2 = new QName("", "name");
    private static final QName REF$4 = new QName("", "ref");
    private static final QName TYPE$6 = new QName("", "type");
    private static final QName USE$8 = new QName("", "use");
    private static final QName DEFAULT$10 = new QName("", "default");
    private static final QName FIXED$12 = new QName("", "fixed");
    private static final QName FORM$14 = new QName("", "form");

    public AttributeImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public LocalSimpleType getSimpleType() {
        synchronized (monitor()) {
            check_orphaned();
            LocalSimpleType target = (LocalSimpleType) get_store().find_element_user(SIMPLETYPE$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public boolean isSetSimpleType() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SIMPLETYPE$0) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void setSimpleType(LocalSimpleType simpleType) {
        generatedSetterHelperImpl(simpleType, SIMPLETYPE$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public LocalSimpleType addNewSimpleType() {
        LocalSimpleType target;
        synchronized (monitor()) {
            check_orphaned();
            target = (LocalSimpleType) get_store().add_element_user(SIMPLETYPE$0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void unsetSimpleType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SIMPLETYPE$0, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public String getName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$2);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public XmlNCName xgetName() {
        XmlNCName target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlNCName) get_store().find_attribute_user(NAME$2);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public boolean isSetName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAME$2) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void setName(String name) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$2);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(NAME$2);
            }
            target.setStringValue(name);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void xsetName(XmlNCName name) {
        synchronized (monitor()) {
            check_orphaned();
            XmlNCName target = (XmlNCName) get_store().find_attribute_user(NAME$2);
            if (target == null) {
                target = (XmlNCName) get_store().add_attribute_user(NAME$2);
            }
            target.set(name);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAME$2);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public QName getRef() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(REF$4);
            if (target == null) {
                return null;
            }
            return target.getQNameValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public XmlQName xgetRef() {
        XmlQName target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlQName) get_store().find_attribute_user(REF$4);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public boolean isSetRef() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(REF$4) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void setRef(QName ref) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(REF$4);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(REF$4);
            }
            target.setQNameValue(ref);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void xsetRef(XmlQName ref) {
        synchronized (monitor()) {
            check_orphaned();
            XmlQName target = (XmlQName) get_store().find_attribute_user(REF$4);
            if (target == null) {
                target = (XmlQName) get_store().add_attribute_user(REF$4);
            }
            target.set(ref);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void unsetRef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(REF$4);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public QName getType() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(TYPE$6);
            if (target == null) {
                return null;
            }
            return target.getQNameValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public XmlQName xgetType() {
        XmlQName target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlQName) get_store().find_attribute_user(TYPE$6);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public boolean isSetType() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TYPE$6) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void setType(QName type) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(TYPE$6);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(TYPE$6);
            }
            target.setQNameValue(type);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void xsetType(XmlQName type) {
        synchronized (monitor()) {
            check_orphaned();
            XmlQName target = (XmlQName) get_store().find_attribute_user(TYPE$6);
            if (target == null) {
                target = (XmlQName) get_store().add_attribute_user(TYPE$6);
            }
            target.set(type);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void unsetType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TYPE$6);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public Attribute.Use.Enum getUse() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(USE$8);
            if (target == null) {
                target = (SimpleValue) get_default_attribute_value(USE$8);
            }
            if (target == null) {
                return null;
            }
            return (Attribute.Use.Enum) target.getEnumValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public Attribute.Use xgetUse() {
        Attribute.Use use;
        synchronized (monitor()) {
            check_orphaned();
            Attribute.Use target = (Attribute.Use) get_store().find_attribute_user(USE$8);
            if (target == null) {
                target = (Attribute.Use) get_default_attribute_value(USE$8);
            }
            use = target;
        }
        return use;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public boolean isSetUse() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(USE$8) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void setUse(Attribute.Use.Enum use) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(USE$8);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(USE$8);
            }
            target.setEnumValue(use);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void xsetUse(Attribute.Use use) {
        synchronized (monitor()) {
            check_orphaned();
            Attribute.Use target = (Attribute.Use) get_store().find_attribute_user(USE$8);
            if (target == null) {
                target = (Attribute.Use) get_store().add_attribute_user(USE$8);
            }
            target.set(use);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void unsetUse() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(USE$8);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public String getDefault() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(DEFAULT$10);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public XmlString xgetDefault() {
        XmlString target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlString) get_store().find_attribute_user(DEFAULT$10);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public boolean isSetDefault() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DEFAULT$10) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void setDefault(String xdefault) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(DEFAULT$10);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(DEFAULT$10);
            }
            target.setStringValue(xdefault);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void xsetDefault(XmlString xdefault) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString target = (XmlString) get_store().find_attribute_user(DEFAULT$10);
            if (target == null) {
                target = (XmlString) get_store().add_attribute_user(DEFAULT$10);
            }
            target.set(xdefault);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void unsetDefault() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DEFAULT$10);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public String getFixed() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FIXED$12);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public XmlString xgetFixed() {
        XmlString target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlString) get_store().find_attribute_user(FIXED$12);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public boolean isSetFixed() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FIXED$12) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void setFixed(String fixed) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FIXED$12);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(FIXED$12);
            }
            target.setStringValue(fixed);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void xsetFixed(XmlString fixed) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString target = (XmlString) get_store().find_attribute_user(FIXED$12);
            if (target == null) {
                target = (XmlString) get_store().add_attribute_user(FIXED$12);
            }
            target.set(fixed);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void unsetFixed() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FIXED$12);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public FormChoice.Enum getForm() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FORM$14);
            if (target == null) {
                return null;
            }
            return (FormChoice.Enum) target.getEnumValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public FormChoice xgetForm() {
        FormChoice target;
        synchronized (monitor()) {
            check_orphaned();
            target = (FormChoice) get_store().find_attribute_user(FORM$14);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public boolean isSetForm() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FORM$14) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void setForm(FormChoice.Enum form) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FORM$14);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(FORM$14);
            }
            target.setEnumValue(form);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void xsetForm(FormChoice form) {
        synchronized (monitor()) {
            check_orphaned();
            FormChoice target = (FormChoice) get_store().find_attribute_user(FORM$14);
            if (target == null) {
                target = (FormChoice) get_store().add_attribute_user(FORM$14);
            }
            target.set(form);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Attribute
    public void unsetForm() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FORM$14);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/AttributeImpl$UseImpl.class */
    public static class UseImpl extends JavaStringEnumerationHolderEx implements Attribute.Use {
        private static final long serialVersionUID = 1;

        public UseImpl(SchemaType sType) {
            super(sType, false);
        }

        protected UseImpl(SchemaType sType, boolean b) {
            super(sType, b);
        }
    }
}
