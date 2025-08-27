package org.apache.poi.sl.usermodel;

import com.alibaba.excel.constant.ExcelXmlConstants;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/RectAlign.class */
public enum RectAlign {
    TOP_LEFT("tl"),
    TOP("t"),
    TOP_RIGHT("tr"),
    LEFT("l"),
    CENTER("ctr"),
    RIGHT(ExcelXmlConstants.POSITION),
    BOTTOM_LEFT("bl"),
    BOTTOM("b"),
    BOTTOM_RIGHT(CompressorStreamFactory.BROTLI);

    private final String dir;

    RectAlign(String dir) {
        this.dir = dir;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.dir;
    }
}
