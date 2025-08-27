package org.openxmlformats.schemas.drawingml.x2006.main;

import com.alibaba.excel.constant.ExcelXmlConstants;
import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/main/STTextTabAlignType$Enum.class */
public final class STTextTabAlignType$Enum extends StringEnumAbstractBase {
    static final int INT_L = 1;
    static final int INT_CTR = 2;
    static final int INT_R = 3;
    static final int INT_DEC = 4;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STTextTabAlignType$Enum[]{new STTextTabAlignType$Enum("l", 1), new STTextTabAlignType$Enum("ctr", 2), new STTextTabAlignType$Enum(ExcelXmlConstants.POSITION, 3), new STTextTabAlignType$Enum("dec", 4)});
    private static final long serialVersionUID = 1;

    public static STTextTabAlignType$Enum forString(String str) {
        return (STTextTabAlignType$Enum) table.forString(str);
    }

    public static STTextTabAlignType$Enum forInt(int i) {
        return (STTextTabAlignType$Enum) table.forInt(i);
    }

    private STTextTabAlignType$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
