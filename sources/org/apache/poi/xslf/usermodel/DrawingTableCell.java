package org.apache.poi.xslf.usermodel;

import org.apache.poi.util.Removal;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell;

@Removal(version = "3.18")
/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/usermodel/DrawingTableCell.class */
public class DrawingTableCell {
    private final CTTableCell cell;
    private final DrawingTextBody drawingTextBody;

    public DrawingTableCell(CTTableCell cell) {
        this.cell = cell;
        this.drawingTextBody = new DrawingTextBody(this.cell.getTxBody());
    }

    public DrawingTextBody getTextBody() {
        return this.drawingTextBody;
    }
}
