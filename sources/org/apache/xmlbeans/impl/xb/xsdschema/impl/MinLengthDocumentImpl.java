package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.MinLengthDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.NumFacet;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/MinLengthDocumentImpl.class */
public class MinLengthDocumentImpl extends XmlComplexContentImpl implements MinLengthDocument {
    private static final long serialVersionUID = 1;
    private static final QName MINLENGTH$0 = new QName("http://www.w3.org/2001/XMLSchema", "minLength");

    public MinLengthDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.MinLengthDocument
    public NumFacet getMinLength() {
        synchronized (monitor()) {
            check_orphaned();
            NumFacet target = (NumFacet) get_store().find_element_user(MINLENGTH$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.MinLengthDocument
    public void setMinLength(NumFacet minLength) {
        generatedSetterHelperImpl(minLength, MINLENGTH$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.MinLengthDocument
    public NumFacet addNewMinLength() {
        NumFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NumFacet) get_store().add_element_user(MINLENGTH$0);
        }
        return target;
    }
}
