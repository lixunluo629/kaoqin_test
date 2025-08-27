package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.ElementDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelElement;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/ElementDocumentImpl.class */
public class ElementDocumentImpl extends XmlComplexContentImpl implements ElementDocument {
    private static final long serialVersionUID = 1;
    private static final QName ELEMENT$0 = new QName("http://www.w3.org/2001/XMLSchema", "element");

    public ElementDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ElementDocument
    public TopLevelElement getElement() {
        synchronized (monitor()) {
            check_orphaned();
            TopLevelElement target = (TopLevelElement) get_store().find_element_user(ELEMENT$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ElementDocument
    public void setElement(TopLevelElement element) {
        generatedSetterHelperImpl(element, ELEMENT$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ElementDocument
    public TopLevelElement addNewElement() {
        TopLevelElement target;
        synchronized (monitor()) {
            check_orphaned();
            target = (TopLevelElement) get_store().add_element_user(ELEMENT$0);
        }
        return target;
    }
}
