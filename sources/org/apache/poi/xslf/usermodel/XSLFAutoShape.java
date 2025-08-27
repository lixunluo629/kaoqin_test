package org.apache.poi.xslf.usermodel;

import com.mysql.jdbc.MysqlErrorNumbers;
import org.apache.poi.sl.usermodel.AutoShape;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import org.openxmlformats.schemas.drawingml.x2006.main.STShapeType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType;
import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTShapeNonVisual;
import org.springframework.beans.PropertyAccessor;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/usermodel/XSLFAutoShape.class */
public class XSLFAutoShape extends XSLFTextShape implements AutoShape<XSLFShape, XSLFTextParagraph> {
    XSLFAutoShape(CTShape shape, XSLFSheet sheet) {
        super(shape, sheet);
    }

    static XSLFAutoShape create(CTShape shape, XSLFSheet sheet) {
        if (shape.getSpPr().isSetCustGeom()) {
            return new XSLFFreeformShape(shape, sheet);
        }
        if (shape.getNvSpPr().getCNvSpPr().isSetTxBox()) {
            return new XSLFTextBox(shape, sheet);
        }
        return new XSLFAutoShape(shape, sheet);
    }

    static CTShape prototype(int shapeId) {
        CTShape ct = CTShape.Factory.newInstance();
        CTShapeNonVisual nvSpPr = ct.addNewNvSpPr();
        CTNonVisualDrawingProps cnv = nvSpPr.addNewCNvPr();
        cnv.setName("AutoShape " + shapeId);
        cnv.setId(shapeId + 1);
        nvSpPr.addNewCNvSpPr();
        nvSpPr.addNewNvPr();
        CTShapeProperties spPr = ct.addNewSpPr();
        CTPresetGeometry2D prst = spPr.addNewPrstGeom();
        prst.setPrst(STShapeType.RECT);
        prst.addNewAvLst();
        return ct;
    }

    protected static void initTextBody(CTTextBody txBody) {
        CTTextBodyProperties bodypr = txBody.addNewBodyPr();
        bodypr.setAnchor(STTextAnchoringType.T);
        bodypr.setRtlCol(false);
        CTTextParagraph p = txBody.addNewP();
        p.addNewPPr().setAlgn(STTextAlignType.L);
        CTTextCharacterProperties endPr = p.addNewEndParaRPr();
        endPr.setLang("en-US");
        endPr.setSz(MysqlErrorNumbers.ER_TABLE_NOT_LOCKED);
        p.addNewR().setT("");
        txBody.addNewLstStyle();
    }

    @Override // org.apache.poi.xslf.usermodel.XSLFTextShape
    protected CTTextBody getTextBody(boolean create) {
        CTShape shape = (CTShape) getXmlObject();
        CTTextBody txBody = shape.getTxBody();
        if (txBody == null && create) {
            txBody = shape.addNewTxBody();
            initTextBody(txBody);
        }
        return txBody;
    }

    public String toString() {
        return PropertyAccessor.PROPERTY_KEY_PREFIX + getClass().getSimpleName() + "] " + getShapeName();
    }
}
