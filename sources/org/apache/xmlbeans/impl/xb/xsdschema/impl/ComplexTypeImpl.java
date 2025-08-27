package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlNCName;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroupRef;
import org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.ComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.DerivationSet;
import org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.GroupRef;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleContentDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.Wildcard;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/ComplexTypeImpl.class */
public class ComplexTypeImpl extends AnnotatedImpl implements ComplexType {
    private static final long serialVersionUID = 1;
    private static final QName SIMPLECONTENT$0 = new QName("http://www.w3.org/2001/XMLSchema", "simpleContent");
    private static final QName COMPLEXCONTENT$2 = new QName("http://www.w3.org/2001/XMLSchema", "complexContent");
    private static final QName GROUP$4 = new QName("http://www.w3.org/2001/XMLSchema", "group");
    private static final QName ALL$6 = new QName("http://www.w3.org/2001/XMLSchema", "all");
    private static final QName CHOICE$8 = new QName("http://www.w3.org/2001/XMLSchema", "choice");
    private static final QName SEQUENCE$10 = new QName("http://www.w3.org/2001/XMLSchema", "sequence");
    private static final QName ATTRIBUTE$12 = new QName("http://www.w3.org/2001/XMLSchema", BeanDefinitionParserDelegate.QUALIFIER_ATTRIBUTE_ELEMENT);
    private static final QName ATTRIBUTEGROUP$14 = new QName("http://www.w3.org/2001/XMLSchema", "attributeGroup");
    private static final QName ANYATTRIBUTE$16 = new QName("http://www.w3.org/2001/XMLSchema", "anyAttribute");
    private static final QName NAME$18 = new QName("", "name");
    private static final QName MIXED$20 = new QName("", "mixed");
    private static final QName ABSTRACT$22 = new QName("", BeanDefinitionParserDelegate.ABSTRACT_ATTRIBUTE);
    private static final QName FINAL$24 = new QName("", "final");
    private static final QName BLOCK$26 = new QName("", "block");

    public ComplexTypeImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public SimpleContentDocument.SimpleContent getSimpleContent() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleContentDocument.SimpleContent target = (SimpleContentDocument.SimpleContent) get_store().find_element_user(SIMPLECONTENT$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public boolean isSetSimpleContent() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SIMPLECONTENT$0) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void setSimpleContent(SimpleContentDocument.SimpleContent simpleContent) {
        generatedSetterHelperImpl(simpleContent, SIMPLECONTENT$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public SimpleContentDocument.SimpleContent addNewSimpleContent() {
        SimpleContentDocument.SimpleContent target;
        synchronized (monitor()) {
            check_orphaned();
            target = (SimpleContentDocument.SimpleContent) get_store().add_element_user(SIMPLECONTENT$0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void unsetSimpleContent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SIMPLECONTENT$0, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public ComplexContentDocument.ComplexContent getComplexContent() {
        synchronized (monitor()) {
            check_orphaned();
            ComplexContentDocument.ComplexContent target = (ComplexContentDocument.ComplexContent) get_store().find_element_user(COMPLEXCONTENT$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public boolean isSetComplexContent() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(COMPLEXCONTENT$2) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void setComplexContent(ComplexContentDocument.ComplexContent complexContent) {
        generatedSetterHelperImpl(complexContent, COMPLEXCONTENT$2, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public ComplexContentDocument.ComplexContent addNewComplexContent() {
        ComplexContentDocument.ComplexContent target;
        synchronized (monitor()) {
            check_orphaned();
            target = (ComplexContentDocument.ComplexContent) get_store().add_element_user(COMPLEXCONTENT$2);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void unsetComplexContent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(COMPLEXCONTENT$2, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public GroupRef getGroup() {
        synchronized (monitor()) {
            check_orphaned();
            GroupRef target = (GroupRef) get_store().find_element_user(GROUP$4, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public boolean isSetGroup() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(GROUP$4) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void setGroup(GroupRef group) {
        generatedSetterHelperImpl(group, GROUP$4, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public GroupRef addNewGroup() {
        GroupRef target;
        synchronized (monitor()) {
            check_orphaned();
            target = (GroupRef) get_store().add_element_user(GROUP$4);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void unsetGroup() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(GROUP$4, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public All getAll() {
        synchronized (monitor()) {
            check_orphaned();
            All target = (All) get_store().find_element_user(ALL$6, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public boolean isSetAll() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(ALL$6) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void setAll(All all) {
        generatedSetterHelperImpl(all, ALL$6, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public All addNewAll() {
        All target;
        synchronized (monitor()) {
            check_orphaned();
            target = (All) get_store().add_element_user(ALL$6);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void unsetAll() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ALL$6, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public ExplicitGroup getChoice() {
        synchronized (monitor()) {
            check_orphaned();
            ExplicitGroup target = (ExplicitGroup) get_store().find_element_user(CHOICE$8, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public boolean isSetChoice() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(CHOICE$8) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void setChoice(ExplicitGroup choice) {
        generatedSetterHelperImpl(choice, CHOICE$8, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public ExplicitGroup addNewChoice() {
        ExplicitGroup target;
        synchronized (monitor()) {
            check_orphaned();
            target = (ExplicitGroup) get_store().add_element_user(CHOICE$8);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void unsetChoice() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CHOICE$8, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public ExplicitGroup getSequence() {
        synchronized (monitor()) {
            check_orphaned();
            ExplicitGroup target = (ExplicitGroup) get_store().find_element_user(SEQUENCE$10, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public boolean isSetSequence() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SEQUENCE$10) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void setSequence(ExplicitGroup sequence) {
        generatedSetterHelperImpl(sequence, SEQUENCE$10, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public ExplicitGroup addNewSequence() {
        ExplicitGroup target;
        synchronized (monitor()) {
            check_orphaned();
            target = (ExplicitGroup) get_store().add_element_user(SEQUENCE$10);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void unsetSequence() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SEQUENCE$10, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public Attribute[] getAttributeArray() {
        Attribute[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(ATTRIBUTE$12, targetList);
            result = new Attribute[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public Attribute getAttributeArray(int i) {
        Attribute target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Attribute) get_store().find_element_user(ATTRIBUTE$12, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public int sizeOfAttributeArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(ATTRIBUTE$12);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void setAttributeArray(Attribute[] attributeArray) {
        check_orphaned();
        arraySetterHelper(attributeArray, ATTRIBUTE$12);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void setAttributeArray(int i, Attribute attribute) {
        generatedSetterHelperImpl(attribute, ATTRIBUTE$12, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public Attribute insertNewAttribute(int i) {
        Attribute target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Attribute) get_store().insert_element_user(ATTRIBUTE$12, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public Attribute addNewAttribute() {
        Attribute target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Attribute) get_store().add_element_user(ATTRIBUTE$12);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void removeAttribute(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ATTRIBUTE$12, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public AttributeGroupRef[] getAttributeGroupArray() {
        AttributeGroupRef[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(ATTRIBUTEGROUP$14, targetList);
            result = new AttributeGroupRef[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public AttributeGroupRef getAttributeGroupArray(int i) {
        AttributeGroupRef target;
        synchronized (monitor()) {
            check_orphaned();
            target = (AttributeGroupRef) get_store().find_element_user(ATTRIBUTEGROUP$14, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public int sizeOfAttributeGroupArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(ATTRIBUTEGROUP$14);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void setAttributeGroupArray(AttributeGroupRef[] attributeGroupArray) {
        check_orphaned();
        arraySetterHelper(attributeGroupArray, ATTRIBUTEGROUP$14);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void setAttributeGroupArray(int i, AttributeGroupRef attributeGroup) {
        generatedSetterHelperImpl(attributeGroup, ATTRIBUTEGROUP$14, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public AttributeGroupRef insertNewAttributeGroup(int i) {
        AttributeGroupRef target;
        synchronized (monitor()) {
            check_orphaned();
            target = (AttributeGroupRef) get_store().insert_element_user(ATTRIBUTEGROUP$14, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public AttributeGroupRef addNewAttributeGroup() {
        AttributeGroupRef target;
        synchronized (monitor()) {
            check_orphaned();
            target = (AttributeGroupRef) get_store().add_element_user(ATTRIBUTEGROUP$14);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void removeAttributeGroup(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ATTRIBUTEGROUP$14, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public Wildcard getAnyAttribute() {
        synchronized (monitor()) {
            check_orphaned();
            Wildcard target = (Wildcard) get_store().find_element_user(ANYATTRIBUTE$16, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public boolean isSetAnyAttribute() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(ANYATTRIBUTE$16) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void setAnyAttribute(Wildcard anyAttribute) {
        generatedSetterHelperImpl(anyAttribute, ANYATTRIBUTE$16, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public Wildcard addNewAnyAttribute() {
        Wildcard target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Wildcard) get_store().add_element_user(ANYATTRIBUTE$16);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void unsetAnyAttribute() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ANYATTRIBUTE$16, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public String getName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$18);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public XmlNCName xgetName() {
        XmlNCName target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlNCName) get_store().find_attribute_user(NAME$18);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public boolean isSetName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAME$18) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void setName(String name) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$18);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(NAME$18);
            }
            target.setStringValue(name);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void xsetName(XmlNCName name) {
        synchronized (monitor()) {
            check_orphaned();
            XmlNCName target = (XmlNCName) get_store().find_attribute_user(NAME$18);
            if (target == null) {
                target = (XmlNCName) get_store().add_attribute_user(NAME$18);
            }
            target.set(name);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAME$18);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public boolean getMixed() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(MIXED$20);
            if (target == null) {
                target = (SimpleValue) get_default_attribute_value(MIXED$20);
            }
            if (target == null) {
                return false;
            }
            return target.getBooleanValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public XmlBoolean xgetMixed() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean target = (XmlBoolean) get_store().find_attribute_user(MIXED$20);
            if (target == null) {
                target = (XmlBoolean) get_default_attribute_value(MIXED$20);
            }
            xmlBoolean = target;
        }
        return xmlBoolean;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public boolean isSetMixed() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MIXED$20) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void setMixed(boolean mixed) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(MIXED$20);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(MIXED$20);
            }
            target.setBooleanValue(mixed);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void xsetMixed(XmlBoolean mixed) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean target = (XmlBoolean) get_store().find_attribute_user(MIXED$20);
            if (target == null) {
                target = (XmlBoolean) get_store().add_attribute_user(MIXED$20);
            }
            target.set(mixed);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void unsetMixed() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MIXED$20);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public boolean getAbstract() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(ABSTRACT$22);
            if (target == null) {
                target = (SimpleValue) get_default_attribute_value(ABSTRACT$22);
            }
            if (target == null) {
                return false;
            }
            return target.getBooleanValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public XmlBoolean xgetAbstract() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean target = (XmlBoolean) get_store().find_attribute_user(ABSTRACT$22);
            if (target == null) {
                target = (XmlBoolean) get_default_attribute_value(ABSTRACT$22);
            }
            xmlBoolean = target;
        }
        return xmlBoolean;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public boolean isSetAbstract() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ABSTRACT$22) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void setAbstract(boolean xabstract) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(ABSTRACT$22);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(ABSTRACT$22);
            }
            target.setBooleanValue(xabstract);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void xsetAbstract(XmlBoolean xabstract) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean target = (XmlBoolean) get_store().find_attribute_user(ABSTRACT$22);
            if (target == null) {
                target = (XmlBoolean) get_store().add_attribute_user(ABSTRACT$22);
            }
            target.set(xabstract);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void unsetAbstract() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ABSTRACT$22);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public Object getFinal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FINAL$24);
            if (target == null) {
                return null;
            }
            return target.getObjectValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public DerivationSet xgetFinal() {
        DerivationSet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (DerivationSet) get_store().find_attribute_user(FINAL$24);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public boolean isSetFinal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FINAL$24) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void setFinal(Object xfinal) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FINAL$24);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(FINAL$24);
            }
            target.setObjectValue(xfinal);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void xsetFinal(DerivationSet xfinal) {
        synchronized (monitor()) {
            check_orphaned();
            DerivationSet target = (DerivationSet) get_store().find_attribute_user(FINAL$24);
            if (target == null) {
                target = (DerivationSet) get_store().add_attribute_user(FINAL$24);
            }
            target.set(xfinal);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void unsetFinal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FINAL$24);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public Object getBlock() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(BLOCK$26);
            if (target == null) {
                return null;
            }
            return target.getObjectValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public DerivationSet xgetBlock() {
        DerivationSet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (DerivationSet) get_store().find_attribute_user(BLOCK$26);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public boolean isSetBlock() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BLOCK$26) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void setBlock(Object block) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(BLOCK$26);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(BLOCK$26);
            }
            target.setObjectValue(block);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void xsetBlock(DerivationSet block) {
        synchronized (monitor()) {
            check_orphaned();
            DerivationSet target = (DerivationSet) get_store().find_attribute_user(BLOCK$26);
            if (target == null) {
                target = (DerivationSet) get_store().add_attribute_user(BLOCK$26);
            }
            target.set(block);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    public void unsetBlock() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BLOCK$26);
        }
    }
}
