package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.TotalDigitsDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/TotalDigitsDocumentImpl.class */
public class TotalDigitsDocumentImpl extends XmlComplexContentImpl implements TotalDigitsDocument {
    private static final long serialVersionUID = 1;
    private static final QName TOTALDIGITS$0 = new QName("http://www.w3.org/2001/XMLSchema", "totalDigits");

    public TotalDigitsDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.TotalDigitsDocument
    public TotalDigitsDocument.TotalDigits getTotalDigits() {
        synchronized (monitor()) {
            check_orphaned();
            TotalDigitsDocument.TotalDigits target = (TotalDigitsDocument.TotalDigits) get_store().find_element_user(TOTALDIGITS$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.TotalDigitsDocument
    public void setTotalDigits(TotalDigitsDocument.TotalDigits totalDigits) {
        generatedSetterHelperImpl(totalDigits, TOTALDIGITS$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.TotalDigitsDocument
    public TotalDigitsDocument.TotalDigits addNewTotalDigits() {
        TotalDigitsDocument.TotalDigits target;
        synchronized (monitor()) {
            check_orphaned();
            target = (TotalDigitsDocument.TotalDigits) get_store().add_element_user(TOTALDIGITS$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/TotalDigitsDocumentImpl$TotalDigitsImpl.class */
    public static class TotalDigitsImpl extends NumFacetImpl implements TotalDigitsDocument.TotalDigits {
        private static final long serialVersionUID = 1;

        public TotalDigitsImpl(SchemaType sType) {
            super(sType);
        }
    }
}
