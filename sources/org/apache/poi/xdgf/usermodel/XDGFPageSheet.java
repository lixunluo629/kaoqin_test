package org.apache.poi.xdgf.usermodel;

import com.microsoft.schemas.office.visio.x2012.main.PageSheetType;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xdgf/usermodel/XDGFPageSheet.class */
public class XDGFPageSheet extends XDGFSheet {
    PageSheetType _pageSheet;

    public XDGFPageSheet(PageSheetType sheet, XDGFDocument document) {
        super(sheet, document);
        this._pageSheet = sheet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.apache.poi.xdgf.usermodel.XDGFSheet
    public PageSheetType getXmlObject() {
        return this._pageSheet;
    }
}
