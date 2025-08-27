package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.Facet;
import org.apache.xmlbeans.impl.xb.xsdschema.MinExclusiveDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/MinExclusiveDocumentImpl.class */
public class MinExclusiveDocumentImpl extends XmlComplexContentImpl implements MinExclusiveDocument {
    private static final long serialVersionUID = 1;
    private static final QName MINEXCLUSIVE$0 = new QName("http://www.w3.org/2001/XMLSchema", "minExclusive");

    public MinExclusiveDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.MinExclusiveDocument
    public Facet getMinExclusive() {
        synchronized (monitor()) {
            check_orphaned();
            Facet target = (Facet) get_store().find_element_user(MINEXCLUSIVE$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.MinExclusiveDocument
    public void setMinExclusive(Facet minExclusive) {
        generatedSetterHelperImpl(minExclusive, MINEXCLUSIVE$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.MinExclusiveDocument
    public Facet addNewMinExclusive() {
        Facet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Facet) get_store().add_element_user(MINEXCLUSIVE$0);
        }
        return target;
    }
}
