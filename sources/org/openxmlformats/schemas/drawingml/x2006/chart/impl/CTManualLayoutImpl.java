package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutMode;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLayoutTarget;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/CTManualLayoutImpl.class */
public class CTManualLayoutImpl extends XmlComplexContentImpl implements CTManualLayout {
    private static final QName LAYOUTTARGET$0 = new QName(XSSFRelation.NS_CHART, "layoutTarget");
    private static final QName XMODE$2 = new QName(XSSFRelation.NS_CHART, "xMode");
    private static final QName YMODE$4 = new QName(XSSFRelation.NS_CHART, "yMode");
    private static final QName WMODE$6 = new QName(XSSFRelation.NS_CHART, "wMode");
    private static final QName HMODE$8 = new QName(XSSFRelation.NS_CHART, "hMode");
    private static final QName X$10 = new QName(XSSFRelation.NS_CHART, "x");
    private static final QName Y$12 = new QName(XSSFRelation.NS_CHART, "y");
    private static final QName W$14 = new QName(XSSFRelation.NS_CHART, "w");
    private static final QName H$16 = new QName(XSSFRelation.NS_CHART, "h");
    private static final QName EXTLST$18 = new QName(XSSFRelation.NS_CHART, "extLst");

    public CTManualLayoutImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTLayoutTarget getLayoutTarget() {
        synchronized (monitor()) {
            check_orphaned();
            CTLayoutTarget cTLayoutTarget = (CTLayoutTarget) get_store().find_element_user(LAYOUTTARGET$0, 0);
            if (cTLayoutTarget == null) {
                return null;
            }
            return cTLayoutTarget;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public boolean isSetLayoutTarget() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(LAYOUTTARGET$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void setLayoutTarget(CTLayoutTarget cTLayoutTarget) {
        synchronized (monitor()) {
            check_orphaned();
            CTLayoutTarget cTLayoutTarget2 = (CTLayoutTarget) get_store().find_element_user(LAYOUTTARGET$0, 0);
            if (cTLayoutTarget2 == null) {
                cTLayoutTarget2 = (CTLayoutTarget) get_store().add_element_user(LAYOUTTARGET$0);
            }
            cTLayoutTarget2.set(cTLayoutTarget);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTLayoutTarget addNewLayoutTarget() {
        CTLayoutTarget cTLayoutTarget;
        synchronized (monitor()) {
            check_orphaned();
            cTLayoutTarget = (CTLayoutTarget) get_store().add_element_user(LAYOUTTARGET$0);
        }
        return cTLayoutTarget;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void unsetLayoutTarget() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(LAYOUTTARGET$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTLayoutMode getXMode() {
        synchronized (monitor()) {
            check_orphaned();
            CTLayoutMode cTLayoutMode = (CTLayoutMode) get_store().find_element_user(XMODE$2, 0);
            if (cTLayoutMode == null) {
                return null;
            }
            return cTLayoutMode;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public boolean isSetXMode() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(XMODE$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void setXMode(CTLayoutMode cTLayoutMode) {
        synchronized (monitor()) {
            check_orphaned();
            CTLayoutMode cTLayoutMode2 = (CTLayoutMode) get_store().find_element_user(XMODE$2, 0);
            if (cTLayoutMode2 == null) {
                cTLayoutMode2 = (CTLayoutMode) get_store().add_element_user(XMODE$2);
            }
            cTLayoutMode2.set(cTLayoutMode);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTLayoutMode addNewXMode() {
        CTLayoutMode cTLayoutMode;
        synchronized (monitor()) {
            check_orphaned();
            cTLayoutMode = (CTLayoutMode) get_store().add_element_user(XMODE$2);
        }
        return cTLayoutMode;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void unsetXMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(XMODE$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTLayoutMode getYMode() {
        synchronized (monitor()) {
            check_orphaned();
            CTLayoutMode cTLayoutMode = (CTLayoutMode) get_store().find_element_user(YMODE$4, 0);
            if (cTLayoutMode == null) {
                return null;
            }
            return cTLayoutMode;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public boolean isSetYMode() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(YMODE$4) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void setYMode(CTLayoutMode cTLayoutMode) {
        synchronized (monitor()) {
            check_orphaned();
            CTLayoutMode cTLayoutMode2 = (CTLayoutMode) get_store().find_element_user(YMODE$4, 0);
            if (cTLayoutMode2 == null) {
                cTLayoutMode2 = (CTLayoutMode) get_store().add_element_user(YMODE$4);
            }
            cTLayoutMode2.set(cTLayoutMode);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTLayoutMode addNewYMode() {
        CTLayoutMode cTLayoutMode;
        synchronized (monitor()) {
            check_orphaned();
            cTLayoutMode = (CTLayoutMode) get_store().add_element_user(YMODE$4);
        }
        return cTLayoutMode;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void unsetYMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(YMODE$4, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTLayoutMode getWMode() {
        synchronized (monitor()) {
            check_orphaned();
            CTLayoutMode cTLayoutMode = (CTLayoutMode) get_store().find_element_user(WMODE$6, 0);
            if (cTLayoutMode == null) {
                return null;
            }
            return cTLayoutMode;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public boolean isSetWMode() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(WMODE$6) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void setWMode(CTLayoutMode cTLayoutMode) {
        synchronized (monitor()) {
            check_orphaned();
            CTLayoutMode cTLayoutMode2 = (CTLayoutMode) get_store().find_element_user(WMODE$6, 0);
            if (cTLayoutMode2 == null) {
                cTLayoutMode2 = (CTLayoutMode) get_store().add_element_user(WMODE$6);
            }
            cTLayoutMode2.set(cTLayoutMode);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTLayoutMode addNewWMode() {
        CTLayoutMode cTLayoutMode;
        synchronized (monitor()) {
            check_orphaned();
            cTLayoutMode = (CTLayoutMode) get_store().add_element_user(WMODE$6);
        }
        return cTLayoutMode;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void unsetWMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(WMODE$6, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTLayoutMode getHMode() {
        synchronized (monitor()) {
            check_orphaned();
            CTLayoutMode cTLayoutMode = (CTLayoutMode) get_store().find_element_user(HMODE$8, 0);
            if (cTLayoutMode == null) {
                return null;
            }
            return cTLayoutMode;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public boolean isSetHMode() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(HMODE$8) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void setHMode(CTLayoutMode cTLayoutMode) {
        synchronized (monitor()) {
            check_orphaned();
            CTLayoutMode cTLayoutMode2 = (CTLayoutMode) get_store().find_element_user(HMODE$8, 0);
            if (cTLayoutMode2 == null) {
                cTLayoutMode2 = (CTLayoutMode) get_store().add_element_user(HMODE$8);
            }
            cTLayoutMode2.set(cTLayoutMode);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTLayoutMode addNewHMode() {
        CTLayoutMode cTLayoutMode;
        synchronized (monitor()) {
            check_orphaned();
            cTLayoutMode = (CTLayoutMode) get_store().add_element_user(HMODE$8);
        }
        return cTLayoutMode;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void unsetHMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(HMODE$8, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTDouble getX() {
        synchronized (monitor()) {
            check_orphaned();
            CTDouble cTDouble = (CTDouble) get_store().find_element_user(X$10, 0);
            if (cTDouble == null) {
                return null;
            }
            return cTDouble;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public boolean isSetX() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(X$10) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void setX(CTDouble cTDouble) {
        synchronized (monitor()) {
            check_orphaned();
            CTDouble cTDouble2 = (CTDouble) get_store().find_element_user(X$10, 0);
            if (cTDouble2 == null) {
                cTDouble2 = (CTDouble) get_store().add_element_user(X$10);
            }
            cTDouble2.set(cTDouble);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTDouble addNewX() {
        CTDouble cTDouble;
        synchronized (monitor()) {
            check_orphaned();
            cTDouble = (CTDouble) get_store().add_element_user(X$10);
        }
        return cTDouble;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void unsetX() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(X$10, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTDouble getY() {
        synchronized (monitor()) {
            check_orphaned();
            CTDouble cTDouble = (CTDouble) get_store().find_element_user(Y$12, 0);
            if (cTDouble == null) {
                return null;
            }
            return cTDouble;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public boolean isSetY() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(Y$12) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void setY(CTDouble cTDouble) {
        synchronized (monitor()) {
            check_orphaned();
            CTDouble cTDouble2 = (CTDouble) get_store().find_element_user(Y$12, 0);
            if (cTDouble2 == null) {
                cTDouble2 = (CTDouble) get_store().add_element_user(Y$12);
            }
            cTDouble2.set(cTDouble);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTDouble addNewY() {
        CTDouble cTDouble;
        synchronized (monitor()) {
            check_orphaned();
            cTDouble = (CTDouble) get_store().add_element_user(Y$12);
        }
        return cTDouble;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void unsetY() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(Y$12, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTDouble getW() {
        synchronized (monitor()) {
            check_orphaned();
            CTDouble cTDouble = (CTDouble) get_store().find_element_user(W$14, 0);
            if (cTDouble == null) {
                return null;
            }
            return cTDouble;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public boolean isSetW() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(W$14) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void setW(CTDouble cTDouble) {
        synchronized (monitor()) {
            check_orphaned();
            CTDouble cTDouble2 = (CTDouble) get_store().find_element_user(W$14, 0);
            if (cTDouble2 == null) {
                cTDouble2 = (CTDouble) get_store().add_element_user(W$14);
            }
            cTDouble2.set(cTDouble);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTDouble addNewW() {
        CTDouble cTDouble;
        synchronized (monitor()) {
            check_orphaned();
            cTDouble = (CTDouble) get_store().add_element_user(W$14);
        }
        return cTDouble;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void unsetW() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(W$14, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTDouble getH() {
        synchronized (monitor()) {
            check_orphaned();
            CTDouble cTDouble = (CTDouble) get_store().find_element_user(H$16, 0);
            if (cTDouble == null) {
                return null;
            }
            return cTDouble;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public boolean isSetH() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(H$16) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void setH(CTDouble cTDouble) {
        synchronized (monitor()) {
            check_orphaned();
            CTDouble cTDouble2 = (CTDouble) get_store().find_element_user(H$16, 0);
            if (cTDouble2 == null) {
                cTDouble2 = (CTDouble) get_store().add_element_user(H$16);
            }
            cTDouble2.set(cTDouble);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTDouble addNewH() {
        CTDouble cTDouble;
        synchronized (monitor()) {
            check_orphaned();
            cTDouble = (CTDouble) get_store().add_element_user(H$16);
        }
        return cTDouble;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void unsetH() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(H$16, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$18, 0);
            if (cTExtensionListFind_element_user == null) {
                return null;
            }
            return cTExtensionListFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$18) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void setExtLst(CTExtensionList cTExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$18, 0);
            if (cTExtensionListFind_element_user == null) {
                cTExtensionListFind_element_user = (CTExtensionList) get_store().add_element_user(EXTLST$18);
            }
            cTExtensionListFind_element_user.set(cTExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public CTExtensionList addNewExtLst() {
        CTExtensionList cTExtensionListAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTExtensionListAdd_element_user = get_store().add_element_user(EXTLST$18);
        }
        return cTExtensionListAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$18, 0);
        }
    }
}
