package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.KeyrefDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/KeyrefDocumentImpl.class */
public class KeyrefDocumentImpl extends XmlComplexContentImpl implements KeyrefDocument {
    private static final long serialVersionUID = 1;
    private static final QName KEYREF$0 = new QName("http://www.w3.org/2001/XMLSchema", "keyref");

    public KeyrefDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.KeyrefDocument
    public KeyrefDocument.Keyref getKeyref() {
        synchronized (monitor()) {
            check_orphaned();
            KeyrefDocument.Keyref target = (KeyrefDocument.Keyref) get_store().find_element_user(KEYREF$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.KeyrefDocument
    public void setKeyref(KeyrefDocument.Keyref keyref) {
        generatedSetterHelperImpl(keyref, KEYREF$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.KeyrefDocument
    public KeyrefDocument.Keyref addNewKeyref() {
        KeyrefDocument.Keyref target;
        synchronized (monitor()) {
            check_orphaned();
            target = (KeyrefDocument.Keyref) get_store().add_element_user(KEYREF$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/KeyrefDocumentImpl$KeyrefImpl.class */
    public static class KeyrefImpl extends KeybaseImpl implements KeyrefDocument.Keyref {
        private static final long serialVersionUID = 1;
        private static final QName REFER$0 = new QName("", "refer");

        public KeyrefImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.KeyrefDocument.Keyref
        public QName getRefer() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(REFER$0);
                if (target == null) {
                    return null;
                }
                return target.getQNameValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.KeyrefDocument.Keyref
        public XmlQName xgetRefer() {
            XmlQName target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlQName) get_store().find_attribute_user(REFER$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.KeyrefDocument.Keyref
        public void setRefer(QName refer) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(REFER$0);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(REFER$0);
                }
                target.setQNameValue(refer);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.KeyrefDocument.Keyref
        public void xsetRefer(XmlQName refer) {
            synchronized (monitor()) {
                check_orphaned();
                XmlQName target = (XmlQName) get_store().find_attribute_user(REFER$0);
                if (target == null) {
                    target = (XmlQName) get_store().add_attribute_user(REFER$0);
                }
                target.set(refer);
            }
        }
    }
}
