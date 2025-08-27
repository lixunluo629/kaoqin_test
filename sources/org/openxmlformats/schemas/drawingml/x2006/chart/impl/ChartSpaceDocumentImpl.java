package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace;
import org.openxmlformats.schemas.drawingml.x2006.chart.ChartSpaceDocument;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/ChartSpaceDocumentImpl.class */
public class ChartSpaceDocumentImpl extends XmlComplexContentImpl implements ChartSpaceDocument {
    private static final QName CHARTSPACE$0 = new QName(XSSFRelation.NS_CHART, "chartSpace");

    public ChartSpaceDocumentImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.ChartSpaceDocument
    public CTChartSpace getChartSpace() {
        synchronized (monitor()) {
            check_orphaned();
            CTChartSpace cTChartSpace = (CTChartSpace) get_store().find_element_user(CHARTSPACE$0, 0);
            if (cTChartSpace == null) {
                return null;
            }
            return cTChartSpace;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.ChartSpaceDocument
    public void setChartSpace(CTChartSpace cTChartSpace) {
        synchronized (monitor()) {
            check_orphaned();
            CTChartSpace cTChartSpace2 = (CTChartSpace) get_store().find_element_user(CHARTSPACE$0, 0);
            if (cTChartSpace2 == null) {
                cTChartSpace2 = (CTChartSpace) get_store().add_element_user(CHARTSPACE$0);
            }
            cTChartSpace2.set(cTChartSpace);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.ChartSpaceDocument
    public CTChartSpace addNewChartSpace() {
        CTChartSpace cTChartSpace;
        synchronized (monitor()) {
            check_orphaned();
            cTChartSpace = (CTChartSpace) get_store().add_element_user(CHARTSPACE$0);
        }
        return cTChartSpace;
    }
}
