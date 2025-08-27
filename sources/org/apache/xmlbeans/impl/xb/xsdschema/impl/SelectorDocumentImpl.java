package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.JavaStringHolderEx;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.SelectorDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/SelectorDocumentImpl.class */
public class SelectorDocumentImpl extends XmlComplexContentImpl implements SelectorDocument {
    private static final long serialVersionUID = 1;
    private static final QName SELECTOR$0 = new QName("http://www.w3.org/2001/XMLSchema", "selector");

    public SelectorDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SelectorDocument
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

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SelectorDocument
    public void setSelector(SelectorDocument.Selector selector) {
        generatedSetterHelperImpl(selector, SELECTOR$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SelectorDocument
    public SelectorDocument.Selector addNewSelector() {
        SelectorDocument.Selector target;
        synchronized (monitor()) {
            check_orphaned();
            target = (SelectorDocument.Selector) get_store().add_element_user(SELECTOR$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/SelectorDocumentImpl$SelectorImpl.class */
    public static class SelectorImpl extends AnnotatedImpl implements SelectorDocument.Selector {
        private static final long serialVersionUID = 1;
        private static final QName XPATH$0 = new QName("", "xpath");

        public SelectorImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SelectorDocument.Selector
        public String getXpath() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(XPATH$0);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SelectorDocument.Selector
        public SelectorDocument.Selector.Xpath xgetXpath() {
            SelectorDocument.Selector.Xpath target;
            synchronized (monitor()) {
                check_orphaned();
                target = (SelectorDocument.Selector.Xpath) get_store().find_attribute_user(XPATH$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SelectorDocument.Selector
        public void setXpath(String xpath) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(XPATH$0);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(XPATH$0);
                }
                target.setStringValue(xpath);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SelectorDocument.Selector
        public void xsetXpath(SelectorDocument.Selector.Xpath xpath) {
            synchronized (monitor()) {
                check_orphaned();
                SelectorDocument.Selector.Xpath target = (SelectorDocument.Selector.Xpath) get_store().find_attribute_user(XPATH$0);
                if (target == null) {
                    target = (SelectorDocument.Selector.Xpath) get_store().add_attribute_user(XPATH$0);
                }
                target.set(xpath);
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/SelectorDocumentImpl$SelectorImpl$XpathImpl.class */
        public static class XpathImpl extends JavaStringHolderEx implements SelectorDocument.Selector.Xpath {
            private static final long serialVersionUID = 1;

            public XpathImpl(SchemaType sType) {
                super(sType, false);
            }

            protected XpathImpl(SchemaType sType, boolean b) {
                super(sType, b);
            }
        }
    }
}
