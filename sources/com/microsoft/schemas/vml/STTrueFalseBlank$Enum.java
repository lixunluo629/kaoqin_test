package com.microsoft.schemas.vml;

import com.alibaba.excel.constant.ExcelXmlConstants;
import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/STTrueFalseBlank$Enum.class */
public final class STTrueFalseBlank$Enum extends StringEnumAbstractBase {
    static final int INT_T = 1;
    static final int INT_F = 2;
    static final int INT_TRUE = 3;
    static final int INT_FALSE = 4;
    static final int INT_X = 5;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STTrueFalseBlank$Enum[]{new STTrueFalseBlank$Enum("t", 1), new STTrueFalseBlank$Enum(ExcelXmlConstants.CELL_FORMULA_TAG, 2), new STTrueFalseBlank$Enum("true", 3), new STTrueFalseBlank$Enum("false", 4), new STTrueFalseBlank$Enum("", 5)});
    private static final long serialVersionUID = 1;

    public static STTrueFalseBlank$Enum forString(String str) {
        return (STTrueFalseBlank$Enum) table.forString(str);
    }

    public static STTrueFalseBlank$Enum forInt(int i) {
        return (STTrueFalseBlank$Enum) table.forInt(i);
    }

    private STTrueFalseBlank$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
