package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.apache.xmlbeans.impl.xb.xsdschema.AllDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/AllDocumentImpl.class */
public class AllDocumentImpl extends XmlComplexContentImpl implements AllDocument {
    private static final long serialVersionUID = 1;
    private static final QName ALL$0 = new QName("http://www.w3.org/2001/XMLSchema", "all");

    public AllDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AllDocument
    public All getAll() {
        synchronized (monitor()) {
            check_orphaned();
            All target = (All) get_store().find_element_user(ALL$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AllDocument
    public void setAll(All all) {
        generatedSetterHelperImpl(all, ALL$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AllDocument
    public All addNewAll() {
        All target;
        synchronized (monitor()) {
            check_orphaned();
            target = (All) get_store().add_element_user(ALL$0);
        }
        return target;
    }
}
