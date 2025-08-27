package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.Facet;
import org.apache.xmlbeans.impl.xb.xsdschema.MaxExclusiveDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/MaxExclusiveDocumentImpl.class */
public class MaxExclusiveDocumentImpl extends XmlComplexContentImpl implements MaxExclusiveDocument {
    private static final long serialVersionUID = 1;
    private static final QName MAXEXCLUSIVE$0 = new QName("http://www.w3.org/2001/XMLSchema", "maxExclusive");

    public MaxExclusiveDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.MaxExclusiveDocument
    public Facet getMaxExclusive() {
        synchronized (monitor()) {
            check_orphaned();
            Facet target = (Facet) get_store().find_element_user(MAXEXCLUSIVE$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.MaxExclusiveDocument
    public void setMaxExclusive(Facet maxExclusive) {
        generatedSetterHelperImpl(maxExclusive, MAXEXCLUSIVE$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.MaxExclusiveDocument
    public Facet addNewMaxExclusive() {
        Facet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Facet) get_store().add_element_user(MAXEXCLUSIVE$0);
        }
        return target;
    }
}
