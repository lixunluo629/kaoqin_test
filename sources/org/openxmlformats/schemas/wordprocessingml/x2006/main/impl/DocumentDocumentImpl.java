package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.DocumentDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/DocumentDocumentImpl.class */
public class DocumentDocumentImpl extends XmlComplexContentImpl implements DocumentDocument {
    private static final QName DOCUMENT$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "document");

    public DocumentDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.DocumentDocument
    public CTDocument1 getDocument() {
        synchronized (monitor()) {
            check_orphaned();
            CTDocument1 cTDocument1 = (CTDocument1) get_store().find_element_user(DOCUMENT$0, 0);
            if (cTDocument1 == null) {
                return null;
            }
            return cTDocument1;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.DocumentDocument
    public void setDocument(CTDocument1 cTDocument1) {
        synchronized (monitor()) {
            check_orphaned();
            CTDocument1 cTDocument12 = (CTDocument1) get_store().find_element_user(DOCUMENT$0, 0);
            if (cTDocument12 == null) {
                cTDocument12 = (CTDocument1) get_store().add_element_user(DOCUMENT$0);
            }
            cTDocument12.set(cTDocument1);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.DocumentDocument
    public CTDocument1 addNewDocument() {
        CTDocument1 cTDocument1;
        synchronized (monitor()) {
            check_orphaned();
            cTDocument1 = (CTDocument1) get_store().add_element_user(DOCUMENT$0);
        }
        return cTDocument1;
    }
}
