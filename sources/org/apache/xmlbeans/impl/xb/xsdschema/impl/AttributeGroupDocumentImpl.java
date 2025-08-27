package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroupDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.NamedAttributeGroup;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/AttributeGroupDocumentImpl.class */
public class AttributeGroupDocumentImpl extends XmlComplexContentImpl implements AttributeGroupDocument {
    private static final long serialVersionUID = 1;
    private static final QName ATTRIBUTEGROUP$0 = new QName("http://www.w3.org/2001/XMLSchema", "attributeGroup");

    public AttributeGroupDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroupDocument
    public NamedAttributeGroup getAttributeGroup() {
        synchronized (monitor()) {
            check_orphaned();
            NamedAttributeGroup target = (NamedAttributeGroup) get_store().find_element_user(ATTRIBUTEGROUP$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroupDocument
    public void setAttributeGroup(NamedAttributeGroup attributeGroup) {
        generatedSetterHelperImpl(attributeGroup, ATTRIBUTEGROUP$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroupDocument
    public NamedAttributeGroup addNewAttributeGroup() {
        NamedAttributeGroup target;
        synchronized (monitor()) {
            check_orphaned();
            target = (NamedAttributeGroup) get_store().add_element_user(ATTRIBUTEGROUP$0);
        }
        return target;
    }
}
