package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import com.itextpdf.kernel.xmp.PdfConst;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlLanguage;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/DocumentationDocumentImpl.class */
public class DocumentationDocumentImpl extends XmlComplexContentImpl implements DocumentationDocument {
    private static final long serialVersionUID = 1;
    private static final QName DOCUMENTATION$0 = new QName("http://www.w3.org/2001/XMLSchema", "documentation");

    public DocumentationDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument
    public DocumentationDocument.Documentation getDocumentation() {
        synchronized (monitor()) {
            check_orphaned();
            DocumentationDocument.Documentation target = (DocumentationDocument.Documentation) get_store().find_element_user(DOCUMENTATION$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument
    public void setDocumentation(DocumentationDocument.Documentation documentation) {
        generatedSetterHelperImpl(documentation, DOCUMENTATION$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument
    public DocumentationDocument.Documentation addNewDocumentation() {
        DocumentationDocument.Documentation target;
        synchronized (monitor()) {
            check_orphaned();
            target = (DocumentationDocument.Documentation) get_store().add_element_user(DOCUMENTATION$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/DocumentationDocumentImpl$DocumentationImpl.class */
    public static class DocumentationImpl extends XmlComplexContentImpl implements DocumentationDocument.Documentation {
        private static final long serialVersionUID = 1;
        private static final QName SOURCE$0 = new QName("", PdfConst.Source);
        private static final QName LANG$2 = new QName("http://www.w3.org/XML/1998/namespace", AbstractHtmlElementTag.LANG_ATTRIBUTE);

        public DocumentationImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation
        public String getSource() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(SOURCE$0);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation
        public XmlAnyURI xgetSource() {
            XmlAnyURI target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlAnyURI) get_store().find_attribute_user(SOURCE$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation
        public boolean isSetSource() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(SOURCE$0) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation
        public void setSource(String source) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(SOURCE$0);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(SOURCE$0);
                }
                target.setStringValue(source);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation
        public void xsetSource(XmlAnyURI source) {
            synchronized (monitor()) {
                check_orphaned();
                XmlAnyURI target = (XmlAnyURI) get_store().find_attribute_user(SOURCE$0);
                if (target == null) {
                    target = (XmlAnyURI) get_store().add_attribute_user(SOURCE$0);
                }
                target.set(source);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation
        public void unsetSource() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(SOURCE$0);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation
        public String getLang() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(LANG$2);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation
        public XmlLanguage xgetLang() {
            XmlLanguage target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlLanguage) get_store().find_attribute_user(LANG$2);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation
        public boolean isSetLang() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(LANG$2) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation
        public void setLang(String lang) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(LANG$2);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(LANG$2);
                }
                target.setStringValue(lang);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation
        public void xsetLang(XmlLanguage lang) {
            synchronized (monitor()) {
                check_orphaned();
                XmlLanguage target = (XmlLanguage) get_store().find_attribute_user(LANG$2);
                if (target == null) {
                    target = (XmlLanguage) get_store().add_attribute_user(LANG$2);
                }
                target.set(lang);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument.Documentation
        public void unsetLang() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(LANG$2);
            }
        }
    }
}
