package com.microsoft.schemas.vml;

import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/STFillType$Enum.class */
public final class STFillType$Enum extends StringEnumAbstractBase {
    static final int INT_SOLID = 1;
    static final int INT_GRADIENT = 2;
    static final int INT_GRADIENT_RADIAL = 3;
    static final int INT_TILE = 4;
    static final int INT_PATTERN = 5;
    static final int INT_FRAME = 6;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STFillType$Enum[]{new STFillType$Enum("solid", 1), new STFillType$Enum("gradient", 2), new STFillType$Enum("gradientRadial", 3), new STFillType$Enum("tile", 4), new STFillType$Enum("pattern", 5), new STFillType$Enum("frame", 6)});
    private static final long serialVersionUID = 1;

    public static STFillType$Enum forString(String str) {
        return (STFillType$Enum) table.forString(str);
    }

    public static STFillType$Enum forInt(int i) {
        return (STFillType$Enum) table.forInt(i);
    }

    private STFillType$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
