package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/CTNumDataSourceImpl.class */
public class CTNumDataSourceImpl extends XmlComplexContentImpl implements CTNumDataSource {
    private static final QName NUMREF$0 = new QName(XSSFRelation.NS_CHART, "numRef");
    private static final QName NUMLIT$2 = new QName(XSSFRelation.NS_CHART, "numLit");

    public CTNumDataSourceImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource
    public CTNumRef getNumRef() {
        synchronized (monitor()) {
            check_orphaned();
            CTNumRef cTNumRef = (CTNumRef) get_store().find_element_user(NUMREF$0, 0);
            if (cTNumRef == null) {
                return null;
            }
            return cTNumRef;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource
    public boolean isSetNumRef() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(NUMREF$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource
    public void setNumRef(CTNumRef cTNumRef) {
        synchronized (monitor()) {
            check_orphaned();
            CTNumRef cTNumRef2 = (CTNumRef) get_store().find_element_user(NUMREF$0, 0);
            if (cTNumRef2 == null) {
                cTNumRef2 = (CTNumRef) get_store().add_element_user(NUMREF$0);
            }
            cTNumRef2.set(cTNumRef);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource
    public CTNumRef addNewNumRef() {
        CTNumRef cTNumRef;
        synchronized (monitor()) {
            check_orphaned();
            cTNumRef = (CTNumRef) get_store().add_element_user(NUMREF$0);
        }
        return cTNumRef;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource
    public void unsetNumRef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(NUMREF$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource
    public CTNumData getNumLit() {
        synchronized (monitor()) {
            check_orphaned();
            CTNumData cTNumData = (CTNumData) get_store().find_element_user(NUMLIT$2, 0);
            if (cTNumData == null) {
                return null;
            }
            return cTNumData;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource
    public boolean isSetNumLit() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(NUMLIT$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource
    public void setNumLit(CTNumData cTNumData) {
        synchronized (monitor()) {
            check_orphaned();
            CTNumData cTNumData2 = (CTNumData) get_store().find_element_user(NUMLIT$2, 0);
            if (cTNumData2 == null) {
                cTNumData2 = (CTNumData) get_store().add_element_user(NUMLIT$2);
            }
            cTNumData2.set(cTNumData);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource
    public CTNumData addNewNumLit() {
        CTNumData cTNumData;
        synchronized (monitor()) {
            check_orphaned();
            cTNumData = (CTNumData) get_store().add_element_user(NUMLIT$2);
        }
        return cTNumData;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource
    public void unsetNumLit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(NUMLIT$2, 0);
        }
    }
}
