package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D;
import org.openxmlformats.schemas.drawingml.x2006.main.STShapeType;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTPresetGeometry2DImpl.class */
public class CTPresetGeometry2DImpl extends XmlComplexContentImpl implements CTPresetGeometry2D {
    private static final QName AVLST$0 = new QName(XSSFRelation.NS_DRAWINGML, "avLst");
    private static final QName PRST$2 = new QName("", "prst");

    public CTPresetGeometry2DImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D
    public CTGeomGuideList getAvLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTGeomGuideList cTGeomGuideList = (CTGeomGuideList) get_store().find_element_user(AVLST$0, 0);
            if (cTGeomGuideList == null) {
                return null;
            }
            return cTGeomGuideList;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D
    public boolean isSetAvLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(AVLST$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D
    public void setAvLst(CTGeomGuideList cTGeomGuideList) {
        synchronized (monitor()) {
            check_orphaned();
            CTGeomGuideList cTGeomGuideList2 = (CTGeomGuideList) get_store().find_element_user(AVLST$0, 0);
            if (cTGeomGuideList2 == null) {
                cTGeomGuideList2 = (CTGeomGuideList) get_store().add_element_user(AVLST$0);
            }
            cTGeomGuideList2.set(cTGeomGuideList);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D
    public CTGeomGuideList addNewAvLst() {
        CTGeomGuideList cTGeomGuideList;
        synchronized (monitor()) {
            check_orphaned();
            cTGeomGuideList = (CTGeomGuideList) get_store().add_element_user(AVLST$0);
        }
        return cTGeomGuideList;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D
    public void unsetAvLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(AVLST$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D
    public STShapeType.Enum getPrst() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PRST$2);
            if (simpleValue == null) {
                return null;
            }
            return (STShapeType.Enum) simpleValue.getEnumValue();
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D
    public STShapeType xgetPrst() {
        STShapeType sTShapeType;
        synchronized (monitor()) {
            check_orphaned();
            sTShapeType = (STShapeType) get_store().find_attribute_user(PRST$2);
        }
        return sTShapeType;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D
    public void setPrst(STShapeType.Enum r4) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue simpleValue = (SimpleValue) get_store().find_attribute_user(PRST$2);
            if (simpleValue == null) {
                simpleValue = (SimpleValue) get_store().add_attribute_user(PRST$2);
            }
            simpleValue.setEnumValue(r4);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D
    public void xsetPrst(STShapeType sTShapeType) {
        synchronized (monitor()) {
            check_orphaned();
            STShapeType sTShapeType2 = (STShapeType) get_store().find_attribute_user(PRST$2);
            if (sTShapeType2 == null) {
                sTShapeType2 = (STShapeType) get_store().add_attribute_user(PRST$2);
            }
            sTShapeType2.set(sTShapeType);
        }
    }
}
