package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlNCName;
import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleDerivationSet;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleType;
import org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/SimpleTypeImpl.class */
public class SimpleTypeImpl extends AnnotatedImpl implements SimpleType {
    private static final long serialVersionUID = 1;
    private static final QName RESTRICTION$0 = new QName("http://www.w3.org/2001/XMLSchema", "restriction");
    private static final QName LIST$2 = new QName("http://www.w3.org/2001/XMLSchema", "list");
    private static final QName UNION$4 = new QName("http://www.w3.org/2001/XMLSchema", XmlErrorCodes.UNION);
    private static final QName FINAL$6 = new QName("", "final");
    private static final QName NAME$8 = new QName("", "name");

    public SimpleTypeImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public RestrictionDocument.Restriction getRestriction() {
        synchronized (monitor()) {
            check_orphaned();
            RestrictionDocument.Restriction target = (RestrictionDocument.Restriction) get_store().find_element_user(RESTRICTION$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public boolean isSetRestriction() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(RESTRICTION$0) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public void setRestriction(RestrictionDocument.Restriction restriction) {
        generatedSetterHelperImpl(restriction, RESTRICTION$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public RestrictionDocument.Restriction addNewRestriction() {
        RestrictionDocument.Restriction target;
        synchronized (monitor()) {
            check_orphaned();
            target = (RestrictionDocument.Restriction) get_store().add_element_user(RESTRICTION$0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public void unsetRestriction() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(RESTRICTION$0, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public ListDocument.List getList() {
        synchronized (monitor()) {
            check_orphaned();
            ListDocument.List target = (ListDocument.List) get_store().find_element_user(LIST$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public boolean isSetList() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(LIST$2) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public void setList(ListDocument.List list) {
        generatedSetterHelperImpl(list, LIST$2, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public ListDocument.List addNewList() {
        ListDocument.List target;
        synchronized (monitor()) {
            check_orphaned();
            target = (ListDocument.List) get_store().add_element_user(LIST$2);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public void unsetList() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(LIST$2, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public UnionDocument.Union getUnion() {
        synchronized (monitor()) {
            check_orphaned();
            UnionDocument.Union target = (UnionDocument.Union) get_store().find_element_user(UNION$4, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public boolean isSetUnion() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(UNION$4) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public void setUnion(UnionDocument.Union union) {
        generatedSetterHelperImpl(union, UNION$4, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public UnionDocument.Union addNewUnion() {
        UnionDocument.Union target;
        synchronized (monitor()) {
            check_orphaned();
            target = (UnionDocument.Union) get_store().add_element_user(UNION$4);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public void unsetUnion() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(UNION$4, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public Object getFinal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FINAL$6);
            if (target == null) {
                return null;
            }
            return target.getObjectValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public SimpleDerivationSet xgetFinal() {
        SimpleDerivationSet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (SimpleDerivationSet) get_store().find_attribute_user(FINAL$6);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public boolean isSetFinal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FINAL$6) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public void setFinal(Object xfinal) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FINAL$6);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(FINAL$6);
            }
            target.setObjectValue(xfinal);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public void xsetFinal(SimpleDerivationSet xfinal) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleDerivationSet target = (SimpleDerivationSet) get_store().find_attribute_user(FINAL$6);
            if (target == null) {
                target = (SimpleDerivationSet) get_store().add_attribute_user(FINAL$6);
            }
            target.set(xfinal);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public void unsetFinal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FINAL$6);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public String getName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$8);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public XmlNCName xgetName() {
        XmlNCName target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlNCName) get_store().find_attribute_user(NAME$8);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public boolean isSetName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAME$8) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public void setName(String name) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$8);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(NAME$8);
            }
            target.setStringValue(name);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public void xsetName(XmlNCName name) {
        synchronized (monitor()) {
            check_orphaned();
            XmlNCName target = (XmlNCName) get_store().find_attribute_user(NAME$8);
            if (target == null) {
                target = (XmlNCName) get_store().add_attribute_user(NAME$8);
            }
            target.set(name);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAME$8);
        }
    }
}
