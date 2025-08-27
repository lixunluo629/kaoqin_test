package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFootnotes;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.FootnotesDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/FootnotesDocumentImpl.class */
public class FootnotesDocumentImpl extends XmlComplexContentImpl implements FootnotesDocument {
    private static final QName FOOTNOTES$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "footnotes");

    public FootnotesDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.FootnotesDocument
    public CTFootnotes getFootnotes() {
        synchronized (monitor()) {
            check_orphaned();
            CTFootnotes cTFootnotes = (CTFootnotes) get_store().find_element_user(FOOTNOTES$0, 0);
            if (cTFootnotes == null) {
                return null;
            }
            return cTFootnotes;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.FootnotesDocument
    public void setFootnotes(CTFootnotes cTFootnotes) {
        synchronized (monitor()) {
            check_orphaned();
            CTFootnotes cTFootnotes2 = (CTFootnotes) get_store().find_element_user(FOOTNOTES$0, 0);
            if (cTFootnotes2 == null) {
                cTFootnotes2 = (CTFootnotes) get_store().add_element_user(FOOTNOTES$0);
            }
            cTFootnotes2.set(cTFootnotes);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.FootnotesDocument
    public CTFootnotes addNewFootnotes() {
        CTFootnotes cTFootnotes;
        synchronized (monitor()) {
            check_orphaned();
            cTFootnotes = (CTFootnotes) get_store().add_element_user(FOOTNOTES$0);
        }
        return cTFootnotes;
    }
}
