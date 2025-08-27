package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorksheetDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/WorksheetDocumentImpl.class */
public class WorksheetDocumentImpl extends XmlComplexContentImpl implements WorksheetDocument {
    private static final QName WORKSHEET$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "worksheet");

    public WorksheetDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.WorksheetDocument
    public CTWorksheet getWorksheet() {
        synchronized (monitor()) {
            check_orphaned();
            CTWorksheet cTWorksheet = (CTWorksheet) get_store().find_element_user(WORKSHEET$0, 0);
            if (cTWorksheet == null) {
                return null;
            }
            return cTWorksheet;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.WorksheetDocument
    public void setWorksheet(CTWorksheet cTWorksheet) {
        synchronized (monitor()) {
            check_orphaned();
            CTWorksheet cTWorksheet2 = (CTWorksheet) get_store().find_element_user(WORKSHEET$0, 0);
            if (cTWorksheet2 == null) {
                cTWorksheet2 = (CTWorksheet) get_store().add_element_user(WORKSHEET$0);
            }
            cTWorksheet2.set(cTWorksheet);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.WorksheetDocument
    public CTWorksheet addNewWorksheet() {
        CTWorksheet cTWorksheet;
        synchronized (monitor()) {
            check_orphaned();
            cTWorksheet = (CTWorksheet) get_store().add_element_user(WORKSHEET$0);
        }
        return cTWorksheet;
    }
}
