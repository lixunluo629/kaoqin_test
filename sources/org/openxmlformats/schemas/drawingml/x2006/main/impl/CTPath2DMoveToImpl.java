package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTPath2DMoveToImpl.class */
public class CTPath2DMoveToImpl extends XmlComplexContentImpl implements CTPath2DMoveTo {
    private static final QName PT$0 = new QName(XSSFRelation.NS_DRAWINGML, "pt");

    public CTPath2DMoveToImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo
    public CTAdjPoint2D getPt() {
        synchronized (monitor()) {
            check_orphaned();
            CTAdjPoint2D cTAdjPoint2D = (CTAdjPoint2D) get_store().find_element_user(PT$0, 0);
            if (cTAdjPoint2D == null) {
                return null;
            }
            return cTAdjPoint2D;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo
    public void setPt(CTAdjPoint2D cTAdjPoint2D) {
        synchronized (monitor()) {
            check_orphaned();
            CTAdjPoint2D cTAdjPoint2D2 = (CTAdjPoint2D) get_store().find_element_user(PT$0, 0);
            if (cTAdjPoint2D2 == null) {
                cTAdjPoint2D2 = (CTAdjPoint2D) get_store().add_element_user(PT$0);
            }
            cTAdjPoint2D2.set(cTAdjPoint2D);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo
    public CTAdjPoint2D addNewPt() {
        CTAdjPoint2D cTAdjPoint2D;
        synchronized (monitor()) {
            check_orphaned();
            cTAdjPoint2D = (CTAdjPoint2D) get_store().add_element_user(PT$0);
        }
        return cTAdjPoint2D;
    }
}
