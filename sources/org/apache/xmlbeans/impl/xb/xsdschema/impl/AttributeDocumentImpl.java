package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.AttributeDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelAttribute;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/AttributeDocumentImpl.class */
public class AttributeDocumentImpl extends XmlComplexContentImpl implements AttributeDocument {
    private static final long serialVersionUID = 1;
    private static final QName ATTRIBUTE$0 = new QName("http://www.w3.org/2001/XMLSchema", BeanDefinitionParserDelegate.QUALIFIER_ATTRIBUTE_ELEMENT);

    public AttributeDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AttributeDocument
    public TopLevelAttribute getAttribute() {
        synchronized (monitor()) {
            check_orphaned();
            TopLevelAttribute target = (TopLevelAttribute) get_store().find_element_user(ATTRIBUTE$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AttributeDocument
    public void setAttribute(TopLevelAttribute attribute) {
        generatedSetterHelperImpl(attribute, ATTRIBUTE$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AttributeDocument
    public TopLevelAttribute addNewAttribute() {
        TopLevelAttribute target;
        synchronized (monitor()) {
            check_orphaned();
            target = (TopLevelAttribute) get_store().add_element_user(ATTRIBUTE$0);
        }
        return target;
    }
}
