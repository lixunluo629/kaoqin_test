package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.Keybase;
import org.apache.xmlbeans.impl.xb.xsdschema.UniqueDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/UniqueDocumentImpl.class */
public class UniqueDocumentImpl extends XmlComplexContentImpl implements UniqueDocument {
    private static final long serialVersionUID = 1;
    private static final QName UNIQUE$0 = new QName("http://www.w3.org/2001/XMLSchema", "unique");

    public UniqueDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.UniqueDocument
    public Keybase getUnique() {
        synchronized (monitor()) {
            check_orphaned();
            Keybase target = (Keybase) get_store().find_element_user(UNIQUE$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.UniqueDocument
    public void setUnique(Keybase unique) {
        generatedSetterHelperImpl(unique, UNIQUE$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.UniqueDocument
    public Keybase addNewUnique() {
        Keybase target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Keybase) get_store().add_element_user(UNIQUE$0);
        }
        return target;
    }
}
