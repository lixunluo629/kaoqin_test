package org.apache.poi.sl.draw.binding;

import com.alibaba.excel.constant.ExcelXmlConstants;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.poi.xssf.usermodel.XSSFRelation;

@XmlEnum
@XmlType(name = "ST_RectAlignment", namespace = XSSFRelation.NS_DRAWINGML)
/* loaded from: poi-3.17.jar:org/apache/poi/sl/draw/binding/STRectAlignment.class */
public enum STRectAlignment {
    TL("tl"),
    T("t"),
    TR("tr"),
    L("l"),
    CTR("ctr"),
    R(ExcelXmlConstants.POSITION),
    BL("bl"),
    B("b"),
    BR(CompressorStreamFactory.BROTLI);

    private final String value;

    STRectAlignment(String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }

    public static STRectAlignment fromValue(String v) {
        STRectAlignment[] arr$ = values();
        for (STRectAlignment c : arr$) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
