package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.EnumerationDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.NoFixedFacet;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/EnumerationDocumentImpl.class */
public class EnumerationDocumentImpl extends XmlComplexContentImpl implements EnumerationDocument {
    private static final long serialVersionUID = 1;
    private static final QName ENUMERATION$0 = new QName("http://www.w3.org/2001/XMLSchema", "enumeration");

    public EnumerationDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.EnumerationDocument
    public NoFixedFacet getEnumeration() {
        synchronized (monitor()) {
            check_orphaned();
            NoFixedFacet target = (NoFixedFacet) get_store().find_element_user(ENUMERATION$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.EnumerationDocument
    public void setEnumeration(NoFixedFacet enumeration) {
        generatedSetterHelperImpl(enumeration, ENUMERATION$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.EnumerationDocument
    public NoFixedFacet addNewEnumeration() {
        NoFixedFacet target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NoFixedFacet) get_store().add_element_user(ENUMERATION$0);
        }
        return target;
    }
}
