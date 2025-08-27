package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.StringEnumAbstractBase;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/STHint$Enum.class */
public final class STHint$Enum extends StringEnumAbstractBase {
    static final int INT_DEFAULT = 1;
    static final int INT_EAST_ASIA = 2;
    static final int INT_CS = 3;
    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new STHint$Enum[]{new STHint$Enum("default", 1), new STHint$Enum("eastAsia", 2), new STHint$Enum("cs", 3)});
    private static final long serialVersionUID = 1;

    public static STHint$Enum forString(String str) {
        return (STHint$Enum) table.forString(str);
    }

    public static STHint$Enum forInt(int i) {
        return (STHint$Enum) table.forInt(i);
    }

    private STHint$Enum(String str, int i) {
        super(str, i);
    }

    private Object readResolve() {
        return forInt(intValue());
    }
}
