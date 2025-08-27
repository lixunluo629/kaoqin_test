package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalSimpleType;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/ListDocumentImpl.class */
public class ListDocumentImpl extends XmlComplexContentImpl implements ListDocument {
    private static final long serialVersionUID = 1;
    private static final QName LIST$0 = new QName("http://www.w3.org/2001/XMLSchema", "list");

    public ListDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ListDocument
    public ListDocument.List getList() {
        synchronized (monitor()) {
            check_orphaned();
            ListDocument.List target = (ListDocument.List) get_store().find_element_user(LIST$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ListDocument
    public void setList(ListDocument.List list) {
        generatedSetterHelperImpl(list, LIST$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ListDocument
    public ListDocument.List addNewList() {
        ListDocument.List target;
        synchronized (monitor()) {
            check_orphaned();
            target = (ListDocument.List) get_store().add_element_user(LIST$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/ListDocumentImpl$ListImpl.class */
    public static class ListImpl extends AnnotatedImpl implements ListDocument.List {
        private static final long serialVersionUID = 1;
        private static final QName SIMPLETYPE$0 = new QName("http://www.w3.org/2001/XMLSchema", "simpleType");
        private static final QName ITEMTYPE$2 = new QName("", "itemType");

        public ListImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List
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

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List
        public boolean isSetSimpleType() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().count_elements(SIMPLETYPE$0) != 0;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List
        public void setSimpleType(LocalSimpleType simpleType) {
            generatedSetterHelperImpl(simpleType, SIMPLETYPE$0, 0, (short) 1);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List
        public LocalSimpleType addNewSimpleType() {
            LocalSimpleType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (LocalSimpleType) get_store().add_element_user(SIMPLETYPE$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List
        public void unsetSimpleType() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(SIMPLETYPE$0, 0);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List
        public QName getItemType() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(ITEMTYPE$2);
                if (target == null) {
                    return null;
                }
                return target.getQNameValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List
        public XmlQName xgetItemType() {
            XmlQName target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlQName) get_store().find_attribute_user(ITEMTYPE$2);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List
        public boolean isSetItemType() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(ITEMTYPE$2) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List
        public void setItemType(QName itemType) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(ITEMTYPE$2);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(ITEMTYPE$2);
                }
                target.setQNameValue(itemType);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List
        public void xsetItemType(XmlQName itemType) {
            synchronized (monitor()) {
                check_orphaned();
                XmlQName target = (XmlQName) get_store().find_attribute_user(ITEMTYPE$2);
                if (target == null) {
                    target = (XmlQName) get_store().add_attribute_user(ITEMTYPE$2);
                }
                target.set(itemType);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List
        public void unsetItemType() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(ITEMTYPE$2);
            }
        }
    }
}
