package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.FractionDigitsDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.NumFacet;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/FractionDigitsDocumentImpl.class */
public class FractionDigitsDocumentImpl extends XmlComplexContentImpl implements FractionDigitsDocument {
    private static final long serialVersionUID = 1;
    private static final QName FRACTIONDIGITS$0 = new QName("http://www.w3.org/2001/XMLSchema", "fractionDigits");

    public FractionDigitsDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.FractionDigitsDocument
    public NumFacet getFractionDigits() {
        synchronized (monitor()) {
            check_orphaned();
            NumFacet target = (NumFacet) get_store().find_element_user(FRACTIONDIGITS$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.FractionDigitsDocument
    public void setFractionDigits(NumFacet fractionDigits) {
        generatedSetterHelperImpl(fractionDigits, FRACTIONDIGITS$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.FractionDigitsDocument
    public NumFacet addNewFractionDigits() {
        NumFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NumFacet) get_store().add_element_user(FRACTIONDIGITS$0);
        }
        return target;
    }
}
