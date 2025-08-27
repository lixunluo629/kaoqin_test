package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMaster;
import org.openxmlformats.schemas.presentationml.x2006.main.NotesMasterDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/impl/NotesMasterDocumentImpl.class */
public class NotesMasterDocumentImpl extends XmlComplexContentImpl implements NotesMasterDocument {
    private static final QName NOTESMASTER$0 = new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "notesMaster");

    public NotesMasterDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.NotesMasterDocument
    public CTNotesMaster getNotesMaster() {
        synchronized (monitor()) {
            check_orphaned();
            CTNotesMaster cTNotesMaster = (CTNotesMaster) get_store().find_element_user(NOTESMASTER$0, 0);
            if (cTNotesMaster == null) {
                return null;
            }
            return cTNotesMaster;
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.NotesMasterDocument
    public void setNotesMaster(CTNotesMaster cTNotesMaster) {
        synchronized (monitor()) {
            check_orphaned();
            CTNotesMaster cTNotesMaster2 = (CTNotesMaster) get_store().find_element_user(NOTESMASTER$0, 0);
            if (cTNotesMaster2 == null) {
                cTNotesMaster2 = (CTNotesMaster) get_store().add_element_user(NOTESMASTER$0);
            }
            cTNotesMaster2.set(cTNotesMaster);
        }
    }

    @Override // org.openxmlformats.schemas.presentationml.x2006.main.NotesMasterDocument
    public CTNotesMaster addNewNotesMaster() {
        CTNotesMaster cTNotesMaster;
        synchronized (monitor()) {
            check_orphaned();
            cTNotesMaster = (CTNotesMaster) get_store().add_element_user(NOTESMASTER$0);
        }
        return cTNotesMaster;
    }
}
