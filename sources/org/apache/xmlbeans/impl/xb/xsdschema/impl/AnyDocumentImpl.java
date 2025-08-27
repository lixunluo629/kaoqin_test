package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import java.math.BigInteger;
import javax.xml.namespace.QName;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlNonNegativeInteger;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.AllNNI;
import org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/AnyDocumentImpl.class */
public class AnyDocumentImpl extends XmlComplexContentImpl implements AnyDocument {
    private static final long serialVersionUID = 1;
    private static final QName ANY$0 = new QName("http://www.w3.org/2001/XMLSchema", Languages.ANY);

    public AnyDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument
    public AnyDocument.Any getAny() {
        synchronized (monitor()) {
            check_orphaned();
            AnyDocument.Any target = (AnyDocument.Any) get_store().find_element_user(ANY$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument
    public void setAny(AnyDocument.Any any) {
        generatedSetterHelperImpl(any, ANY$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument
    public AnyDocument.Any addNewAny() {
        AnyDocument.Any target;
        synchronized (monitor()) {
            check_orphaned();
            target = (AnyDocument.Any) get_store().add_element_user(ANY$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/AnyDocumentImpl$AnyImpl.class */
    public static class AnyImpl extends WildcardImpl implements AnyDocument.Any {
        private static final long serialVersionUID = 1;
        private static final QName MINOCCURS$0 = new QName("", "minOccurs");
        private static final QName MAXOCCURS$2 = new QName("", "maxOccurs");

        public AnyImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any
        public BigInteger getMinOccurs() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(MINOCCURS$0);
                if (target == null) {
                    target = (SimpleValue) get_default_attribute_value(MINOCCURS$0);
                }
                if (target == null) {
                    return null;
                }
                return target.getBigIntegerValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any
        public XmlNonNegativeInteger xgetMinOccurs() {
            XmlNonNegativeInteger xmlNonNegativeInteger;
            synchronized (monitor()) {
                check_orphaned();
                XmlNonNegativeInteger target = (XmlNonNegativeInteger) get_store().find_attribute_user(MINOCCURS$0);
                if (target == null) {
                    target = (XmlNonNegativeInteger) get_default_attribute_value(MINOCCURS$0);
                }
                xmlNonNegativeInteger = target;
            }
            return xmlNonNegativeInteger;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any
        public boolean isSetMinOccurs() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(MINOCCURS$0) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any
        public void setMinOccurs(BigInteger minOccurs) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(MINOCCURS$0);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(MINOCCURS$0);
                }
                target.setBigIntegerValue(minOccurs);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any
        public void xsetMinOccurs(XmlNonNegativeInteger minOccurs) {
            synchronized (monitor()) {
                check_orphaned();
                XmlNonNegativeInteger target = (XmlNonNegativeInteger) get_store().find_attribute_user(MINOCCURS$0);
                if (target == null) {
                    target = (XmlNonNegativeInteger) get_store().add_attribute_user(MINOCCURS$0);
                }
                target.set(minOccurs);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any
        public void unsetMinOccurs() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(MINOCCURS$0);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any
        public Object getMaxOccurs() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(MAXOCCURS$2);
                if (target == null) {
                    target = (SimpleValue) get_default_attribute_value(MAXOCCURS$2);
                }
                if (target == null) {
                    return null;
                }
                return target.getObjectValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any
        public AllNNI xgetMaxOccurs() {
            AllNNI allNNI;
            synchronized (monitor()) {
                check_orphaned();
                AllNNI target = (AllNNI) get_store().find_attribute_user(MAXOCCURS$2);
                if (target == null) {
                    target = (AllNNI) get_default_attribute_value(MAXOCCURS$2);
                }
                allNNI = target;
            }
            return allNNI;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any
        public boolean isSetMaxOccurs() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(MAXOCCURS$2) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any
        public void setMaxOccurs(Object maxOccurs) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(MAXOCCURS$2);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(MAXOCCURS$2);
                }
                target.setObjectValue(maxOccurs);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any
        public void xsetMaxOccurs(AllNNI maxOccurs) {
            synchronized (monitor()) {
                check_orphaned();
                AllNNI target = (AllNNI) get_store().find_attribute_user(MAXOCCURS$2);
                if (target == null) {
                    target = (AllNNI) get_store().add_attribute_user(MAXOCCURS$2);
                }
                target.set(maxOccurs);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnyDocument.Any
        public void unsetMaxOccurs() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(MAXOCCURS$2);
            }
        }
    }
}
