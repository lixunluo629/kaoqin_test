package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAnchorClientData;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/spreadsheetDrawing/impl/CTOneCellAnchorImpl.class */
public class CTOneCellAnchorImpl extends XmlComplexContentImpl implements CTOneCellAnchor {
    private static final QName FROM$0 = new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "from");
    private static final QName EXT$2 = new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "ext");
    private static final QName SP$4 = new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "sp");
    private static final QName GRPSP$6 = new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "grpSp");
    private static final QName GRAPHICFRAME$8 = new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "graphicFrame");
    private static final QName CXNSP$10 = new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "cxnSp");
    private static final QName PIC$12 = new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "pic");
    private static final QName CLIENTDATA$14 = new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "clientData");

    public CTOneCellAnchorImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public CTMarker getFrom() {
        synchronized (monitor()) {
            check_orphaned();
            CTMarker cTMarker = (CTMarker) get_store().find_element_user(FROM$0, 0);
            if (cTMarker == null) {
                return null;
            }
            return cTMarker;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public void setFrom(CTMarker cTMarker) {
        synchronized (monitor()) {
            check_orphaned();
            CTMarker cTMarker2 = (CTMarker) get_store().find_element_user(FROM$0, 0);
            if (cTMarker2 == null) {
                cTMarker2 = (CTMarker) get_store().add_element_user(FROM$0);
            }
            cTMarker2.set(cTMarker);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public CTMarker addNewFrom() {
        CTMarker cTMarker;
        synchronized (monitor()) {
            check_orphaned();
            cTMarker = (CTMarker) get_store().add_element_user(FROM$0);
        }
        return cTMarker;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public CTPositiveSize2D getExt() {
        synchronized (monitor()) {
            check_orphaned();
            CTPositiveSize2D cTPositiveSize2D = (CTPositiveSize2D) get_store().find_element_user(EXT$2, 0);
            if (cTPositiveSize2D == null) {
                return null;
            }
            return cTPositiveSize2D;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public void setExt(CTPositiveSize2D cTPositiveSize2D) {
        synchronized (monitor()) {
            check_orphaned();
            CTPositiveSize2D cTPositiveSize2D2 = (CTPositiveSize2D) get_store().find_element_user(EXT$2, 0);
            if (cTPositiveSize2D2 == null) {
                cTPositiveSize2D2 = (CTPositiveSize2D) get_store().add_element_user(EXT$2);
            }
            cTPositiveSize2D2.set(cTPositiveSize2D);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public CTPositiveSize2D addNewExt() {
        CTPositiveSize2D cTPositiveSize2D;
        synchronized (monitor()) {
            check_orphaned();
            cTPositiveSize2D = (CTPositiveSize2D) get_store().add_element_user(EXT$2);
        }
        return cTPositiveSize2D;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public CTShape getSp() {
        synchronized (monitor()) {
            check_orphaned();
            CTShape cTShape = (CTShape) get_store().find_element_user(SP$4, 0);
            if (cTShape == null) {
                return null;
            }
            return cTShape;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public boolean isSetSp() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SP$4) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public void setSp(CTShape cTShape) {
        synchronized (monitor()) {
            check_orphaned();
            CTShape cTShape2 = (CTShape) get_store().find_element_user(SP$4, 0);
            if (cTShape2 == null) {
                cTShape2 = (CTShape) get_store().add_element_user(SP$4);
            }
            cTShape2.set(cTShape);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public CTShape addNewSp() {
        CTShape cTShape;
        synchronized (monitor()) {
            check_orphaned();
            cTShape = (CTShape) get_store().add_element_user(SP$4);
        }
        return cTShape;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public void unsetSp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SP$4, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public CTGroupShape getGrpSp() {
        synchronized (monitor()) {
            check_orphaned();
            CTGroupShape cTGroupShape = (CTGroupShape) get_store().find_element_user(GRPSP$6, 0);
            if (cTGroupShape == null) {
                return null;
            }
            return cTGroupShape;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public boolean isSetGrpSp() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(GRPSP$6) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public void setGrpSp(CTGroupShape cTGroupShape) {
        synchronized (monitor()) {
            check_orphaned();
            CTGroupShape cTGroupShape2 = (CTGroupShape) get_store().find_element_user(GRPSP$6, 0);
            if (cTGroupShape2 == null) {
                cTGroupShape2 = (CTGroupShape) get_store().add_element_user(GRPSP$6);
            }
            cTGroupShape2.set(cTGroupShape);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public CTGroupShape addNewGrpSp() {
        CTGroupShape cTGroupShape;
        synchronized (monitor()) {
            check_orphaned();
            cTGroupShape = (CTGroupShape) get_store().add_element_user(GRPSP$6);
        }
        return cTGroupShape;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public void unsetGrpSp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(GRPSP$6, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public CTGraphicalObjectFrame getGraphicFrame() {
        synchronized (monitor()) {
            check_orphaned();
            CTGraphicalObjectFrame cTGraphicalObjectFrame = (CTGraphicalObjectFrame) get_store().find_element_user(GRAPHICFRAME$8, 0);
            if (cTGraphicalObjectFrame == null) {
                return null;
            }
            return cTGraphicalObjectFrame;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public boolean isSetGraphicFrame() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(GRAPHICFRAME$8) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public void setGraphicFrame(CTGraphicalObjectFrame cTGraphicalObjectFrame) {
        synchronized (monitor()) {
            check_orphaned();
            CTGraphicalObjectFrame cTGraphicalObjectFrame2 = (CTGraphicalObjectFrame) get_store().find_element_user(GRAPHICFRAME$8, 0);
            if (cTGraphicalObjectFrame2 == null) {
                cTGraphicalObjectFrame2 = (CTGraphicalObjectFrame) get_store().add_element_user(GRAPHICFRAME$8);
            }
            cTGraphicalObjectFrame2.set(cTGraphicalObjectFrame);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public CTGraphicalObjectFrame addNewGraphicFrame() {
        CTGraphicalObjectFrame cTGraphicalObjectFrame;
        synchronized (monitor()) {
            check_orphaned();
            cTGraphicalObjectFrame = (CTGraphicalObjectFrame) get_store().add_element_user(GRAPHICFRAME$8);
        }
        return cTGraphicalObjectFrame;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public void unsetGraphicFrame() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(GRAPHICFRAME$8, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public CTConnector getCxnSp() {
        synchronized (monitor()) {
            check_orphaned();
            CTConnector cTConnector = (CTConnector) get_store().find_element_user(CXNSP$10, 0);
            if (cTConnector == null) {
                return null;
            }
            return cTConnector;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public boolean isSetCxnSp() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(CXNSP$10) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public void setCxnSp(CTConnector cTConnector) {
        synchronized (monitor()) {
            check_orphaned();
            CTConnector cTConnector2 = (CTConnector) get_store().find_element_user(CXNSP$10, 0);
            if (cTConnector2 == null) {
                cTConnector2 = (CTConnector) get_store().add_element_user(CXNSP$10);
            }
            cTConnector2.set(cTConnector);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public CTConnector addNewCxnSp() {
        CTConnector cTConnector;
        synchronized (monitor()) {
            check_orphaned();
            cTConnector = (CTConnector) get_store().add_element_user(CXNSP$10);
        }
        return cTConnector;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public void unsetCxnSp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CXNSP$10, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public CTPicture getPic() {
        synchronized (monitor()) {
            check_orphaned();
            CTPicture cTPicture = (CTPicture) get_store().find_element_user(PIC$12, 0);
            if (cTPicture == null) {
                return null;
            }
            return cTPicture;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public boolean isSetPic() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PIC$12) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public void setPic(CTPicture cTPicture) {
        synchronized (monitor()) {
            check_orphaned();
            CTPicture cTPicture2 = (CTPicture) get_store().find_element_user(PIC$12, 0);
            if (cTPicture2 == null) {
                cTPicture2 = (CTPicture) get_store().add_element_user(PIC$12);
            }
            cTPicture2.set(cTPicture);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public CTPicture addNewPic() {
        CTPicture cTPicture;
        synchronized (monitor()) {
            check_orphaned();
            cTPicture = (CTPicture) get_store().add_element_user(PIC$12);
        }
        return cTPicture;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public void unsetPic() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PIC$12, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public CTAnchorClientData getClientData() {
        synchronized (monitor()) {
            check_orphaned();
            CTAnchorClientData cTAnchorClientData = (CTAnchorClientData) get_store().find_element_user(CLIENTDATA$14, 0);
            if (cTAnchorClientData == null) {
                return null;
            }
            return cTAnchorClientData;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public void setClientData(CTAnchorClientData cTAnchorClientData) {
        synchronized (monitor()) {
            check_orphaned();
            CTAnchorClientData cTAnchorClientData2 = (CTAnchorClientData) get_store().find_element_user(CLIENTDATA$14, 0);
            if (cTAnchorClientData2 == null) {
                cTAnchorClientData2 = (CTAnchorClientData) get_store().add_element_user(CLIENTDATA$14);
            }
            cTAnchorClientData2.set(cTAnchorClientData);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor
    public CTAnchorClientData addNewClientData() {
        CTAnchorClientData cTAnchorClientData;
        synchronized (monitor()) {
            check_orphaned();
            cTAnchorClientData = (CTAnchorClientData) get_store().add_element_user(CLIENTDATA$14);
        }
        return cTAnchorClientData;
    }
}
