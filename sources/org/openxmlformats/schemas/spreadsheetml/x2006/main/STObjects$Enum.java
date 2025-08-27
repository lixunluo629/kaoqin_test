package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/STObjects$Enum.class */
public final class STObjects$Enum extends StringEnumAbstractBase {
    static final int INT_ALL = 1;
    static final int INT_PLACEHOLDERS = 2;
    static final int INT_NONE = 3;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STObjects$Enum[]{new STObjects$Enum("all", 1), new STObjects$Enum("placeholders", 2), new STObjects$Enum("none", 3)});
    private static final long serialVersionUID = 1;

    public static STObjects$Enum forString(String str) {
        return (STObjects$Enum) table.forString(str);
    }

    public static STObjects$Enum forInt(int i) {
        return (STObjects$Enum) table.forInt(i);
    }

    private STObjects$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
