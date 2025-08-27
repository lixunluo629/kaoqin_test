package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.KeyDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.Keybase;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/KeyDocumentImpl.class */
public class KeyDocumentImpl extends XmlComplexContentImpl implements KeyDocument {
    private static final long serialVersionUID = 1;
    private static final QName KEY$0 = new QName("http://www.w3.org/2001/XMLSchema", "key");

    public KeyDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.KeyDocument
    public Keybase getKey() {
        synchronized (monitor()) {
            check_orphaned();
            Keybase target = (Keybase) get_store().find_element_user(KEY$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.KeyDocument
    public void setKey(Keybase key) {
        generatedSetterHelperImpl(key, KEY$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.KeyDocument
    public Keybase addNewKey() {
        Keybase target;
        synchronized (monitor()) {
            check_orphaned();
            target = (Keybase) get_store().add_element_user(KEY$0);
        }
        return target;
    }
}
