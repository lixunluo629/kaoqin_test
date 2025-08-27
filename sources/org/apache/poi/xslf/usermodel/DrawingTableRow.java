package org.apache.poi.xslf.usermodel;

import org.apache.poi.util.Removal;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow;

@Removal(version = "3.18")
/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/usermodel/DrawingTableRow.class */
public class DrawingTableRow {
    private final CTTableRow row;

    public DrawingTableRow(CTTableRow row) {
        this.row = row;
    }

    public DrawingTableCell[] getCells() {
        CTTableCell[] ctTableCells = this.row.getTcArray();
        DrawingTableCell[] o = new DrawingTableCell[ctTableCells.length];
        for (int i = 0; i < o.length; i++) {
            o[i] = new DrawingTableCell(ctTableCells[i]);
        }
        return o;
    }
}
