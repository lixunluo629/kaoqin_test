package org.apache.poi.xwpf.usermodel;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xwpf/usermodel/XWPFDefaultRunStyle.class */
public class XWPFDefaultRunStyle {
    private CTRPr rpr;

    public XWPFDefaultRunStyle(CTRPr rpr) {
        this.rpr = rpr;
    }

    protected CTRPr getRPr() {
        return this.rpr;
    }

    public int getFontSize() {
        if (this.rpr.isSetSz()) {
            return this.rpr.getSz().getVal().intValue() / 2;
        }
        return -1;
    }
}
