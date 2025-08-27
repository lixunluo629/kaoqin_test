package com.microsoft.schemas.vml;

import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/STStrokeEndCap$Enum.class */
public final class STStrokeEndCap$Enum extends StringEnumAbstractBase {
    static final int INT_FLAT = 1;
    static final int INT_SQUARE = 2;
    static final int INT_ROUND = 3;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STStrokeEndCap$Enum[]{new STStrokeEndCap$Enum("flat", 1), new STStrokeEndCap$Enum("square", 2), new STStrokeEndCap$Enum("round", 3)});
    private static final long serialVersionUID = 1;

    public static STStrokeEndCap$Enum forString(String str) {
        return (STStrokeEndCap$Enum) table.forString(str);
    }

    public static STStrokeEndCap$Enum forInt(int i) {
        return (STStrokeEndCap$Enum) table.forInt(i);
    }

    private STStrokeEndCap$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
