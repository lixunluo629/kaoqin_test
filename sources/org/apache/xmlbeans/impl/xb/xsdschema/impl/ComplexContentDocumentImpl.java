package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.ComplexRestrictionType;
import org.apache.xmlbeans.impl.xb.xsdschema.ExtensionType;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/ComplexContentDocumentImpl.class */
public class ComplexContentDocumentImpl extends XmlComplexContentImpl implements ComplexContentDocument {
    private static final long serialVersionUID = 1;
    private static final QName COMPLEXCONTENT$0 = new QName("http://www.w3.org/2001/XMLSchema", "complexContent");

    public ComplexContentDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument
    public ComplexContentDocument.ComplexContent getComplexContent() {
        synchronized (monitor()) {
            check_orphaned();
            ComplexContentDocument.ComplexContent target = (ComplexContentDocument.ComplexContent) get_store().find_element_user(COMPLEXCONTENT$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument
    public void setComplexContent(ComplexContentDocument.ComplexContent complexContent) {
        generatedSetterHelperImpl(complexContent, COMPLEXCONTENT$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument
    public ComplexContentDocument.ComplexContent addNewComplexContent() {
        ComplexContentDocument.ComplexContent target;
        synchronized (monitor()) {
            check_orphaned();
            target = (ComplexContentDocument.ComplexContent) get_store().add_element_user(COMPLEXCONTENT$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/ComplexContentDocumentImpl$ComplexContentImpl.class */
    public static class ComplexContentImpl extends AnnotatedImpl implements ComplexContentDocument.ComplexContent {
        private static final long serialVersionUID = 1;
        private static final QName RESTRICTION$0 = new QName("http://www.w3.org/2001/XMLSchema", "restriction");
        private static final QName EXTENSION$2 = new QName("http://www.w3.org/2001/XMLSchema", "extension");
        private static final QName MIXED$4 = new QName("", "mixed");

        public ComplexContentImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument.ComplexContent
        public ComplexRestrictionType getRestriction() {
            synchronized (monitor()) {
                check_orphaned();
                ComplexRestrictionType target = (ComplexRestrictionType) get_store().find_element_user(RESTRICTION$0, 0);
                if (target == null) {
                    return null;
                }
                return target;
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument.ComplexContent
        public boolean isSetRestriction() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().count_elements(RESTRICTION$0) != 0;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument.ComplexContent
        public void setRestriction(ComplexRestrictionType restriction) {
            generatedSetterHelperImpl(restriction, RESTRICTION$0, 0, (short) 1);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument.ComplexContent
        public ComplexRestrictionType addNewRestriction() {
            ComplexRestrictionType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (ComplexRestrictionType) get_store().add_element_user(RESTRICTION$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument.ComplexContent
        public void unsetRestriction() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(RESTRICTION$0, 0);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument.ComplexContent
        public ExtensionType getExtension() {
            synchronized (monitor()) {
                check_orphaned();
                ExtensionType target = (ExtensionType) get_store().find_element_user(EXTENSION$2, 0);
                if (target == null) {
                    return null;
                }
                return target;
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument.ComplexContent
        public boolean isSetExtension() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().count_elements(EXTENSION$2) != 0;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument.ComplexContent
        public void setExtension(ExtensionType extension) {
            generatedSetterHelperImpl(extension, EXTENSION$2, 0, (short) 1);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument.ComplexContent
        public ExtensionType addNewExtension() {
            ExtensionType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (ExtensionType) get_store().add_element_user(EXTENSION$2);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument.ComplexContent
        public void unsetExtension() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(EXTENSION$2, 0);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument.ComplexContent
        public boolean getMixed() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(MIXED$4);
                if (target == null) {
                    return false;
                }
                return target.getBooleanValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument.ComplexContent
        public XmlBoolean xgetMixed() {
            XmlBoolean target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlBoolean) get_store().find_attribute_user(MIXED$4);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument.ComplexContent
        public boolean isSetMixed() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(MIXED$4) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument.ComplexContent
        public void setMixed(boolean mixed) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(MIXED$4);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(MIXED$4);
                }
                target.setBooleanValue(mixed);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument.ComplexContent
        public void xsetMixed(XmlBoolean mixed) {
            synchronized (monitor()) {
                check_orphaned();
                XmlBoolean target = (XmlBoolean) get_store().find_attribute_user(MIXED$4);
                if (target == null) {
                    target = (XmlBoolean) get_store().add_attribute_user(MIXED$4);
                }
                target.set(mixed);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument.ComplexContent
        public void unsetMixed() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(MIXED$4);
            }
        }
    }
}
