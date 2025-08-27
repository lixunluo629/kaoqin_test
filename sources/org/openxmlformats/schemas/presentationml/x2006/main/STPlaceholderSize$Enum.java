package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/presentationml/x2006/main/STPlaceholderSize$Enum.class */
public final class STPlaceholderSize$Enum extends StringEnumAbstractBase {
    static final int INT_FULL = 1;
    static final int INT_HALF = 2;
    static final int INT_QUARTER = 3;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STPlaceholderSize$Enum[]{new STPlaceholderSize$Enum("full", 1), new STPlaceholderSize$Enum("half", 2), new STPlaceholderSize$Enum("quarter", 3)});
    private static final long serialVersionUID = 1;

    public static STPlaceholderSize$Enum forString(String str) {
        return (STPlaceholderSize$Enum) table.forString(str);
    }

    public static STPlaceholderSize$Enum forInt(int i) {
        return (STPlaceholderSize$Enum) table.forInt(i);
    }

    private STPlaceholderSize$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
