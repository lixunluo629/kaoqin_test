package org.apache.poi.xssf.usermodel;

import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.xssf.usermodel.extensions.XSSFHeaderFooter;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/XSSFOddHeader.class */
public class XSSFOddHeader extends XSSFHeaderFooter implements Header {
    protected XSSFOddHeader(CTHeaderFooter headerFooter) {
        super(headerFooter);
    }

    @Override // org.apache.poi.xssf.usermodel.extensions.XSSFHeaderFooter
    public String getText() {
        return getHeaderFooter().getOddHeader();
    }

    @Override // org.apache.poi.xssf.usermodel.extensions.XSSFHeaderFooter
    public void setText(String text) {
        if (text == null) {
            getHeaderFooter().unsetOddHeader();
        } else {
            getHeaderFooter().setOddHeader(text);
        }
    }
}
