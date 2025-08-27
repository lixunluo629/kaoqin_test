package org.apache.poi.xslf.usermodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.POIXMLException;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.poi.util.Removal;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTable;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.presentationml.x2006.main.CTApplicationNonVisualDrawingProps;
import org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;

@Removal(version = "3.18")
/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/usermodel/XSLFCommonSlideData.class */
public class XSLFCommonSlideData {
    private final CTCommonSlideData data;

    public XSLFCommonSlideData(CTCommonSlideData data) {
        this.data = data;
    }

    public List<DrawingTextBody> getDrawingText() {
        CTGroupShape gs = this.data.getSpTree();
        List<DrawingTextBody> out = new ArrayList<>();
        processShape(gs, out);
        CTGroupShape[] arr$ = gs.getGrpSpArray();
        for (CTGroupShape shape : arr$) {
            processShape(shape, out);
        }
        CTGraphicalObjectFrame[] arr$2 = gs.getGraphicFrameArray();
        for (CTGraphicalObjectFrame frame : arr$2) {
            CTGraphicalObjectData data = frame.getGraphic().getGraphicData();
            XmlCursor c = data.newCursor();
            c.selectPath("declare namespace pic='" + CTTable.type.getName().getNamespaceURI() + "' .//pic:tbl");
            while (c.toNextSelection()) {
                XmlObject o = c.getObject();
                if (o instanceof XmlAnyTypeImpl) {
                    try {
                        o = CTTable.Factory.parse(o.toString(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
                    } catch (XmlException e) {
                        throw new POIXMLException(e);
                    }
                }
                if (o instanceof CTTable) {
                    DrawingTable table = new DrawingTable((CTTable) o);
                    DrawingTableRow[] arr$3 = table.getRows();
                    for (DrawingTableRow row : arr$3) {
                        DrawingTableCell[] arr$4 = row.getCells();
                        for (DrawingTableCell cell : arr$4) {
                            DrawingTextBody textBody = cell.getTextBody();
                            out.add(textBody);
                        }
                    }
                }
            }
            c.dispose();
        }
        return out;
    }

    public List<DrawingParagraph> getText() {
        List<DrawingParagraph> paragraphs = new ArrayList<>();
        for (DrawingTextBody textBody : getDrawingText()) {
            paragraphs.addAll(Arrays.asList(textBody.getParagraphs()));
        }
        return paragraphs;
    }

    private void processShape(CTGroupShape gs, List<DrawingTextBody> out) {
        DrawingTextBody textBody;
        CTShape[] arr$ = gs.getSpArray();
        for (CTShape shape : arr$) {
            CTTextBody ctTextBody = shape.getTxBody();
            if (ctTextBody != null) {
                CTApplicationNonVisualDrawingProps nvpr = shape.getNvSpPr().getNvPr();
                if (nvpr.isSetPh()) {
                    textBody = new DrawingTextPlaceholder(ctTextBody, nvpr.getPh());
                } else {
                    textBody = new DrawingTextBody(ctTextBody);
                }
                out.add(textBody);
            }
        }
    }
}
