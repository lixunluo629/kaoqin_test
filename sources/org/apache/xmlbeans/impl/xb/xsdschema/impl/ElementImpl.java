package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlNCName;
import org.apache.xmlbeans.XmlNonNegativeInteger;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.xb.xsdschema.AllNNI;
import org.apache.xmlbeans.impl.xb.xsdschema.BlockSet;
import org.apache.xmlbeans.impl.xb.xsdschema.DerivationSet;
import org.apache.xmlbeans.impl.xb.xsdschema.Element;
import org.apache.xmlbeans.impl.xb.xsdschema.FormChoice;
import org.apache.xmlbeans.impl.xb.xsdschema.Keybase;
import org.apache.xmlbeans.impl.xb.xsdschema.KeyrefDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalSimpleType;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/ElementImpl.class */
public class ElementImpl extends AnnotatedImpl implements Element {
    private static final long serialVersionUID = 1;
    private static final QName SIMPLETYPE$0 = new QName("http://www.w3.org/2001/XMLSchema", "simpleType");
    private static final QName COMPLEXTYPE$2 = new QName("http://www.w3.org/2001/XMLSchema", "complexType");
    private static final QName UNIQUE$4 = new QName("http://www.w3.org/2001/XMLSchema", "unique");
    private static final QName KEY$6 = new QName("http://www.w3.org/2001/XMLSchema", "key");
    private static final QName KEYREF$8 = new QName("http://www.w3.org/2001/XMLSchema", "keyref");
    private static final QName NAME$10 = new QName("", "name");
    private static final QName REF$12 = new QName("", "ref");
    private static final QName TYPE$14 = new QName("", "type");
    private static final QName SUBSTITUTIONGROUP$16 = new QName("", "substitutionGroup");
    private static final QName MINOCCURS$18 = new QName("", "minOccurs");
    private static final QName MAXOCCURS$20 = new QName("", "maxOccurs");
    private static final QName DEFAULT$22 = new QName("", "default");
    private static final QName FIXED$24 = new QName("", "fixed");
    private static final QName NILLABLE$26 = new QName("", "nillable");
    private static final QName ABSTRACT$28 = new QName("", BeanDefinitionParserDelegate.ABSTRACT_ATTRIBUTE);
    private static final QName FINAL$30 = new QName("", "final");
    private static final QName BLOCK$32 = new QName("", "block");
    private static final QName FORM$34 = new QName("", "form");

    public ElementImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
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

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public boolean isSetSimpleType() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SIMPLETYPE$0) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setSimpleType(LocalSimpleType simpleType) {
        generatedSetterHelperImpl(simpleType, SIMPLETYPE$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public LocalSimpleType addNewSimpleType() {
        LocalSimpleType target;
        synchronized (monitor()) {
            check_orphaned();
            target = (LocalSimpleType) get_store().add_element_user(SIMPLETYPE$0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void unsetSimpleType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SIMPLETYPE$0, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public LocalComplexType getComplexType() {
        synchronized (monitor()) {
            check_orphaned();
            LocalComplexType target = (LocalComplexType) get_store().find_element_user(COMPLEXTYPE$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public boolean isSetComplexType() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(COMPLEXTYPE$2) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setComplexType(LocalComplexType complexType) {
        generatedSetterHelperImpl(complexType, COMPLEXTYPE$2, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public LocalComplexType addNewComplexType() {
        LocalComplexType target;
        synchronized (monitor()) {
            check_orphaned();
            target = (LocalComplexType) get_store().add_element_user(COMPLEXTYPE$2);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void unsetComplexType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(COMPLEXTYPE$2, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public Keybase[] getUniqueArray() {
        Keybase[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(UNIQUE$4, targetList);
            result = new Keybase[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public Keybase getUniqueArray(int i) {
        Keybase target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Keybase) get_store().find_element_user(UNIQUE$4, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public int sizeOfUniqueArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(UNIQUE$4);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setUniqueArray(Keybase[] uniqueArray) {
        check_orphaned();
        arraySetterHelper(uniqueArray, UNIQUE$4);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setUniqueArray(int i, Keybase unique) {
        generatedSetterHelperImpl(unique, UNIQUE$4, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public Keybase insertNewUnique(int i) {
        Keybase target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Keybase) get_store().insert_element_user(UNIQUE$4, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public Keybase addNewUnique() {
        Keybase target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Keybase) get_store().add_element_user(UNIQUE$4);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void removeUnique(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(UNIQUE$4, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public Keybase[] getKeyArray() {
        Keybase[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(KEY$6, targetList);
            result = new Keybase[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public Keybase getKeyArray(int i) {
        Keybase target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Keybase) get_store().find_element_user(KEY$6, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public int sizeOfKeyArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(KEY$6);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setKeyArray(Keybase[] keyArray) {
        check_orphaned();
        arraySetterHelper(keyArray, KEY$6);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setKeyArray(int i, Keybase key) {
        generatedSetterHelperImpl(key, KEY$6, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public Keybase insertNewKey(int i) {
        Keybase target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Keybase) get_store().insert_element_user(KEY$6, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public Keybase addNewKey() {
        Keybase target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Keybase) get_store().add_element_user(KEY$6);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void removeKey(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(KEY$6, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public KeyrefDocument.Keyref[] getKeyrefArray() {
        KeyrefDocument.Keyref[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(KEYREF$8, targetList);
            result = new KeyrefDocument.Keyref[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public KeyrefDocument.Keyref getKeyrefArray(int i) {
        KeyrefDocument.Keyref target;
        synchronized (monitor()) {
            check_orphaned();
            target = (KeyrefDocument.Keyref) get_store().find_element_user(KEYREF$8, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public int sizeOfKeyrefArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(KEYREF$8);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setKeyrefArray(KeyrefDocument.Keyref[] keyrefArray) {
        check_orphaned();
        arraySetterHelper(keyrefArray, KEYREF$8);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setKeyrefArray(int i, KeyrefDocument.Keyref keyref) {
        generatedSetterHelperImpl(keyref, KEYREF$8, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public KeyrefDocument.Keyref insertNewKeyref(int i) {
        KeyrefDocument.Keyref target;
        synchronized (monitor()) {
            check_orphaned();
            target = (KeyrefDocument.Keyref) get_store().insert_element_user(KEYREF$8, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public KeyrefDocument.Keyref addNewKeyref() {
        KeyrefDocument.Keyref target;
        synchronized (monitor()) {
            check_orphaned();
            target = (KeyrefDocument.Keyref) get_store().add_element_user(KEYREF$8);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void removeKeyref(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(KEYREF$8, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public String getName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$10);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public XmlNCName xgetName() {
        XmlNCName target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlNCName) get_store().find_attribute_user(NAME$10);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public boolean isSetName() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NAME$10) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setName(String name) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$10);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(NAME$10);
            }
            target.setStringValue(name);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void xsetName(XmlNCName name) {
        synchronized (monitor()) {
            check_orphaned();
            XmlNCName target = (XmlNCName) get_store().find_attribute_user(NAME$10);
            if (target == null) {
                target = (XmlNCName) get_store().add_attribute_user(NAME$10);
            }
            target.set(name);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NAME$10);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public QName getRef() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(REF$12);
            if (target == null) {
                return null;
            }
            return target.getQNameValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public XmlQName xgetRef() {
        XmlQName target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlQName) get_store().find_attribute_user(REF$12);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public boolean isSetRef() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(REF$12) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setRef(QName ref) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(REF$12);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(REF$12);
            }
            target.setQNameValue(ref);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void xsetRef(XmlQName ref) {
        synchronized (monitor()) {
            check_orphaned();
            XmlQName target = (XmlQName) get_store().find_attribute_user(REF$12);
            if (target == null) {
                target = (XmlQName) get_store().add_attribute_user(REF$12);
            }
            target.set(ref);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void unsetRef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(REF$12);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public QName getType() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(TYPE$14);
            if (target == null) {
                return null;
            }
            return target.getQNameValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public XmlQName xgetType() {
        XmlQName target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlQName) get_store().find_attribute_user(TYPE$14);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public boolean isSetType() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(TYPE$14) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setType(QName type) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(TYPE$14);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(TYPE$14);
            }
            target.setQNameValue(type);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void xsetType(XmlQName type) {
        synchronized (monitor()) {
            check_orphaned();
            XmlQName target = (XmlQName) get_store().find_attribute_user(TYPE$14);
            if (target == null) {
                target = (XmlQName) get_store().add_attribute_user(TYPE$14);
            }
            target.set(type);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void unsetType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(TYPE$14);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public QName getSubstitutionGroup() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(SUBSTITUTIONGROUP$16);
            if (target == null) {
                return null;
            }
            return target.getQNameValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public XmlQName xgetSubstitutionGroup() {
        XmlQName target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlQName) get_store().find_attribute_user(SUBSTITUTIONGROUP$16);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public boolean isSetSubstitutionGroup() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(SUBSTITUTIONGROUP$16) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setSubstitutionGroup(QName substitutionGroup) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(SUBSTITUTIONGROUP$16);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(SUBSTITUTIONGROUP$16);
            }
            target.setQNameValue(substitutionGroup);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void xsetSubstitutionGroup(XmlQName substitutionGroup) {
        synchronized (monitor()) {
            check_orphaned();
            XmlQName target = (XmlQName) get_store().find_attribute_user(SUBSTITUTIONGROUP$16);
            if (target == null) {
                target = (XmlQName) get_store().add_attribute_user(SUBSTITUTIONGROUP$16);
            }
            target.set(substitutionGroup);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void unsetSubstitutionGroup() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(SUBSTITUTIONGROUP$16);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public BigInteger getMinOccurs() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(MINOCCURS$18);
            if (target == null) {
                target = (SimpleValue) get_default_attribute_value(MINOCCURS$18);
            }
            if (target == null) {
                return null;
            }
            return target.getBigIntegerValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public XmlNonNegativeInteger xgetMinOccurs() {
        XmlNonNegativeInteger xmlNonNegativeInteger;
        synchronized (monitor()) {
            check_orphaned();
            XmlNonNegativeInteger target = (XmlNonNegativeInteger) get_store().find_attribute_user(MINOCCURS$18);
            if (target == null) {
                target = (XmlNonNegativeInteger) get_default_attribute_value(MINOCCURS$18);
            }
            xmlNonNegativeInteger = target;
        }
        return xmlNonNegativeInteger;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public boolean isSetMinOccurs() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MINOCCURS$18) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setMinOccurs(BigInteger minOccurs) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(MINOCCURS$18);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(MINOCCURS$18);
            }
            target.setBigIntegerValue(minOccurs);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void xsetMinOccurs(XmlNonNegativeInteger minOccurs) {
        synchronized (monitor()) {
            check_orphaned();
            XmlNonNegativeInteger target = (XmlNonNegativeInteger) get_store().find_attribute_user(MINOCCURS$18);
            if (target == null) {
                target = (XmlNonNegativeInteger) get_store().add_attribute_user(MINOCCURS$18);
            }
            target.set(minOccurs);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void unsetMinOccurs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MINOCCURS$18);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public Object getMaxOccurs() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(MAXOCCURS$20);
            if (target == null) {
                target = (SimpleValue) get_default_attribute_value(MAXOCCURS$20);
            }
            if (target == null) {
                return null;
            }
            return target.getObjectValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public AllNNI xgetMaxOccurs() {
        AllNNI allNNI;
        synchronized (monitor()) {
            check_orphaned();
            AllNNI target = (AllNNI) get_store().find_attribute_user(MAXOCCURS$20);
            if (target == null) {
                target = (AllNNI) get_default_attribute_value(MAXOCCURS$20);
            }
            allNNI = target;
        }
        return allNNI;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public boolean isSetMaxOccurs() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(MAXOCCURS$20) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setMaxOccurs(Object maxOccurs) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(MAXOCCURS$20);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(MAXOCCURS$20);
            }
            target.setObjectValue(maxOccurs);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void xsetMaxOccurs(AllNNI maxOccurs) {
        synchronized (monitor()) {
            check_orphaned();
            AllNNI target = (AllNNI) get_store().find_attribute_user(MAXOCCURS$20);
            if (target == null) {
                target = (AllNNI) get_store().add_attribute_user(MAXOCCURS$20);
            }
            target.set(maxOccurs);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void unsetMaxOccurs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MAXOCCURS$20);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public String getDefault() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(DEFAULT$22);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public XmlString xgetDefault() {
        XmlString target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlString) get_store().find_attribute_user(DEFAULT$22);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public boolean isSetDefault() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(DEFAULT$22) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setDefault(String xdefault) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(DEFAULT$22);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(DEFAULT$22);
            }
            target.setStringValue(xdefault);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void xsetDefault(XmlString xdefault) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString target = (XmlString) get_store().find_attribute_user(DEFAULT$22);
            if (target == null) {
                target = (XmlString) get_store().add_attribute_user(DEFAULT$22);
            }
            target.set(xdefault);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void unsetDefault() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(DEFAULT$22);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public String getFixed() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FIXED$24);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public XmlString xgetFixed() {
        XmlString target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlString) get_store().find_attribute_user(FIXED$24);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public boolean isSetFixed() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FIXED$24) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setFixed(String fixed) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FIXED$24);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(FIXED$24);
            }
            target.setStringValue(fixed);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void xsetFixed(XmlString fixed) {
        synchronized (monitor()) {
            check_orphaned();
            XmlString target = (XmlString) get_store().find_attribute_user(FIXED$24);
            if (target == null) {
                target = (XmlString) get_store().add_attribute_user(FIXED$24);
            }
            target.set(fixed);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void unsetFixed() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FIXED$24);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public boolean getNillable() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NILLABLE$26);
            if (target == null) {
                target = (SimpleValue) get_default_attribute_value(NILLABLE$26);
            }
            if (target == null) {
                return false;
            }
            return target.getBooleanValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public XmlBoolean xgetNillable() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean target = (XmlBoolean) get_store().find_attribute_user(NILLABLE$26);
            if (target == null) {
                target = (XmlBoolean) get_default_attribute_value(NILLABLE$26);
            }
            xmlBoolean = target;
        }
        return xmlBoolean;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public boolean isSetNillable() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(NILLABLE$26) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setNillable(boolean nillable) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NILLABLE$26);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(NILLABLE$26);
            }
            target.setBooleanValue(nillable);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void xsetNillable(XmlBoolean nillable) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean target = (XmlBoolean) get_store().find_attribute_user(NILLABLE$26);
            if (target == null) {
                target = (XmlBoolean) get_store().add_attribute_user(NILLABLE$26);
            }
            target.set(nillable);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void unsetNillable() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(NILLABLE$26);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public boolean getAbstract() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(ABSTRACT$28);
            if (target == null) {
                target = (SimpleValue) get_default_attribute_value(ABSTRACT$28);
            }
            if (target == null) {
                return false;
            }
            return target.getBooleanValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public XmlBoolean xgetAbstract() {
        XmlBoolean xmlBoolean;
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean target = (XmlBoolean) get_store().find_attribute_user(ABSTRACT$28);
            if (target == null) {
                target = (XmlBoolean) get_default_attribute_value(ABSTRACT$28);
            }
            xmlBoolean = target;
        }
        return xmlBoolean;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public boolean isSetAbstract() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ABSTRACT$28) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setAbstract(boolean xabstract) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(ABSTRACT$28);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(ABSTRACT$28);
            }
            target.setBooleanValue(xabstract);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void xsetAbstract(XmlBoolean xabstract) {
        synchronized (monitor()) {
            check_orphaned();
            XmlBoolean target = (XmlBoolean) get_store().find_attribute_user(ABSTRACT$28);
            if (target == null) {
                target = (XmlBoolean) get_store().add_attribute_user(ABSTRACT$28);
            }
            target.set(xabstract);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void unsetAbstract() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ABSTRACT$28);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public Object getFinal() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FINAL$30);
            if (target == null) {
                return null;
            }
            return target.getObjectValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public DerivationSet xgetFinal() {
        DerivationSet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (DerivationSet) get_store().find_attribute_user(FINAL$30);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public boolean isSetFinal() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FINAL$30) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setFinal(Object xfinal) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FINAL$30);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(FINAL$30);
            }
            target.setObjectValue(xfinal);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void xsetFinal(DerivationSet xfinal) {
        synchronized (monitor()) {
            check_orphaned();
            DerivationSet target = (DerivationSet) get_store().find_attribute_user(FINAL$30);
            if (target == null) {
                target = (DerivationSet) get_store().add_attribute_user(FINAL$30);
            }
            target.set(xfinal);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void unsetFinal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FINAL$30);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public Object getBlock() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(BLOCK$32);
            if (target == null) {
                return null;
            }
            return target.getObjectValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public BlockSet xgetBlock() {
        BlockSet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (BlockSet) get_store().find_attribute_user(BLOCK$32);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public boolean isSetBlock() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(BLOCK$32) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setBlock(Object block) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(BLOCK$32);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(BLOCK$32);
            }
            target.setObjectValue(block);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void xsetBlock(BlockSet block) {
        synchronized (monitor()) {
            check_orphaned();
            BlockSet target = (BlockSet) get_store().find_attribute_user(BLOCK$32);
            if (target == null) {
                target = (BlockSet) get_store().add_attribute_user(BLOCK$32);
            }
            target.set(block);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void unsetBlock() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BLOCK$32);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public FormChoice.Enum getForm() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FORM$34);
            if (target == null) {
                return null;
            }
            return (FormChoice.Enum) target.getEnumValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public FormChoice xgetForm() {
        FormChoice target;
        synchronized (monitor()) {
            check_orphaned();
            target = (FormChoice) get_store().find_attribute_user(FORM$34);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public boolean isSetForm() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(FORM$34) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void setForm(FormChoice.Enum form) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(FORM$34);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(FORM$34);
            }
            target.setEnumValue(form);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void xsetForm(FormChoice form) {
        synchronized (monitor()) {
            check_orphaned();
            FormChoice target = (FormChoice) get_store().find_attribute_user(FORM$34);
            if (target == null) {
                target = (FormChoice) get_store().add_attribute_user(FORM$34);
            }
            target.set(form);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    public void unsetForm() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(FORM$34);
        }
    }
}
