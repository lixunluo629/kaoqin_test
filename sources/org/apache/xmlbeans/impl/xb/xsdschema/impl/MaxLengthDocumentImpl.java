package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.MaxLengthDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.NumFacet;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/MaxLengthDocumentImpl.class */
public class MaxLengthDocumentImpl extends XmlComplexContentImpl implements MaxLengthDocument {
    private static final long serialVersionUID = 1;
    private static final QName MAXLENGTH$0 = new QName("http://www.w3.org/2001/XMLSchema", "maxLength");

    public MaxLengthDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.MaxLengthDocument
    public NumFacet getMaxLength() {
        synchronized (monitor()) {
            check_orphaned();
            NumFacet target = (NumFacet) get_store().find_element_user(MAXLENGTH$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.MaxLengthDocument
    public void setMaxLength(NumFacet maxLength) {
        generatedSetterHelperImpl(maxLength, MAXLENGTH$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.MaxLengthDocument
    public NumFacet addNewMaxLength() {
        NumFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NumFacet) get_store().add_element_user(MAXLENGTH$0);
        }
        return target;
    }
}
