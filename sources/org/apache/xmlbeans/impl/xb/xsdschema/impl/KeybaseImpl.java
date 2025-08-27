package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlNCName;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.apache.xmlbeans.impl.xb.xsdschema.FieldDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.Keybase;
import org.apache.xmlbeans.impl.xb.xsdschema.SelectorDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/KeybaseImpl.class */
public class KeybaseImpl extends AnnotatedImpl implements Keybase {
    private static final long serialVersionUID = 1;
    private static final QName SELECTOR$0 = new QName("http://www.w3.org/2001/XMLSchema", "selector");
    private static final QName FIELD$2 = new QName("http://www.w3.org/2001/XMLSchema", JamXmlElements.FIELD);
    private static final QName NAME$4 = new QName("", "name");

    public KeybaseImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Keybase
    public SelectorDocument.Selector getSelector() {
        synchronized (monitor()) {
            check_orphaned();
            SelectorDocument.Selector target = (SelectorDocument.Selector) get_store().find_element_user(SELECTOR$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Keybase
    public void setSelector(SelectorDocument.Selector selector) {
        generatedSetterHelperImpl(selector, SELECTOR$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Keybase
    public SelectorDocument.Selector addNewSelector() {
        SelectorDocument.Selector target;
        synchronized (monitor()) {
            check_orphaned();
            target = (SelectorDocument.Selector) get_store().add_element_user(SELECTOR$0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Keybase
    public FieldDocument.Field[] getFieldArray() {
        FieldDocument.Field[] result;
        synchronized (monitor()) {
            check_orphaned();
            List targetList = new ArrayList();
            get_store().find_all_element_users(FIELD$2, targetList);
            result = new FieldDocument.Field[targetList.size()];
            targetList.toArray(result);
        }
        return result;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Keybase
    public FieldDocument.Field getFieldArray(int i) {
        FieldDocument.Field target;
        synchronized (monitor()) {
            check_orphaned();
            target = (FieldDocument.Field) get_store().find_element_user(FIELD$2, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Keybase
    public int sizeOfFieldArray() {
        int iCount_elements;
        synchronized (monitor()) {
            check_orphaned();
            iCount_elements = get_store().count_elements(FIELD$2);
        }
        return iCount_elements;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Keybase
    public void setFieldArray(FieldDocument.Field[] fieldArray) {
        check_orphaned();
        arraySetterHelper(fieldArray, FIELD$2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Keybase
    public void setFieldArray(int i, FieldDocument.Field field) {
        generatedSetterHelperImpl(field, FIELD$2, i, (short) 2);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Keybase
    public FieldDocument.Field insertNewField(int i) {
        FieldDocument.Field target;
        synchronized (monitor()) {
            check_orphaned();
            target = (FieldDocument.Field) get_store().insert_element_user(FIELD$2, i);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Keybase
    public FieldDocument.Field addNewField() {
        FieldDocument.Field target;
        synchronized (monitor()) {
            check_orphaned();
            target = (FieldDocument.Field) get_store().add_element_user(FIELD$2);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Keybase
    public void removeField(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(FIELD$2, i);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Keybase
    public String getName() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$4);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Keybase
    public XmlNCName xgetName() {
        XmlNCName target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlNCName) get_store().find_attribute_user(NAME$4);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Keybase
    public void setName(String name) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$4);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(NAME$4);
            }
            target.setStringValue(name);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Keybase
    public void xsetName(XmlNCName name) {
        synchronized (monitor()) {
            check_orphaned();
            XmlNCName target = (XmlNCName) get_store().find_attribute_user(NAME$4);
            if (target == null) {
                target = (XmlNCName) get_store().add_attribute_user(NAME$4);
            }
            target.set(name);
        }
    }
}
