package com.microsoft.schemas.office.office;

import com.alibaba.excel.constant.ExcelXmlConstants;
import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/office/office/STTrueFalseBlank$Enum.class */
public final class STTrueFalseBlank$Enum extends StringEnumAbstractBase {
    static final int INT_X = 1;
    static final int INT_T = 2;
    static final int INT_F = 3;
    static final int INT_TRUE = 4;
    static final int INT_FALSE = 5;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STTrueFalseBlank$Enum[]{new STTrueFalseBlank$Enum("", 1), new STTrueFalseBlank$Enum("t", 2), new STTrueFalseBlank$Enum(ExcelXmlConstants.CELL_FORMULA_TAG, 3), new STTrueFalseBlank$Enum("true", 4), new STTrueFalseBlank$Enum("false", 5)});
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
