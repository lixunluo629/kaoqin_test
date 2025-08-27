package org.apache.poi.xssf.usermodel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLException;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.PackageRelationshipTypes;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.ImageUtils;
import org.apache.poi.util.Internal;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.model.CommentsTable;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGroupTransform2D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTDrawing;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/XSSFDrawing.class */
public final class XSSFDrawing extends POIXMLDocumentPart implements Drawing<XSSFShape> {
    private static final POILogger LOG;
    private CTDrawing drawing;
    private long numOfGraphicFrames;
    protected static final String NAMESPACE_A = "http://schemas.openxmlformats.org/drawingml/2006/main";
    protected static final String NAMESPACE_C = "http://schemas.openxmlformats.org/drawingml/2006/chart";
    static final /* synthetic */ boolean $assertionsDisabled;

    /*  JADX ERROR: Failed to decode insn: 0x0027: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[8]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    private org.apache.poi.xssf.usermodel.XSSFGraphicFrame createGraphicFrame(org.apache.poi.xssf.usermodel.XSSFClientAnchor r9) {
        /*
            r8 = this;
            r0 = r8
            r1 = r9
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor r0 = r0.createTwoCellAnchor(r1)
            r10 = r0
            r0 = r10
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame r0 = r0.addNewGraphicFrame()
            r11 = r0
            r0 = r11
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame r1 = org.apache.poi.xssf.usermodel.XSSFGraphicFrame.prototype()
            org.apache.xmlbeans.XmlObject r0 = r0.set(r1)
            r0 = r11
            r1 = r8
            r2 = r9
            org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D r1 = r1.createXfrm(r2)
            r0.setXfrm(r1)
            r0 = r8
            r1 = r0
            long r1 = r1.numOfGraphicFrames
            // decode failed: arraycopy: source index -1 out of bounds for object array[8]
            r2 = 1
            long r1 = r1 + r2
            r0.numOfGraphicFrames = r1
            r12 = r-1
            org.apache.poi.xssf.usermodel.XSSFGraphicFrame r-1 = new org.apache.poi.xssf.usermodel.XSSFGraphicFrame
            r0 = r-1
            r1 = r8
            r2 = r11
            r0.<init>(r1, r2)
            r14 = r-1
            r-1 = r14
            r0 = r9
            r-1.setAnchor(r0)
            r-1 = r14
            r0 = r12
            r-1.setId(r0)
            r-1 = r14
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            java.lang.String r1 = "Diagramm"
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r12
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r-1.setName(r0)
            r-1 = r14
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.xssf.usermodel.XSSFDrawing.createGraphicFrame(org.apache.poi.xssf.usermodel.XSSFClientAnchor):org.apache.poi.xssf.usermodel.XSSFGraphicFrame");
    }

    static {
        $assertionsDisabled = !XSSFDrawing.class.desiredAssertionStatus();
        LOG = POILogFactory.getLogger((Class<?>) XSSFDrawing.class);
    }

    protected XSSFDrawing() {
        this.numOfGraphicFrames = 0L;
        this.drawing = newDrawing();
    }

    public XSSFDrawing(PackagePart part) throws XmlException, IOException {
        super(part);
        this.numOfGraphicFrames = 0L;
        XmlOptions options = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
        options.setLoadReplaceDocumentElement(null);
        InputStream is = part.getInputStream();
        try {
            this.drawing = CTDrawing.Factory.parse(is, options);
            is.close();
        } catch (Throwable th) {
            is.close();
            throw th;
        }
    }

    private static CTDrawing newDrawing() {
        return CTDrawing.Factory.newInstance();
    }

    @Internal
    public CTDrawing getCTDrawing() {
        return this.drawing;
    }

    @Override // org.apache.poi.POIXMLDocumentPart
    protected void commit() throws IOException {
        XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
        xmlOptions.setSaveSyntheticDocumentElement(new QName(CTDrawing.type.getName().getNamespaceURI(), "wsDr", "xdr"));
        PackagePart part = getPackagePart();
        OutputStream out = part.getOutputStream();
        this.drawing.save(out, xmlOptions);
        out.close();
    }

    @Override // org.apache.poi.ss.usermodel.Drawing
    public XSSFClientAnchor createAnchor(int dx1, int dy1, int dx2, int dy2, int col1, int row1, int col2, int row2) {
        return new XSSFClientAnchor(dx1, dy1, dx2, dy2, col1, row1, col2, row2);
    }

    public XSSFTextBox createTextbox(XSSFClientAnchor anchor) {
        long shapeId = newShapeId();
        CTTwoCellAnchor ctAnchor = createTwoCellAnchor(anchor);
        CTShape ctShape = ctAnchor.addNewSp();
        ctShape.set(XSSFSimpleShape.prototype());
        ctShape.getNvSpPr().getCNvPr().setId(shapeId);
        XSSFTextBox shape = new XSSFTextBox(this, ctShape);
        shape.anchor = anchor;
        return shape;
    }

    public XSSFPicture createPicture(XSSFClientAnchor anchor, int pictureIndex) {
        PackageRelationship rel = addPictureReference(pictureIndex);
        long shapeId = newShapeId();
        CTTwoCellAnchor ctAnchor = createTwoCellAnchor(anchor);
        CTPicture ctShape = ctAnchor.addNewPic();
        ctShape.set(XSSFPicture.prototype());
        ctShape.getNvPicPr().getCNvPr().setId(shapeId);
        XSSFPicture shape = new XSSFPicture(this, ctShape);
        shape.anchor = anchor;
        shape.setPictureReference(rel);
        ctShape.getSpPr().setXfrm(createXfrm(anchor));
        return shape;
    }

    @Override // org.apache.poi.ss.usermodel.Drawing
    public XSSFPicture createPicture(ClientAnchor anchor, int pictureIndex) {
        return createPicture((XSSFClientAnchor) anchor, pictureIndex);
    }

    public XSSFChart createChart(XSSFClientAnchor anchor) {
        int chartNumber = getPackagePart().getPackage().getPartsByContentType(XSSFRelation.CHART.getContentType()).size() + 1;
        POIXMLDocumentPart.RelationPart rp = createRelationship(XSSFRelation.CHART, XSSFFactory.getInstance(), chartNumber, false);
        XSSFChart chart = (XSSFChart) rp.getDocumentPart();
        String chartRelId = rp.getRelationship().getId();
        XSSFGraphicFrame frame = createGraphicFrame(anchor);
        frame.setChart(chart, chartRelId);
        frame.getCTGraphicalObjectFrame().setXfrm(createXfrm(anchor));
        return chart;
    }

    @Override // org.apache.poi.ss.usermodel.Drawing
    public XSSFChart createChart(ClientAnchor anchor) {
        return createChart((XSSFClientAnchor) anchor);
    }

    protected PackageRelationship addPictureReference(int pictureIndex) {
        XSSFWorkbook wb = (XSSFWorkbook) getParent().getParent();
        XSSFPictureData data = wb.getAllPictures().get(pictureIndex);
        XSSFPictureData pic = new XSSFPictureData(data.getPackagePart());
        POIXMLDocumentPart.RelationPart rp = addRelation(null, XSSFRelation.IMAGES, pic);
        return rp.getRelationship();
    }

    public XSSFSimpleShape createSimpleShape(XSSFClientAnchor anchor) {
        long shapeId = newShapeId();
        CTTwoCellAnchor ctAnchor = createTwoCellAnchor(anchor);
        CTShape ctShape = ctAnchor.addNewSp();
        ctShape.set(XSSFSimpleShape.prototype());
        ctShape.getNvSpPr().getCNvPr().setId(shapeId);
        ctShape.getSpPr().setXfrm(createXfrm(anchor));
        XSSFSimpleShape shape = new XSSFSimpleShape(this, ctShape);
        shape.anchor = anchor;
        return shape;
    }

    public XSSFConnector createConnector(XSSFClientAnchor anchor) {
        CTTwoCellAnchor ctAnchor = createTwoCellAnchor(anchor);
        CTConnector ctShape = ctAnchor.addNewCxnSp();
        ctShape.set(XSSFConnector.prototype());
        XSSFConnector shape = new XSSFConnector(this, ctShape);
        shape.anchor = anchor;
        return shape;
    }

    public XSSFShapeGroup createGroup(XSSFClientAnchor anchor) {
        CTTwoCellAnchor ctAnchor = createTwoCellAnchor(anchor);
        CTGroupShape ctGroup = ctAnchor.addNewGrpSp();
        ctGroup.set(XSSFShapeGroup.prototype());
        CTTransform2D xfrm = createXfrm(anchor);
        CTGroupTransform2D grpXfrm = ctGroup.getGrpSpPr().getXfrm();
        grpXfrm.setOff(xfrm.getOff());
        grpXfrm.setExt(xfrm.getExt());
        grpXfrm.setChExt(xfrm.getExt());
        XSSFShapeGroup shape = new XSSFShapeGroup(this, ctGroup);
        shape.anchor = anchor;
        return shape;
    }

    @Override // org.apache.poi.ss.usermodel.Drawing
    public XSSFComment createCellComment(ClientAnchor anchor) {
        XSSFClientAnchor ca = (XSSFClientAnchor) anchor;
        XSSFSheet sheet = getSheet();
        CommentsTable comments = sheet.getCommentsTable(true);
        XSSFVMLDrawing vml = sheet.getVMLDrawing(true);
        com.microsoft.schemas.vml.CTShape vmlShape = vml.newCommentShape();
        if (ca.isSet()) {
            int dx1Pixels = ca.getDx1() / 9525;
            int dy1Pixels = ca.getDy1() / 9525;
            int dx2Pixels = ca.getDx2() / 9525;
            int dy2Pixels = ca.getDy2() / 9525;
            String position = ((int) ca.getCol1()) + ", " + dx1Pixels + ", " + ca.getRow1() + ", " + dy1Pixels + ", " + ((int) ca.getCol2()) + ", " + dx2Pixels + ", " + ca.getRow2() + ", " + dy2Pixels;
            vmlShape.getClientDataArray(0).setAnchorArray(0, position);
        }
        CellAddress ref = new CellAddress(ca.getRow1(), ca.getCol1());
        if (comments.findCellComment(ref) != null) {
            throw new IllegalArgumentException("Multiple cell comments in one cell are not allowed, cell: " + ref);
        }
        return new XSSFComment(comments, comments.newComment(ref), vmlShape);
    }

    @Override // org.apache.poi.ss.usermodel.Drawing
    public XSSFObjectData createObjectData(ClientAnchor anchor, int storageId, int pictureIndex) {
        XSSFSheet sh = getSheet();
        PackagePart sheetPart = sh.getPackagePart();
        XSSFSheet sheet = getSheet();
        XSSFWorkbook wb = sheet.getWorkbook();
        int sheetIndex = wb.getSheetIndex(sheet);
        long shapeId = ((sheetIndex + 1) * 1024) + newShapeId();
        try {
            PackagePartName olePN = PackagingURIHelper.createPartName("/xl/embeddings/oleObject" + storageId + ".bin");
            PackageRelationship olePR = sheetPart.addRelationship(olePN, TargetMode.INTERNAL, POIXMLDocument.OLE_OBJECT_REL_TYPE);
            XSSFPictureData imgPD = sh.getWorkbook().getAllPictures().get(pictureIndex);
            PackagePartName imgPN = imgPD.getPackagePart().getPartName();
            PackageRelationship imgSheetPR = sheetPart.addRelationship(imgPN, TargetMode.INTERNAL, PackageRelationshipTypes.IMAGE_PART);
            PackageRelationship imgDrawPR = getPackagePart().addRelationship(imgPN, TargetMode.INTERNAL, PackageRelationshipTypes.IMAGE_PART);
            CTWorksheet cwb = sh.getCTWorksheet();
            CTOleObjects oo = cwb.isSetOleObjects() ? cwb.getOleObjects() : cwb.addNewOleObjects();
            CTOleObject ole1 = oo.addNewOleObject();
            ole1.setProgId("Package");
            ole1.setShapeId(shapeId);
            ole1.setId(olePR.getId());
            XmlCursor cur1 = ole1.newCursor();
            cur1.toEndToken();
            cur1.beginElement("objectPr", XSSFRelation.NS_SPREADSHEETML);
            cur1.insertAttributeWithValue("id", PackageRelationshipTypes.CORE_PROPERTIES_ECMA376_NS, imgSheetPR.getId());
            cur1.insertAttributeWithValue("defaultSize", "0");
            cur1.beginElement("anchor", XSSFRelation.NS_SPREADSHEETML);
            cur1.insertAttributeWithValue("moveWithCells", "1");
            CTTwoCellAnchor ctAnchor = createTwoCellAnchor((XSSFClientAnchor) anchor);
            XmlCursor cur2 = ctAnchor.newCursor();
            cur2.copyXmlContents(cur1);
            cur2.dispose();
            cur1.toParent();
            cur1.toFirstChild();
            cur1.setName(new QName(XSSFRelation.NS_SPREADSHEETML, "from"));
            cur1.toNextSibling();
            cur1.setName(new QName(XSSFRelation.NS_SPREADSHEETML, "to"));
            cur1.dispose();
            CTShape ctShape = ctAnchor.addNewSp();
            ctShape.set(XSSFObjectData.prototype());
            ctShape.getSpPr().setXfrm(createXfrm((XSSFClientAnchor) anchor));
            CTBlipFillProperties blipFill = ctShape.getSpPr().addNewBlipFill();
            blipFill.addNewBlip().setEmbed(imgDrawPR.getId());
            blipFill.addNewStretch().addNewFillRect();
            CTNonVisualDrawingProps cNvPr = ctShape.getNvSpPr().getCNvPr();
            cNvPr.setId(shapeId);
            cNvPr.setName("Object " + shapeId);
            XmlCursor extCur = cNvPr.getExtLst().getExtArray(0).newCursor();
            extCur.toFirstChild();
            extCur.setAttributeText(new QName("spid"), "_x0000_s" + shapeId);
            extCur.dispose();
            XSSFObjectData shape = new XSSFObjectData(this, ctShape);
            shape.anchor = (XSSFClientAnchor) anchor;
            return shape;
        } catch (InvalidFormatException e) {
            throw new POIXMLException(e);
        }
    }

    public List<XSSFChart> getCharts() {
        List<XSSFChart> charts = new ArrayList<>();
        for (POIXMLDocumentPart part : getRelations()) {
            if (part instanceof XSSFChart) {
                charts.add((XSSFChart) part);
            }
        }
        return charts;
    }

    private CTTwoCellAnchor createTwoCellAnchor(XSSFClientAnchor anchor) {
        STEditAs.Enum aditAs;
        CTTwoCellAnchor ctAnchor = this.drawing.addNewTwoCellAnchor();
        ctAnchor.setFrom(anchor.getFrom());
        ctAnchor.setTo(anchor.getTo());
        ctAnchor.addNewClientData();
        anchor.setTo(ctAnchor.getTo());
        anchor.setFrom(ctAnchor.getFrom());
        switch (anchor.getAnchorType()) {
            case DONT_MOVE_AND_RESIZE:
                aditAs = STEditAs.ABSOLUTE;
                break;
            case MOVE_AND_RESIZE:
                aditAs = STEditAs.TWO_CELL;
                break;
            case MOVE_DONT_RESIZE:
                aditAs = STEditAs.ONE_CELL;
                break;
            default:
                aditAs = STEditAs.ONE_CELL;
                break;
        }
        ctAnchor.setEditAs(aditAs);
        return ctAnchor;
    }

    private CTTransform2D createXfrm(XSSFClientAnchor anchor) {
        CTTransform2D xfrm = CTTransform2D.Factory.newInstance();
        CTPoint2D off = xfrm.addNewOff();
        off.setX(anchor.getDx1());
        off.setY(anchor.getDy1());
        XSSFSheet sheet = getSheet();
        double widthPx = 0.0d;
        for (int col = anchor.getCol1(); col < anchor.getCol2(); col++) {
            widthPx += sheet.getColumnWidthInPixels(col);
        }
        double heightPx = 0.0d;
        for (int row = anchor.getRow1(); row < anchor.getRow2(); row++) {
            heightPx += ImageUtils.getRowHeightInPixels(sheet, row);
        }
        long width = Units.pixelToEMU((int) widthPx);
        long height = Units.pixelToEMU((int) heightPx);
        CTPositiveSize2D ext = xfrm.addNewExt();
        ext.setCx((width - anchor.getDx1()) + anchor.getDx2());
        ext.setCy((height - anchor.getDy1()) + anchor.getDy2());
        return xfrm;
    }

    private long newShapeId() {
        return 1 + this.drawing.sizeOfAbsoluteAnchorArray() + this.drawing.sizeOfOneCellAnchorArray() + this.drawing.sizeOfTwoCellAnchorArray();
    }

    public List<XSSFShape> getShapes() {
        List<XSSFShape> lst = new ArrayList<>();
        XmlCursor cur = this.drawing.newCursor();
        try {
            if (cur.toFirstChild()) {
                addShapes(cur, lst);
            }
            return lst;
        } finally {
            cur.dispose();
        }
    }

    public List<XSSFShape> getShapes(XSSFShapeGroup groupshape) {
        List<XSSFShape> lst = new ArrayList<>();
        XmlCursor cur = groupshape.getCTGroupShape().newCursor();
        try {
            addShapes(cur, lst);
            cur.dispose();
            return lst;
        } catch (Throwable th) {
            cur.dispose();
            throw th;
        }
    }

    private void addShapes(XmlCursor cur, List<XSSFShape> lst) {
        XSSFShape shape;
        do {
            try {
                cur.push();
                if (cur.toFirstChild()) {
                    do {
                        XmlObject obj = cur.getObject();
                        if (!(obj instanceof CTMarker)) {
                            if (obj instanceof CTPicture) {
                                shape = new XSSFPicture(this, (CTPicture) obj);
                            } else if (obj instanceof CTConnector) {
                                shape = new XSSFConnector(this, (CTConnector) obj);
                            } else if (obj instanceof CTShape) {
                                shape = hasOleLink(obj) ? new XSSFObjectData(this, (CTShape) obj) : new XSSFSimpleShape(this, (CTShape) obj);
                            } else if (obj instanceof CTGraphicalObjectFrame) {
                                shape = new XSSFGraphicFrame(this, (CTGraphicalObjectFrame) obj);
                            } else if (obj instanceof CTGroupShape) {
                                shape = new XSSFShapeGroup(this, (CTGroupShape) obj);
                            } else if (obj instanceof XmlAnyTypeImpl) {
                                LOG.log(5, "trying to parse AlternateContent, this unlinks the returned Shapes from the underlying xml content, so those shapes can't be used to modify the drawing, i.e. modifications will be ignored!");
                                cur.push();
                                cur.toFirstChild();
                                XmlCursor cur2 = null;
                                try {
                                    try {
                                        CTDrawing alterWS = CTDrawing.Factory.parse(cur.newXMLStreamReader());
                                        cur2 = alterWS.newCursor();
                                        if (cur2.toFirstChild()) {
                                            addShapes(cur2, lst);
                                        }
                                        if (cur2 != null) {
                                            cur2.dispose();
                                        }
                                        cur.pop();
                                    } catch (XmlException e) {
                                        LOG.log(5, "unable to parse CTDrawing in alternate content.", e);
                                        if (cur2 != null) {
                                            cur2.dispose();
                                        }
                                        cur.pop();
                                    }
                                } catch (Throwable th) {
                                    if (cur2 != null) {
                                        cur2.dispose();
                                    }
                                    cur.pop();
                                    throw th;
                                }
                            }
                            if (!$assertionsDisabled && shape == null) {
                                throw new AssertionError();
                            }
                            shape.anchor = getAnchorFromParent(obj);
                            lst.add(shape);
                        }
                    } while (cur.toNextSibling());
                }
                cur.pop();
            } finally {
                cur.dispose();
            }
        } while (cur.toNextSibling());
    }

    private boolean hasOleLink(XmlObject shape) {
        QName uriName = new QName(null, "uri");
        XmlCursor cur = shape.newCursor();
        cur.selectPath("declare namespace a='http://schemas.openxmlformats.org/drawingml/2006/main' .//a:extLst/a:ext");
        while (cur.toNextSelection()) {
            try {
                String uri = cur.getAttributeText(uriName);
                if ("{63B3BB69-23CF-44E3-9099-C40C66FF867C}".equals(uri)) {
                    return true;
                }
            } finally {
                cur.dispose();
            }
        }
        cur.dispose();
        return false;
    }

    private XSSFAnchor getAnchorFromParent(XmlObject obj) {
        XSSFAnchor anchor = null;
        XmlObject parentXbean = null;
        XmlCursor cursor = obj.newCursor();
        if (cursor.toParent()) {
            parentXbean = cursor.getObject();
        }
        cursor.dispose();
        if (parentXbean != null) {
            if (parentXbean instanceof CTTwoCellAnchor) {
                CTTwoCellAnchor ct = (CTTwoCellAnchor) parentXbean;
                anchor = new XSSFClientAnchor(ct.getFrom(), ct.getTo());
            } else if (parentXbean instanceof CTOneCellAnchor) {
                CTOneCellAnchor ct2 = (CTOneCellAnchor) parentXbean;
                anchor = new XSSFClientAnchor(getSheet(), ct2.getFrom(), ct2.getExt());
            } else if (parentXbean instanceof CTAbsoluteAnchor) {
                CTAbsoluteAnchor ct3 = (CTAbsoluteAnchor) parentXbean;
                anchor = new XSSFClientAnchor(getSheet(), ct3.getPos(), ct3.getExt());
            }
        }
        return anchor;
    }

    @Override // java.lang.Iterable
    public Iterator<XSSFShape> iterator() {
        return getShapes().iterator();
    }

    public XSSFSheet getSheet() {
        return (XSSFSheet) getParent();
    }
}
