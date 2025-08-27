package org.apache.poi.xwpf.usermodel;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xwpf/usermodel/XWPFSDTCell.class */
public class XWPFSDTCell extends AbstractXWPFSDT implements ICell {
    private final XWPFSDTContentCell cellContent;

    public XWPFSDTCell(CTSdtCell sdtCell, XWPFTableRow xwpfTableRow, IBody part) {
        super(sdtCell.getSdtPr(), part);
        this.cellContent = new XWPFSDTContentCell(sdtCell.getSdtContent(), xwpfTableRow, part);
    }

    @Override // org.apache.poi.xwpf.usermodel.AbstractXWPFSDT
    public ISDTContent getContent() {
        return this.cellContent;
    }
}
