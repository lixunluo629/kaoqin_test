package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.LengthDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.NumFacet;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/LengthDocumentImpl.class */
public class LengthDocumentImpl extends XmlComplexContentImpl implements LengthDocument {
    private static final long serialVersionUID = 1;
    private static final QName LENGTH$0 = new QName("http://www.w3.org/2001/XMLSchema", "length");

    public LengthDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.LengthDocument
    public NumFacet getLength() {
        synchronized (monitor()) {
            check_orphaned();
            NumFacet target = (NumFacet) get_store().find_element_user(LENGTH$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.LengthDocument
    public void setLength(NumFacet length) {
        generatedSetterHelperImpl(length, LENGTH$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.LengthDocument
    public NumFacet addNewLength() {
        NumFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NumFacet) get_store().add_element_user(LENGTH$0);
        }
        return target;
    }
}
