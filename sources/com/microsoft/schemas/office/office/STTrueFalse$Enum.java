package com.microsoft.schemas.office.office;

import com.alibaba.excel.constant.ExcelXmlConstants;
import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/office/STTrueFalse$Enum.class */
public final class STTrueFalse$Enum extends StringEnumAbstractBase {
    static final int INT_T = 1;
    static final int INT_F = 2;
    static final int INT_TRUE = 3;
    static final int INT_FALSE = 4;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STTrueFalse$Enum[]{new STTrueFalse$Enum("t", 1), new STTrueFalse$Enum(ExcelXmlConstants.CELL_FORMULA_TAG, 2), new STTrueFalse$Enum("true", 3), new STTrueFalse$Enum("false", 4)});
    private static final long serialVersionUID = 1;

    public static STTrueFalse$Enum forString(String str) {
        return (STTrueFalse$Enum) table.forString(str);
    }

    public static STTrueFalse$Enum forInt(int i) {
        return (STTrueFalse$Enum) table.forInt(i);
    }

    private STTrueFalse$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
