package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleTypeDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelSimpleType;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/SimpleTypeDocumentImpl.class */
public class SimpleTypeDocumentImpl extends XmlComplexContentImpl implements SimpleTypeDocument {
    private static final long serialVersionUID = 1;
    private static final QName SIMPLETYPE$0 = new QName("http://www.w3.org/2001/XMLSchema", "simpleType");

    public SimpleTypeDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleTypeDocument
    public TopLevelSimpleType getSimpleType() {
        synchronized (monitor()) {
            check_orphaned();
            TopLevelSimpleType target = (TopLevelSimpleType) get_store().find_element_user(SIMPLETYPE$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleTypeDocument
    public void setSimpleType(TopLevelSimpleType simpleType) {
        generatedSetterHelperImpl(simpleType, SIMPLETYPE$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleTypeDocument
    public TopLevelSimpleType addNewSimpleType() {
        TopLevelSimpleType target;
        synchronized (monitor()) {
            check_orphaned();
            target = (TopLevelSimpleType) get_store().add_element_user(SIMPLETYPE$0);
        }
        return target;
    }
}
