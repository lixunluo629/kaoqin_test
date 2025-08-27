package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlNCName;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/NotationDocumentImpl.class */
public class NotationDocumentImpl extends XmlComplexContentImpl implements NotationDocument {
    private static final long serialVersionUID = 1;
    private static final QName NOTATION$0 = new QName("http://www.w3.org/2001/XMLSchema", "notation");

    public NotationDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument
    public NotationDocument.Notation getNotation() {
        synchronized (monitor()) {
            check_orphaned();
            NotationDocument.Notation target = (NotationDocument.Notation) get_store().find_element_user(NOTATION$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument
    public void setNotation(NotationDocument.Notation notation) {
        generatedSetterHelperImpl(notation, NOTATION$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument
    public NotationDocument.Notation addNewNotation() {
        NotationDocument.Notation target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NotationDocument.Notation) get_store().add_element_user(NOTATION$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/NotationDocumentImpl$NotationImpl.class */
    public static class NotationImpl extends AnnotatedImpl implements NotationDocument.Notation {
        private static final long serialVersionUID = 1;
        private static final QName NAME$0 = new QName("", "name");
        private static final QName PUBLIC$2 = new QName("", "public");
        private static final QName SYSTEM$4 = new QName("", "system");

        public NotationImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument.Notation
        public String getName() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$0);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument.Notation
        public XmlNCName xgetName() {
            XmlNCName target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlNCName) get_store().find_attribute_user(NAME$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument.Notation
        public void setName(String name) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(NAME$0);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(NAME$0);
                }
                target.setStringValue(name);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument.Notation
        public void xsetName(XmlNCName name) {
            synchronized (monitor()) {
                check_orphaned();
                XmlNCName target = (XmlNCName) get_store().find_attribute_user(NAME$0);
                if (target == null) {
                    target = (XmlNCName) get_store().add_attribute_user(NAME$0);
                }
                target.set(name);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument.Notation
        public String getPublic() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(PUBLIC$2);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument.Notation
        public Public xgetPublic() {
            Public target;
            synchronized (monitor()) {
                check_orphaned();
                target = (Public) get_store().find_attribute_user(PUBLIC$2);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument.Notation
        public boolean isSetPublic() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(PUBLIC$2) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument.Notation
        public void setPublic(String xpublic) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(PUBLIC$2);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(PUBLIC$2);
                }
                target.setStringValue(xpublic);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument.Notation
        public void xsetPublic(Public xpublic) {
            synchronized (monitor()) {
                check_orphaned();
                Public target = (Public) get_store().find_attribute_user(PUBLIC$2);
                if (target == null) {
                    target = (Public) get_store().add_attribute_user(PUBLIC$2);
                }
                target.set(xpublic);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument.Notation
        public void unsetPublic() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(PUBLIC$2);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument.Notation
        public String getSystem() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(SYSTEM$4);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument.Notation
        public XmlAnyURI xgetSystem() {
            XmlAnyURI target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlAnyURI) get_store().find_attribute_user(SYSTEM$4);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument.Notation
        public boolean isSetSystem() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(SYSTEM$4) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument.Notation
        public void setSystem(String system) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(SYSTEM$4);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(SYSTEM$4);
                }
                target.setStringValue(system);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument.Notation
        public void xsetSystem(XmlAnyURI system) {
            synchronized (monitor()) {
                check_orphaned();
                XmlAnyURI target = (XmlAnyURI) get_store().find_attribute_user(SYSTEM$4);
                if (target == null) {
                    target = (XmlAnyURI) get_store().add_attribute_user(SYSTEM$4);
                }
                target.set(system);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument.Notation
        public void unsetSystem() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(SYSTEM$4);
            }
        }
    }
}
