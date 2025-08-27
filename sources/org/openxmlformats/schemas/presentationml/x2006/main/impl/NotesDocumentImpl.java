package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide;
import org.openxmlformats.schemas.presentationml.x2006.main.NotesDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/impl/NotesDocumentImpl.class */
public class NotesDocumentImpl extends XmlComplexContentImpl implements NotesDocument {
    private static final QName NOTES$0 = new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "notes");

    public NotesDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.NotesDocument
    public CTNotesSlide getNotes() {
        synchronized (monitor()) {
            check_orphaned();
            CTNotesSlide cTNotesSlide = (CTNotesSlide) get_store().find_element_user(NOTES$0, 0);
            if (cTNotesSlide == null) {
                return null;
            }
            return cTNotesSlide;
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.NotesDocument
    public void setNotes(CTNotesSlide cTNotesSlide) {
        synchronized (monitor()) {
            check_orphaned();
            CTNotesSlide cTNotesSlide2 = (CTNotesSlide) get_store().find_element_user(NOTES$0, 0);
            if (cTNotesSlide2 == null) {
                cTNotesSlide2 = (CTNotesSlide) get_store().add_element_user(NOTES$0);
            }
            cTNotesSlide2.set(cTNotesSlide);
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.NotesDocument
    public CTNotesSlide addNewNotes() {
        CTNotesSlide cTNotesSlide;
        synchronized (monitor()) {
            check_orphaned();
            cTNotesSlide = (CTNotesSlide) get_store().add_element_user(NOTES$0);
        }
        return cTNotesSlide;
    }
}
