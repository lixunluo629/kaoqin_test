package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.PatternDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/PatternDocumentImpl.class */
public class PatternDocumentImpl extends XmlComplexContentImpl implements PatternDocument {
    private static final long serialVersionUID = 1;
    private static final QName PATTERN$0 = new QName("http://www.w3.org/2001/XMLSchema", "pattern");

    public PatternDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.PatternDocument
    public PatternDocument.Pattern getPattern() {
        synchronized (monitor()) {
            check_orphaned();
            PatternDocument.Pattern target = (PatternDocument.Pattern) get_store().find_element_user(PATTERN$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.PatternDocument
    public void setPattern(PatternDocument.Pattern pattern) {
        generatedSetterHelperImpl(pattern, PATTERN$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.PatternDocument
    public PatternDocument.Pattern addNewPattern() {
        PatternDocument.Pattern target;
        synchronized (monitor()) {
            check_orphaned();
            target = (PatternDocument.Pattern) get_store().add_element_user(PATTERN$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/PatternDocumentImpl$PatternImpl.class */
    public static class PatternImpl extends NoFixedFacetImpl implements PatternDocument.Pattern {
        private static final long serialVersionUID = 1;

        public PatternImpl(SchemaType sType) {
            super(sType);
        }
    }
}
