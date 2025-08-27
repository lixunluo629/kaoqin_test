package org.apache.poi.xwpf.usermodel;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xwpf/usermodel/XWPFDefaultParagraphStyle.class */
public class XWPFDefaultParagraphStyle {
    private CTPPr ppr;

    public XWPFDefaultParagraphStyle(CTPPr ppr) {
        this.ppr = ppr;
    }

    protected CTPPr getPPr() {
        return this.ppr;
    }

    public int getSpacingAfter() {
        if (this.ppr.isSetSpacing()) {
            return this.ppr.getSpacing().getAfter().intValue();
        }
        return -1;
    }
}
