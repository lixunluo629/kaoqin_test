package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleContentDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleExtensionType;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleRestrictionType;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/SimpleContentDocumentImpl.class */
public class SimpleContentDocumentImpl extends XmlComplexContentImpl implements SimpleContentDocument {
    private static final long serialVersionUID = 1;
    private static final QName SIMPLECONTENT$0 = new QName("http://www.w3.org/2001/XMLSchema", "simpleContent");

    public SimpleContentDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleContentDocument
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

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleContentDocument
    public void setSimpleContent(SimpleContentDocument.SimpleContent simpleContent) {
        generatedSetterHelperImpl(simpleContent, SIMPLECONTENT$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleContentDocument
    public SimpleContentDocument.SimpleContent addNewSimpleContent() {
        SimpleContentDocument.SimpleContent target;
        synchronized (monitor()) {
            check_orphaned();
            target = (SimpleContentDocument.SimpleContent) get_store().add_element_user(SIMPLECONTENT$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/SimpleContentDocumentImpl$SimpleContentImpl.class */
    public static class SimpleContentImpl extends AnnotatedImpl implements SimpleContentDocument.SimpleContent {
        private static final long serialVersionUID = 1;
        private static final QName RESTRICTION$0 = new QName("http://www.w3.org/2001/XMLSchema", "restriction");
        private static final QName EXTENSION$2 = new QName("http://www.w3.org/2001/XMLSchema", "extension");

        public SimpleContentImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleContentDocument.SimpleContent
        public SimpleRestrictionType getRestriction() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleRestrictionType target = (SimpleRestrictionType) get_store().find_element_user(RESTRICTION$0, 0);
                if (target == null) {
                    return null;
                }
                return target;
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleContentDocument.SimpleContent
        public boolean isSetRestriction() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().count_elements(RESTRICTION$0) != 0;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleContentDocument.SimpleContent
        public void setRestriction(SimpleRestrictionType restriction) {
            generatedSetterHelperImpl(restriction, RESTRICTION$0, 0, (short) 1);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleContentDocument.SimpleContent
        public SimpleRestrictionType addNewRestriction() {
            SimpleRestrictionType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (SimpleRestrictionType) get_store().add_element_user(RESTRICTION$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleContentDocument.SimpleContent
        public void unsetRestriction() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(RESTRICTION$0, 0);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleContentDocument.SimpleContent
        public SimpleExtensionType getExtension() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleExtensionType target = (SimpleExtensionType) get_store().find_element_user(EXTENSION$2, 0);
                if (target == null) {
                    return null;
                }
                return target;
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleContentDocument.SimpleContent
        public boolean isSetExtension() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().count_elements(EXTENSION$2) != 0;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleContentDocument.SimpleContent
        public void setExtension(SimpleExtensionType extension) {
            generatedSetterHelperImpl(extension, EXTENSION$2, 0, (short) 1);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleContentDocument.SimpleContent
        public SimpleExtensionType addNewExtension() {
            SimpleExtensionType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (SimpleExtensionType) get_store().add_element_user(EXTENSION$2);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleContentDocument.SimpleContent
        public void unsetExtension() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(EXTENSION$2, 0);
            }
        }
    }
}
