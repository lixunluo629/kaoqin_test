package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.TableDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/TableDocumentImpl.class */
public class TableDocumentImpl extends XmlComplexContentImpl implements TableDocument {
    private static final QName TABLE$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "table");

    public TableDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.TableDocument
    public CTTable getTable() {
        synchronized (monitor()) {
            check_orphaned();
            CTTable cTTable = (CTTable) get_store().find_element_user(TABLE$0, 0);
            if (cTTable == null) {
                return null;
            }
            return cTTable;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.TableDocument
    public void setTable(CTTable cTTable) {
        synchronized (monitor()) {
            check_orphaned();
            CTTable cTTable2 = (CTTable) get_store().find_element_user(TABLE$0, 0);
            if (cTTable2 == null) {
                cTTable2 = (CTTable) get_store().add_element_user(TABLE$0);
            }
            cTTable2.set(cTTable);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.TableDocument
    public CTTable addNewTable() {
        CTTable cTTable;
        synchronized (monitor()) {
            check_orphaned();
            cTTable = (CTTable) get_store().add_element_user(TABLE$0);
        }
        return cTTable;
    }
}
