package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectFrameLocking;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/impl/CTNonVisualGraphicFramePropertiesImpl.class */
public class CTNonVisualGraphicFramePropertiesImpl extends XmlComplexContentImpl implements CTNonVisualGraphicFrameProperties {
    private static final QName GRAPHICFRAMELOCKS$0 = new QName(XSSFRelation.NS_DRAWINGML, "graphicFrameLocks");
    private static final QName EXTLST$2 = new QName(XSSFRelation.NS_DRAWINGML, "extLst");

    public CTNonVisualGraphicFramePropertiesImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties
    public CTGraphicalObjectFrameLocking getGraphicFrameLocks() {
        synchronized (monitor()) {
            check_orphaned();
            CTGraphicalObjectFrameLocking cTGraphicalObjectFrameLocking = (CTGraphicalObjectFrameLocking) get_store().find_element_user(GRAPHICFRAMELOCKS$0, 0);
            if (cTGraphicalObjectFrameLocking == null) {
                return null;
            }
            return cTGraphicalObjectFrameLocking;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties
    public boolean isSetGraphicFrameLocks() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(GRAPHICFRAMELOCKS$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties
    public void setGraphicFrameLocks(CTGraphicalObjectFrameLocking cTGraphicalObjectFrameLocking) {
        synchronized (monitor()) {
            check_orphaned();
            CTGraphicalObjectFrameLocking cTGraphicalObjectFrameLocking2 = (CTGraphicalObjectFrameLocking) get_store().find_element_user(GRAPHICFRAMELOCKS$0, 0);
            if (cTGraphicalObjectFrameLocking2 == null) {
                cTGraphicalObjectFrameLocking2 = (CTGraphicalObjectFrameLocking) get_store().add_element_user(GRAPHICFRAMELOCKS$0);
            }
            cTGraphicalObjectFrameLocking2.set(cTGraphicalObjectFrameLocking);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties
    public CTGraphicalObjectFrameLocking addNewGraphicFrameLocks() {
        CTGraphicalObjectFrameLocking cTGraphicalObjectFrameLocking;
        synchronized (monitor()) {
            check_orphaned();
            cTGraphicalObjectFrameLocking = (CTGraphicalObjectFrameLocking) get_store().add_element_user(GRAPHICFRAMELOCKS$0);
        }
        return cTGraphicalObjectFrameLocking;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties
    public void unsetGraphicFrameLocks() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(GRAPHICFRAMELOCKS$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties
    public CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTOfficeArtExtensionList cTOfficeArtExtensionList = (CTOfficeArtExtensionList) get_store().find_element_user(EXTLST$2, 0);
            if (cTOfficeArtExtensionList == null) {
                return null;
            }
            return cTOfficeArtExtensionList;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties
    public void setExtLst(CTOfficeArtExtensionList cTOfficeArtExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTOfficeArtExtensionList cTOfficeArtExtensionList2 = (CTOfficeArtExtensionList) get_store().find_element_user(EXTLST$2, 0);
            if (cTOfficeArtExtensionList2 == null) {
                cTOfficeArtExtensionList2 = (CTOfficeArtExtensionList) get_store().add_element_user(EXTLST$2);
            }
            cTOfficeArtExtensionList2.set(cTOfficeArtExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties
    public CTOfficeArtExtensionList addNewExtLst() {
        CTOfficeArtExtensionList cTOfficeArtExtensionList;
        synchronized (monitor()) {
            check_orphaned();
            cTOfficeArtExtensionList = (CTOfficeArtExtensionList) get_store().add_element_user(EXTLST$2);
        }
        return cTOfficeArtExtensionList;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$2, 0);
        }
    }
}
