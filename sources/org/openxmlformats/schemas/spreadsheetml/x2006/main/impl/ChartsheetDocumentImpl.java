package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.ChartsheetDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/impl/ChartsheetDocumentImpl.class */
public class ChartsheetDocumentImpl extends XmlComplexContentImpl implements ChartsheetDocument {
    private static final QName CHARTSHEET$0 = new QName(XSSFRelation.NS_SPREADSHEETML, "chartsheet");

    public ChartsheetDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.ChartsheetDocument
    public CTChartsheet getChartsheet() {
        synchronized (monitor()) {
            check_orphaned();
            CTChartsheet cTChartsheet = (CTChartsheet) get_store().find_element_user(CHARTSHEET$0, 0);
            if (cTChartsheet == null) {
                return null;
            }
            return cTChartsheet;
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.ChartsheetDocument
    public void setChartsheet(CTChartsheet cTChartsheet) {
        synchronized (monitor()) {
            check_orphaned();
            CTChartsheet cTChartsheet2 = (CTChartsheet) get_store().find_element_user(CHARTSHEET$0, 0);
            if (cTChartsheet2 == null) {
                cTChartsheet2 = (CTChartsheet) get_store().add_element_user(CHARTSHEET$0);
            }
            cTChartsheet2.set(cTChartsheet);
        }
    }

    @Override // org.openxmlformats.schemas.spreadsheetml.x2006.main.ChartsheetDocument
    public CTChartsheet addNewChartsheet() {
        CTChartsheet cTChartsheet;
        synchronized (monitor()) {
            check_orphaned();
            cTChartsheet = (CTChartsheet) get_store().add_element_user(CHARTSHEET$0);
        }
        return cTChartsheet;
    }
}
