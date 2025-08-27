package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroupRef;
import org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType;
import org.apache.xmlbeans.impl.xb.xsdschema.GroupRef;
import org.apache.xmlbeans.impl.xb.xsdschema.Wildcard;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/ExtensionTypeImpl.class */
public class ExtensionTypeImpl extends AnnotatedImpl implements ExtensionType {
    private static final long serialVersionUID = 1;
    private static final QName GROUP$0 = new QName("http://www.w3.org/2001/XMLSchema", "group");
    private static final QName ALL$2 = new QName("http://www.w3.org/2001/XMLSchema", "all");
    private static final QName CHOICE$4 = new QName("http://www.w3.org/2001/XMLSchema", "choice");
    private static final QName SEQUENCE$6 = new QName("http://www.w3.org/2001/XMLSchema", "sequence");
    private static final QName ATTRIBUTE$8 = new QName("http://www.w3.org/2001/XMLSchema", BeanDefinitionParserDelegate.QUALIFIER_ATTRIBUTE_ELEMENT);
    private static final QName ATTRIBUTEGROUP$10 = new QName("http://www.w3.org/2001/XMLSchema", "attributeGroup");
    private static final QName ANYATTRIBUTE$12 = new QName("http://www.w3.org/2001/XMLSchema", "anyAttribute");
    private static final QName BASE$14 = new QName("", "base");

    public ExtensionTypeImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public GroupRef getGroup() {
        synchronized (monitor()) {
            check_orphaned();
            GroupRef target = (GroupRef) get_store().find_element_user(GROUP$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public boolean isSetGroup() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(GROUP$0) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public void setGroup(GroupRef group) {
        generatedSetterHelperImpl(group, GROUP$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public GroupRef addNewGroup() {
        GroupRef target;
        synchronized (monitor()) {
            check_orphaned();
            target = (GroupRef) get_store().add_element_user(GROUP$0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public void unsetGroup() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(GROUP$0, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public All getAll() {
        synchronized (monitor()) {
            check_orphaned();
            All target = (All) get_store().find_element_user(ALL$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public boolean isSetAll() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(ALL$2) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public void setAll(All all) {
        generatedSetterHelperImpl(all, ALL$2, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public All addNewAll() {
        All target;
        synchronized (monitor()) {
            check_orphaned();
            target = (All) get_store().add_element_user(ALL$2);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public void unsetAll() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ALL$2, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public ExplicitGroup getChoice() {
        synchronized (monitor()) {
            check_orphaned();
            ExplicitGroup target = (ExplicitGroup) get_store().find_element_user(CHOICE$4, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public boolean isSetChoice() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(CHOICE$4) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public void setChoice(ExplicitGroup choice) {
        generatedSetterHelperImpl(choice, CHOICE$4, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public ExplicitGroup addNewChoice() {
        ExplicitGroup target;
        synchronized (monitor()) {
            check_orphaned();
            target = (ExplicitGroup) get_store().add_element_user(CHOICE$4);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public void unsetChoice() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CHOICE$4, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public ExplicitGroup getSequence() {
        synchronized (monitor()) {
            check_orphaned();
            ExplicitGroup target = (ExplicitGroup) get_store().find_element_user(SEQUENCE$6, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public boolean isSetSequence() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SEQUENCE$6) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public void setSequence(ExplicitGroup sequence) {
        generatedSetterHelperImpl(sequence, SEQUENCE$6, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public ExplicitGroup addNewSequence() {
        ExplicitGroup target;
        synchronized (monitor()) {
            check_orphaned();
            target = (ExplicitGroup) get_store().add_element_user(SEQUENCE$6);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public void unsetSequence() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SEQUENCE$6, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public Attribute[] getAttributeArray() {
        Attribute[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(ATTRIBUTE$8, targetList);
            result = new Attribute[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public Attribute getAttributeArray(int i) {
        Attribute target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Attribute) get_store().find_element_user(ATTRIBUTE$8, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public int sizeOfAttributeArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(ATTRIBUTE$8);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public void setAttributeArray(Attribute[] attributeArray) {
        check_orphaned();
        arraySetterHelper(attributeArray, ATTRIBUTE$8);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public void setAttributeArray(int i, Attribute attribute) {
        generatedSetterHelperImpl(attribute, ATTRIBUTE$8, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public Attribute insertNewAttribute(int i) {
        Attribute target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Attribute) get_store().insert_element_user(ATTRIBUTE$8, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public Attribute addNewAttribute() {
        Attribute target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Attribute) get_store().add_element_user(ATTRIBUTE$8);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public void removeAttribute(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ATTRIBUTE$8, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public AttributeGroupRef[] getAttributeGroupArray() {
        AttributeGroupRef[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(ATTRIBUTEGROUP$10, targetList);
            result = new AttributeGroupRef[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public AttributeGroupRef getAttributeGroupArray(int i) {
        AttributeGroupRef target;
        synchronized (monitor()) {
            check_orphaned();
            target = (AttributeGroupRef) get_store().find_element_user(ATTRIBUTEGROUP$10, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public int sizeOfAttributeGroupArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(ATTRIBUTEGROUP$10);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public void setAttributeGroupArray(AttributeGroupRef[] attributeGroupArray) {
        check_orphaned();
        arraySetterHelper(attributeGroupArray, ATTRIBUTEGROUP$10);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public void setAttributeGroupArray(int i, AttributeGroupRef attributeGroup) {
        generatedSetterHelperImpl(attributeGroup, ATTRIBUTEGROUP$10, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public AttributeGroupRef insertNewAttributeGroup(int i) {
        AttributeGroupRef target;
        synchronized (monitor()) {
            check_orphaned();
            target = (AttributeGroupRef) get_store().insert_element_user(ATTRIBUTEGROUP$10, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public AttributeGroupRef addNewAttributeGroup() {
        AttributeGroupRef target;
        synchronized (monitor()) {
            check_orphaned();
            target = (AttributeGroupRef) get_store().add_element_user(ATTRIBUTEGROUP$10);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public void removeAttributeGroup(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ATTRIBUTEGROUP$10, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public Wildcard getAnyAttribute() {
        synchronized (monitor()) {
            check_orphaned();
            Wildcard target = (Wildcard) get_store().find_element_user(ANYATTRIBUTE$12, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public boolean isSetAnyAttribute() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(ANYATTRIBUTE$12) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public void setAnyAttribute(Wildcard anyAttribute) {
        generatedSetterHelperImpl(anyAttribute, ANYATTRIBUTE$12, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public Wildcard addNewAnyAttribute() {
        Wildcard target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Wildcard) get_store().add_element_user(ANYATTRIBUTE$12);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public void unsetAnyAttribute() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ANYATTRIBUTE$12, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public QName getBase() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(BASE$14);
            if (target == null) {
                return null;
            }
            return target.getQNameValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public XmlQName xgetBase() {
        XmlQName target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlQName) get_store().find_attribute_user(BASE$14);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public void setBase(QName base) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(BASE$14);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(BASE$14);
            }
            target.setQNameValue(base);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType
    public void xsetBase(XmlQName base) {
        synchronized (monitor()) {
            check_orphaned();
            XmlQName target = (XmlQName) get_store().find_attribute_user(BASE$14);
            if (target == null) {
                target = (XmlQName) get_store().add_attribute_user(BASE$14);
            }
            target.set(base);
        }
    }
}
