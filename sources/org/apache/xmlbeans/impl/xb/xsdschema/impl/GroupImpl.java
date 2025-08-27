package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlNCName;
import org.apache.xmlbeans.XmlNonNegativeInteger;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.apache.xmlbeans.impl.xb.xsdschema.AllNNI;
import org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.Group;
import org.apache.xmlbeans.impl.xb.xsdschema.GroupRef;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalElement;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/GroupImpl.class */
public class GroupImpl extends AnnotatedImpl implements Group {
    private static final long serialVersionUID = 1;
    private static final QName ELEMENT$0 = new QName("http://www.w3.org/2001/XMLSchema", "element");
    private static final QName GROUP$2 = new QName("http://www.w3.org/2001/XMLSchema", "group");
    private static final QName ALL$4 = new QName("http://www.w3.org/2001/XMLSchema", "all");
    private static final QName CHOICE$6 = new QName("http://www.w3.org/2001/XMLSchema", "choice");
    private static final QName SEQUENCE$8 = new QName("http://www.w3.org/2001/XMLSchema", "sequence");
    private static final QName ANY$10 = new QName("http://www.w3.org/2001/XMLSchema", Languages.ANY);
    private static final QName NAME$12 = new QName("", "name");
    private static final QName REF$14 = new QName("", "ref");
    private static final QName MINOCCURS$16 = new QName("", "minOccurs");
    private static final QName MAXOCCURS$18 = new QName("", "maxOccurs");

    public GroupImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public LocalElement[] getElementArray() {
        LocalElement[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(ELEMENT$0, targetList);
            result = new LocalElement[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public LocalElement getElementArray(int i) {
        LocalElement target;
        synchronized (monitor()) {
            check_orphaned();
            target = (LocalElement) get_store().find_element_user(ELEMENT$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public int sizeOfElementArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(ELEMENT$0);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void setElementArray(LocalElement[] elementArray) {
        check_orphaned();
        arraySetterHelper(elementArray, ELEMENT$0);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void setElementArray(int i, LocalElement element) {
        generatedSetterHelperImpl(element, ELEMENT$0, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public LocalElement insertNewElement(int i) {
        LocalElement target;
        synchronized (monitor()) {
            check_orphaned();
            target = (LocalElement) get_store().insert_element_user(ELEMENT$0, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public LocalElement addNewElement() {
        LocalElement target;
        synchronized (monitor()) {
            check_orphaned();
            target = (LocalElement) get_store().add_element_user(ELEMENT$0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void removeElement(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ELEMENT$0, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public GroupRef[] getGroupArray() {
        GroupRef[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(GROUP$2, targetList);
            result = new GroupRef[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public GroupRef getGroupArray(int i) {
        GroupRef target;
        synchronized (monitor()) {
            check_orphaned();
            target = (GroupRef) get_store().find_element_user(GROUP$2, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public int sizeOfGroupArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(GROUP$2);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void setGroupArray(GroupRef[] groupArray) {
        check_orphaned();
        arraySetterHelper(groupArray, GROUP$2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void setGroupArray(int i, GroupRef group) {
        generatedSetterHelperImpl(group, GROUP$2, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public GroupRef insertNewGroup(int i) {
        GroupRef target;
        synchronized (monitor()) {
            check_orphaned();
            target = (GroupRef) get_store().insert_element_user(GROUP$2, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public GroupRef addNewGroup() {
        GroupRef target;
        synchronized (monitor()) {
            check_orphaned();
            target = (GroupRef) get_store().add_element_user(GROUP$2);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void removeGroup(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(GROUP$2, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public All[] getAllArray() {
        All[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(ALL$4, targetList);
            result = new All[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public All getAllArray(int i) {
        All target;
        synchronized (monitor()) {
            check_orphaned();
            target = (All) get_store().find_element_user(ALL$4, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public int sizeOfAllArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(ALL$4);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void setAllArray(All[] allArray) {
        check_orphaned();
        arraySetterHelper(allArray, ALL$4);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void setAllArray(int i, All all) {
        generatedSetterHelperImpl(all, ALL$4, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public All insertNewAll(int i) {
        All target;
        synchronized (monitor()) {
            check_orphaned();
            target = (All) get_store().insert_element_user(ALL$4, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public All addNewAll() {
        All target;
        synchronized (monitor()) {
            check_orphaned();
            target = (All) get_store().add_element_user(ALL$4);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void removeAll(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ALL$4, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public ExplicitGroup[] getChoiceArray() {
        ExplicitGroup[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(CHOICE$6, targetList);
            result = new ExplicitGroup[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public ExplicitGroup getChoiceArray(int i) {
        ExplicitGroup target;
        synchronized (monitor()) {
            check_orphaned();
            target = (ExplicitGroup) get_store().find_element_user(CHOICE$6, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public int sizeOfChoiceArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(CHOICE$6);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void setChoiceArray(ExplicitGroup[] choiceArray) {
        check_orphaned();
        arraySetterHelper(choiceArray, CHOICE$6);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void setChoiceArray(int i, ExplicitGroup choice) {
        generatedSetterHelperImpl(choice, CHOICE$6, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public ExplicitGroup insertNewChoice(int i) {
        ExplicitGroup target;
        synchronized (monitor()) {
            check_orphaned();
            target = (ExplicitGroup) get_store().insert_element_user(CHOICE$6, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public ExplicitGroup addNewChoice() {
        ExplicitGroup target;
        synchronized (monitor()) {
            check_orphaned();
            target = (ExplicitGroup) get_store().add_element_user(CHOICE$6);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void removeChoice(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CHOICE$6, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public ExplicitGroup[] getSequenceArray() {
        ExplicitGroup[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(SEQUENCE$8, targetList);
            result = new ExplicitGroup[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public ExplicitGroup getSequenceArray(int i) {
        ExplicitGroup target;
        synchronized (monitor()) {
            check_orphaned();
            target = (ExplicitGroup) get_store().find_element_user(SEQUENCE$8, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public int sizeOfSequenceArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(SEQUENCE$8);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void setSequenceArray(ExplicitGroup[] sequenceArray) {
        check_orphaned();
        arraySetterHelper(sequenceArray, SEQUENCE$8);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void setSequenceArray(int i, ExplicitGroup sequence) {
        generatedSetterHelperImpl(sequence, SEQUENCE$8, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public ExplicitGroup insertNewSequence(int i) {
        ExplicitGroup target;
        synchronized (monitor()) {
            check_orphaned();
            target = (ExplicitGroup) get_store().insert_element_user(SEQUENCE$8, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public ExplicitGroup addNewSequence() {
        ExplicitGroup target;
        synchronized (monitor()) {
            check_orphaned();
            target = (ExplicitGroup) get_store().add_element_user(SEQUENCE$8);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void removeSequence(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SEQUENCE$8, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public AnyDocument.Any[] getAnyArray() {
        AnyDocument.Any[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(ANY$10, targetList);
            result = new AnyDocument.Any[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public AnyDocument.Any getAnyArray(int i) {
        AnyDocument.Any target;
        synchronized (monitor()) {
            check_orphaned();
            target = (AnyDocument.Any) get_store().find_element_user(ANY$10, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public int sizeOfAnyArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(ANY$10);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void setAnyArray(AnyDocument.Any[] anyArray) {
        check_orphaned();
        arraySetterHelper(anyArray, ANY$10);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void setAnyArray(int i, AnyDocument.Any any) {
        generatedSetterHelperImpl(any, ANY$10, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public AnyDocument.Any insertNewAny(int i) {
        AnyDocument.Any target;
        synchronized (monitor()) {
            check_orphaned();
            target = (AnyDocument.Any) get_store().insert_element_user(ANY$10, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public AnyDocument.Any addNewAny() {
        AnyDocument.Any target;
        synchronized (monitor()) {
            check_orphaned();
            target = (AnyDocument.Any) get_store().add_element_user(ANY$10);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void removeAny(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ANY$10, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public String getName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$12);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public XmlNCName xgetName() {
        XmlNCName target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlNCName) get_store().find_attribute_user(NAME$12);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public boolean isSetName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAME$12) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void setName(String name) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$12);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(NAME$12);
            }
            target.setStringValue(name);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void xsetName(XmlNCName name) {
        synchronized (monitor()) {
            check_orphaned();
            XmlNCName target = (XmlNCName) get_store().find_attribute_user(NAME$12);
            if (target == null) {
                target = (XmlNCName) get_store().add_attribute_user(NAME$12);
            }
            target.set(name);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAME$12);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public QName getRef() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(REF$14);
            if (target == null) {
                return null;
            }
            return target.getQNameValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public XmlQName xgetRef() {
        XmlQName target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlQName) get_store().find_attribute_user(REF$14);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public boolean isSetRef() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(REF$14) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void setRef(QName ref) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(REF$14);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(REF$14);
            }
            target.setQNameValue(ref);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void xsetRef(XmlQName ref) {
        synchronized (monitor()) {
            check_orphaned();
            XmlQName target = (XmlQName) get_store().find_attribute_user(REF$14);
            if (target == null) {
                target = (XmlQName) get_store().add_attribute_user(REF$14);
            }
            target.set(ref);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void unsetRef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(REF$14);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public BigInteger getMinOccurs() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(MINOCCURS$16);
            if (target == null) {
                target = (SimpleValue) get_default_attribute_value(MINOCCURS$16);
            }
            if (target == null) {
                return null;
            }
            return target.getBigIntegerValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public XmlNonNegativeInteger xgetMinOccurs() {
        XmlNonNegativeInteger xmlNonNegativeInteger;
        synchronized (monitor()) {
            check_orphaned();
            XmlNonNegativeInteger target = (XmlNonNegativeInteger) get_store().find_attribute_user(MINOCCURS$16);
            if (target == null) {
                target = (XmlNonNegativeInteger) get_default_attribute_value(MINOCCURS$16);
            }
            xmlNonNegativeInteger = target;
        }
        return xmlNonNegativeInteger;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public boolean isSetMinOccurs() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MINOCCURS$16) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void setMinOccurs(BigInteger minOccurs) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(MINOCCURS$16);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(MINOCCURS$16);
            }
            target.setBigIntegerValue(minOccurs);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void xsetMinOccurs(XmlNonNegativeInteger minOccurs) {
        synchronized (monitor()) {
            check_orphaned();
            XmlNonNegativeInteger target = (XmlNonNegativeInteger) get_store().find_attribute_user(MINOCCURS$16);
            if (target == null) {
                target = (XmlNonNegativeInteger) get_store().add_attribute_user(MINOCCURS$16);
            }
            target.set(minOccurs);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void unsetMinOccurs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MINOCCURS$16);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public Object getMaxOccurs() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(MAXOCCURS$18);
            if (target == null) {
                target = (SimpleValue) get_default_attribute_value(MAXOCCURS$18);
            }
            if (target == null) {
                return null;
            }
            return target.getObjectValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public AllNNI xgetMaxOccurs() {
        AllNNI allNNI;
        synchronized (monitor()) {
            check_orphaned();
            AllNNI target = (AllNNI) get_store().find_attribute_user(MAXOCCURS$18);
            if (target == null) {
                target = (AllNNI) get_default_attribute_value(MAXOCCURS$18);
            }
            allNNI = target;
        }
        return allNNI;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public boolean isSetMaxOccurs() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MAXOCCURS$18) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void setMaxOccurs(Object maxOccurs) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(MAXOCCURS$18);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(MAXOCCURS$18);
            }
            target.setObjectValue(maxOccurs);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void xsetMaxOccurs(AllNNI maxOccurs) {
        synchronized (monitor()) {
            check_orphaned();
            AllNNI target = (AllNNI) get_store().find_attribute_user(MAXOCCURS$18);
            if (target == null) {
                target = (AllNNI) get_store().add_attribute_user(MAXOCCURS$18);
            }
            target.set(maxOccurs);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    public void unsetMaxOccurs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MAXOCCURS$18);
        }
    }
}
