package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/WorkbookDocumentImpl.class */
public class WorkbookDocumentImpl extends XmlComplexContentImpl implements WorkbookDocument {
    private static final QName WORKBOOK$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "workbook");

    public WorkbookDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument
    public CTWorkbook getWorkbook() {
        synchronized (monitor()) {
            check_orphaned();
            CTWorkbook cTWorkbook = (CTWorkbook) get_store().find_element_user(WORKBOOK$0, 0);
            if (cTWorkbook == null) {
                return null;
            }
            return cTWorkbook;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument
    public void setWorkbook(CTWorkbook cTWorkbook) {
        synchronized (monitor()) {
            check_orphaned();
            CTWorkbook cTWorkbook2 = (CTWorkbook) get_store().find_element_user(WORKBOOK$0, 0);
            if (cTWorkbook2 == null) {
                cTWorkbook2 = (CTWorkbook) get_store().add_element_user(WORKBOOK$0);
            }
            cTWorkbook2.set(cTWorkbook);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument
    public CTWorkbook addNewWorkbook() {
        CTWorkbook cTWorkbook;
        synchronized (monitor()) {
            check_orphaned();
            cTWorkbook = (CTWorkbook) get_store().add_element_user(WORKBOOK$0);
        }
        return cTWorkbook;
    }
}
