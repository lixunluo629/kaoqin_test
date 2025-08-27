package org.apache.poi.xslf.usermodel;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.sl.draw.DrawFactory;
import org.apache.poi.sl.draw.DrawTextShape;
import org.apache.poi.sl.usermodel.TableShape;
import org.apache.poi.sl.usermodel.TextShape;
import org.apache.poi.util.Internal;
import org.apache.poi.util.Units;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTable;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrameNonVisual;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/usermodel/XSLFTable.class */
public class XSLFTable extends XSLFGraphicFrame implements Iterable<XSLFTableRow>, TableShape<XSLFShape, XSLFTextParagraph> {
    static final String TABLE_URI = "http://schemas.openxmlformats.org/drawingml/2006/table";
    static final String DRAWINGML_URI = "http://schemas.openxmlformats.org/drawingml/2006/main";
    private CTTable _table;
    private List<XSLFTableRow> _rows;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !XSLFTable.class.desiredAssertionStatus();
    }

    XSLFTable(CTGraphicalObjectFrame shape, XSLFSheet sheet) {
        super(shape, sheet);
        CTGraphicalObjectData god = shape.getGraphic().getGraphicData();
        XmlCursor xc = god.newCursor();
        if (!xc.toChild("http://schemas.openxmlformats.org/drawingml/2006/main", "tbl")) {
            throw new IllegalStateException("a:tbl element was not found in\n " + god);
        }
        XmlObject xo = xc.getObject();
        if (xo instanceof XmlAnyTypeImpl) {
            throw new IllegalStateException("Schemas (*.xsb) for CTTable can't be loaded - usually this happens when OSGI loading is used and the thread context classloader has no reference to the xmlbeans classes - use POIXMLTypeLoader.setClassLoader() to set the loader, e.g. with CTTable.class.getClassLoader()");
        }
        this._table = (CTTable) xo;
        xc.dispose();
        this._rows = new ArrayList(this._table.sizeOfTrArray());
        CTTableRow[] arr$ = this._table.getTrArray();
        for (CTTableRow row : arr$) {
            this._rows.add(new XSLFTableRow(row, this));
        }
        updateRowColIndexes();
    }

    @Override // org.apache.poi.sl.usermodel.TableShape
    public XSLFTableCell getCell(int row, int col) {
        XSLFTableRow r;
        List<XSLFTableRow> rows = getRows();
        if (row < 0 || rows.size() <= row || (r = rows.get(row)) == null) {
            return null;
        }
        List<XSLFTableCell> cells = r.getCells();
        if (col < 0 || cells.size() <= col) {
            return null;
        }
        return cells.get(col);
    }

    @Internal
    public CTTable getCTTable() {
        return this._table;
    }

    @Override // org.apache.poi.sl.usermodel.TableShape
    public int getNumberOfColumns() {
        return this._table.getTblGrid().sizeOfGridColArray();
    }

    @Override // org.apache.poi.sl.usermodel.TableShape
    public int getNumberOfRows() {
        return this._table.sizeOfTrArray();
    }

    @Override // org.apache.poi.sl.usermodel.TableShape
    public double getColumnWidth(int idx) {
        return Units.toPoints(this._table.getTblGrid().getGridColArray(idx).getW());
    }

    @Override // org.apache.poi.sl.usermodel.TableShape
    public void setColumnWidth(int idx, double width) {
        this._table.getTblGrid().getGridColArray(idx).setW(Units.toEMU(width));
    }

    @Override // org.apache.poi.sl.usermodel.TableShape
    public double getRowHeight(int row) {
        return Units.toPoints(this._table.getTrArray(row).getH());
    }

    @Override // org.apache.poi.sl.usermodel.TableShape
    public void setRowHeight(int row, double height) {
        this._table.getTrArray(row).setH(Units.toEMU(height));
    }

    @Override // java.lang.Iterable
    public Iterator<XSLFTableRow> iterator() {
        return this._rows.iterator();
    }

    public List<XSLFTableRow> getRows() {
        return Collections.unmodifiableList(this._rows);
    }

    public XSLFTableRow addRow() {
        CTTableRow tr = this._table.addNewTr();
        XSLFTableRow row = new XSLFTableRow(tr, this);
        row.setHeight(20.0d);
        this._rows.add(row);
        updateRowColIndexes();
        return row;
    }

    static CTGraphicalObjectFrame prototype(int shapeId) {
        CTGraphicalObjectFrame frame = CTGraphicalObjectFrame.Factory.newInstance();
        CTGraphicalObjectFrameNonVisual nvGr = frame.addNewNvGraphicFramePr();
        CTNonVisualDrawingProps cnv = nvGr.addNewCNvPr();
        cnv.setName("Table " + shapeId);
        cnv.setId(shapeId + 1);
        nvGr.addNewCNvGraphicFramePr().addNewGraphicFrameLocks().setNoGrp(true);
        nvGr.addNewNvPr();
        frame.addNewXfrm();
        CTGraphicalObjectData gr = frame.addNewGraphic().addNewGraphicData();
        XmlCursor grCur = gr.newCursor();
        grCur.toNextToken();
        grCur.beginElement(new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tbl"));
        CTTable tbl = CTTable.Factory.newInstance();
        tbl.addNewTblPr();
        tbl.addNewTblGrid();
        XmlCursor tblCur = tbl.newCursor();
        tblCur.moveXmlContents(grCur);
        tblCur.dispose();
        grCur.dispose();
        gr.setUri(TABLE_URI);
        return frame;
    }

    public void mergeCells(int firstRow, int lastRow, int firstCol, int lastCol) {
        if (firstRow > lastRow) {
            throw new IllegalArgumentException("Cannot merge, first row > last row : " + firstRow + " > " + lastRow);
        }
        if (firstCol > lastCol) {
            throw new IllegalArgumentException("Cannot merge, first column > last column : " + firstCol + " > " + lastCol);
        }
        int rowSpan = (lastRow - firstRow) + 1;
        boolean mergeRowRequired = rowSpan > 1;
        int colSpan = (lastCol - firstCol) + 1;
        boolean mergeColumnRequired = colSpan > 1;
        for (int i = firstRow; i <= lastRow; i++) {
            XSLFTableRow row = this._rows.get(i);
            for (int colPos = firstCol; colPos <= lastCol; colPos++) {
                XSLFTableCell cell = row.getCells().get(colPos);
                if (mergeRowRequired) {
                    if (i == firstRow) {
                        cell.setRowSpan(rowSpan);
                    } else {
                        cell.setVMerge(true);
                    }
                }
                if (mergeColumnRequired) {
                    if (colPos == firstCol) {
                        cell.setGridSpan(colSpan);
                    } else {
                        cell.setHMerge(true);
                    }
                }
            }
        }
    }

    protected XSLFTableStyle getTableStyle() {
        CTTable tab = getCTTable();
        if (!tab.isSetTblPr() || !tab.getTblPr().isSetTableStyleId()) {
            return null;
        }
        String styleId = tab.getTblPr().getTableStyleId();
        XSLFTableStyles styles = getSheet().getSlideShow().getTableStyles();
        for (XSLFTableStyle style : styles.getStyles()) {
            if (style.getStyleId().equals(styleId)) {
                return style;
            }
        }
        return null;
    }

    void updateRowColIndexes() {
        int rowIdx = 0;
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            XSLFTableRow xr = i$.next();
            int colIdx = 0;
            Iterator i$2 = xr.iterator();
            while (i$2.hasNext()) {
                XSLFTableCell tc = i$2.next();
                tc.setRowColIndex(rowIdx, colIdx);
                colIdx++;
            }
            rowIdx++;
        }
    }

    void updateCellAnchor() {
        int rows = getNumberOfRows();
        int cols = getNumberOfColumns();
        double[] colWidths = new double[cols];
        double[] rowHeights = new double[rows];
        for (int row = 0; row < rows; row++) {
            rowHeights[row] = getRowHeight(row);
        }
        for (int col = 0; col < cols; col++) {
            colWidths[col] = getColumnWidth(col);
        }
        Rectangle2D tblAnc = getAnchor();
        DrawFactory df = DrawFactory.getInstance(null);
        double newY = tblAnc.getY();
        for (int row2 = 0; row2 < rows; row2++) {
            double maxHeight = 0.0d;
            for (int col2 = 0; col2 < cols; col2++) {
                XSLFTableCell tc = getCell(row2, col2);
                if (tc != null && tc.getGridSpan() == 1 && tc.getRowSpan() == 1) {
                    tc.setAnchor(new Rectangle2D.Double(0.0d, 0.0d, colWidths[col2], 0.0d));
                    DrawTextShape dts = df.getDrawable((TextShape<?, ?>) tc);
                    maxHeight = Math.max(maxHeight, dts.getTextHeight());
                }
            }
            rowHeights[row2] = Math.max(rowHeights[row2], maxHeight);
        }
        for (int row3 = 0; row3 < rows; row3++) {
            double newX = tblAnc.getX();
            for (int col3 = 0; col3 < cols; col3++) {
                Rectangle2D.Double r0 = new Rectangle2D.Double(newX, newY, colWidths[col3], rowHeights[row3]);
                XSLFTableCell tc2 = getCell(row3, col3);
                if (tc2 != null) {
                    tc2.setAnchor(r0);
                    newX += colWidths[col3] + 2.0d;
                }
            }
            newY += rowHeights[row3] + 2.0d;
        }
        for (int row4 = 0; row4 < rows; row4++) {
            for (int col4 = 0; col4 < cols; col4++) {
                XSLFTableCell tc3 = getCell(row4, col4);
                if (tc3 != null) {
                    Rectangle2D mergedBounds = tc3.getAnchor();
                    for (int col22 = col4 + 1; col22 < col4 + tc3.getGridSpan(); col22++) {
                        if (!$assertionsDisabled && col22 >= cols) {
                            throw new AssertionError();
                        }
                        XSLFTableCell tc22 = getCell(row4, col22);
                        if (!$assertionsDisabled && (tc22.getGridSpan() != 1 || tc22.getRowSpan() != 1)) {
                            throw new AssertionError();
                        }
                        mergedBounds.add(tc22.getAnchor());
                    }
                    for (int row22 = row4 + 1; row22 < row4 + tc3.getRowSpan(); row22++) {
                        if (!$assertionsDisabled && row22 >= rows) {
                            throw new AssertionError();
                        }
                        XSLFTableCell tc23 = getCell(row22, col4);
                        if (!$assertionsDisabled && (tc23.getGridSpan() != 1 || tc23.getRowSpan() != 1)) {
                            throw new AssertionError();
                        }
                        mergedBounds.add(tc23.getAnchor());
                    }
                    tc3.setAnchor(mergedBounds);
                }
            }
        }
    }
}
