package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.ComplexTypeDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/ComplexTypeDocumentImpl.class */
public class ComplexTypeDocumentImpl extends XmlComplexContentImpl implements ComplexTypeDocument {
    private static final long serialVersionUID = 1;
    private static final QName COMPLEXTYPE$0 = new QName("http://www.w3.org/2001/XMLSchema", "complexType");

    public ComplexTypeDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexTypeDocument
    public TopLevelComplexType getComplexType() {
        synchronized (monitor()) {
            check_orphaned();
            TopLevelComplexType target = (TopLevelComplexType) get_store().find_element_user(COMPLEXTYPE$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexTypeDocument
    public void setComplexType(TopLevelComplexType complexType) {
        generatedSetterHelperImpl(complexType, COMPLEXTYPE$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexTypeDocument
    public TopLevelComplexType addNewComplexType() {
        TopLevelComplexType target;
        synchronized (monitor()) {
            check_orphaned();
            target = (TopLevelComplexType) get_store().add_element_user(COMPLEXTYPE$0);
        }
        return target;
    }
}
