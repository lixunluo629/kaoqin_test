package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.Facet;
import org.apache.xmlbeans.impl.xb.xsdschema.MinInclusiveDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/MinInclusiveDocumentImpl.class */
public class MinInclusiveDocumentImpl extends XmlComplexContentImpl implements MinInclusiveDocument {
    private static final long serialVersionUID = 1;
    private static final QName MININCLUSIVE$0 = new QName("http://www.w3.org/2001/XMLSchema", "minInclusive");

    public MinInclusiveDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.MinInclusiveDocument
    public Facet getMinInclusive() {
        synchronized (monitor()) {
            check_orphaned();
            Facet target = (Facet) get_store().find_element_user(MININCLUSIVE$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.MinInclusiveDocument
    public void setMinInclusive(Facet minInclusive) {
        generatedSetterHelperImpl(minInclusive, MININCLUSIVE$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.MinInclusiveDocument
    public Facet addNewMinInclusive() {
        Facet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Facet) get_store().add_element_user(MININCLUSIVE$0);
        }
        return target;
    }
}
