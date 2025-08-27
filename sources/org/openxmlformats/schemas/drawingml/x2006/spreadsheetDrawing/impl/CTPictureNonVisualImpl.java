package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPictureNonVisual;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/spreadsheetDrawing/impl/CTPictureNonVisualImpl.class */
public class CTPictureNonVisualImpl extends XmlComplexContentImpl implements CTPictureNonVisual {
    private static final QName CNVPR$0 = new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "cNvPr");
    private static final QName CNVPICPR$2 = new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "cNvPicPr");

    public CTPictureNonVisualImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPictureNonVisual
    public CTNonVisualDrawingProps getCNvPr() {
        synchronized (monitor()) {
            check_orphaned();
            CTNonVisualDrawingProps cTNonVisualDrawingProps = (CTNonVisualDrawingProps) get_store().find_element_user(CNVPR$0, 0);
            if (cTNonVisualDrawingProps == null) {
                return null;
            }
            return cTNonVisualDrawingProps;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPictureNonVisual
    public void setCNvPr(CTNonVisualDrawingProps cTNonVisualDrawingProps) {
        synchronized (monitor()) {
            check_orphaned();
            CTNonVisualDrawingProps cTNonVisualDrawingProps2 = (CTNonVisualDrawingProps) get_store().find_element_user(CNVPR$0, 0);
            if (cTNonVisualDrawingProps2 == null) {
                cTNonVisualDrawingProps2 = (CTNonVisualDrawingProps) get_store().add_element_user(CNVPR$0);
            }
            cTNonVisualDrawingProps2.set(cTNonVisualDrawingProps);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPictureNonVisual
    public CTNonVisualDrawingProps addNewCNvPr() {
        CTNonVisualDrawingProps cTNonVisualDrawingProps;
        synchronized (monitor()) {
            check_orphaned();
            cTNonVisualDrawingProps = (CTNonVisualDrawingProps) get_store().add_element_user(CNVPR$0);
        }
        return cTNonVisualDrawingProps;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPictureNonVisual
    public CTNonVisualPictureProperties getCNvPicPr() {
        synchronized (monitor()) {
            check_orphaned();
            CTNonVisualPictureProperties cTNonVisualPictureProperties = (CTNonVisualPictureProperties) get_store().find_element_user(CNVPICPR$2, 0);
            if (cTNonVisualPictureProperties == null) {
                return null;
            }
            return cTNonVisualPictureProperties;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPictureNonVisual
    public void setCNvPicPr(CTNonVisualPictureProperties cTNonVisualPictureProperties) {
        synchronized (monitor()) {
            check_orphaned();
            CTNonVisualPictureProperties cTNonVisualPictureProperties2 = (CTNonVisualPictureProperties) get_store().find_element_user(CNVPICPR$2, 0);
            if (cTNonVisualPictureProperties2 == null) {
                cTNonVisualPictureProperties2 = (CTNonVisualPictureProperties) get_store().add_element_user(CNVPICPR$2);
            }
            cTNonVisualPictureProperties2.set(cTNonVisualPictureProperties);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPictureNonVisual
    public CTNonVisualPictureProperties addNewCNvPicPr() {
        CTNonVisualPictureProperties cTNonVisualPictureProperties;
        synchronized (monitor()) {
            check_orphaned();
            cTNonVisualPictureProperties = (CTNonVisualPictureProperties) get_store().add_element_user(CNVPICPR$2);
        }
        return cTNonVisualPictureProperties;
    }
}
