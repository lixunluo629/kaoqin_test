package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTGraphicalObjectImpl.class */
public class CTGraphicalObjectImpl extends XmlComplexContentImpl implements CTGraphicalObject {
    private static final QName GRAPHICDATA$0 = new QName(XSSFRelation.NS_DRAWINGML, "graphicData");

    public CTGraphicalObjectImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject
    public CTGraphicalObjectData getGraphicData() {
        synchronized (monitor()) {
            check_orphaned();
            CTGraphicalObjectData cTGraphicalObjectData = (CTGraphicalObjectData) get_store().find_element_user(GRAPHICDATA$0, 0);
            if (cTGraphicalObjectData == null) {
                return null;
            }
            return cTGraphicalObjectData;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject
    public void setGraphicData(CTGraphicalObjectData cTGraphicalObjectData) {
        synchronized (monitor()) {
            check_orphaned();
            CTGraphicalObjectData cTGraphicalObjectData2 = (CTGraphicalObjectData) get_store().find_element_user(GRAPHICDATA$0, 0);
            if (cTGraphicalObjectData2 == null) {
                cTGraphicalObjectData2 = (CTGraphicalObjectData) get_store().add_element_user(GRAPHICDATA$0);
            }
            cTGraphicalObjectData2.set(cTGraphicalObjectData);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject
    public CTGraphicalObjectData addNewGraphicData() {
        CTGraphicalObjectData cTGraphicalObjectData;
        synchronized (monitor()) {
            check_orphaned();
            cTGraphicalObjectData = (CTGraphicalObjectData) get_store().add_element_user(GRAPHICDATA$0);
        }
        return cTGraphicalObjectData;
    }
}
