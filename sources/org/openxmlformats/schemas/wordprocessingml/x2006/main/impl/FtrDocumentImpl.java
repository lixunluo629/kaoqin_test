package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/impl/FtrDocumentImpl.class */
public class FtrDocumentImpl extends XmlComplexContentImpl implements FtrDocument {
    private static final QName FTR$0 = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "ftr");

    public FtrDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument
    public CTHdrFtr getFtr() {
        synchronized (monitor()) {
            check_orphaned();
            CTHdrFtr cTHdrFtr = (CTHdrFtr) get_store().find_element_user(FTR$0, 0);
            if (cTHdrFtr == null) {
                return null;
            }
            return cTHdrFtr;
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument
    public void setFtr(CTHdrFtr cTHdrFtr) {
        synchronized (monitor()) {
            check_orphaned();
            CTHdrFtr cTHdrFtr2 = (CTHdrFtr) get_store().find_element_user(FTR$0, 0);
            if (cTHdrFtr2 == null) {
                cTHdrFtr2 = (CTHdrFtr) get_store().add_element_user(FTR$0);
            }
            cTHdrFtr2.set(cTHdrFtr);
        }
    }

    @Override // org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument
    public CTHdrFtr addNewFtr() {
        CTHdrFtr cTHdrFtr;
        synchronized (monitor()) {
            check_orphaned();
            cTHdrFtr = (CTHdrFtr) get_store().add_element_user(FTR$0);
        }
        return cTHdrFtr;
    }
}
