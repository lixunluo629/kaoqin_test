package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/CTLayoutImpl.class */
public class CTLayoutImpl extends XmlComplexContentImpl implements CTLayout {
    private static final QName MANUALLAYOUT$0 = new QName(XSSFRelation.NS_CHART, "manualLayout");
    private static final QName EXTLST$2 = new QName(XSSFRelation.NS_CHART, "extLst");

    public CTLayoutImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout
    public CTManualLayout getManualLayout() {
        synchronized (monitor()) {
            check_orphaned();
            CTManualLayout cTManualLayout = (CTManualLayout) get_store().find_element_user(MANUALLAYOUT$0, 0);
            if (cTManualLayout == null) {
                return null;
            }
            return cTManualLayout;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout
    public boolean isSetManualLayout() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(MANUALLAYOUT$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout
    public void setManualLayout(CTManualLayout cTManualLayout) {
        synchronized (monitor()) {
            check_orphaned();
            CTManualLayout cTManualLayout2 = (CTManualLayout) get_store().find_element_user(MANUALLAYOUT$0, 0);
            if (cTManualLayout2 == null) {
                cTManualLayout2 = (CTManualLayout) get_store().add_element_user(MANUALLAYOUT$0);
            }
            cTManualLayout2.set(cTManualLayout);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout
    public CTManualLayout addNewManualLayout() {
        CTManualLayout cTManualLayout;
        synchronized (monitor()) {
            check_orphaned();
            cTManualLayout = (CTManualLayout) get_store().add_element_user(MANUALLAYOUT$0);
        }
        return cTManualLayout;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout
    public void unsetManualLayout() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(MANUALLAYOUT$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout
    public CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$2, 0);
            if (cTExtensionListFind_element_user == null) {
                return null;
            }
            return cTExtensionListFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout
    public void setExtLst(CTExtensionList cTExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$2, 0);
            if (cTExtensionListFind_element_user == null) {
                cTExtensionListFind_element_user = (CTExtensionList) get_store().add_element_user(EXTLST$2);
            }
            cTExtensionListFind_element_user.set(cTExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout
    public CTExtensionList addNewExtLst() {
        CTExtensionList cTExtensionListAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTExtensionListAdd_element_user = get_store().add_element_user(EXTLST$2);
        }
        return cTExtensionListAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$2, 0);
        }
    }
}
