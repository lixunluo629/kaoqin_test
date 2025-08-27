package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.NumberingDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/NumberingDocumentImpl.class */
public class NumberingDocumentImpl extends XmlComplexContentImpl implements NumberingDocument {
    private static final QName NUMBERING$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "numbering");

    public NumberingDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.NumberingDocument
    public CTNumbering getNumbering() {
        synchronized (monitor()) {
            check_orphaned();
            CTNumbering cTNumbering = (CTNumbering) get_store().find_element_user(NUMBERING$0, 0);
            if (cTNumbering == null) {
                return null;
            }
            return cTNumbering;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.NumberingDocument
    public void setNumbering(CTNumbering cTNumbering) {
        synchronized (monitor()) {
            check_orphaned();
            CTNumbering cTNumbering2 = (CTNumbering) get_store().find_element_user(NUMBERING$0, 0);
            if (cTNumbering2 == null) {
                cTNumbering2 = (CTNumbering) get_store().add_element_user(NUMBERING$0);
            }
            cTNumbering2.set(cTNumbering);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.NumberingDocument
    public CTNumbering addNewNumbering() {
        CTNumbering cTNumbering;
        synchronized (monitor()) {
            check_orphaned();
            cTNumbering = (CTNumbering) get_store().add_element_user(NUMBERING$0);
        }
        return cTNumbering;
    }
}
