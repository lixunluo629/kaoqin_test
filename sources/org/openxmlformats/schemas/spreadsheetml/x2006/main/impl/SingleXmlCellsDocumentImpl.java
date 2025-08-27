package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCells;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.SingleXmlCellsDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/SingleXmlCellsDocumentImpl.class */
public class SingleXmlCellsDocumentImpl extends XmlComplexContentImpl implements SingleXmlCellsDocument {
    private static final QName SINGLEXMLCELLS$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "singleXmlCells");

    public SingleXmlCellsDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.SingleXmlCellsDocument
    public CTSingleXmlCells getSingleXmlCells() {
        synchronized (monitor()) {
            check_orphaned();
            CTSingleXmlCells cTSingleXmlCells = (CTSingleXmlCells) get_store().find_element_user(SINGLEXMLCELLS$0, 0);
            if (cTSingleXmlCells == null) {
                return null;
            }
            return cTSingleXmlCells;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.SingleXmlCellsDocument
    public void setSingleXmlCells(CTSingleXmlCells cTSingleXmlCells) {
        synchronized (monitor()) {
            check_orphaned();
            CTSingleXmlCells cTSingleXmlCells2 = (CTSingleXmlCells) get_store().find_element_user(SINGLEXMLCELLS$0, 0);
            if (cTSingleXmlCells2 == null) {
                cTSingleXmlCells2 = (CTSingleXmlCells) get_store().add_element_user(SINGLEXMLCELLS$0);
            }
            cTSingleXmlCells2.set(cTSingleXmlCells);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.SingleXmlCellsDocument
    public CTSingleXmlCells addNewSingleXmlCells() {
        CTSingleXmlCells cTSingleXmlCells;
        synchronized (monitor()) {
            check_orphaned();
            cTSingleXmlCells = (CTSingleXmlCells) get_store().add_element_user(SINGLEXMLCELLS$0);
        }
        return cTSingleXmlCells;
    }
}
