package com.microsoft.schemas.vml;

import org.apache.commons.codec.language.bm.Languages;
import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:com/microsoft/schemas/vml/STFillMethod$Enum.class */
public final class STFillMethod$Enum extends StringEnumAbstractBase {
    static final int INT_NONE = 1;
    static final int INT_LINEAR = 2;
    static final int INT_SIGMA = 3;
    static final int INT_ANY = 4;
    static final int INT_LINEAR_SIGMA = 5;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STFillMethod$Enum[]{new STFillMethod$Enum("none", 1), new STFillMethod$Enum("linear", 2), new STFillMethod$Enum("sigma", 3), new STFillMethod$Enum(Languages.ANY, 4), new STFillMethod$Enum("linear sigma", 5)});
    private static final long serialVersionUID = 1;

    public static STFillMethod$Enum forString(String str) {
        return (STFillMethod$Enum) table.forString(str);
    }

    public static STFillMethod$Enum forInt(int i) {
        return (STFillMethod$Enum) table.forInt(i);
    }

    private STFillMethod$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
