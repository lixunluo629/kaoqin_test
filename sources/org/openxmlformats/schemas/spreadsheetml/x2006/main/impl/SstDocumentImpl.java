package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSst;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.SstDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/SstDocumentImpl.class */
public class SstDocumentImpl extends XmlComplexContentImpl implements SstDocument {
    private static final QName SST$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "sst");

    public SstDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.SstDocument
    public CTSst getSst() {
        synchronized (monitor()) {
            check_orphaned();
            CTSst cTSst = (CTSst) get_store().find_element_user(SST$0, 0);
            if (cTSst == null) {
                return null;
            }
            return cTSst;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.SstDocument
    public void setSst(CTSst cTSst) {
        synchronized (monitor()) {
            check_orphaned();
            CTSst cTSst2 = (CTSst) get_store().find_element_user(SST$0, 0);
            if (cTSst2 == null) {
                cTSst2 = (CTSst) get_store().add_element_user(SST$0);
            }
            cTSst2.set(cTSst);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.SstDocument
    public CTSst addNewSst() {
        CTSst cTSst;
        synchronized (monitor()) {
            check_orphaned();
            cTSst = (CTSst) get_store().add_element_user(SST$0);
        }
        return cTSst;
    }
}
