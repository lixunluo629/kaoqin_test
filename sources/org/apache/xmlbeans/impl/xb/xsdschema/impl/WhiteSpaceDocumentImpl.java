package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.WhiteSpaceDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/WhiteSpaceDocumentImpl.class */
public class WhiteSpaceDocumentImpl extends XmlComplexContentImpl implements WhiteSpaceDocument {
    private static final long serialVersionUID = 1;
    private static final QName WHITESPACE$0 = new QName("http://www.w3.org/2001/XMLSchema", "whiteSpace");

    public WhiteSpaceDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.WhiteSpaceDocument
    public WhiteSpaceDocument.WhiteSpace getWhiteSpace() {
        synchronized (monitor()) {
            check_orphaned();
            WhiteSpaceDocument.WhiteSpace target = (WhiteSpaceDocument.WhiteSpace) get_store().find_element_user(WHITESPACE$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.WhiteSpaceDocument
    public void setWhiteSpace(WhiteSpaceDocument.WhiteSpace whiteSpace) {
        generatedSetterHelperImpl(whiteSpace, WHITESPACE$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.WhiteSpaceDocument
    public WhiteSpaceDocument.WhiteSpace addNewWhiteSpace() {
        WhiteSpaceDocument.WhiteSpace target;
        synchronized (monitor()) {
            check_orphaned();
            target = (WhiteSpaceDocument.WhiteSpace) get_store().add_element_user(WHITESPACE$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/WhiteSpaceDocumentImpl$WhiteSpaceImpl.class */
    public static class WhiteSpaceImpl extends FacetImpl implements WhiteSpaceDocument.WhiteSpace {
        private static final long serialVersionUID = 1;

        public WhiteSpaceImpl(SchemaType sType) {
            super(sType);
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/WhiteSpaceDocumentImpl$WhiteSpaceImpl$ValueImpl.class */
        public static class ValueImpl extends JavaStringEnumerationHolderEx implements WhiteSpaceDocument.WhiteSpace.Value {
            private static final long serialVersionUID = 1;

            public ValueImpl(SchemaType sType) {
                super(sType, false);
            }

            protected ValueImpl(SchemaType sType, boolean b) {
                super(sType, b);
            }
        }
    }
}
